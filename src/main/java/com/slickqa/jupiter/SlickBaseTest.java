package com.slickqa.jupiter;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SchedulingExecutionCondition.class)
@ExtendWith(SlickTestWatcher.class)
@ExtendWith(BeforeEachExtension.class)
public class SlickBaseTest {

}
