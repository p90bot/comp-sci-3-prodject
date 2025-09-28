package com.team.db.countries;

import com.team.db.Database;
import java.nio.file.*;
import java.sql.*;

public class CountryVerifier {
    public static void main(String[] args) throws Exception {
        // Read summary file
        String text = Files.readString(Path.of("data/summaries/countries_summary.txt"));
        int expected = parseCount(text);

        // Count rows in database
        int actual;
        try (Connection c = Database.get();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM countries")) {
            actual = rs.getInt(1);
        }

        // Compare
        if (expected == actual) {
            System.out.println("OK ✅ Summary matches table count (" + actual + ")");
        } else {
            System.out.println("MISMATCH ⚠️ Summary=" + expected + " table=" + actual);
        }
    }

    // Helper: read "records=N" from summary file
    static int parseCount(String text) {
        for (String line : text.split("\\n")) {
            if (line.startsWith("records=")) {
                return Integer.parseInt(line.split("=")[1]);
            }
        }
        return -1; // fallback
    }
}