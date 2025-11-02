package com.hoshino.cti.Modifier;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;

public class Qcf extends BattleModifier {
    /**
     * @author firefly
     * <h6>具体实现在Mixin内</h6>
     * @see com.hoshino.cti.mixin.L2.TeleportMixin
     */
    @Override
    public boolean havenolevel() {
        return true;
    }
}
