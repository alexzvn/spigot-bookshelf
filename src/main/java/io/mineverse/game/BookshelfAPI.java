package io.mineverse.game;

import com.rabbitmq.client.Connection;

import io.mineverse.game.bookshelf.PlayerRepository;
import io.socket.client.Socket;

public final class BookshelfAPI {
    private static BookshelfAPI instance;

    private static Bookshelf book;

    public BookshelfAPI(Bookshelf bookshelf) {
        book = bookshelf;
    }

    /**
     * @deprecated
     */
    public String lastEventId() {
        return book.getEventRegister().getLastEventId();
    }

    /**
     * @deprecated
     */
    public PlayerRepository getPlayerRepository() {
        return new PlayerRepository();
    }

    /**
     * Get socket connect to bookshelf server.
     * @deprecated
     */
    public Socket getSocket() {
        return book.getBookshelfSocket();
    }

    /**
     * Get rabbitmq queue connection
     * @return
     */
    public Connection getRabbitConnection() {
        return book.getRabbitConnection();
    }

    static void setInstance(BookshelfAPI api) {
        instance = api;
    }

    /**
     * Get the current instance of the BookshelfAPI.
     * @return static or null if not initialized.
     * 
     * @deprecated
     */
    public static BookshelfAPI getInstance() {
        return instance;
    }

    public static BookshelfAPI get() {
        return instance;
    }
}
