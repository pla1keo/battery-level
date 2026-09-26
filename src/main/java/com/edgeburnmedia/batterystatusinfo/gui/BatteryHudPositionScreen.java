package com.edgeburnmedia.batterystatusinfo.gui;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

public class BatteryHudPositionScreen extends Screen {
    private static final int ICON_SIZE = 21;
    private static final int TEXT_GAP = 1;
    private final BatteryStatusInfoConfig config;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;
    private int hudX;
    private int hudY;
    private int hudWidth;
    private int textHeight;
    private String chargeText;

    public BatteryHudPositionScreen(BatteryStatusInfoConfig config) {
        super(Component.translatable("screen.battery-level.hud-editor.title"));
        this.config = config;
    }

    @Override
    protected void init() {
        BatteryStatus status = BatteryStatusInfoModClient.batteryCheckerThread.getBatteryStatus();
        chargeText = BatteryUtils.getChargePercent(status.getCharge()) + "%";
        textHeight = font.lineHeight;
        hudWidth = ICON_SIZE + TEXT_GAP + font.width(chargeText);
        hudX = BatteryHud.getX(width, font.width(chargeText), config.getPosition(), config);
        hudY = BatteryHud.getY(height, config.getPosition(), config);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        BatteryStatus status = BatteryStatusInfoModClient.batteryCheckerThread.getBatteryStatus();
        chargeText = BatteryUtils.getChargePercent(status.getCharge()) + "%";
        hudWidth = ICON_SIZE + TEXT_GAP + font.width(chargeText);
        hudX = clampX(hudX);
        hudY = clampY(hudY);

        graphics.fill(hudX - 3, hudY - 3, hudX + hudWidth + 3, hudY + ICON_SIZE + 3, 0x90000000);
        graphics.outline(hudX - 3, hudY - 3, hudWidth + 6, ICON_SIZE + 6, 0xFFFFFFFF);
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                status.getBatteryIcon(),
                hudX,
                hudY,
                0,
                0,
                ICON_SIZE,
                ICON_SIZE,
                ICON_SIZE,
                ICON_SIZE);
        graphics.text(
                font, chargeText, hudX + ICON_SIZE + TEXT_GAP, hudY + (ICON_SIZE - textHeight) / 2, CommonColors.WHITE);
        graphics.centeredText(
                font,
                Component.translatable("screen.battery-level.hud-editor.instruction"),
                width / 2,
                height - 30,
                CommonColors.WHITE);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT
                && event.x() >= hudX - 3
                && event.x() <= hudX + hudWidth + 3
                && event.y() >= hudY - 3
                && event.y() <= hudY + ICON_SIZE + 3) {
            dragging = true;
            dragOffsetX = (int) event.x() - hudX;
            dragOffsetY = (int) event.y() - hudY;
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (dragging && event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            hudX = clampX((int) event.x() - dragOffsetX);
            hudY = clampY((int) event.y() - dragOffsetY);
            return true;
        }
        return super.mouseDragged(event, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT && dragging) {
            dragging = false;
            savePosition();
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public void onClose() {
        savePosition();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    private int clampX(int x) {
        return Math.max(0, Math.min(x, Math.max(0, width - hudWidth - 1)));
    }

    private int clampY(int y) {
        return Math.max(0, Math.min(y, Math.max(0, height - ICON_SIZE - 1)));
    }

    private void savePosition() {
        double maxX = Math.max(1, width - hudWidth - 1);
        double maxY = Math.max(1, height - ICON_SIZE - 1);
        config.setHudPosition(hudX / maxX, hudY / maxY);
        AutoConfig.getConfigHolder(BatteryStatusInfoConfig.class).save();
    }
}
