package com.hoshino.cti.client.hud;

public class EnvironmentalPlayerData {
    public static float PLAYER_IONIZE;
    public static double IONIZE_BUILD;

    public static void setIonizeValue(float amount, double amount2) {
        PLAYER_IONIZE = amount;
        IONIZE_BUILD = amount2;
    }

    public static float getIonizeValue() {
        return PLAYER_IONIZE;
    }

    public static double getIonizeBuild() {
        return IONIZE_BUILD;
    }

    public static float SCORCH;
    public static double SCORCH_BUILD;

    public static void setScorchValue(float amount, double amount2) {
        SCORCH = amount;
        SCORCH_BUILD = amount2;
    }

    public static float getScorchValue() {
        return SCORCH;
    }

    public static double getScorchBuild() {
        return SCORCH_BUILD;
    }

    public static float FROZEN;
    public static double FROZEN_BUILD;

    public static void setFrozenValue(float amount, double amount2) {
        FROZEN = amount;
        FROZEN_BUILD = amount2;
    }

    public static float getFrozenValue() {
        return FROZEN;
    }

    public static double getFrozenBuild() {
        return FROZEN_BUILD;
    }

    public static float PRESSURE;
    public static double PRESSURE_BUILD;

    public static void setPressureValue(float amount, double amount2) {
        PRESSURE = amount;
        PRESSURE_BUILD = amount2;
    }

    public static float getPressureValue() {
        return PRESSURE;
    }

    public static double getPressureBuild() {
        return PRESSURE_BUILD;
    }
}
