package com.hoshino.cti.api.interfaces;

import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;

/** 用于服务端→客户端同步热交换器状态。 */
public interface IMachineHeatExchangerProvider {
    IHeatExchangerLogic getHeatExchanger();
}