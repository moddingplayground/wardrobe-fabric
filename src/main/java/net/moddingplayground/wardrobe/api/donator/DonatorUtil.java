package net.moddingplayground.wardrobe.api.donator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.moddingplayground.wardrobe.api.Wardrobe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public interface DonatorUtil {
    String SOURCE_URL = "https://gist.githubusercontent.com/andantet/45e21724086c75a5b0de71261893dfd0/raw/source.txt";

    static String getStringFromUrl(String rawUrl) {
        try {
            InputStream stream = new URL(rawUrl).openStream();

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));

            String line;
            List<String> lines = new ArrayList<>();
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }

            in.close();
            stream.close();

            StringBuilder str = new StringBuilder();
            for (int i = 0, l = lines.size(); i < l; i++) {
                str.append(lines.get(i));
                if (i != l - 1) str.append("\n");
            }

            return str.toString();
        } catch (MalformedURLException e) {
            Wardrobe.LOGGER.error("Malformed URL: {}", e.getMessage());
        } catch (IOException e) {
            Wardrobe.LOGGER.error("I/O Error: {}", e.getMessage());
        }

        return "";
    }

    static JsonObject getJsonObjectFromUrl(String rawUrl) {
        JsonElement json = JsonParser.parseString(getStringFromUrl(rawUrl));
        if (json instanceof JsonObject object) {
            return object;
        }

        throw new JsonParseException("Source was not a json object");
    }

    static JsonObject getJsonObjectForUser(String uuid) {
        try {
            return getJsonObjectFromUrl("%s%s.json".formatted(DonatorUtil.getDataUrlFromSource(), uuid));
        } catch (JsonParseException ignored) {}
        return DonatorUtil.constructDefaultJsonForUser(uuid);
    }

    static JsonObject constructDefaultJsonForUser(String uuid) {
        JsonObject json = new JsonObject();
        json.add("uuid", new JsonPrimitive(uuid));
        return json;
    }

    static String getDataUrlFromSource() {
        return getStringFromUrl(SOURCE_URL);
    }
}
