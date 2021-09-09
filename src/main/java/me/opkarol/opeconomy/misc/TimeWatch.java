package me.opkarol.opeconomy.misc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TimeWatch {
    long starts;

    @Contract(" -> new")
    public static @NotNull TimeWatch start() {
        return new TimeWatch();
    }

    public TimeWatch() {
        reset();
    }

    public void reset() {
        starts = System.currentTimeMillis();
    }

    public long time() {
        long ends = System.currentTimeMillis();
        return ends - starts;
    }

    public long time(@NotNull TimeUnit unit) {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
}
