package kp.rollingcube.levelConverter.level.properties;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.Data;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
@Data
public final class PropertyInfo
{
    private final @NonNull PropertyType type;
    private final @NonNull String name;
    private final @NonNull PropertyValue defaultValue;
    private final @NonNull String[] enumValues;
    
    private PropertyInfo(@NonNull PropertyType type, @NonNull String name, @NonNull PropertyValue defaultValue, @NonNull String[] enumValues)
    {
        this.type = type;
        this.name = name;
        this.enumValues = enumValues;
        
        this.defaultValue = validateValueCopy(defaultValue);
    }
    
    public static @NonNull PropertyInfo ofInteger(@NonNull String name, int defaultValue)
    {
        return new PropertyInfo(PropertyType.INTEGER, name, PropertyValue.of(defaultValue), new String[0]);
    }
    public static @NonNull PropertyInfo ofFloat(@NonNull String name, float defaultValue)
    {
        return new PropertyInfo(PropertyType.FLOAT, name, PropertyValue.of(defaultValue), new String[0]);
    }
    public static @NonNull PropertyInfo ofBoolean(@NonNull String name, boolean defaultValue)
    {
        return new PropertyInfo(PropertyType.BOOLEAN, name, PropertyValue.of(defaultValue), new String[0]);
    }
    public static @NonNull PropertyInfo ofString(@NonNull String name, @NonNull String defaultValue)
    {
        return new PropertyInfo(PropertyType.STRING, name, PropertyValue.of(defaultValue), new String[0]);
    }
    public static @NonNull PropertyInfo ofEnum(@NonNull String name, int defaultIndex, @NonNull String... enumValues)
    {
        if(enumValues.length < 1)
            throw new IllegalArgumentException("enumValues");
        
        @NonNull String[] inputEnumValues = Stream.of(enumValues)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        
        if(inputEnumValues == null || inputEnumValues.length < 1)
            throw new IllegalArgumentException("enumValues");
        
        if(defaultIndex < 0 || defaultIndex >= inputEnumValues.length)
            throw new IndexOutOfBoundsException("parameter 'defaultIndex' index out of range: " + defaultIndex);
        
        return new PropertyInfo(PropertyType.ENUM, name, PropertyValue.of(defaultIndex), inputEnumValues);
    }
    
    
    public @NonNull PropertyValue getDefaultValue() { return defaultValue.copy(); }
    
    public @NonNull String[] getEnumValues()
    {
        if(enumValues == null || enumValues.length < 1)
            return new String[0];
        
        return Arrays.copyOf(enumValues, enumValues.length);
    }
    
    public @NonNull String getEnumValueSafe(int enumOrdinal)
    {
        return enumOrdinal < 0 || enumOrdinal >= enumValues.length ? "null" : enumValues[enumOrdinal];
    }
    
    public int getEnumOrdinalFromValue(@NonNull String value)
    {
        for(int i = 0; i < enumValues.length; ++i)
            if(enumValues[i].equals(value))
                return i;
        
        return defaultValue.getEnumOrdinal();
    }
    
    
    public @NonNull PropertyValue validateValueRef(@NonNull PropertyValue value)
    {
        if(type == PropertyType.ENUM)
            value.setEnumOrdinal(Math.abs(value.getEnumOrdinal()) % enumValues.length);
        return value;
    }
    
    public @NonNull PropertyValue validateValueCopy(@NonNull PropertyValue value)
    {
        return validateValueRef(value.copy());
    }
    
    
    public @NonNull Property createNewProperty()
    {
        return new Property(this);
    }
}
