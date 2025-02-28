package kp.rollingcube.levelConverter.level.cubosphere;

import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
abstract class CubosphereLevelElement
{
    @Getter private final @NonNull String template;
    
    private final HashMap<String, CubospherePropertyValue> properties = new HashMap<>();
    
    CubosphereLevelElement(String template)
    {
        this.template = template == null ? "" : template;
    }
    
    public final boolean hasInvalidTemplate() { return template.isBlank(); }
    
    public final boolean hasProperty(String name) { return properties.containsKey(name); }
    
    public final boolean hasAnyProperty() { return !properties.isEmpty(); }
    
    public final @NonNull CubospherePropertyValue getProperty(String name)
    {
        var value = properties.getOrDefault(name, null);
        if(value == null)
            return CubospherePropertyValue.zero();
        return value;
    }
    public final @NonNull CubospherePropertyValue getProperty(String name, @NonNull CubospherePropertyValue defaultValue)
    {
        var value = properties.getOrDefault(name, null);
        if(value == null)
            return defaultValue;
        return value;
    }
    
    public final int getPropertyInteger(String name) { return getProperty(name).toInt(); }
    public final int getPropertyInteger(String name, int defaultValue) { return getProperty(name, CubospherePropertyValue.of(defaultValue)).toInt(); }
    public final float getPropertyFloat(String name) { return getProperty(name).toFloat(); }
    public final float getPropertyFloat(String name, float defaultValue) { return getProperty(name, CubospherePropertyValue.of(defaultValue)).toFloat(); }
    public final boolean getPropertyBoolean(String name) { return getProperty(name).toBoolean(); }
    public final boolean getPropertyBoolean(String name, boolean defaultValue) { return getProperty(name, CubospherePropertyValue.of(defaultValue)).toBoolean(); }
    public final @NonNull String getPropertyString(String name) { return getProperty(name).toString(); }
    public final @NonNull String getPropertyString(String name, @NonNull String defaultValue)
    {
        return getProperty(name, CubospherePropertyValue.of(defaultValue)).toString();
    }
    
    public final void setProperty(String name, CubospherePropertyValue value)
    {
        if(value == null)
            properties.remove(name);
        
        properties.put(name, value);
    }
    
    
    public final void setPropertyNull(String name) { setProperty(name, null); }
    public final void setPropertyInteger(String name, int value) { setProperty(name, CubospherePropertyValue.of(value)); }
    public final void setPropertyFloat(String name, float value) { setProperty(name, CubospherePropertyValue.of(value)); }
    public final void setPropertyBoolean(String name, boolean value) { setProperty(name, CubospherePropertyValue.of(value)); }
    public final void setPropertyString(String name, String value) { setProperty(name, CubospherePropertyValue.of(value)); }
}
