package club.maxstats.zkesp;

import club.maxstats.zkesp.event.Subscribe;
import club.maxstats.zkesp.event.events.Render3DEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import org.lwjgl.opengl.GL11;

public class RenderListener {
    public RenderListener() {
        MixinLoader.bus.subscribe(this);
    }

    @Subscribe
    public void onRender(Render3DEvent event) {
        if (MixinLoader.toggle.getState()) {
            if (Minecraft.getMinecraft().theWorld != null) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
                GL11.glPushMatrix();

                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                GL11.glEnable(GL11.GL_LINE_SMOOTH);

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                    if (entity instanceof EntityCaveSpider) {
                        GL11.glPushMatrix();

                        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

                        GL11.glTranslated(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) - renderManager.viewerPosX,
                                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) - renderManager.viewerPosY,
                                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) - renderManager.viewerPosZ);

                        GL11.glRotatef(-renderManager.playerViewY, 0F, 1F, 0F);
                        GL11.glRotatef(renderManager.playerViewX, 1F, 0F, 0F);

                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glEnable(GL11.GL_BLEND);

                        GL11.glColor4d(1.0, 0.0, 0.0, 1.0);

                        GL11.glBegin(GL11.GL_LINE_LOOP);
                        GL11.glVertex2d(1, 0);
                        GL11.glVertex2d(-1, 0);
                        GL11.glVertex2d(-1, 1);
                        GL11.glVertex2d(1, 1);
                        GL11.glEnd();

                        GL11.glPopMatrix();
                    }
                }

                GL11.glPopMatrix();
                GL11.glPopAttrib();

                GL11.glColor4f(1F, 1F, 1F, 1F);
            }
        }
    }
}
