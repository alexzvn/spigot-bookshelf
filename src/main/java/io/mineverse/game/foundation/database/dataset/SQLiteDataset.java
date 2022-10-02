package io.mineverse.game.foundation.database.dataset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple dataset used to store key, values on database somehow.
 */
public class SQLiteDataset {

    private Connection conn;

    private String table;

    public SQLiteDataset(Connection connection, String table) {
        this.conn = connection;
        this.table = table;

        String query = new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS ")
            .append(table)
            .append(" (")
            .append("id INTEGER PRIMARY KEY AUTOINCREMENT,")
            .append("key VARCHAR(2048),")
            .append("value TEXT,")
            .append("created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
            .append(");")
            .toString();

        try {
            connection.createStatement().execute(query);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String key, String value) throws SQLException {
        String query = "INSERT INTO " + table + " (key, value) VALUES (?, ?);";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, key);
        statement.setString(2, value);
        statement.executeUpdate();
    }

    /**
     * Get data by key and remove it from database.
     */
    public String[] pull(String key) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + table + " WHERE key = ? " + "ORDER BY created_at ASC;");

        statement.setString(1, key);
        statement.execute();

        List<String> data = new ArrayList<>();
        ResultSet result = statement.getResultSet();

        while (result.next()) {
            data.add(result.getString("value"));
        }

        statement = conn.prepareStatement("DELETE FROM " + table + " WHERE key = ?;");
        statement.setString(1, key);
        statement.executeUpdate();

        return data.toArray(new String[0]);
    }

    public void clear() throws SQLException {
        String query = "DELETE FROM " + table + ";";

        Statement statement = conn.createStatement();
        statement.executeUpdate(query);
    }
}
