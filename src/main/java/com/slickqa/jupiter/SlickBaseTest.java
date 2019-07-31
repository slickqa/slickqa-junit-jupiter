package com.slickqa.jupiter;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlickTestWatcher.class)
@ExtendWith(BeforeEachExtension.class)
@ExtendWith(BeforeTestExecutionExtension.class)
public class SlickBaseTest {

}
