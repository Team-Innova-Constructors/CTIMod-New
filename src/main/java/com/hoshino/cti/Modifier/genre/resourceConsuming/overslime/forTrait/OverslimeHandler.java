package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.forTrait;

import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.hoshino.cti.content.materialGenre.GenreManager.OVERSLIME_GENRE;

//用ModifierTraitModule添加的隐藏词条，负责黏液流增伤/减伤的计算
public class OverslimeHandler extends Modifier implements MeleeDamageModifierHook , MeleeHitModifierHook {
    public static final int OVERSLIME_MODIFIER_PRIORITY = 150;
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_ARMOR = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("overslime_armor"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT,ModifierHooks.MELEE_DAMAGE);
        hookBuilder.addModule(new ArmorLevelModule(KEY_ARMOR,false,null));
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    public int getPriority() {
        return 150;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var stats = tool.getStats();
        float consumption = stats.getInt(OVERSLIME_GENRE.consumption);
        float efficiency = stats.getInt(CtiToolStats.EFFICIENCY);
        float power = stats.getInt(CtiToolStats.POWER);
        consumption*=power/efficiency;
        int actualConsumption = (int) consumption+ (RANDOM.nextFloat()<consumption-(int)consumption ? 1 : 0);
        var overslime = TinkerModifiers.overslime.get();
        if (overslime.getShield(tool)<actualConsumption) return damage;
        float baseBonus = stats.get(OVERSLIME_GENRE.baseStat)*power;
        float mulBonus = stats.get(OVERSLIME_GENRE.mulStat)*power;
        damage += baseBonus;
        damage += damage*mulBonus;
        OverslimePostHitHandler.addTask(context,new OverslimePostHitHandler.PostHitTask(tool,context,damage));
        overslime.addOverslime(tool,modifier,-actualConsumption);
        return damage;
    }

    @Override
    public void failedMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageAttempted) {
        this.postHit(context);
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        this.postHit(context);
    }
    public void postHit(ToolAttackContext context){
        OverslimePostHitHandler.resolveTasks(context);
    }

    @Mod.EventBusSubscriber(modid = Cti.MOD_ID)
    public static class OverslimePostHitHandler{
        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event){
            if (event.getServer().getTickCount()%100==0) clearInvalidTasks();
        }
        private static final List<OverslimeExtraDamageListener> LIST_LISTENER = new ArrayList<>();
        private static final Map<ToolAttackContext,PostHitTask> MAP_TASK = new ConcurrentHashMap<>();
        public record PostHitTask(IToolStackView tool, ToolAttackContext context,float damage){
            public boolean isValid(){
                return context.getAttacker().isAlive()&&context.getTarget().isAlive();
            }
            public void processTask(){
                LIST_LISTENER.forEach(listener->
                        listener.doPostHitEffect(tool,context,damage));
            }
        }
        public static void addTask(ToolAttackContext context,PostHitTask task){
            MAP_TASK.put(context,task);
        }
        public static void registerListener(OverslimeExtraDamageListener listener){
            LIST_LISTENER.add(listener);
        }
        public static void resolveTasks(ToolAttackContext context){
            var task = MAP_TASK.get(context);
            if (task!=null){
                if (task.isValid()) task.processTask();
            }
        }
        public static void clearInvalidTasks(){
            new ArrayList<>(MAP_TASK.keySet()).forEach(context -> {
                if (MAP_TASK.get(context)==null) MAP_TASK.remove(context);
                if (MAP_TASK.get(context)!=null&&!MAP_TASK.get(context).isValid()) MAP_TASK.remove(context);
            });
        }
    }
    public static abstract class OverslimeExtraDamageListener{
        public abstract void doPostHitEffect(IToolStackView tool, ToolAttackContext context, float damage);
    }
}
