package com.slickqa.jupiter;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.*;

import java.util.logging.Logger;

public class BeforeTestExecutionExtension implements BeforeTestExecutionCallback {
    /**
     * Callback that is invoked <em>immediately before</em> each test is executed.
     *
     * @param context the current extension context; never {@code null}
     */
    private static final Logger LOGGER = Logger.getLogger( BeforeEachExtension.class.getName() );
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        LOGGER.info("Slick Extension called");
        Store store = context.getStore(Namespace.create(context.getUniqueId()));
        LOGGER.info("test will not skip as all @BeforeEach (setup) methods have completed");
        store.put("skipTest", false);
    }
}
