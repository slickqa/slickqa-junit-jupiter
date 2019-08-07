package com.slickqa.jupiter;

import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.LogEntry;
import com.slickqa.client.model.Result;
import com.slickqa.jupiter.annotations.SlickLogger;
import com.slickqa.jupiter.annotations.TestCaseInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.*;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

public class SlickTestWatcher implements TestWatcher{


    private ThreadLocal<SlickLogger> logger;
    private SlickJunitController controller;
    private static final Logger LOGGER = Logger.getLogger( BeforeEachExtension.class.getName() );


    private String PASS = "PASS";
    private String FINISHED = "FINISHED";
    private String FAIL = "FAIL";
    private String BROKEN_TEST = "BROKEN_TEST";
    private String SKIPPED = "SKIPPED";
    private boolean wasAborted = false;

    public SlickTestWatcher() {
        logger = new ThreadLocal<>();
        controller = SlickJunitControllerFactory.getControllerInstance();
        logger.set(new SlickResultLogger(controller));
    }

    private boolean isUsingSlick() {
        boolean retval = false;

        if(controller != null && SlickJunitController.isUsingSlick()) {
            retval = true;
        }

        return retval;
    }

    private boolean isScheduleMode() {
        if(controller != null && controller.getConfigurationSource().getConfigurationEntry(ConfigurationNames.SCHEDULE_TEST_MODE, "false").equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    /**
     * Invoked after a disabled test has been skipped.
     *
     * @param context the current extension context; never {@code null}
     * @param reason  the reason the test is disabled; never {@code null}
     */
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        LOGGER.info("SlickTestWatcher got \"disabled\" result.  Result for " + context.getUniqueId() + "will not report to slick");
    }

    /**
     * Invoked after a test has completed successfully.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void testSuccessful(ExtensionContext context) {
        LOGGER.info("SlickTestWatcher got \"pass\" result");
        if (isUsingSlick()) {
            logger.get().debug("Test PASSED.  Reporting to slick");
            LOGGER.info("Test PASSED.  Reporting to slick");
            SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
            Optional<Method> testOptional = context.getTestMethod();
            if (testOptional.isPresent()) {
                Result result = controller.getResultFor(context.getUniqueId());
                Result update = new Result();
                update.setFinished(new Date());
                update.setStatus(PASS);
                update.setRunstatus(FINISHED);
                try {
                    Result updatedResult = controller.getSlickClient().result(result.getId()).update(update);
                    SlickJunitController.currentResult.set(updatedResult);
                } catch (SlickError e) {
                    e.printStackTrace();
                    logger.get().error("!! ERROR: Unable to update slick result with pass!!", e);
                    LOGGER.severe("!! ERROR: Unable to update slick result with pass!!");
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
        LOGGER.info("SlickTestWatcher got \"aborted\" result.  Calling testFailed to fail test.");
        testFailed(context, cause);
        wasAborted = true;
    }

    /**
     * Invoked after a test has failed.
     *
     * @param context the current extension context; never {@code null}
     * @param cause   the throwable that caused test failure; may be {@code null}
     */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if(!wasAborted) {
            LOGGER.info("SlickTestWatcher got \"failed\" result.");
        }
        if (isUsingSlick() && !isScheduleMode()) {
            String status = BROKEN_TEST;
            Store store = context.getStore(Namespace.create(context.getUniqueId()));
            Boolean skipped = store.get("skipTest", boolean.class);
            if (skipped) {
                status = SKIPPED;
            }
            SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
            Method testMethod = null;
            Optional<Method> testOptional = context.getTestMethod();
            if (testOptional.isPresent()) {
                testMethod = testOptional.get();
                if (cause != null) {
                    if (java.lang.AssertionError.class.isAssignableFrom(cause.getClass())) {
                        status = FAIL;
                    }
                    logger.get().error(cause.toString());
                    logger.get().error(Arrays.toString(cause.getStackTrace()).replace(" ", "\r\n"));
                }
                Result result = controller.getResultFor(context.getUniqueId());
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
                        if (status.equals(SKIPPED)) {
                            update.setReason("Setup Exception: " + e.getMessage() + "\n" + sw.toString());
                        } else {
                            update.setReason(e.getMessage() + "\n" + sw.toString());
                        }
                    }
                    try {
                        Result updatedResult = controller.getSlickClient().result(result.getId()).update(update);
                        SlickJunitController.currentResult.set(updatedResult);
                    } catch (SlickError err) {
                        err.printStackTrace();
                       LOGGER.severe("!! ERROR: Unable to update slick result with a fail status!!");
                    }
                    TestCaseInfo testInfo = testMethod.getAnnotation(TestCaseInfo.class);
                    if(testInfo != null && testInfo.triageNote() != null && !"".equals(testInfo.triageNote())) {
                        log().debug(testInfo.triageNote());

                        LogEntry triageNoteEntry = new LogEntry();
                        triageNoteEntry.setLoggerName("slick.note");
                        triageNoteEntry.setLevel("WARN");
                        triageNoteEntry.setEntryTime(new Date());
                        triageNoteEntry.setMessage(testInfo.triageNote());

                        SlickResultLogger triageLogger = new SlickResultLogger(controller);
                        triageLogger.setLoggerName("slick.note");
                        triageLogger.addLogEntry(triageNoteEntry);
                        triageLogger.flushLogs();
                    }
                }
            }
        }
    }

    private SlickLogger log() {
        return logger.get();
    }
}
