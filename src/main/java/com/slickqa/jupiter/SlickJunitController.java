package com.slickqa.jupiter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.slickqa.client.SlickClient;
import com.slickqa.client.SlickClientFactory;
import com.slickqa.client.errors.SlickError;
import com.slickqa.client.impl.JsonUtil;
import com.slickqa.client.model.*;
import com.slickqa.jupiter.annotations.TestCaseInfo;
//import org.junit.runner.Description;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/*
 * Common class used by SlickTestWatcher (sends final test result after a test is run)
 * This class will initialize the slick
 * client, create the testrun in slick, and will hold a mapping of junit tests to slick results.  The test writer will
 * not likely have to interact with this class unless they want to customize the process by extending.
 */
public class SlickJunitController {
    protected static boolean usingSlick;
    protected static boolean singleTestMode;
    protected String singleTestModeResultID;
    public static ThreadLocal<Result> currentResult;
    protected SlickConfigurationSource configurationSource;
    protected SlickClient slickClient;
    protected Project project;
    protected Testrun testrun;

    protected Map<String, Result> results;

    protected SlickJunitController() {
        usingSlick = false;
        configurationSource = initializeConfigurationSource();
        results = new HashMap<>();
        currentResult = new ThreadLocal<>();
        initializeController();
    }

    /**
     * This method exists in case you need to override where slick get's it's configuration data from.  By default
     * it will get it from system properties.
     *
     * @return an instance of
     */
    protected SlickConfigurationSource initializeConfigurationSource() {
        return new PropertyOrEnvVariableConfigurationSource();
    }

    protected void initializeController() {
        String baseurl = configurationSource.getConfigurationEntry(ConfigurationNames.BASE_URL, null);
        String projectName = configurationSource.getConfigurationEntry(ConfigurationNames.PROJECT_NAME, null);
        if((baseurl != null && projectName != null) || !configurationSource.getConfigurationEntry(ConfigurationNames
                .RESULT_URL, "").isEmpty()) {
            try {

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
                mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                JsonUtil.mapper = mapper;

                if(!configurationSource.getConfigurationEntry(ConfigurationNames.RESULT_URL, "").isEmpty()) {
                    // get result from slick
                    System.out.println("Single test mode.  Called with a slick Result URL.");
                    singleTestMode = true;
                    URL slickurl;
                    try {
                        slickurl = new URL(configurationSource.getConfigurationEntry(ConfigurationNames.RESULT_URL));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        throw new SlickError("Result URL is not valid");
                    }
                    String[] pathParts = slickurl.getPath().split("/");
                    singleTestModeResultID = pathParts[pathParts.length - 1];
                    String port = "";
                    if (slickurl.getPort() > 0) {
                        port = ":" + Integer.toString(slickurl.getPort());
                    }
                    baseurl = slickurl.getProtocol() + "://" + slickurl.getHost() + port + "/api/";
                    slickClient = SlickClientFactory.getSlickClient(baseurl);
                    usingSlick = true;
                    return;
                } else {
                    if(!baseurl.endsWith("api") && !baseurl.endsWith("api/")) {
                        String add = "api/";
                        if(baseurl.endsWith("/")) {
                            baseurl = baseurl + add;
                        } else {
                            baseurl = baseurl + "/" + add;
                        }
                    }
                    slickClient = SlickClientFactory.getSlickClient(baseurl);
                }
                ProjectReference projectReference = new ProjectReference();
                ReleaseReference releaseReference = null;
                BuildReference buildReference = null;
                String testplanId = null;
                String testrunName = null;
                try {
                    project = slickClient.project(projectName).get();
                } catch (SlickError e) {
                    project = new Project();
                    project.setName(projectName);
                    project = slickClient.projects().create(project);
                }
                projectReference.setName(project.getName());
                projectReference.setId(project.getId());

                String releaseName = configurationSource.getConfigurationEntry(ConfigurationNames.RELEASE_NAME, null);
                if(releaseName != null) {
                    releaseReference = new ReleaseReference();
                    releaseReference.setName(releaseName);
                }
                String buildName = configurationSource.getConfigurationEntry(ConfigurationNames.BUILD_NAME, null);
                if(buildName != null) {
                    buildReference = new BuildReference();
                    buildReference.setName(buildName);
                }

                String testplanName = configurationSource.getConfigurationEntry(ConfigurationNames.TESTPLAN_NAME, null);
                if(testplanName == null) {
                    testplanName = "Default_Testplan";
                }
                HashMap<String, String> query = new HashMap<>();
                query.put("project.id", project.getId());
                query.put("name", testplanName);
                TestPlan tplan = null;
                try {
                    List<TestPlan> tplans = slickClient.testplans(query).getList();
                    if (tplans != null && tplans.size() > 0) {
                        tplan = tplans.get(0);
                    }
                } catch (SlickError e) {
                    // don't care
                }
                if (tplan == null) {
                    tplan = new TestPlan();
                    tplan.setName(testplanName);
                    tplan.setProject(projectReference);
                    tplan = slickClient.testplans().create(tplan);
                }
                testplanId = tplan.getId();

                String testrunId = configurationSource.getConfigurationEntry(ConfigurationNames.TESTRUN_ID, null);
                if(testrunId != null) {
                    try {
                        testrun = slickClient.testrun(testrunId).get();
                    } catch (SlickError e) {
                        // doesn't matter, we'll just create it.
                    }
                }
                if(testrun == null) {
                    testrun = new Testrun();
                    testrun.setId(testrunId);
                    testrun.setName(testplanName);
                    testrun.setTestplanId(testplanId);
                    testrun.setProject(projectReference);
                    testrun.setRelease(releaseReference);
                    testrun.setBuild(buildReference);
                    if(configurationSource.getConfigurationEntry(ConfigurationNames.SCHEDULE_TEST_MODE, "false").equalsIgnoreCase("true")) {
                        HashMap<String, String> testrunAttrs = new HashMap<>();
                        testrunAttrs.put("scheduled", "true");
                        testrun.setAttributes(testrunAttrs);
                    }
                    testrun = slickClient.testruns().create(testrun);
                }

                usingSlick = true;
            } catch (SlickError e) {
                e.printStackTrace();
                System.err.println("!!!!!! Error occurred when initializing slick, no slick report will happen !!!!!!");
            }
        }
    }

