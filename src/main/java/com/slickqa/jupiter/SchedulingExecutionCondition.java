package com.slickqa.jupiter;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.util.Optional;

public class SchedulingExecutionCondition implements ExecutionCondition, TestInstancePostProcessor {
    /**
     * Callback for post-processing the supplied test instance.
     *
     * <p><strong>Note</strong>: the {@code ExtensionContext} supplied to a
     * {@code TestInstancePostProcessor} will always return an empty
     * {@link Optional} value from {@link ExtensionContext#getTestInstance()
     * getTestInstance()}. A {@code TestInstancePostProcessor} should therefore
     * only attempt to process the supplied {@code testInstance}.
     *
     * @param testInstance the instance to post-process; never {@code null}
     * @param context      the current extension context; never {@code null}
     */
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        System.out.println("yomama");
    }

    /**
     * Evaluate this condition for the supplied {@link ExtensionContext}.
     *
     * indicates that the container or test should be executed; whereas, a
     * {@linkplain ConditionEvaluationResult#disabled disabled} result
     * indicates that the container or test should not be executed.
     *
     * @param context the current extension context; never {@code null}
     * @return the result of evaluating this condition; never {@code null}
     **/
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
        if (controller.configurationSource.getConfigurationEntry("scheduleTests", "false").
                equalsIgnoreCase("true")) {
            return ConditionEvaluationResult.disabled("Test scheduled only");
        }
        return ConditionEvaluationResult.enabled("Test/s will run");
    }
}
