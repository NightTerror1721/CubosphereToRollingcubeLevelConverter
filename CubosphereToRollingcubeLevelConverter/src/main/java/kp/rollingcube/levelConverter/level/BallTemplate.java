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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kp.rollingcube.levelConverter.level.properties.PropertyInfo;
import kp.rollingcube.levelConverter.utils.Pair;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public enum BallTemplate implements Template
{
    NO_BALL(""),
    HIRO("Hiro", "ball1", "ball2", "ball3", "kwhiro"),
    HILLS("Hills", "kwhills"),
    INCA("Inca", "inca", "inca2", "inca3", "kwinca"),
    VALLEY("Valley", "valley", "valley2", "valley3"),
    ARTIC("Artic", "arctic", "arctic2", "arctic3", "kwartic"),
    COWBOY("Cowboy", "kwcowboy"),
    FIELD("Field", "kwfield"),
    ATLANT("Atlant", "kwatlant"),
    HAZE("Haze", "kwhaze", "kwhaze2"),
    MARS("Mars", "mars", "kwmars"),
    HELL("Hell", "eyeball", "kwhell"),
    STORM("Storm", "stormy", "stormy2", "stormy3"),
    INSOLITUS("Insolitus", "insolitus", "insolitus2", "insolitus3"),
    CHINA("China1", "china", "china2", "china3"),
    CHESS("Chess", "chess", "chess2", "chess3"),
    BONUS_1("Bonus1", "kwbonus1"),
    BONUS_2("Bonus2", "kwbonus2"),
    BONUS_3("Bonus3", "kwbonus3"),
    MARBLE_RHOMBUS("MarbleRhombus");
    
    public static final @NonNull BallTemplate DEFAULT = HIRO;
    public static final @NonNull BallTemplate NULL = NO_BALL;
    
    @Getter private final @NonNull String rollingcubeKey;
    private final @NonNull Set<String> cubosphereKeys;
    
    private BallTemplate(@NonNull String rollingcubeKey, String... cubosphereKeys)
    {
        this.rollingcubeKey = rollingcubeKey;
        if(cubosphereKeys == null || cubosphereKeys.length < 1)
            this.cubosphereKeys = Set.of();
        else
            this.cubosphereKeys = Set.of(cubosphereKeys);
    }
    
    @Override
    public final boolean isNull() { return this == NO_BALL; }
    public static boolean isNull(BallTemplate template) { return template == null || template.isNull(); }
    
    @Override
    public final @NonNull Map<String, PropertyInfo> getProperties() { return Map.of(); }
    
    @Override
    public final boolean hasProperty(String name) { return false; }
    
    @Override
    public final @NonNull PropertyInfo getProperty(String name)
    {
        throw new IllegalArgumentException("'" + name + "' property not found");
    }
    
    public final @NonNull Set<String> getCubosphereKeys() { return Set.copyOf(cubosphereKeys); }
    
    @Override
    public final String toString() { return getRollingcubeKey(); }
    
    
    private static final @NonNull Map<String, BallTemplate> MAP = Stream.of(values())
            .flatMap(th -> Stream.concat(
                    th.cubosphereKeys.stream().map(ck -> Pair.of(ck.toLowerCase(), th)),
                    Stream.of(Pair.of(th.getRollingcubeKey().toLowerCase(), th))))
            .collect(Collectors.toMap(Pair::first, Pair::second));
    
    public static @NonNull BallTemplate fromKey(String key)
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
    
    
    
    public static final class Serializer extends StdSerializer<BallTemplate>
    {
        public Serializer() { super(BallTemplate.class); }
        public Serializer(Class<BallTemplate> cls) { super(cls); }

        @Override
        public void serialize(BallTemplate value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(isNull(value))
                gen.writeString("");
            else
                gen.writeString(value.toString());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<BallTemplate>
    {
        public Deserializer() { super(BallTemplate.class); }
        public Deserializer(Class<BallTemplate> cls) { super(cls); }

        @Override
        public @NonNull BallTemplate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isTextual())
                return DEFAULT;
            
            return fromKey(node.asText());
        }
    }
}
