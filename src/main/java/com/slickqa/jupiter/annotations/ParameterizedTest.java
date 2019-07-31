package com.slickqa.jupiter.annotations;

import com.slickqa.jupiter.parameterized.SlickParameterizedTestExtension;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(SlickParameterizedTestExtension.class)
public @interface ParameterizedTest {

    /**
     * The display name to be used for individual invocations of the
     * parameterized test; never blank or consisting solely of whitespace.
     *
     * <p>Defaults to {@link org.junit.jupiter.params.ParameterizedTest#DEFAULT_DISPLAY_NAME}.
     *
     * <h4>Supported placeholders</h4>
     * <ul>
     * <li>{@link org.junit.jupiter.params.ParameterizedTest#DISPLAY_NAME_PLACEHOLDER}</li>
     * <li>{@link org.junit.jupiter.params.ParameterizedTest#INDEX_PLACEHOLDER}</li>
     * <li>{@link org.junit.jupiter.params.ParameterizedTest#ARGUMENTS_PLACEHOLDER}</li>
     * <li><code>{0}</code>, <code>{1}</code>, etc.: an individual argument (0-based)</li>
     * </ul>
     *
     * <p>For the latter, you may use {@link java.text.MessageFormat} patterns
     * to customize formatting.
     *
     * @see java.text.MessageFormat
     */
    String name() default org.junit.jupiter.params.ParameterizedTest.DEFAULT_DISPLAY_NAME;

}
