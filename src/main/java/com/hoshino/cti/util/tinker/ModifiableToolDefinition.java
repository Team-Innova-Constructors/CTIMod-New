package com.hoshino.cti.util.tinker;

import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.ToolDefinitionData;

//可在创建实例时就传入已经写好了的ToolDefinitionData的ToolDefinition。不需要继承这个类，直接new就好了。
public class ModifiableToolDefinition extends ToolDefinition {
    public ModifiableToolDefinition(ResourceLocation id, ToolDefinitionData data) {
        super(id);
        this.data = data;
    }
    //阻止其它方法修改我我们的ToolDefinitionData，因为它是固定的
    @Override
    public void setData(ToolDefinitionData data) {
    }
    @Override
    protected void clearData() {
    }
}
