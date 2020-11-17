package main.database;

import main.RssMain;
import main.database.data.RssAnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseManager {

    private static final Logger logger = LoggerFactory.getLogger(RssMain.class);
    //TO-DO add logging on all project

    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    public DatabaseManager() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("Cannot load DB driver");
        }

        createTable();
    }

    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS RSS_ANALYSE " +
                "(ID CHAR(36) PRIMARY KEY," +
                "URLS VARCHAR(200)," +
                "TOPIC1 VARCHAR(30)," +
                "TOPIC2 VARCHAR(30)," +
                "TOPIC3 VARCHAR(30));";

        Connection connection = null;

        try {
            connection = DriverManager.
                    getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
            logger.info("Table created.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertIntoAnalyse(RssAnalysisResult rssAnalysisResult) {
        String insertAnalyseSql = "INSERT INTO RSS_ANALYSE VALUES (?, ?, ?, ?, ?)";

        Connection connection = null;

        try {
            connection = DriverManager.
                    getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(insertAnalyseSql)) {
            preparedStatement.setString(1, rssAnalysisResult.getUuid());
            preparedStatement.setString(2, rssAnalysisResult.getUrls());
            preparedStatement.setString(3, rssAnalysisResult.getTopic1());
            preparedStatement.setString(4, rssAnalysisResult.getTopic2());
            preparedStatement.setString(5, rssAnalysisResult.getTopic3());

            int numberOfRows = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public RssAnalysisResult getRssAnalysisById(String id) {
        String selectRowSql = "SELECT ID, URLS, TOPIC1, TOPIC2, TOPIC3 FROM RSS_ANALYSE WHERE ID=?;";

        Connection connection = null;
        RssAnalysisResult rssAnalysisResult = null;

        try {
            connection = DriverManager.
                    getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(selectRowSql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String uuid = resultSet.getString("ID");
                String urls = resultSet.getString("URLS");
                String topic1 = resultSet.getString("TOPIC1");
                String topic2 = resultSet.getString("TOPIC2");
                String topic3 = resultSet.getString("TOPIC3");

                rssAnalysisResult = new RssAnalysisResult(uuid, urls, topic1, topic2, topic3);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return rssAnalysisResult;
    }
}
