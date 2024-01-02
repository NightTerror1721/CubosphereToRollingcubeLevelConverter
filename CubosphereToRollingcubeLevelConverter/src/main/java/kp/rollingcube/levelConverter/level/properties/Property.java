package kp.rollingcube.levelConverter.level.properties;

import static kp.rollingcube.levelConverter.level.properties.PropertyType.INTEGER;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public final class Property
{
    @Getter private final @NonNull PropertyInfo info;
    private final @NonNull PropertyValue value;
    
    Property(@NonNull PropertyInfo info)
    {
        this.info = info;
        this.value = info.getDefaultValue();
    }
    
    public @NonNull PropertyValue getValue() { return value.copy(); }
    
    public int getIntegerValue() { return value.getInteger(); }
    public float getFloatValue() { return value.getFloat(); }
    public boolean getBooleanValue() { return value.getBoolean(); }
    public @NonNull String getStringValue() { return value.getString(); }
    public int getEnumOrdinalValue() { return value.getEnumOrdinal(); }
    public @NonNull String getEnumValue() { return info.getEnumValueSafe(getEnumOrdinalValue()); }
            
    public void setValue(PropertyValue value)
    {
        this.value.copyFrom(value == null ? PropertyValue.zero() : value);
        validateValue();
    }
    
    public void setIntegerValue(int value) { this.value.setInteger(value); validateValue(); }
    public void setFloatValue(float value) { this.value.setFloat(value); validateValue(); }
    public void setBooleanValue(boolean value) { this.value.setBoolean(value); validateValue(); }
    public void setStringValue(@NonNull String value) { this.value.setString(value); validateValue(); }
    public void setEnumOrdinalValue(int value) { this.value.setEnumOrdinal(value); validateValue(); }
    public void setEnumValue(@NonNull String value)
    {
        setEnumOrdinalValue(info.getEnumOrdinalFromValue(value));
    }
    
    private void validateValue() { info.validateValueRef(value); }
    
    
    public @NonNull JsonProperty toJsonProperty()
    {
        return JsonProperty.of(switch(info.getType())
        {
            case INTEGER -> Integer.toString(value.getInteger());
            case FLOAT -> Float.toString(value.getFloat());
            case BOOLEAN -> value.getBoolean()? "true" : "false";
            case STRING -> value.getString();
            case ENUM -> Integer.toString(value.getEnumOrdinal());
            default -> "null";
        });
    }
    
    public void setFromJsonProperty(@NonNull JsonProperty property)
    {
        try
        {
            value.copyFrom(switch(info.getType())
            {
                case INTEGER -> PropertyValue.fromStringAsInteger(property.getValue());
                case FLOAT -> PropertyValue.fromStringAsFloat(property.getValue());
                case BOOLEAN -> PropertyValue.fromStringAsBoolean(property.getValue());
                case STRING -> PropertyValue.fromStringAsString(property.getValue());
                case ENUM -> PropertyValue.fromStringAsInteger(property.getValue());
                default -> info.getDefaultValue();
            });
        }
        catch(NumberFormatException ex)
        {
            value.copyFrom(info.getDefaultValue());
        }
        validateValue();
    }
}