    public static boolean isUsingSlick() {
        return usingSlick;
    }

    public Testrun getTestrun() {
        return testrun;
    }

    public SlickClient getSlickClient() {
        if(usingSlick) {
            return slickClient;
        } else {
            return null;
        }
    }

    public String getAutomationIdFromTestCaseInfo(Method testDescription) {
        String automationId = null;
        try {
            TestCaseInfo testCaseInfo = testDescription.getAnnotation(TestCaseInfo.class);
            if(testCaseInfo != null && testCaseInfo.automationId() != null && !"".equals(testCaseInfo.automationId())) {
                automationId = testCaseInfo.automationId();
            }
        } catch (RuntimeException e) {
            // ignore
        }

        return automationId;
    }

    public void addResultFor(Method testDescription, String uniqueID, String displayName) throws SlickError {
        if (isUsingSlick()) {
            TestCaseInfo testCaseInfo = testDescription.getAnnotation(TestCaseInfo.class);
            boolean hasTestCaseInfo = testCaseInfo != null;

            String automationId = getAutomationIdFromTestCaseInfo(testDescription);
            if(automationId == null) {
                automationId = uniqueID;
            }
            Testcase testcase = null;

            HashMap<String, String> query = new HashMap<>();
            query.put("project.id", project.getId());
            query.put("automationId", automationId);
            ProjectReference projectReference = new ProjectReference();
            projectReference.setName(project.getName());
            projectReference.setId(project.getId());

            try {
                List<Testcase> testcases = slickClient.testcases(query).getList();
                if (testcases != null && testcases.size() > 0) {
                    testcase = testcases.get(0);
                    // update author if needed
                    if (hasTestCaseInfo) {
                        if (!testCaseInfo.author().equals("") && !testCaseInfo.author().equals(testcase.getAuthor())) {
                            testcase.setAuthor(testCaseInfo.author());
                            getSlickClient().testcase(testcase.getId()).update(testcase);
                        }
                    }
                }
            } catch (SlickError e) {
                // ignore
            }

            boolean newTestcase = false;
            if (testcase == null) {
                testcase = new Testcase();
                newTestcase = true;
            }

            if (hasTestCaseInfo && !"".equals(testCaseInfo.title())) {
                testcase.setName(testCaseInfo.title());
            } else {
                testcase.setName(displayName);
            }
            testcase.setProject(projectReference);
            if (newTestcase) {
                testcase = slickClient.testcases().create(testcase);
            }


            testcase.setAutomated(true);
            testcase.setAutomationId(automationId);
            testcase.setAutomationTool("junit_5");
            if (hasTestCaseInfo) {
                testcase.setAutomationKey(getValueOrNullIfEmpty(testCaseInfo.automationKey()));
                testcase.setPurpose(testCaseInfo.purpose());
                ComponentReference componentReference = null;
                Component component = null;
                if (testCaseInfo.component() != null && !"".equals(testCaseInfo.component())) {
                    componentReference = new ComponentReference();
                    componentReference.setName(testCaseInfo.component());
                    if (project.getComponents() != null) {
                        for (Component possible : project.getComponents()) {
                            if (testCaseInfo.component().equals(possible.getName())) {
                                componentReference.setId(possible.getId());
                                componentReference.setCode(possible.getCode());
                                component = possible;
                                break;
                            }
                        }
                    }
                    if (componentReference.getId() == null) {
                        component = new Component();
                        component.setName(testCaseInfo.component());
                        try {
                            component = slickClient.project(project.getId()).components().create(component);
                            componentReference.setId(component.getId());
                            project = slickClient.project(project.getId()).get();
                        } catch (SlickError e) {
                            component = null;
                            componentReference = null;
                        }
                    }
                }
                testcase.setComponent(componentReference);
                FeatureReference featureReference = null;
                if (testCaseInfo.feature() != null && !"".equals(testCaseInfo.feature()) && component != null) {
                    featureReference = new FeatureReference();
                    featureReference.setName(testCaseInfo.feature());
                    Feature feature = null;
                    if (component.getFeatures() != null) {
                        for (Feature possible : component.getFeatures()) {
                            if (testCaseInfo.feature().equals(possible.getName())) {
                                featureReference.setId(possible.getId());
                                feature = possible;
                                break;
                            }
                        }
                    }
                    if (feature == null) {
                        feature = new Feature();
                        feature.setName(testCaseInfo.feature());
                        if (component.getFeatures() == null) {
                            component.setFeatures(new ArrayList<Feature>(1));
                        }
                        component.getFeatures().add(feature);
                        try {
                            component = slickClient.project(project.getId()).component(component.getId()).update(component);
                            project = slickClient.project(project.getId()).get();
                            if (component.getFeatures() != null) {
                                for (Feature possible : component.getFeatures()) {
                                    if (testCaseInfo.feature().equals(possible.getName())) {
                                        featureReference.setId(feature.getId());
                                    }
                                }

                            } else {
                                // this shouldn't be possible which probably means it'll happen
                                feature = null;
                                featureReference = null;
                            }
                        } catch (SlickError e) {
                            feature = null;
                            featureReference = null;
                        }
                    }
                    testcase.setFeature(featureReference);
                    if (testCaseInfo.steps() != null && testCaseInfo.steps().length > 0) {
                        testcase.setSteps(new ArrayList<Step>(testCaseInfo.steps().length));
                        for (com.slickqa.jupiter.annotations.Step testStep : testCaseInfo.steps()) {
                            Step slickStep = new Step();
                            slickStep.setName(testStep.step());
                            slickStep.setExpectedResult(testStep.expectation());
                            testcase.getSteps().add(slickStep);
                        }
                    }
                }
            }
            testcase = slickClient.testcase(testcase.getId()).update(testcase);
            TestcaseReference testReference = new TestcaseReference();
            testReference.setName(testcase.getName());
            testReference.setAutomationId(testcase.getAutomationId());
            testReference.setAutomationKey(testcase.getAutomationKey());
            testReference.setTestcaseId(testcase.getId());
            testReference.setAutomationTool(testcase.getAutomationTool());

            TestrunReference testrunReference = new TestrunReference();
            testrunReference.setName(testrun.getName());
            testrunReference.setTestrunId(testrun.getId());

            Result result = new Result();
            result.setProject(projectReference);
            result.setTestrun(testrunReference);
            result.setTestcase(testReference);
            if(testcase != null && testcase.getComponent() != null) {
                result.setComponent(testcase.getComponent());
            }

            HashMap<String, String> attrs = DefaultAttributes.getAttributesFromEnvironment();

            result.setStatus("NO_RESULT");
            if(configurationSource.getConfigurationEntry(ConfigurationNames.SCHEDULE_TEST_MODE, "false").equalsIgnoreCase("true")) {
                result.setRunstatus("SCHEDULED");
                attrs.put("scheduled", "true");
            }
            result.setAttributes(attrs);

            result.setReason("not run yet...");
            result.setRecorded(new Date());
            result = slickClient.results().create(result);
            results.put(automationId, result);
        }
    }

