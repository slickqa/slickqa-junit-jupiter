package com.slickqa.jupiter.e2e;

import com.slickqa.client.SlickClient;
import com.slickqa.client.SlickClientFactory;
import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.Result;
import com.slickqa.jupiter.ConfigurationNames;
import com.slickqa.jupiter.SlickJunitControllerFactory;
import org.bson.types.ObjectId;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class SlickJunitRunner {
    public SlickClient slick;
    private String project = "slickqa-junit-jupiter";
    private String release;
    private String build;
    private String testplanName;
    private String testrunId;


    public SlickJunitRunner() {
        slick = SlickClientFactory.getSlickClient(System.getenv("SLICK_E2E_BASEURL"));
        SlickJunitControllerFactory.INSTANCE = null;
        release = "end-to-end";
        build = "latest";
        testplanName = "";
        testrunId = ObjectId.get().toHexString();
    }

    public void setSystemProperties() {
        System.setProperty(ConfigurationNames.BASE_URL, System.getenv("SLICK_E2E_BASEURL"));
        System.setProperty(ConfigurationNames.PROJECT_NAME, project);
        System.setProperty(ConfigurationNames.RELEASE_NAME, release);
        System.setProperty(ConfigurationNames.BUILD_NAME, build);
        System.setProperty(ConfigurationNames.TESTPLAN_NAME, testplanName);
        System.setProperty(ConfigurationNames.TESTRUN_ID, testrunId);
    }

    public List<Result> runTests(LauncherDiscoveryRequest request, boolean setSystemProperties) throws SlickError {
        if(setSystemProperties) {
            setSystemProperties();
        }
        Launcher launcher = LauncherFactory.create();
        launcher.execute(request);
        Map<String, String> query = new HashMap<String, String>() {{
            put("testrun.testrunId", testrunId);
        }};
        return slick.results(query).getList();
    }

    public Result runTestMethod(Method method, boolean setSystemProperties) throws SlickError {
        if(testplanName == null || testplanName.equals("")) {
            testplanName="Method: " + method.getDeclaringClass().getName() + ":" + method.getName();
        }
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectMethod(method.getDeclaringClass(), method)
                ).build();
        List<Result> results = runTests(request, setSystemProperties);
        // check results to make sure there is 1 and only 1 result
        return results.get(0);
    }

    public Result runTestMethod(Method method) throws SlickError {
        return runTestMethod(method, true);
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getTestplanName() {
        return testplanName;
    }

    public void setTestplanName(String testplanName) {
        this.testplanName = testplanName;
    }

    public String getTestrunId() {
        return testrunId;
    }

    public void setTestrunId(String testrunId) {
        this.testrunId = testrunId;
    }
}
