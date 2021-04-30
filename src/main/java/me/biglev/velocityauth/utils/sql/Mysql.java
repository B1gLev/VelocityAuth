package me.biglev.velocityauth.utils.sql;

import com.zaxxer.hikari.HikariDataSource;
import me.biglev.velocityauth.utils.settings.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql {

    private HikariDataSource hikariDataSource;

    public void setupDatabase() {

        try {
            hikariDataSource = new HikariDataSource();

            hikariDataSource.setMaximumPoolSize(10);
            hikariDataSource.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            hikariDataSource.addDataSourceProperty("serverName", Manager.getSettings().getDatabase().getHost());
            hikariDataSource.addDataSourceProperty("port", Manager.getSettings().getDatabase().getPort());
            hikariDataSource.addDataSourceProperty("databaseName", Manager.getSettings().getDatabase().getDatabase());
            hikariDataSource.addDataSourceProperty("user", Manager.getSettings().getDatabase().getUsername());
            hikariDataSource.addDataSourceProperty("password", Manager.getSettings().getDatabase().getPassword());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }

    public void createTable() {
        Connection con;
        Statement st;

        String table = "CREATE TABLE IF NOT EXISTS `auth` (\n" +
                "\t`id` int(10) NOT NULL auto_increment,\n" +
                "\t`realname` text,\n" +
                "\t`uuid` longtext,\n" +
                "\t`password` longtext,\n" +
                "\t`premium` boolean,\n" +
                "\t`ipaddress` text,\n" +
                "\tPRIMARY KEY( `id` )\n" +
                ");";

        try {
            con = getHikariDataSource().getConnection();
            st = con.createStatement();
            st.executeUpdate(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
