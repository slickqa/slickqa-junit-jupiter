package com.slickqa.jupiter;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class SlickTestWatcher implements TestWatcher{
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

    }
}
