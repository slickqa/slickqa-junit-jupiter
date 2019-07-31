package com.slickqa.jupiter;

import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.Result;
import com.slickqa.client.model.Testrun;
import com.slickqa.jupiter.parameterized.ArgumentsChecksumStore;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BeforeEachExtension implements BeforeEachCallback {

    Pattern indexPattern = Pattern.compile(".*(\\d+)\\]$");

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
                if(controller.getConfigurationSource().getConfigurationEntry(ConfigurationNames.SCHEDULE_TEST_MODE, "false").equalsIgnoreCase("true")) {
                    addDataDrivenAttributesIfNeeded(controller, context, result);
                    throw new Exception("Scheduling mode, not running test.");
                }
                validateDataDrivenArgumentsIfNeeded(context, result);

                Result update = new Result();
                update.setStarted(new Date());
                update.setReason("");
                update.setRunstatus("RUNNING");
                Result updatedResult;
                if(controller.getTestrun().getRunStarted() == null) {
                    Testrun trUpdate = new Testrun();
                    trUpdate.setRunStarted(new Date());
                    try {
                        controller.testrun = controller.getSlickClient().testrun(controller.getTestrun().getId()).update(trUpdate);
                    } catch (SlickError e) {
                        e.printStackTrace();
                        System.err.println("Slick Communication Error: problem updating testrun start time, not critical so continuing.");
                    }
                }
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

    boolean isUsingSlick() {
        boolean retval = false;

        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
        if(controller != null && SlickJunitController.isUsingSlick()) {
            retval = true;
        }

        return retval;
    }

    boolean resultIsRunningFromPreviousScheduling(Result result) {
        return result != null && result.getAttributes() != null && "true".equalsIgnoreCase(result.getAttributes().get("scheduled"));
    }

    void validateDataDrivenArgumentsIfNeeded(ExtensionContext context, Result result) throws Exception {
        Matcher isDataDriven = indexPattern.matcher(context.getUniqueId());
        if(resultIsRunningFromPreviousScheduling(result) && isDataDriven.matches()) {
            Long index = Long.parseLong(isDataDriven.group(1));
            ArgumentsChecksumStore checksums = context.getStore(ExtensionContext.Namespace.create(ArgumentsChecksumStore.class, context.getRequiredTestMethod())).get("checksums", ArgumentsChecksumStore.class);
            if(checksums != null && result.getAttributes().containsKey("jupiterArgumentsChecksum") &&
                    !result.getAttributes().get("jupiterArgumentsChecksum").equalsIgnoreCase(checksums.getChecksum(index))) {
                throw new Exception("Invalid data driven arguments");
            }
        }
    }

    private void addDataDrivenAttributesIfNeeded(SlickJunitController controller, ExtensionContext context, Result result) throws Exception {
        Matcher isDataDriven = indexPattern.matcher(context.getUniqueId());
        if(isDataDriven.matches()) {
            Long index = Long.parseLong(isDataDriven.group(1));
            ArgumentsChecksumStore checksums = context.getStore(ExtensionContext.Namespace.create(ArgumentsChecksumStore.class, context.getRequiredTestMethod())).get("checksums", ArgumentsChecksumStore.class);
            if(checksums != null) {
                Result update = new Result();
                update.setAttributes(new HashMap<>(result.getAttributes()));
                update.getAttributes().put("jupiterArgumentsChecksum", checksums.getChecksum(index));
                controller.getSlickClient().result(result.getId()).update(update);
            }
        }
    }
}
