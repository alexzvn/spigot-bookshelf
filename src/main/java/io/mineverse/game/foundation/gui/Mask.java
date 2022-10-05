package io.mineverse.game.foundation.gui;

import java.util.ArrayList;
import java.util.List;

abstract public class Mask {

    protected final BaseGUI gui;

    protected List<String> patterns = new ArrayList<>();

    public Mask(BaseGUI gui) {
        this.gui = gui;
    }

    public Mask pattern(String pattern) {
        patterns.add(pattern);
        return this;
    }

    abstract public void apply();
}
