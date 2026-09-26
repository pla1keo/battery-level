package com.edgeburnmedia.batterystatusinfo.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = "batterystatusinfo")
public class BatteryStatusInfoConfig implements ConfigData {
    @ConfigEntry.Category("general")
    @ConfigEntry.BoundedDiscrete(max = 99, min = 1)
    int lowBatteryThreshold = 20;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip
    long checkInterval = 5000;

    @ConfigEntry.Category("alerts")
    boolean showLowBatteryAlert = true;

    @ConfigEntry.Category("alerts")
    boolean showFullyChargedAlert = true;

    @ConfigEntry.Category("alerts")
    boolean showChargingAlert = true;

    @ConfigEntry.Category("alerts")
    @ConfigEntry.Gui.PrefixText
    boolean showDischargingAlert = true;

    @ConfigEntry.Category("hud")
    boolean showHud = true;

    @ConfigEntry.Category("hud")
    boolean showHudWhenFullyCharged = true;

    @ConfigEntry.Category("hud")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    Position position = Position.BOTTOM_LEFT;

    @ConfigEntry.Category("hud")
    @ConfigEntry.Gui.Excluded
    double hudX = -1.0d;

    @ConfigEntry.Category("hud")
    @ConfigEntry.Gui.Excluded
    double hudY = -1.0d;
    //	@ConfigEntry.Category("hud")
    //	int hudIconScale = 16;

    public int getHudIconScale() {
        return 16;
    }

    public Position getPosition() {
        return position;
    }

    public boolean hasCustomHudPosition() {
        return hudX >= 0.0d && hudY >= 0.0d;
    }

    public double getHudX() {
        return hudX;
    }

    public double getHudY() {
        return hudY;
    }

    public void setHudPosition(double x, double y) {
        hudX = Math.max(0.0d, Math.min(1.0d, x));
        hudY = Math.max(0.0d, Math.min(1.0d, y));
    }

    public boolean isShowHud() {
        return showHud;
    }

    public boolean isShowHudWhenFullyCharged() {
        return showHudWhenFullyCharged;
    }

    public boolean isShowLowBatteryAlert() {
        return showLowBatteryAlert;
    }

    public boolean isShowFullyChargedAlert() {
        return showFullyChargedAlert;
    }

    public boolean isShowChargingAlert() {
        return showChargingAlert;
    }

    public boolean isShowDischargingAlert() {
        return showDischargingAlert;
    }

    public int getLowBatteryThreshold() {
        return lowBatteryThreshold;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public enum Position {
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        TOP_RIGHT
    }
}
