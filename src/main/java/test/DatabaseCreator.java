package test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseCreator {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.username}")
    private String rootUser;
    @Value("${spring.datasource.password}")
    private String rootPassword;

    // ✅ DB 생성 메서드
    public boolean createDatabaseIfNotExists(String dbName) {
        String url = "jdbc:mysql://localhost:3306/?serverTimezone=Asia/Seoul";
        String sql = "CREATE DATABASE IF NOT EXISTS " + dbName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

        try (Connection conn = DriverManager.getConnection(url, rootUser, rootPassword);
             Statement stmt = conn.createStatement()) {
            Class.forName(driverClassName);
            stmt.executeUpdate(sql);
            System.out.println("✅ Database created or already exists: " + dbName);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Database creation failed: " + e.getMessage());
            return false;
        }
    }
}