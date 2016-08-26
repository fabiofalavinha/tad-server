package org.religion.umbanda.tad.log;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.MissingFormatArgumentException;

class Slf4jImpl implements Log {
    private final Logger logger;

    public Slf4jImpl(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }

    public Slf4jImpl(Class<?> clazz) {
        this(clazz.getName());
    }

    @Override
    public void info(String message, Object... args) {
        try {
            String logMessage = message;
            if (args != null && args.length > 0) {
                logMessage = String.format(logMessage, args);
            }
            logger.info(logMessage);
        } catch (MissingFormatArgumentException ex) {
            logger.error(String.format("Error logging info message [%s]. Argument is missing!", message));
        }
    }

    @Override
    public void debug(String message, Object... args) {
        try {
            String logMessage = message;
            if (args != null && args.length > 0) {
                logMessage = String.format(logMessage, args);
            }
            logger.debug(logMessage);
        } catch (MissingFormatArgumentException ex) {
            logger.error(String.format("Error logging debug message [%s]. Argument is missing!", message));
        }
    }

    @Override
    public void warn(String message, Object... args) {
        try {
            String logMessage = message;
            if (args != null && args.length > 0) {
                logMessage = String.format(logMessage, args);
            }
            logger.warn(logMessage);
        } catch (MissingFormatArgumentException ex) {
            logger.error(String.format("Error logging warning message [%s]. Argument is missing!", message));
        }
    }

    @Override
    public void error(String message, Object... args) {
        try {
            String logMessage = message;
            if (args != null && args.length > 0) {
                logMessage = String.format(logMessage, args);
            }
            logger.warn(logMessage);
        } catch (MissingFormatArgumentException ex) {
            logger.error(String.format("Error logging message [%s]. Argument is missing!", message));
        }
    }

    @Override
    public void fatal(String message, Object... args) {
        try {
            String logMessage = message;
            if (args != null && args.length > 0) {
                logMessage = String.format(logMessage, args);
            }
            logger.error(logMessage);
        } catch (MissingFormatArgumentException ex) {
            logger.error(String.format("Error logging fatal message [%s]. Argument is missing!", message));
        }
    }

    @Override
    public void exception(Throwable error, String message, Object... args) {
        try {
            String logMessage = message;
            if (args != null && args.length > 0) {
                logMessage = String.format(logMessage, args);
            }
            logMessage += ": " + ExceptionUtils.getStackTrace(error);
            logger.error(logMessage);
        } catch (MissingFormatArgumentException ex) {
            logger.error(String.format("Error logging exception message [%s]. Argument is missing!", message));
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }
}
