package site.bleem.pipeworks.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Bsfit spark logger.
 */
public class BleemLogger {

    /**
     * The constant log.
     */
    private static Logger log = LoggerFactory.getLogger(BleemLogger.class);

    /**
     * Sets debug.
     *
     * @param debug the debug
     */
    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    /**
     * Get status boolean.
     *
     * @return the boolean
     */
    public Boolean getStatus(){
        return this.debug;
    }

    /**
     * The Debug.
     */
    private Boolean debug;

    /**
     * Instantiates a new Bsfit spark logger.
     */
    private BleemLogger() {
        this.debug = false;
    }

    /**
     * The type Singleton holder.
     */
    private static class SingletonHolder {
        /**
         * The constant INSTANCE.
         */
        private static final BleemLogger INSTANCE = new BleemLogger();
    }

    /**
     * Get bsfit spark logger.
     *
     * @return the bsfit spark logger
     */
    public static final BleemLogger get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Set.
     *
     * @param debug the debug
     */
    public static final void set(Boolean debug) {
        SingletonHolder.INSTANCE.setDebug(debug);
    }

    /**
     * Info.
     *
     * @param message the message
     */
    public void info(String message) {
        log.info(message);
    }

    /**
     * Debug.
     *
     * @param message the message
     */
    public static final void debug(String message) {
        if (SingletonHolder.INSTANCE.debug && !"".equals(message)) {
            log.info(message);
            System.out.println(nowDate() + " DEBUG : " + message);
        }
    }

    /**
     * Error.
     *
     * @param throwable the throwable
     */
    public static final void error(Throwable throwable) {
        if (SingletonHolder.INSTANCE.debug) {
            log.error("日志记录异常",throwable);
        }
    }

    /**
     * Debug.
     *
     * @param message the message
     */
    public static void debug(MessageBuilder message) {
        if (SingletonHolder.INSTANCE.debug) {
            debug(message.builderMessage());
        }
    }

    /**
     * Now date string.
     *
     * @return the string
     */
    private static String nowDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(now);
        return date;
    }
}