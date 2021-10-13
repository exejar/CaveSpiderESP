package club.maxstats.zkesp.mixin;

import club.maxstats.zkesp.MixinLoader;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
@SideOnly(Side.CLIENT)
public class MixinRendererLivingEntity {

    @Inject(method = "doRender", at = @At("HEAD"))
    private <T extends EntityLivingBase> void doRenderHead(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        if (MixinLoader.toggle.getState()) {
            if (entity instanceof EntityCaveSpider) {
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(1.0F, -1000000F);
            }
        }
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    private <T extends EntityLivingBase> void doRenderReturn(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        if (MixinLoader.toggle.getState()) {
            if (entity instanceof EntityCaveSpider) {
                GL11.glPolygonOffset(1.0F, 1000000F);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            }
        }
    }

}
