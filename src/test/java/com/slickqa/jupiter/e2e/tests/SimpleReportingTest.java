package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.SlickClient;
import com.slickqa.client.SlickClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Simple Reporting Tests")
public class SimpleReportingTest {
    private SlickClient slick;

    @BeforeEach
    public void setup() throws Exception {
        slick = SlickClientFactory.getSlickClient(System.getenv("SLICK_E2E_BASEURL"));
    }

    @Test
    @DisplayName("Slick instance is accessible (precondition).")
    public void slickUpAndRunning() throws Exception {
        // this will throw an exception if it can't reach it
        slick.projects().getList();
    }
}
