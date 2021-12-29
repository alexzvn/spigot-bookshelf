package io.mineverse.game;

import java.io.File;
import java.io.IOException;

import io.mineverse.game.foundation.Event;
import io.mineverse.game.utils.Instance;
import io.mineverse.game.utils.Log;
import io.mineverse.game.utils.Util;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class EventRegister {

    protected File lastEventId = Util.file("last-event.id");

    protected Socket socket;

    public EventRegister() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void init() throws IOException {
        if (lastEventId.exists() == false) {
            lastEventId.createNewFile();
        }
    }

    public void bind(Socket socket, String token) {
        this.socket = socket;

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.warning("Can't connect to the bookshelf server!");
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("authenticate", token);
            }
        });

        socket.on("authenticated", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.info("Authenticated with bookshelf server.");

                socket.emit("ready", getLastEventId());
            }
        });
    }

    public void dispatchConfirm(Event event, String id) {
        Instance.dispatchEvent(event);
        confirm(id);
        putLastStringId(id);
    }

    /**
     * Confirm id of event that proccesed from bookshelf server
     * It's needed because server wait for confirmation before send next event
     */
    protected void confirm(String id) {
        if (socket.connected()) socket.emit("confirm", id);
    }

    public String getLastEventId() {
        return Util.readFile(lastEventId).trim();
    }

    protected void putLastStringId(String id) {
        Util.writeFile(lastEventId, id);
    }
}
