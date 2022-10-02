package io.mineverse.game.foundation.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.mineverse.game.foundation.database.dataset.SQLiteDataset;

public class SQLite {

    protected Connection connection;

    public SQLite(File sqlite) throws SQLException, IOException {
        init(sqlite);
    }

    public void init(File file) throws SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    public SQLiteDataset getDataset(String table) {
        return new SQLiteDataset(connection, table);
    }
}
