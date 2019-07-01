package com.slickqa.jupiter;


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

public class SlickTestExecutionListener implements TestExecutionListener {

    boolean isUsingSlick(SlickJunitController controller) {
        boolean retval = false;

        if(controller != null && SlickJunitController.isUsingSlick()) {
            retval = true;
        }

        return retval;
    }

    /**
     * Called when the execution of the {@link TestPlan} has started,
     * <em>before</em> any test has been executed.
     *
     * @param testPlan describes the tree of tests about to be executed
     */
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        System.out.println(")( SlickTestExecutionListener");
        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
        if (isUsingSlick(controller)) {
            Set<TestIdentifier> testRoot = testPlan.getChildren("[engine:junit-jupiter]");
            for (TestIdentifier child : testRoot) {
                System.out.println(child.getUniqueId() + " is a container: " + child.isContainer());
                for (TestIdentifier h : testPlan.getChildren(child.getUniqueId())) {
                    System.out.println(h.getUniqueId() + " is a container: " + h.isContainer());
                    if (!h.isContainer()) {
                        Optional<TestSource> sourceOptional = h.getSource();
                        if (sourceOptional.isPresent()) {
                            TestSource source = sourceOptional.get();
                            String className = ((MethodSource) source).getClassName();
                            String methodName = ((MethodSource) source).getMethodName();
                            try {
                                Class<?> clazz = Class.forName(className);
                                try {
                                    Method testMethod = clazz.getMethod(methodName);
                                    controller.getOrCreateResultFor(testMethod);
                                } catch (NoSuchMethodException e) {
                                    // nada
                                }
                            } catch (ClassNotFoundException e) {
                                // nada
                            }
                        }
                    }
                }
            }
            if (controller.configurationSource.getConfigurationEntry("scheduleTests").toLowerCase() == "true") {
                // TODO figure out how to stop tests from running
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
        System.out.println(")( executionStarted");

    }

}
