package com.slickqa.jupiter.annotations;

/**
 * For creating steps for TestCaseInfo
 */
public @interface Step {
    String step();
    String expectation() default "Not Available";
}
