package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashMap;
import java.util.Map;
import kp.rollingcube.levelConverter.level.properties.Property;
import kp.rollingcube.levelConverter.level.properties.PropertyInfo;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
abstract class LevelElement<T extends Template>
{
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = Template.JsonIncludeDefaultFilter.class)
    @Getter private final @NonNull T template;
    
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_EMPTY)
    @Getter private final @NonNull Map<String, Property> properties = new HashMap<>();
    
    LevelElement(@NonNull T template)
    {
        this.template = template;
        prepareProperties();
    }
    
    private void prepareProperties()
    {
        template.getProperties().values().stream()
                .map(PropertyInfo::createNewProperty)
                .forEach(p -> properties.put(p.getInfo().getName(), p));
    }
    
    public final boolean hasNullTemplate() { return Template.isNull(template); }
    
    public final boolean hasProperty(String name) { return properties.containsKey(name); }
    
    public final boolean hasAnyProperty() { return !properties.isEmpty(); }
    
    @JsonIgnore
    public final @NonNull Property getProperty(String name)
    {
        var prop = properties.getOrDefault(name, null);
        if(prop == null)
            throw new IllegalArgumentException("Property '" + name + "' not found");
        
        return prop;
    }
    
    @JsonIgnore
    public final void setPropertyInteger(String name, int value) { getProperty(name).setIntegerValue(value); }
    
    @JsonIgnore
    public final void setPropertyFloat(String name, float value) { getProperty(name).setFloatValue(value); }
    
    @JsonIgnore
    public final void setPropertyBoolean(String name, boolean value) { getProperty(name).setBooleanValue(value); }
    
    @JsonIgnore
    public final void setPropertyString(String name, @NonNull String value) { getProperty(name).setStringValue(value); }
    
    @JsonIgnore
    public final void setPropertyEnumOrdinal(String name, int value) { getProperty(name).setEnumOrdinalValue(value); }
}
