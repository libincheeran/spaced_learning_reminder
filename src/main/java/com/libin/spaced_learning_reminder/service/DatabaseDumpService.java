package com.libin.spaced_learning_reminder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@Service
public class DatabaseDumpService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${app.dump.path:sql/leetcode_data.sql}")
    private String dumpPath;

    private int dumpCounter = 0;
    private static final int LOG_INTERVAL = 25;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void scheduledDatabaseDump() {
        log.info("Starting scheduled database dump");
        createDatabaseDump();
    }

    public void createDatabaseDump() {
        try {
            Path absoluteDumpPath = Paths.get(dumpPath).toAbsolutePath();
            
            // Create the SQL file
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 Statement stmt = conn.createStatement();
                 FileWriter writer = new FileWriter(absoluteDumpPath.toFile())) {
                
                // Write table structure
                writer.write("-- MySQL dump\n");
                writer.write("-- Database: leetcode\n\n");
                writer.write("DROP TABLE IF EXISTS `leetcode_problems`;\n");
                writer.write("CREATE TABLE `leetcode_problems` (\n");
                writer.write("  `id` int NOT NULL AUTO_INCREMENT,\n");
                writer.write("  `comments` varchar(255) DEFAULT NULL,\n");
                writer.write("  `description` varchar(255) DEFAULT NULL,\n");
                writer.write("  `problem_id` int NOT NULL,\n");
                writer.write("  `scheduled_time` datetime(6) DEFAULT NULL,\n");
                writer.write("  PRIMARY KEY (`id`)\n");
                writer.write(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n\n");
                
                // Write data
                writer.write("-- Dumping data for table `leetcode_problems`\n\n");
                writer.write("LOCK TABLES `leetcode_problems` WRITE;\n");
                
                ResultSet rs = stmt.executeQuery("SELECT * FROM leetcode_problems");
                while (rs.next()) {
                    writer.write(String.format(
                        "INSERT INTO `leetcode_problems` VALUES (%d,%s,%s,%d,%s);\n",
                        rs.getInt("id"),
                        rs.getString("comments") != null ? "'" + rs.getString("comments").replace("'", "''") + "'" : "NULL",
                        rs.getString("description") != null ? "'" + rs.getString("description").replace("'", "''") + "'" : "NULL",
                        rs.getInt("problem_id"),
                        rs.getTimestamp("scheduled_time") != null ? "'" + rs.getTimestamp("scheduled_time") + "'" : "NULL"
                    ));
                }
                
                writer.write("UNLOCK TABLES;\n");
                
                // Only log success every 25th time
                dumpCounter++;
                if (dumpCounter % LOG_INTERVAL == 0) {
                    log.info("Database dump created successfully at {}", absoluteDumpPath);
                }
            }
        } catch (Exception e) {
            log.error("Error creating database dump", e);
        }
    }
} 