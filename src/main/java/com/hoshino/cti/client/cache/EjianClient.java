package com.hoshino.cti.client.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class EjianClient {

    private static final Map<UUID, Integer> REMAINING = new HashMap<>();

    public static void add(UUID playerId, int duration) {
        REMAINING.put(playerId, Math.max(1, duration));
    }

    public static void remove(UUID playerId) {
        REMAINING.remove(playerId);
    }

    public static boolean isActive(UUID playerId) {
        Integer remaining = REMAINING.get(playerId);
        return remaining != null && remaining > 0;
    }

    public static void tick() {
        if (REMAINING.isEmpty()) {
            return;
        }
        REMAINING.replaceAll((id, remaining) -> remaining - 1);
        REMAINING.entrySet().removeIf(entry -> entry.getValue() <= 0);
    }

    public static void clear() {
        REMAINING.clear();
    }
}
