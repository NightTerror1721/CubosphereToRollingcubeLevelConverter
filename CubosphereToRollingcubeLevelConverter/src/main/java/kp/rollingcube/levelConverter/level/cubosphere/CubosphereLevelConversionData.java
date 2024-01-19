package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.ui.UILogger;
import kp.rollingcube.levelConverter.utils.LoggerUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Marc
 */
@RequiredArgsConstructor(staticName = "of")
public final class CubosphereLevelConversionData
{
    @Getter private final UILogger logger;
    @Getter private int portalExitCount = 0;
    
    public void increasePortalExitCount() { portalExitCount++; }
    
    
    public void success(@NonNull String text, Object... args) { LoggerUtils.success(logger, text, args); }
    public void info(@NonNull String text, Object... args) { LoggerUtils.info(logger, text, args); }
    public void warn(@NonNull String text, Object... args) { LoggerUtils.warn(logger, text, args); }
    public void error(@NonNull String text, Object... args) { LoggerUtils.error(logger, text, args); }
}
