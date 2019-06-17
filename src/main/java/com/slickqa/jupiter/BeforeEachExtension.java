package com.slickqa.jupiter;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;


public class BeforeEachExtension implements BeforeEachCallback {

    boolean isUsingSlick() {
        boolean retval = false;

        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
        if(controller != null && controller.isUsingSlick()) {
            retval = true;
        }

        return retval;
    }

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (isUsingSlick()) {
            System.out.println(")( BeforeEachCallback");
            SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
            if (context.getTestMethod().isPresent()) {
                Method testMethod = context.getTestMethod().get();
                //System.out.println(MessageFormat.format("Test found: {0}", testMethod.getName()));
                controller.addResultFor(testMethod);
            }
        }
    }
}
