package kp.rollingcube.levelConverter.ui;

import lombok.NonNull;

/**
 *
 * @author Marc
 */
public record UILoggerMessage(@NonNull String message, UILogger.Level level) {}
