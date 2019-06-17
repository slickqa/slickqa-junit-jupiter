package com.slickqa.jupiter.yomamatests;

import com.slickqa.jupiter.SlickBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Yomama tests two")
public class YomamaTestTwo extends SlickBaseTest {


    @Test
    @DisplayName("Yomama pass test 2")
    public void yomamaPassTestTwo() throws Exception {
        System.out.println("Soooo Faaat");
    }

    @Test
    @DisplayName("Yomama fail test 2")
    public void testYomamaFailTwo() throws Exception {
        Assertions.assertTrue(false);
    }

    @Test
    @DisplayName("Yomama disabled test 2")
    @Disabled
    public void yomamaDisabledTestTwo() throws Exception {
    }
}
