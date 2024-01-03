package kp.rollingcube.levelConverter.level.cubosphere;

import java.util.HashMap;
import kp.rollingcube.levelConverter.level.properties.PropertyValue;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
abstract class CubosphereLevelElement
{
    @Getter private final @NonNull String template;
    
    private final HashMap<String, PropertyValue> properties = new HashMap<>();
    
    CubosphereLevelElement(String template)
    {
        this.template = template == null ? "" : template;
    }
    
    public final boolean hasInvalidTemplate() { return template.isBlank(); }
    
    public final boolean hasProperty(String name) { return properties.containsKey(name); }
    
    public final boolean hasAnyProperty() { return !properties.isEmpty(); }
    
    public final @NonNull PropertyValue getProperty(String name)
    {
        var value = properties.getOrDefault(name, null);
        if(value == null)
            return PropertyValue.zero();
        return value;
    }
    
    public final int getPropertyInteger(String name) { return getProperty(name).getInteger(); }
    public final float getPropertyFloat(String name) { return getProperty(name).getFloat(); }
    public final boolean getPropertyBoolean(String name) { return getProperty(name).getBoolean(); }
    public final @NonNull String getPropertyString(String name) { return getProperty(name).getString(); }
    
    public final void setProperty(String name, PropertyValue value)
    {
        if(value == null)
            properties.remove(name);
        
        properties.put(name, value);
    }
    
    
    public final void setPropertyNull(String name) { setProperty(name, null); }
    public final void setPropertyInteger(String name, int value) { setProperty(name, PropertyValue.of(value)); }
    public final void setPropertyFloat(String name, float value) { setProperty(name, PropertyValue.of(value)); }
    public final void setPropertyBoolean(String name, boolean value) { setProperty(name, PropertyValue.of(value)); }
    public final void setPropertyString(String name, @NonNull String value) { setProperty(name, PropertyValue.of(value)); }
}
