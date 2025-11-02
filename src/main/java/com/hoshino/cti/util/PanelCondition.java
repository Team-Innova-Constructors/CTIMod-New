package com.hoshino.cti.util;

public enum PanelCondition {
    UPWARD("info.cti.panel_condition.upward"),
    DOWNWARD("info.cti.panel_condition.downward"),
    NULL("")
    ;
    public final String description;
    PanelCondition(String description){
        this.description =description;
    }
}
