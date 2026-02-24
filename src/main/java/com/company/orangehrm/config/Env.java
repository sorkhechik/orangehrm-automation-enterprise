package com.company.orangehrm.config;

public final class Env {

    private Env() {
    }

    public static String current() {
        String cli = System.getProperty("env");
        if (cli != null && !cli.isBlank()) {
            return cli.trim();
        }

        String env = System.getenv("ENV");
        if (env != null && !env.isBlank()) {
            return env.trim();
        }

        return "uat"; // default
    }
}
