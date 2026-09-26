package com.edgeburnmedia.batterystatusinfo.gui;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;

public class BatteryHud {
    private static final Minecraft client = Minecraft.getInstance();
    private static final int SCALE = 21;
    private static final int PERCENT_COLOUR = CommonColors.WHITE;
    private final BatteryStatusInfoConfig config;

    public BatteryHud(BatteryStatusInfoConfig config) {
        this.config = config;
    }

    public void render(BatteryStatus status, GuiGraphicsExtractor drawContext) {
        if (!config.isShowHud()) {
            return;
        }

        if (!config.isShowHudWhenFullyCharged() && status.isFullyCharged()) {
            return;
        }

        final int windowWidth = client.getWindow().getGuiScaledWidth();
        final int windowHeight = client.getWindow().getGuiScaledHeight();

        String text = BatteryUtils.getChargePercent(status.getCharge()) + "%";

        BatteryStatusInfoConfig.Position position = config.getPosition();

        final Identifier texture = status.getBatteryIcon();

        int textWidth = client.font.width(text);
        int textHeight = client.font.lineHeight;

        int x = getX(windowWidth, textWidth, position, config);
        int y = getY(windowHeight, position, config);
        drawContext.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, SCALE, SCALE, SCALE, SCALE);
        drawContext.text(client.font, text, x + 22, y + (SCALE - textHeight) / 2, PERCENT_COLOUR);
    }

    public static int getX(
            int screenWidth, int textWidth, BatteryStatusInfoConfig.Position position, BatteryStatusInfoConfig config) {
        int maxX = Math.max(0, screenWidth - SCALE - 2 - textWidth);
        if (config.hasCustomHudPosition()) {
            return (int) Math.round(config.getHudX() * maxX);
        }
        return switch (position) {
            case TOP_LEFT, BOTTOM_LEFT -> 1;
            case TOP_RIGHT, BOTTOM_RIGHT -> maxX;
        };
    }

    public static int getY(
            int screenHeight, BatteryStatusInfoConfig.Position position, BatteryStatusInfoConfig config) {
        int maxY = Math.max(0, screenHeight - SCALE - 1);
        if (config.hasCustomHudPosition()) {
            return (int) Math.round(config.getHudY() * maxY);
        }
        return switch (position) {
            case TOP_LEFT, TOP_RIGHT -> 0;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> maxY;
        };
    }
}
