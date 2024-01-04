package kp.rollingcube.levelConverter.utils;

import java.util.function.BinaryOperator;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Marc
 */
@UtilityClass
public class MapUtils
{
    @UtilityClass
    public class DuplicatedCriteria
    {
        public <T> BinaryOperator<T> alwaysFirst() { return (first, second) -> first; }
        public <T> BinaryOperator<T> alwaysSecond() { return (first, second) -> first; }
    }
}
