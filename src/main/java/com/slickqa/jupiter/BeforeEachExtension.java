package com.slickqa.jupiter;

import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.Result;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.*;

import java.lang.reflect.Method;
import java.util.Date;


public class BeforeEachExtension implements BeforeEachCallback {

    boolean isUsingSlick() {
        boolean retval = false;

        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
        if(controller != null && SlickJunitController.isUsingSlick()) {
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
            Store store = context.getStore(Namespace.create(context.getUniqueId()));
            store.put("skipTest", true);
            SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
            if (context.getTestMethod().isPresent()) {
                Method testMethod = context.getTestMethod().get();
                //System.out.println(MessageFormat.format("Test found: {0}", testMethod.getName()));
                Result result = controller.getOrCreateResultFor(testMethod, context.getUniqueId(), context.getDisplayName());
                Result update = new Result();
                update.setStarted(new Date());
                update.setReason("");
                update.setRunstatus("RUNNING");
                Result updatedResult;
                try {
                    updatedResult = controller.getSlickClient().result(result.getId()).update(update);
                    SlickJunitController.currentResult.set(updatedResult);
                    //SlickTestWatcher.currentResult.set(controller.getSlickClient().result(result.getId()).get());
                } catch (SlickError e) {
                    e.printStackTrace();
                    System.err.println("!! ERROR: Unable to set result to RUNNING. !!");
                    SlickJunitController.currentResult.set(null);
                }
            }
        }
    }
}
