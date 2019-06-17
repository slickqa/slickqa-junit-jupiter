package com.slickqa.jupiter.annotations;

/**
 * For creating steps for SlickMetaData
 */
public @interface Step {
    String step();
    String expectation() default "Not Available";
}
