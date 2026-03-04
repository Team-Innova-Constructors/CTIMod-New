package com.hoshino.cti.util.tinker;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.ErrorFactory;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.ToolDefinitionData;

import java.util.List;

//可以用Java实现功能的ToolDefinitionData，使用方式类似于Modifier，在子类中实现ToolHook并registerHook
public class ModifiableToolDefinitionData extends ToolDefinitionData {
    private final ModuleHookMap hooks;

    public ModifiableToolDefinitionData() {
        super(List.of(), ErrorFactory.RUNTIME);
        ModuleHookMap.Builder hookBuilder = ModuleHookMap.builder();
        this.registerHook(hookBuilder);
        this.hooks = hookBuilder.build();
    }

    @Override
    public @NotNull ModuleHookMap getHooks() { return hooks; }

    public <T> @NotNull T getHook(@NotNull ModuleHook<T> hook) {
        return this.hooks.getOrDefault(hook);
    }

    public void registerHook(ModuleHookMap.Builder builder){
    }

}
