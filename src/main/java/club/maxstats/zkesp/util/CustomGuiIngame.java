package club.maxstats.zkesp.util;

import club.maxstats.zkesp.MixinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.MathHelper;

public class CustomGuiIngame extends GuiIngame {
    public CustomGuiIngame(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public void renderGameOverlay(float partialTicks) {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();

        if (Minecraft.isFancyGraphicsEnabled()) {
            this.renderVignette(this.mc.thePlayer.getBrightness(partialTicks), scaledresolution);
        } else {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }

        ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);

        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.renderPumpkinOverlay(scaledresolution);
        }

        if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
            float f = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;

            if (f > 0.0F) {
                this.renderPortal(f, scaledresolution);
            }
        }

        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        } else {
            this.renderTooltip(scaledresolution, partialTicks);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(icons);
        GlStateManager.enableBlend();

        if (this.showCrosshair()) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();

        if (this.mc.playerController.shouldDrawHUD()) {
            this.renderPlayerStats(scaledresolution);
        }

        GlStateManager.disableBlend();

        if (this.mc.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int j1 = this.mc.thePlayer.getSleepTimer();
            float f1 = (float)j1 / 100.0F;

            if (f1 > 1.0F) {
                f1 = 1.0F - (float)(j1 - 100) / 10.0F;
            }

            int k = (int)(220.0F * f1) << 24 | 1052704;
            drawRect(0, 0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int k1 = i / 2 - 91;

        if (this.mc.thePlayer.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, k1);
        } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, k1);
        }

        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.renderSelectedItem(scaledresolution);
        } else if (this.mc.thePlayer.isSpectator()) {
            this.spectatorGui.renderSelectedItem(scaledresolution);
        }

        if (this.mc.isDemo()) {
            this.renderDemo(scaledresolution);
        }

        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }

        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            float f2 = (float)this.recordPlayingUpFor - partialTicks;
            int l1 = (int)(f2 * 255.0F / 20.0F);

            if (l1 > 255) {
                l1 = 255;
            }

            if (l1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int l = 16777215;

                if (this.recordIsPlaying) {
                    l = MathHelper.hsvToRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4, l + (l1 << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        if (this.titlesTimer > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            float f3 = (float)this.titlesTimer - partialTicks;
            int i2 = 255;

            if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
                float f4 = (float)(this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - f3;
                i2 = (int)(f4 * 255.0F / (float)this.titleFadeIn);
            }

            if (this.titlesTimer <= this.titleFadeOut) {
                i2 = (int)(f3 * 255.0F / (float)this.titleFadeOut);
            }

            i2 = MathHelper.clamp_int(i2, 0, 255);

            if (i2 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j / 2), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                int j2 = i2 << 24 & -16777216;
                this.getFontRenderer().drawString(this.displayedTitle, (float)(-this.getFontRenderer().getStringWidth(this.displayedTitle) / 2), -10.0F, 16777215 | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                this.getFontRenderer().drawString(this.displayedSubTitle, (float)(-this.getFontRenderer().getStringWidth(this.displayedSubTitle) / 2), 5.0F, 16777215 | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());

        if (scoreplayerteam != null) {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (i1 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

        if (scoreobjective1 != null) {
            this.renderScoreboard(scoreobjective1, scaledresolution);
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, (float)(j - 48), 0.0F);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);

        if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown() || this.mc.isIntegratedServerRunning() && this.mc.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1 && scoreobjective1 == null) {
            this.overlayPlayerList.updatePlayerList(false);
        } else {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (MixinLoader.toggle.getState()) {
            int yOffset = 2;
            for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if (entity instanceof EntityCaveSpider) {
                    Minecraft.getMinecraft().getRenderManager().getFontRenderer().drawString((int) entity.posX + ", " + (int) entity.posY + ", " + (int) entity.posZ, 2, yOffset, 0xFFFFFF);
                    yOffset += Minecraft.getMinecraft().getRenderManager().getFontRenderer().FONT_HEIGHT + 2;
                }
            }
        }

        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }

}
