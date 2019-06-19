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

    private ThreadLocal<Result> currentResult;

    private ThreadLocal<SlickLogger> logger;


    private String PASS = "PASS";
    private String FINISHED = "FINISHED";
    private String FAIL = "FAIL";
    private String BROKEN_TEST = "BROKEN_TEST";
    private String SKIPPED = "SKIPPED";

    public boolean isUsingSlick() {
        boolean retval = false;

        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
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
        System.out.println("Test Success Yomama");
        if (isUsingSlick()) {
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
                    System.err.println("!! ERROR: Unable to update slick result with pass!!");
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
        System.out.println("Test Aborted Yomama");

    }

    /**
     * Invoked after a test has failed.
     *
     * @param context the current extension context; never {@code null}
     * @param cause   the throwable that caused test failure; may be {@code null}
     */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("Test Failed Yomama");
        String status = BROKEN_TEST;
        SlickJunitController controller = SlickJunitControllerFactory.getControllerInstance();
        Method testMethod = null;
        Optional<Method> testOptional = context.getTestMethod();
        if (testOptional.isPresent()) {
            if (isUsingSlick()) {
                Result result = controller.getResultFor(testMethod);
                if (result != null) {
                    log().flushLogs();
                    Result update = new Result();
                    update.setFinished(new Date());
                    update.setStatus(FAIL);
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

//        if(isUsingSlick() && threadSlickResultLogger != null && threadSlickResultLogger.get() != null) {
//            Throwable cause = testResult.getThrowable();
//            if (null != cause) {
//                if (cause.toString().contains("java.lang.AssertionError")) {
//                    status = FAIL;
//                }
//                threadSlickResultLogger.get().error(cause.toString());
//                threadSlickResultLogger.get().error(Arrays.toString(cause.getStackTrace()).replace(" ", "\r\n"));
//            }
//            Result result = SlickSuite.getSlickTestNGController().getResultFor(testResult.getMethod().getConstructorOrMethod().getMethod());
//            if (result != null) {
//                Result update = new Result();
//                update.setFinished(new Date());
//                update.setStatus(status);
//                update.setRunstatus(FINISHED);
//                StringWriter sw = new StringWriter();
//                cause.printStackTrace(new PrintWriter(sw));
//                update.setReason(sw.toString());
//                try {
//                    SlickSuite.getSlickTestNGController().updateResultFor(result.getId(), update);
//                } catch (SlickError e) {
//                    e.printStackTrace();
//                    System.err.println("!! ERROR: Unable to fail result !!");
//                }
//            }
//            SlickMetaData metaData = testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(SlickMetaData.class);
//            if(metaData != null && metaData.triageNote() != null && !"".equals(metaData.triageNote())) {
//                threadSlickResultLogger.get().debug(metaData.triageNote());
//
//                String triageNote = metaData.triageNote();
//                LogEntry triageNoteEntry = new LogEntry();
//                triageNoteEntry.setLoggerName("slick.note");
//                triageNoteEntry.setLevel("WARN");
//                triageNoteEntry.setEntryTime(new Date());
//                triageNoteEntry.setMessage(metaData.triageNote());
//
//                SlickResultLogger triageLogger = new SlickResultLogger(threadCurrentResultId.get());
//                triageLogger.setLoggerName("slick.note");
//                triageLogger.addLogEntry(triageNoteEntry);
//                triageLogger.flushLogs();
//
//            }
//            threadSlickResultLogger.get().flushLogs();
//        }
//        else {
//            logger.debug("Not logging to slick");
//        }

    }

    public SlickLogger log() {
        return logger.get();
    }
}
