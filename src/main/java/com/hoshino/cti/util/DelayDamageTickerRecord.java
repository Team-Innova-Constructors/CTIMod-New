package com.hoshino.cti.util;

import java.util.UUID;

public record DelayDamageTickerRecord(UUID attacker, float totalDamage,int traitLevel) {
    public DelayDamageTickerRecord addDamage(float amount) {
        return new DelayDamageTickerRecord(this.attacker, this.totalDamage + amount,traitLevel);
    }
}
