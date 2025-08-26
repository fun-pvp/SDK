package fr.funpvp.sdk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private GsonProvider() {
        throw new UnsupportedOperationException("GsonProvider is a utility class and cannot be instantiated.");
    }
}
