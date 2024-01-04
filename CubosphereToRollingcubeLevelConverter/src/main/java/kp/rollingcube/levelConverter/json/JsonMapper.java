package kp.rollingcube.levelConverter.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import kp.rollingcube.levelConverter.level.*;
import kp.rollingcube.levelConverter.level.properties.Property;
import kp.rollingcube.levelConverter.utils.Version;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public final class JsonMapper
{
    private JsonMapper() {}
    
    public static final @NonNull ObjectMapper OBJECT_MAPPER = initObjectMapper();
    
    
    private static @NonNull ObjectMapper initObjectMapper()
    {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        SimpleModule module = new SimpleModule();
        module.addSerializer(Version.class, new Version.Serializer());
        module.addDeserializer(Version.class, new Version.Deserializer());
        module.addSerializer(SideTag.class, new SideTag.Serializer());
        module.addDeserializer(SideTag.class, new SideTag.Deserializer());
        module.addSerializer(Direction.class, new Direction.Serializer());
        module.addDeserializer(Direction.class, new Direction.Deserializer());
        module.addSerializer(Theme.class, new Theme.Serializer());
        module.addDeserializer(Theme.class, new Theme.Deserializer());
        module.addSerializer(Music.class, new Music.Serializer());
        module.addDeserializer(Music.class, new Music.Deserializer());
        module.addSerializer(BlockTemplate.class, new BlockTemplate.Serializer());
        module.addDeserializer(BlockTemplate.class, new BlockTemplate.Deserializer());
        module.addSerializer(ItemTemplate.class, new ItemTemplate.Serializer());
        module.addDeserializer(ItemTemplate.class, new ItemTemplate.Deserializer());
        module.addSerializer(EnemyTemplate.class, new EnemyTemplate.Serializer());
        module.addDeserializer(EnemyTemplate.class, new EnemyTemplate.Deserializer());
        module.addSerializer(BallTemplate.class, new BallTemplate.Serializer());
        module.addDeserializer(BallTemplate.class, new BallTemplate.Deserializer());
        module.addSerializer(Property.class, new Property.Serializer());
        mapper.registerModule(module);
        
        return mapper;
    }
}
