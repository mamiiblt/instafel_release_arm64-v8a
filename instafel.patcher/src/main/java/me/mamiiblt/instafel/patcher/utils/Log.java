package me.mamiiblt.instafel.patcher.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {

    private static final Logger LOGGER = Logger.getLogger(Log.class.getName());

    public static void setupLogger() {
        Logger logger = Logger.getLogger("");
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        LogManager.getLogManager().reset();

        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (getFormatter() == null) {
                    setFormatter(new Formatter() {
                        @Override
                        public String format(LogRecord record) {
                            return record.getLevel().toString().charAt(0) + ": "
                                + record.getMessage()
                                + System.getProperty("line.separator");
                        }
                    });
                }

                try {
                    String message = getFormatter().format(record);
                    if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
                        System.err.write(message.getBytes());
                    } else {
                        if (record.getLevel().intValue() >= Level.INFO.intValue()) {
                            System.out.write(message.getBytes());
                        }
                    }
                } catch (Exception ex) {
                    reportError(null, ex, ErrorManager.FORMAT_FAILURE);
                }
            }

            @Override
            public void close() throws SecurityException {}
            @Override
            public void flush(){}
        };
        logger.addHandler(handler);
        handler.setLevel(Level.ALL);
        logger.setLevel(Level.ALL);
    } 


    public static void info(String msg) {
        LOGGER.info(msg);
    }

    public static void warning(String msg) {
        LOGGER.warning(msg);
    }

    public static void severe(String msg) {
        LOGGER.severe(msg);
    }
}
