package com.hoshino.cti.util;

import java.util.Random;
import java.util.UUID;

public class MathUtil {
    public static UUID getUUIDFromString(String str) {
        int hash = str.hashCode();
        Random random = new Random();
        random.setSeed(hash);
        long l0 = random.nextLong();
        long l1 = random.nextLong();
        return new UUID(l0, l1);
    }
}
