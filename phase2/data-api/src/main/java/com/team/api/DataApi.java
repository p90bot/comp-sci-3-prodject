package com.team.api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataApi {
    private static final int PORT = 7001;

    // ✅ Adjusted path: explicitly relative to current working directory
    private static final String DB_PATH = "./project.db";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Health check
        server.createContext("/health", exchange -> sendJson(exchange, 200, "\"OK\""));

        // Countries endpoint
        server.createContext("/countries", exchange -> {
            try {
                List<String> data = getCountries();
                String json = "[" + String.join(",", data) + "]";
                sendJson(exchange, 200, json);
            } catch (Exception e) {
                e.printStackTrace();
                sendJson(exchange, 500, "\"error: " + e.getMessage() + "\"");
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("✅ Data API server running on http://localhost:" + PORT);
    }

    private static List<String> getCountries() throws SQLException {
        List<String> results = new ArrayList<>();

        // ✅ Load driver explicitly for reliability
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }

        // ✅ Correct JDBC connection string
        String url = "jdbc:sqlite:" + DB_PATH;

        String query = """
            SELECT cca2, name_common, region, subregion, population
            FROM countries
            LIMIT 25
        """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String cca2 = safe(rs.getString("cca2"));
                String name = safe(rs.getString("name_common"));
                String region = safe(rs.getString("region"));
                String subregion = safe(rs.getString("subregion"));
                int population = rs.getInt("population");

                String rowJson = String.format(
                        "{\"cca2\":\"%s\",\"name_common\":\"%s\",\"region\":\"%s\",\"subregion\":\"%s\",\"population\":%d}",
                        cca2, name, region, subregion, population
                );

                results.add(rowJson);
            }
        }

        return results;
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.replace("\"", "\\\"");
    }

    private static void sendJson(HttpExchange exchange, int status, String body) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");

        byte[] bytes = body.getBytes("UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}