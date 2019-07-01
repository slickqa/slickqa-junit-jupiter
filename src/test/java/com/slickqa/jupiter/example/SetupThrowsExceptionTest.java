package com.slickqa.jupiter.example;

import com.slickqa.jupiter.SlickBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ExampleTests")
public class SetupThrowsExceptionTest extends SlickBaseTest {



    @BeforeEach
    public void setUp(){
        // setup throws exception

        int i = 4/0;
    }

    @Test
    @DisplayName("testExceptionInSetup")
    public void testExceptionInSetupSkips() throws Exception {
        // this should never execute
    }

}

