package com.slickqa.jupiter;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.*;

public class BeforeTestExecutionExtension implements BeforeTestExecutionCallback {
    /**
     * Callback that is invoked <em>immediately before</em> each test is executed.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println(")( BeforeTestExecutionExtension");
        Store store = context.getStore(Namespace.create(context.getUniqueId()));
        store.put("skipTest", false);

    }
}
