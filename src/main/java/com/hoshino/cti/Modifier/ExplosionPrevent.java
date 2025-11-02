package com.hoshino.cti.Modifier;

import com.hoshino.cti.Event.CommonLivingHurt;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import net.minecraftforge.event.level.ExplosionEvent;

public class ExplosionPrevent extends ArmorModifier {
    /**
     * @author firefly
     * <ul><li><h5>具体判定在监听器这边 {@link CommonLivingHurt#PreventExplosionEvent(ExplosionEvent.Start)}</h5></li></ul>
     */
    @Override
    public boolean havenolevel() {
        return true;
    }
}
