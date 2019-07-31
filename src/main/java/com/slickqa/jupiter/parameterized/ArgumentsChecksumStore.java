package com.slickqa.jupiter.parameterized;

import java.util.HashMap;
import java.util.Map;

public class ArgumentsChecksumStore {
    private Map<Long, String> internalStore;

    public ArgumentsChecksumStore() {
        internalStore = new HashMap<>();
    }

    public void putChecksum(Long index, String checksum) {
        internalStore.put(index, checksum);
    }

    public String getChecksum(Long index) {
        return internalStore.get(index);
    }
}
