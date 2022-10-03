package io.mineverse.game;

import com.rabbitmq.client.Connection;

public final class BookshelfAPI {
    private static BookshelfAPI instance;

    private static Bookshelf book;

    public BookshelfAPI(Bookshelf bookshelf) {
        book = bookshelf;
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
