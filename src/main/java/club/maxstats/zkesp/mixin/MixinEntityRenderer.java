package club.maxstats.zkesp.mixin;

import club.maxstats.zkesp.MixinLoader;
import club.maxstats.zkesp.event.events.Render3DEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
@SideOnly(Side.CLIENT)
public class MixinEntityRenderer {
    @Shadow
    private Minecraft mc;

    @Shadow private Entity pointedEntity;

    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        MixinLoader.bus.callEvent(new Render3DEvent(partialTicks));
    }
}
