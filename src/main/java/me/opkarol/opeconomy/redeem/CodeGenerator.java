package me.opkarol.opeconomy.redeem;

import org.jetbrains.annotations.NotNull;

public class CodeGenerator {
    public enum Mode {
        ALPHA, NUMERIC, ALPHANUMERIC;
    }

    public static @NotNull String getString(int length, @NotNull Mode mode) {
        StringBuilder builder = new StringBuilder();
        String string;
        switch (mode) {
            case ALPHA -> string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            case NUMERIC -> string = "123456789";
            case ALPHANUMERIC -> string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
            default -> throw new IllegalStateException("Unexpected value: " + mode);
        }
        for (int i = 0; i < length; i++) {
            double index = Math.random() * string.length();
            builder.append(string.charAt((int) index));
        }
        return builder.toString();
    }
}
