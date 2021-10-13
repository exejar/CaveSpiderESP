package club.maxstats.zkesp;

import club.maxstats.zkesp.event.Bus;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class MixinLoader implements IFMLLoadingPlugin {
    public static Bus bus;
    public static ToggleListener toggle;

    public MixinLoader() {
        MixinBootstrap.init();
        Mixins.addConfiguration("zkesp.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        bus = new Bus();
        toggle = new ToggleListener();
        new RenderListener();
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
