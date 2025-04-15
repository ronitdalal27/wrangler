package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Represents a byte size value token (e.g., "10KB", "15MB").
 */
public class ByteSize implements Token {
    private final long bytes;

    public ByteSize(String value) {
        this.bytes = parseBytes(value);
    }

    private long parseBytes(String value) {
        if (value.endsWith("KB")) return Long.parseLong(value.replace("KB", "")) * 1024;
        if (value.endsWith("MB")) return Long.parseLong(value.replace("MB", "")) * 1024 * 1024;
        if (value.endsWith("GB")) return Long.parseLong(value.replace("GB", "")) * 1024 * 1024 * 1024;
        if (value.endsWith("TB")) return Long.parseLong(value.replace("TB", "")) * 1024L * 1024L * 1024L * 1024L;
        return Long.parseLong(value);
    }

    @Override
    public Object value() {
        return bytes;
    }

    @Override
    public TokenType type() {
        return TokenType.BYTE_SIZE;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(bytes);
    }

    public String getBytes() {
        return String.valueOf(bytes);
    }
}