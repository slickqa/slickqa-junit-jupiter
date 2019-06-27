package com.slickqa.jupiter;


import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class TestExecutionExtension implements TestExecutionListener {
    /**
     * Called when the execution of the {@link TestPlan} has started,
     * <em>before</em> any test has been executed.
     *
     * @param testPlan describes the tree of tests about to be executed
     */
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        System.out.println("yo");
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

    }

    /**
     * Called when the execution of a leaf or subtree of the {@link TestPlan}
     * has finished, regardless of the outcome.
     *
     * <p>The {@link TestIdentifier} may represent a test or a container.
     *
     * <p>This method will only be called if the test or container has not
     * been {@linkplain #executionSkipped skipped}.
     *
     * <p>This method will be called for a container {@code TestIdentifier}
     * <em>after</em> all of its children have been
     * {@linkplain #executionSkipped skipped} or have
     * {@linkplain #executionFinished finished}.
     *
     * <p>The {@link TestExecutionResult} describes the result of the execution
     * for the supplied {@code TestIdentifier}. The result does not include or
     * aggregate the results of its children. For example, a container with a
     * failing test will be reported as {@link Status#SUCCESSFUL SUCCESSFUL} even
     * if one or more of its children are reported as {@link Status#FAILED FAILED}.
     *
     * @param testIdentifier      the identifier of the finished test or container
     * @param testExecutionResult the (unaggregated) result of the execution for
     *                            the supplied {@code TestIdentifier}
     * @see TestExecutionResult
     */
    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {

    }

    /**
     * Called when additional test reporting data has been published for
     * the supplied {@link TestIdentifier}.
     *
     * <p>Can be called at any time during the execution of a test plan.
     *
     * @param testIdentifier describes the test or container to which the entry pertains
     * @param entry          the published {@code ReportEntry}
     */
    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {

    }
}
