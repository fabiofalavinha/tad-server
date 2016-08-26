package org.religion.umbanda.tad.log;

public interface Log {
    boolean isDebugEnabled();

    void info(String message, Object... args);

    void debug(String message, Object... args);

    void warn(String message, Object... args);

    void error(String message, Object... args);

    void fatal(String message, Object... args);

    void exception(Throwable error, String message, Object... args);
}
