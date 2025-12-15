package com.clibo.utils;

import java.util.UUID;

public class CodeGenerator {
    public static String generateTestRequestCode() {
        return "TR-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
