package io.mineverse.game;

import io.socket.client.Socket;

public final class BookshelfAPI {
    private static BookshelfAPI instance;

    private static Bookshelf book;

    public BookshelfAPI(Bookshelf bookshelf) {
        book = bookshelf;
    }

    /**
     * Get last id of event processed from bookshelf server.
     * @return
     */
    public String lastEventId() {
        return book.getEventRegister().getLastEventId();
    }

    /**
     * Get socket connect to bookshelf server.
     * @return
     */
    public Socket getSocket() {
        return book.getBookshelfSocket();
    }

    public static void setInstance(BookshelfAPI api) {
        instance = api;
    }

    /**
     * Get the current instance of the BookshelfAPI.
     * @return static or null if not initialized.
     */
    public static BookshelfAPI getInstance() {
        return instance;
    }
}
