package site.bleem.pipeworks.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WestonHan
 */
@Slf4j
public class SparkBootStrapException extends RuntimeException{
    public SparkBootStrapException() {
        super();
    }

    public SparkBootStrapException(String message) {
        super(message);
        log.error(message);
    }

    public SparkBootStrapException(String message, Throwable cause) {
        super(message, cause);
        log.error(message);
    }

    public SparkBootStrapException(Throwable cause) {
        super(cause);
    }

    protected SparkBootStrapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        log.error(message);
    }
}
