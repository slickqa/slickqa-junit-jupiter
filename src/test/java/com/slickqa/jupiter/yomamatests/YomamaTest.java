package com.slickqa.jupiter.yomamatests;

import com.slickqa.jupiter.SlickBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("YomamaTests")
public class YomamaTest extends SlickBaseTest {


    @Test
    @DisplayName("Yomama pass test")
    public void yomamaPassTest() throws Exception {
        System.out.println("Soooo Faaat");
    }

    @Test
    @DisplayName("Yomama fail test")
    public void testYomamaFail() throws Exception {
        Assertions.assertTrue(false);
    }

    @Test
    @DisplayName("Yomama disabled test")
    @Disabled
    public void yomamaDisabledTest() throws Exception {
    }
}
