package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Represents a time duration value token (e.g., "150ms", "2s").
 */
public class TimeDuration implements Token {
    private final long millis;

    public TimeDuration(String value) {
        this.millis = parseMillis(value);
    }

    private long parseMillis(String value) {
        if (value.endsWith("ms")) return Long.parseLong(value.replace("ms", ""));
        if (value.endsWith("s")) return Long.parseLong(value.replace("s", "")) * 1000;
        if (value.endsWith("min")) return Long.parseLong(value.replace("min", "")) * 60 * 1000;
        if (value.endsWith("hr")) return Long.parseLong(value.replace("hr", "")) * 60 * 60 * 1000;
        return Long.parseLong(value);
    }

    @Override
    public Object value() {
        return millis;
    }

    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(millis);
    }

    public String getMillis() {
        return String.valueOf(millis);
    }
}