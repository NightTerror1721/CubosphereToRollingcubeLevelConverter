package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kp.rollingcube.levelConverter.utils.Pair;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public enum ThemeMode
{
    NORMAL("Normal"),
    SECRET("Secret"),
    KULA_WORLD("KulaWorld"),
    KULA_WORLD_SECRET("KulaWorldSecret");
        
    public static final @NonNull ThemeMode DEFAULT = NORMAL;
    
    @Getter private final @NonNull String rollingcubeKey;
    
    private ThemeMode(@NonNull String rollingcubeKey)
    {
        this.rollingcubeKey = rollingcubeKey;
    }
    
    @Override
    public final String toString() { return getRollingcubeKey(); }
    
    
    private static final @NonNull Map<String, ThemeMode> MAP = Stream.of(values())
            .flatMap(th -> Stream.of(Pair.of(th.getRollingcubeKey().toLowerCase(), th)))
            .collect(Collectors.toMap(Pair::first, Pair::second));
    
    public static @NonNull ThemeMode fromRollingcubeKey(String key)
    {
        if(key == null)
            return DEFAULT;
        
        return MAP.getOrDefault(key.toLowerCase(), DEFAULT);
    }
    
    public static boolean existsKey(String key)
    {
        if(key == null)
            return false;
        
        return MAP.containsKey(key.toLowerCase());
    }
    
    
    
    public static final class Serializer extends StdSerializer<ThemeMode>
    {
        public Serializer() { super(ThemeMode.class); }
        public Serializer(Class<ThemeMode> cls) { super(cls); }

        @Override
        public void serialize(ThemeMode value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeString(DEFAULT.toString());
            else
                gen.writeString(value.toString());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<ThemeMode>
    {
        public Deserializer() { super(ThemeMode.class); }
        public Deserializer(Class<ThemeMode> cls) { super(cls); }

        @Override
        public @NonNull ThemeMode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isTextual())
                return DEFAULT;
            
            return ThemeMode.fromRollingcubeKey(node.asText());
        }
    }
}
