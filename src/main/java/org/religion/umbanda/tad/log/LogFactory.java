package org.religion.umbanda.tad.log;

public class LogFactory {
    public static Log createLog(Class<?> clazz) {
        return new Slf4jImpl(clazz);
    }

    private LogFactory() {
    }
}
