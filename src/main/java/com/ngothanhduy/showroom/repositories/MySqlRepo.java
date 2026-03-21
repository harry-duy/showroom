package com.ngothanhduy.showroom.repositories;

import com.ngothanhduy.showroom.services.CompanyName;
import com.ngothanhduy.showroom.services.Repo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Repository
public class MySqlRepo implements Repo {
    private final String url;
    private final String username;
    private final String password;

    public MySqlRepo(
            @Value("${mysql.host}") String host,
            @Value("${mysql.port}") int port,
            @Value("${mysql.username}") String username,
            @Value("${mysql.password}") String password,
            @Value("${mysql.database}") String database) {
        this.url = String.format("jdbc:mysql://%s:%d/%s", host, port, database);
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkLogin(String username, String password) {
        var sql = "SELECT username FROM users WHERE username = ? AND password = ?";
        try (
                var conn = getConnection();
                var ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (var rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompanyName loadCompanyName() {
        var sql = "SELECT my_key, my_value FROM settings WHERE my_key = ? OR my_key = ?";
        try (
                var conn = getConnection();
                var ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, "COMPANY_SHORT_NAME");
            ps.setString(2, "COMPANY_LONG_NAME");
            try (var rs = ps.executeQuery()) {
                var shortName = "";
                var longName = "";
                for (var i = 0; i < 2; i++) {
                    if (!rs.next()) {
                        throw new RuntimeException("Company name not configured correctly");
                    }
                    var key = rs.getString("my_key");
                    var value = rs.getString("my_value");
                    if (key.equals("COMPANY_SHORT_NAME")) {
                        shortName = value;
                    } else {
                        longName = value;
                    }
                }
                return new CompanyName(shortName, longName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveCompanyName(CompanyName companyName) {
        var sql = """
        UPDATE settings
        SET my_value = CASE my_key
            WHEN 'COMPANY_SHORT_NAME' THEN ?
            WHEN 'COMPANY_LONG_NAME' THEN ?
            ELSE my_value
        END
        WHERE my_key IN ('COMPANY_SHORT_NAME', 'COMPANY_LONG_NAME')
        """;
        try (
                var conn = getConnection();
                var ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, companyName.shortName());
            ps.setString(2, companyName.longName());
            if (ps.executeUpdate() < 2) {
                throw new RuntimeException("Company name not configured correctly");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}