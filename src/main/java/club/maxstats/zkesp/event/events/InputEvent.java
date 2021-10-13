package club.maxstats.zkesp.event.events;

import club.maxstats.zkesp.MixinLoader;

public class InputEvent {
    public InputEvent(int keycode) {
        this.keycode = keycode;
    }

    private int keycode;

    public int getKeycode() { return this.keycode; }

    public static class KeyEvent extends InputEvent {
        public KeyEvent(int keycode) {
            super(keycode);
            MixinLoader.bus.callEvent(new InputEvent(keycode));
        }
    }

    public static class MouseEvent extends InputEvent {
        public MouseEvent(int keycode) {
            super(keycode);
            MixinLoader.bus.callEvent(new InputEvent(-100 + keycode));
        }
    }
}
