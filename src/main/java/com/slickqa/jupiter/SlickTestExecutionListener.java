package com.slickqa.jupiter;


import org.junit.jupiter.api.Disabled;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class SlickTestExecutionListener implements TestExecutionListener {

    private static final Logger LOGGER = Logger.getLogger( BeforeEachExtension.class.getName() );
    boolean isUsingSlick(SlickJunitController controller) {
        boolean retval = false;

        if(controller != null && SlickJunitController.isUsingSlick()) {
            retval = true;
        }

        return retval;
    }

    protected void fileResultsFor(TestPlan testPlan, TestIdentifier test) {
        LOGGER.info("fileResultFor called for " + test.getUniqueId());
        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
        if(test.isContainer()) {
            LOGGER.info("Found container: " + test.getUniqueId());
            for(TestIdentifier child : testPlan.getChildren(test.getUniqueId())) {
                fileResultsFor(testPlan, child);
            }
        } else {
            String automationID = test.getUniqueId();
            Optional<TestSource> sourceOptional = test.getSource();
            if (sourceOptional.isPresent()) {
                TestSource source = sourceOptional.get();
                String className = ((MethodSource) source).getClassName();
                String methodName = ((MethodSource) source).getMethodName();
                try {
                    Class<?> clazz = Class.forName(className);
                    try {
                        Method testMethod = clazz.getMethod(methodName);
                        if (!testMethod.isAnnotationPresent(Disabled.class)) {
                            LOGGER.info("Creating result for " + automationID);
                            controller.getOrCreateResultFor(testMethod, automationID, test.getDisplayName());
                        }
                    } catch (NoSuchMethodException e) {
                        // nada
                    }
                } catch (ClassNotFoundException e) {
                    // nada
                }
            }

        }
    }

    /**
     * Called when the execution of the {@link TestPlan} has started,
     * <em>before</em> any test has been executed.
     *
     * @param testPlan describes the tree of tests about to be executed
     */
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        LOGGER.info("testPlanExecutionStarted called");
        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
        if(controller.getSingleTestMode()) {
            LOGGER.info("Single test mode, will not file any results from testPlan");
            return;
        }
        if (isUsingSlick(controller)) {
            Set<TestIdentifier> testRoot = testPlan.getChildren("[engine:junit-jupiter]");
            for (TestIdentifier child : testRoot) {
                fileResultsFor(testPlan, child);
            }
        }
    }

    /**
     * Called when the execution of the {@link TestPlan} has finished,
     * <em>after</em> all tests have been executed.
     *
     * @param testPlan describes the tree of tests that have been executed
     */
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {

    }

    /**
     * Called when a new, dynamic {@link TestIdentifier} has been registered.
     *
     * <p>A <em>dynamic test</em> is a test that is not known a-priori and
     * therefore not contained in the original {@link TestPlan}.
     *
     * @param testIdentifier the identifier of the newly registered test
     *                       or container
     */
    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
        fileResultsFor(null, testIdentifier);
    }

    /**
     * Called when the execution of a leaf or subtree of the {@link TestPlan}
     * has been skipped.
     *
     * <p>The {@link TestIdentifier} may represent a test or a container. In
     * the case of a container, no listener methods will be called for any of
     * its descendants.
     *
     * <p>A skipped test or subtree of tests will never be reported as
     * {@linkplain #executionStarted started} or
     * {@linkplain #executionFinished finished}.
     *
     * @param testIdentifier the identifier of the skipped test or container
     * @param reason         a human-readable message describing why the execution
     */
    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {

    }

    /**
     * Called when the execution of a leaf or subtree of the {@link TestPlan}
     * is about to be started.
     *
     * <p>The {@link TestIdentifier} may represent a test or a container.
     *
     * <p>This method will only be called if the test or container has not
     * been {@linkplain #executionSkipped skipped}.
     *
     * <p>This method will be called for a container {@code TestIdentifier}
     * <em>before</em> {@linkplain #executionStarted starting} or
     * {@linkplain #executionSkipped skipping} any of its children.
     *
     * @param testIdentifier the identifier of the started test or container
     */
    @Override
    public void executionStarted(TestIdentifier testIdentifier) {

    }

}
