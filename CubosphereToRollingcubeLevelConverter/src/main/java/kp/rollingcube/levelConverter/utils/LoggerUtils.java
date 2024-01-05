package kp.rollingcube.levelConverter.utils;

import kp.rollingcube.levelConverter.ui.UILogger;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Marc
 */
@UtilityClass
public class LoggerUtils
{
    public void info(UILogger logger, @NonNull String text, Object... args)
    {
        if(logger == null)
            return;
        logger.formatln(UILogger.Level.NORMAL, text, args);
    }
    
    public void success(UILogger logger, @NonNull String text, Object... args)
    {
        if(logger == null)
            return;
        logger.formatln(UILogger.Level.GOOD, text, args);
    }
    
    public void warn(UILogger logger, @NonNull String text, Object... args)
    {
        if(logger == null)
            return;
        logger.formatln(UILogger.Level.WARNING, text, args);
    }
    
    public void error(UILogger logger, @NonNull String text, Object... args)
    {
        if(logger == null)
            return;
        logger.formatln(UILogger.Level.ERROR, text, args);
    }
}
