package kp.rollingcube.levelConverter.level;

import java.util.Map;
import kp.rollingcube.levelConverter.level.properties.PropertyInfo;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Marc
 */
public interface Template
{
    boolean isNull();
    
    @NonNull Map<String, PropertyInfo> getProperties();
    
    boolean hasProperty(String name);
    @NonNull PropertyInfo getProperty(String name);
    
    @Override
    String toString();
    
    
    public static boolean isNull(BlockTemplate template) { return BlockTemplate.isNull(template); }
    public static boolean isNull(ItemTemplate template) { return ItemTemplate.isNull(template); }
    public static boolean isNull(EnemyTemplate template) { return EnemyTemplate.isNull(template); }
    public static boolean isNull(BallTemplate template) { return BallTemplate.isNull(template); }
    
    public static boolean isNull(Template template)
    {
        return switch(template)
        {
            case null -> true;
            case BlockTemplate bt -> isNull(bt);
            case ItemTemplate it -> isNull(it);
            case EnemyTemplate et -> isNull(et);
            case BallTemplate bt -> isNull(bt);
            default -> true;
        };
    }
    
    
    final class JsonIncludeDefaultFilter
    {
        @Override
        public boolean equals(Object other)
        {
            return switch(other)
            {
                case Template t -> isNull(t);
                case null -> false;
                default -> false;
            };
        }

        @Override
        public int hashCode() {
            int hash = 5;
            return hash;
        }
    }
    
    @UtilityClass
    class Utils
    {
        public static PropertyInfo colorProperty(String name, int defaultIndex)
        {
            return PropertyInfo.ofEnum(name, defaultIndex,
                    "Red",
                    "Blue",
                    "Green",
                    "Yellow",
                    "Cyan",
                    "Orange",
                    "Violet",
                    "Magenta",
                    "Turquoise",
                    "Lime",
                    "Salmon",
                    "White"
            );
        }
        
        public static PropertyInfo textColorProperty(String name, int defaultIndex)
        {
            return PropertyInfo.ofEnum(name, defaultIndex,
                    "Black",
                    "DarkBlue",
                    "DarkGreen",
                    "DarkCyan",
                    "DarkRed",
                    "DarkPurple",
                    "Gold",
                    "Gray",
                    "DarkGray",
                    "Blue",
                    "Green",
                    "Cyan",
                    "Red",
                    "LightPurple",
                    "Yellow",
                    "White"
            );
        }
    }
}
