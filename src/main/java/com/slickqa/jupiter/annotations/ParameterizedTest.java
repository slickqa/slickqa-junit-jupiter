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

    String name() default org.junit.jupiter.params.ParameterizedTest.DEFAULT_DISPLAY_NAME;

}
