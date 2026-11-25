package com.hoshino.cti.util.mixin;

public interface IPlayerMixin {
    void cti$preventNextAttackTickerReset();
    boolean cti$isNextAttackTickerResetPrevented();
}
