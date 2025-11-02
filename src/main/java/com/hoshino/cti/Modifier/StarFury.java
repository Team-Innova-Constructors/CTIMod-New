package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Entity.Projectiles.FriendlyMeteor;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PStarFuryC2S;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.c2h6s.etshtinker.util.vecCalc.getNearestLiEnt;
import static com.c2h6s.etshtinker.util.vecCalc.getScatteredVec3;
import static com.hoshino.cti.Cti.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StarFury extends EtSTBaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        Player player = event.getEntity();
        if (event.getSide()== LogicalSide.SERVER&&event.getItemStack().getItem() instanceof IModifiable){
            ToolStack toolStack = ToolStack.from(event.getItemStack());
            if (toolStack.getModifierLevel(CtiModifiers.STAR_FURY.get())>0){
                LivingEntity living = getNearestLiEnt(toolStack.getModifierLevel(CtiModifiers.STAR_FURY.get())+4f,player,player.level);
                if (living!=null) {
                    float baseDamage = toolStack.getStats().get(ToolStats.ATTACK_DAMAGE);
                    float damage = baseDamage;
                    ToolAttackContext context = new ToolAttackContext(player, player, event.getHand(), living, living, false, 1, false);
                    for (ModifierEntry entry : toolStack.getModifierList()) {
                        damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(toolStack, entry, context, baseDamage, damage);
                    }
                    StarFury.summonMeteor(player, toolStack.getModifierLevel(CtiModifiers.STAR_FURY.get()), living, damage);
                }
            }
        }
    }
    @SubscribeEvent
    public static void leftClickEmpty(PlayerInteractEvent.LeftClickEmpty event){
        if (event.getSide()==LogicalSide.CLIENT&&event.getHand()==InteractionHand.MAIN_HAND&&event.getItemStack().getItem() instanceof IModifiable){
            ToolStack toolStack = ToolStack.from(event.getItemStack());
            if (toolStack.getModifierLevel(CtiModifiers.STAR_FURY.get())>0){
                CtiPacketHandler.sendToServer(new PStarFuryC2S());
            }
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (context.isFullyCharged() && context.getAttacker() instanceof Player player&&context.getTarget() instanceof LivingEntity target) {
            summonMeteor(player,modifier.getLevel(),target,damage/4);
        }
        return knockback;
    }


    public static void summonMeteor(Player player, int modifierLevel, Entity target, float damage){
        int count =1 + RANDOM.nextInt(modifierLevel*2);
        for (int i=0;i<count;i++){
            Level level = player.level;
            Vec3 vec3 = getScatteredVec3(new Vec3(0, -0.25, 0), 0.57735);
            double d = RANDOM.nextDouble() * 20;
            Vec3 direction = new Vec3(-(30 + d) * vec3.x, -(30 + d) * vec3.y, -(30 + d) * vec3.z);
            FriendlyMeteor meteor = new FriendlyMeteor(level,target.getX() + direction.x, target.getY() + target.getBbHeight() + direction.y, target.getZ() + direction.z,vec3);
            meteor.setOwner(player);
            meteor.baseDamage=damage/2;
            level.addFreshEntity(meteor);
        }
    }
    public static void summonMeteor(Player player){
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (player.getAttackStrengthScale(0)>0.8&&stack.getItem() instanceof IModifiable) {
            ToolStack toolStack = ToolStack.from(stack);
            if (toolStack.getModifierLevel(CtiModifiers.STAR_FURY.get())>0) {
                LivingEntity living = getNearestLiEnt(toolStack.getModifierLevel(CtiModifiers.STAR_FURY.get()) + 4f, player, player.level);
                if (living!=null) {
                    float baseDamage = toolStack.getStats().get(ToolStats.ATTACK_DAMAGE);
                    float damage = baseDamage;
                    ToolAttackContext context = new ToolAttackContext(player, player, InteractionHand.MAIN_HAND, living, living, false, 1, false);
                    for (ModifierEntry entry : toolStack.getModifierList()) {
                        damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(toolStack, entry, context, baseDamage, damage);
                    }
                    summonMeteor(player, toolStack.getModifierLevel(CtiModifiers.STAR_FURY.get()), living, damage/4);
                }
            }
        }
    }
}
