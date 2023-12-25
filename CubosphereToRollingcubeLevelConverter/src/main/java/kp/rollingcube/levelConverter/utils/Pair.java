package kp.rollingcube.levelConverter.utils;

import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public record Pair<F, S>(@Getter F first, @Getter S second)
{
    public static <F, S> @NonNull Pair<F, S> of(F first, S second)
    {
        return new Pair(first, second);
    }
}
