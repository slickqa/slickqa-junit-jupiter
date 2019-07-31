package com.slickqa.jupiter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.AnnotationUtils;

import java.util.HashMap;
import java.util.List;

@DisplayName("SlickBaseTest Tests")
public class BaseTestTest {
    @Test
    @DisplayName("Base Test Applies Slick Extensions")
    public void testBaseTestAppliesExtensions() throws Exception {
        List<ExtendWith> extensions = AnnotationUtils.findRepeatableAnnotations(SlickBaseTest.class, ExtendWith.class);
        HashMap<Class, Boolean> foundExtensions = new HashMap<>();
        foundExtensions.put(SlickTestWatcher.class, false);
        foundExtensions.put(BeforeEachExtension.class, false);

        for(ExtendWith annotation : extensions) {
            for(Class extension : annotation.value()) {
                if (foundExtensions.containsKey(extension)) {
                    foundExtensions.put(extension, true);
                }
            }
        }

        for(Class expectedExtension : foundExtensions.keySet()) {
            assertTrue(foundExtensions.get(expectedExtension), "Extension " + expectedExtension.getName() + " was not found on SlickBaseTest, but should have been applied with @ExtendWith!");
        }
    }
}
