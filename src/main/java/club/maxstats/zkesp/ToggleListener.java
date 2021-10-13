package club.maxstats.zkesp;

import club.maxstats.zkesp.event.Subscribe;
import club.maxstats.zkesp.event.events.InputEvent;
import org.lwjgl.input.Keyboard;

public class ToggleListener {
    private boolean state;
    /* you can change this to whatever you want...just recompile */
    private int KEY = Keyboard.KEY_LMENU;

    public ToggleListener() {
        MixinLoader.bus.subscribe(this);
    }

    @Subscribe
    public void onInput(InputEvent event) {
        if (event.getKeycode() == KEY) {
            this.toggle();
        }
    }

    public boolean getState() { return this.state; }

    public void toggle() { this.state = !this.state; }
}
