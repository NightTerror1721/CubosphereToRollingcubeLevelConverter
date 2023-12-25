package kp.rollingcube.levelConverter.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import kp.rollingcube.levelConverter.utils.Version;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public final class JsonMapper
{
    private JsonMapper() {}
    
    private static final @NonNull ObjectMapper OBJECT_MAPPER = initObjectMapper();
    
    
    private static @NonNull ObjectMapper initObjectMapper()
    {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        SimpleModule module = new SimpleModule();
        module.addSerializer(Version.class, new Version.Serializer());
        module.addDeserializer(Version.class, new Version.Deserializer());
        mapper.registerModule(module);
        
        return mapper;
    }
}
