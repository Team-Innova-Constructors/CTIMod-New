package com.hoshino.cti.Modifier;

import appeng.client.render.effects.ParticleTypes;
import com.c2h6s.etshtinker.Entities.PlasmaSlashEntity;
import com.c2h6s.etshtinker.hooks.PlasmaSlashCreateModifierHook;
import com.c2h6s.etshtinker.hooks.SlashProcessBlockModifierHook;
import com.c2h6s.etshtinker.init.etshtinkerHook;
import com.hoshino.cti.recipe.RecipeMap;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Optional;

public class CircuitCuttingModifier extends NoLevelsModifier implements SlashProcessBlockModifierHook, PlasmaSlashCreateModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,etshtinkerHook.SLASH_CREATE,etshtinkerHook.SLASH_PROCESS_BLOCK);
    }

    @Override
    public PlasmaSlashEntity plasmaSlashCreate(IToolStackView iToolStackView, FluidStack fluidStack, ServerPlayer serverPlayer, PlasmaSlashEntity plasmaSlashEntity) {
        plasmaSlashEntity.shouldProcessBlock = true;
        return plasmaSlashEntity;
    }

    @Override
    public void processBlockClicked(ToolStack tool, Player player, PlasmaSlashEntity slash, boolean isCritical, float slashDamage, BlockPos pos, BlockState state) {
        var be = player.level.getBlockEntity(pos);
        if (be instanceof DepotBlockEntity depot&&player.level instanceof ServerLevel serverLevel){
            int processMul = isCritical? 64 : (int) (1+slashDamage/50);
            var depotb = depot.getBehaviour(DepotBehaviour.TYPE);
            var stack = depotb.getHeldItemStack();
            if (!stack.isEmpty()){
                int count = Math.min(stack.getCount(),processMul);
                Optional.ofNullable(RecipeMap.getCircuitCuttingRecipes().get(stack.getItem())).ifPresent(recipe->{
                    var output = new ItemStack(recipe.outputPlate,count);
                    var outputPos = Vec3.atBottomCenterOf(pos).add(0,1,0);
                    stack.shrink(count);
                    if (stack.getCount()<=0){
                        depotb.removeHeldItem();
                    }
                    depot.notifyUpdate();
                    var iniAngle = RANDOM.nextFloat()*40;
                    for (int i = 0;i<9;i++){
                        var angle = Math.toRadians(iniAngle+i*40);
                        var velocity = new Vec3(Math.sin(angle),0.02*RANDOM.nextFloat(),Math.cos(angle)).normalize().scale(0.25+0.1*RANDOM.nextFloat());
                        var itemEntity = new ItemEntity(player.level,outputPos.x,outputPos.y,outputPos.z,output.copy());
                        itemEntity.setDeltaMovement(velocity);
                        player.level.addFreshEntity(itemEntity);
                    }
                    serverLevel.sendParticles(ParticleTypes.LIGHTNING.getType(),outputPos.x,outputPos.y,outputPos.z,1+count/16,0,0,0,0);
                    serverLevel.playSound(null,pos, SoundEvents.COPPER_BREAK, SoundSource.PLAYERS,1,1.25f);
                });
            }
        }
    }
}
