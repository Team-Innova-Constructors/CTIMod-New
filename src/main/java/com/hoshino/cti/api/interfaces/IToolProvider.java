package com.hoshino.cti.api.interfaces;

import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public interface IToolProvider {
    void cti$setTool(IToolStackView tool);
    IToolStackView cti$getTool();
}