    public static String getValueOrNullIfEmpty(String value) {
        if (value == null || "".equals(value)) {
            return null;
        } else {
            return value;
        }
    }

    public Result getResultFor(String automationId) {
        return results.getOrDefault(automationId, null);
    }

    public Result getOrCreateResultFor(Method testDescription, String uniqueID, String displayName) {
        if(isUsingSlick()) {
            Result result = null;
            if(singleTestMode){
                // in single test mode we only need get the result once even if multiple extensions call this...
                if(currentResult.get() == null) {
                    try {
                        result = slickClient.result(singleTestModeResultID).get();
                    } catch (SlickError e) {
                        e.printStackTrace();
                        System.err.println("!!!! ERROR getting result in single test mode for result ID: " + singleTestModeResultID + " !!!!");
                    }
                    try {
                        testrun = slickClient.testrun(result.getTestrun().getTestrunId()).get();
                    } catch (SlickError e) {
                        System.err.println("!!!! This shouldn't happen so it will... ERROR getting testrun for an existing result in single test mode !!!!");
                    }
                    currentResult.set(result);
                    // single test mode results we add to this map and get reported on
                    // must match the result passed in from the command line
                    if(result.getTestcase().getAutomationId().equals(uniqueID)) {
                        results.put(uniqueID, result);
                        System.err.println("Single test mode for automationID:\n" + result.getTestcase().getAutomationId() +
                                "\nmatches the junit test to be run: \n" + uniqueID);
                    } else {
                        System.err.println("!!!! ERROR Single test mode for automationID:\n" + result.getTestcase().getAutomationId() +
                                "\ndoes not match the junit test to be run: \n" + uniqueID + "\nThis test will not run nor report to slick");
                        return null;
                    }
                } else {
                    return currentResult.get();
                }
            } else {
                result = getResultFor(uniqueID);
            }
            if(result == null) {
                try {
                    addResultFor(testDescription, uniqueID, displayName);
                    return getResultFor(uniqueID);
                } catch (SlickError e) {
                    e.printStackTrace();
                    System.err.println("!!!! ERROR creating slick result for " + testDescription.getName() + " !!!!");
                    return null;
                }
            } else {
                return result;
            }
        } else {
            return null;
        }
    }

    public SlickConfigurationSource getConfigurationSource() {
        return configurationSource;
    }

    /*public void createSuiteResults(ArrayList<Description> children) {
        if(isUsingSlick()) {
            for (Description child : children) {
                if (child.isTest()) {
                    try {
                        addResultFor(child);
                    } catch (SlickError e) {
                        e.printStackTrace();
                        System.err.println("!!!! ERROR creating slick result for " + child.getDisplayName() + " !!!!");
                    }
                } else {
                    if (child.getChildren() != null) {
                        createSuiteResults(child.getChildren());
                    }
                }
            }
        }
    }*/
}
