package be.timonc.customenchantments.other;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class CustomEnchantLogFilter extends AbstractFilter {

    private static final Logger logger = (Logger) LogManager.getRootLogger();
    private static final CustomEnchantLogFilter filter = new CustomEnchantLogFilter();

    public static void addFilter() {
        logger.addFilter(filter);
    }

    public static void removeFilter() {
        Configuration config = logger.getContext().getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);

        loggerConfig.removeFilter(filter);
        Configurator.reconfigure();
    }

    @Override
    public Result filter(LogEvent event) {
        return event == null ? Result.NEUTRAL : isLoggable(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return isLoggable(msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return isLoggable(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return msg == null ? Result.NEUTRAL : isLoggable(msg.toString());
    }

    private Result isLoggable(String msg) {
        return Result.DENY;
    }
}
