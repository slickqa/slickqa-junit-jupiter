package com.slickqa.jupiter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This should be applied only to @Test methods.
 * TestCaseInfo will be used to create Results
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface TestCaseInfo {
    String empty = "";

    String title() default empty;
    String purpose() default empty;
    String component() default empty;
    String feature() default empty;
    String automationId() default empty;
    String automationKey() default empty;
    Step[] steps() default {};
    String triageNote() default empty;
    String author() default empty;
}
