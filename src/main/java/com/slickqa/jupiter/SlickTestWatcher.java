package com.slickqa.jupiter;

import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.LogEntry;
import com.slickqa.client.model.Result;
import com.slickqa.jupiter.annotations.SlickLogger;
import com.slickqa.jupiter.annotations.SlickMetaData;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import javax.swing.text.html.Option;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class SlickTestWatcher implements TestWatcher{


    private ThreadLocal<SlickLogger> logger;
    private SlickJunitController controller;


    private String PASS = "PASS";
    private String FINISHED = "FINISHED";
    private String FAIL = "FAIL";
    private String BROKEN_TEST = "BROKEN_TEST";
    private String SKIPPED = "SKIPPED";

    public SlickTestWatcher() {
        logger = new ThreadLocal<>();
        controller = SlickJunitControllerFactory.getControllerInstance();
        logger.set(new SlickResultLogger(controller));
    }

    public boolean isUsingSlick() {
        boolean retval = false;

        if(controller != null && controller.isUsingSlick()) {
            retval = true;
        }

        return retval;
    }

    /**
     * Invoked after a disabled test has been skipped.
     *
     * @param context the current extension context; never {@code null}
     * @param reason  the reason the test is disabled; never {@code null}
     */
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        System.out.println("Test is disabled Yomama");

    }

    /**
     * Invoked after a test has completed successfully.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void testSuccessful(ExtensionContext context) {
        if (isUsingSlick()) {
            logger.get().debug("Test PASSED.  Reporting to slick");
            SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
            Method testMethod = null;
            Optional<Method> testOptional = context.getTestMethod();
            if (testOptional.isPresent()) {
                testMethod = testOptional.get();
                Result result = controller.getResultFor(testMethod);
                Result update = new Result();
                update.setFinished(new Date());
                update.setStatus(PASS);
                update.setRunstatus(FINISHED);
                try {
                    controller.getSlickClient().result(result.getId()).update(update);
                } catch (SlickError e) {
                    e.printStackTrace();
                    logger.get().error("!! ERROR: Unable to update slick result with pass!!", e);
                }
            }
        }
    }

    /**
     * Invoked after a test has been aborted.
     *
     * @param context the current extension context; never {@code null}
     * @param cause   the throwable responsible for the test being aborted; may be {@code null}
     */
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        testFailed(context, cause);
    }

    /**
     * Invoked after a test has failed.
     *
     * @param context the current extension context; never {@code null}
     * @param cause   the throwable that caused test failure; may be {@code null}
     */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (isUsingSlick()) {
            String status = BROKEN_TEST;
            SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
            Method testMethod = null;
            Optional<Method> testOptional = context.getTestMethod();
            if (testOptional.isPresent()) {
                testMethod = testOptional.get();
                if (null != cause) {
                    if (java.lang.AssertionError.class.isAssignableFrom(cause.getClass())) {
                        status = FAIL;
                    }
                    logger.get().error(cause.toString());
                    logger.get().error(Arrays.toString(cause.getStackTrace()).replace(" ", "\r\n"));
                }
                Result result = controller.getResultFor(testMethod);
                if (result != null) {
                    log().flushLogs();
                    Result update = new Result();
                    update.setFinished(new Date());
                    update.setStatus(status);
                    update.setRunstatus(FINISHED);
                    Optional<Throwable> eOptional = context.getExecutionException();
                    Throwable e;
                    if (eOptional.isPresent()) {
                        e = eOptional.get();
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        update.setReason(e.getMessage() + "\n" + sw.toString());
                    }
                    try {
                        controller.getSlickClient().result(result.getId()).update(update);
                    } catch (SlickError err) {
                        err.printStackTrace();
                        System.err.println("!! ERROR: Unable to update slick result with a fail status!!");
                    }
                }
            }
        }
    }

    public SlickLogger log() {
        return logger.get();
    }
}
