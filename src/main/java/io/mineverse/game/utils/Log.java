package io.mineverse.game.utils;

import java.util.logging.Logger;

public class Log {
    public static Logger log() {
        return Instance.logger();
    }

    public static void info(String message) {
        log().info(Message.prefixColor(message));
    }

    public static void warning(String message) {
        log().warning(Message.prefixColor(message));
    }
}
