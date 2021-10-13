package club.maxstats.zkesp.mixin;

import club.maxstats.zkesp.MixinLoader;
import club.maxstats.zkesp.event.events.InputEvent;
import club.maxstats.zkesp.util.CustomGuiIngame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public class MixinMinecraft {
    @Shadow public GuiScreen currentScreen;
    @Shadow public GuiIngame ingameGUI;

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    private void onKey(CallbackInfo callbackInfo) {
        if (Keyboard.getEventKeyState() && currentScreen == null) {
            MixinLoader.bus.callEvent(new InputEvent.KeyEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;fireMouseInput()V", shift = At.Shift.BEFORE))
    private void onMouse(CallbackInfo callbackInfo) {
        if (Mouse.getEventButtonState() && currentScreen == null) {
            MixinLoader.bus.callEvent(new InputEvent.MouseEvent(Mouse.getEventButton()));
        }
    }

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    private void startGame(CallbackInfo callbackInfo) {
        this.ingameGUI = new CustomGuiIngame(Minecraft.getMinecraft());
    }
}
