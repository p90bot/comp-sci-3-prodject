package com.team.db;

import java.nio.file.*;
import java.sql.*;

public class RunSql {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("Pass a .sql file path to run");
        }
        String sql = Files.readString(Path.of(args[0]));
        try (Connection c = Database.get(); Statement s = c.createStatement()) {
            s.executeUpdate(sql);
            System.out.println("Ran SQL file: " + args[0]);
        }
    }
}