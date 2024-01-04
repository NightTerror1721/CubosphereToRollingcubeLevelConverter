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
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kp.rollingcube.levelConverter.level.properties.PropertyInfo;
import kp.rollingcube.levelConverter.utils.MapUtils;
import kp.rollingcube.levelConverter.utils.Pair;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public enum EnemyTemplate implements Template
{
    NO_ENEMY(""),
    SPEEDER("Speeder",
            PropertyInfo.ofInteger("ForwardSteps", 1),
            PropertyInfo.ofInteger("BackwardSteps", 0),
            PropertyInfo.ofFloat("Phase", 0),
            PropertyInfo.ofFloat("Speed", 1)
    ),
    CAPTIVATOR("Captivator",
            PropertyInfo.ofFloat("Phase", 0),
            PropertyInfo.ofFloat("DelayTime", 1),
            PropertyInfo.ofFloat("AttackTime", 1)
    ),
    GEAR("Gear",
            PropertyInfo.ofString("Path", "f"),
            PropertyInfo.ofFloat("ForwardSpeed", 2),
            PropertyInfo.ofFloat("RotateSpeed", 1)
    ),
    GYRO("Gyro",
            PropertyInfo.ofString("Path", "f"),
            PropertyInfo.ofFloat("Speed", 1)
    ),
    SLOW_SPIKES("SlowSpikes",
            PropertyInfo.ofFloat("Speed", 1.62f),
            PropertyInfo.ofEnum("TurnDirection", 0, "Left", "Right"),
            PropertyInfo.ofBoolean("TiedToPlane", true),
            PropertyInfo.ofEnum("SideRestriction", 3, "None", "Items", "Specials", "Both")
    ),
    RANDOM_WALKER("RandomWalker",
            PropertyInfo.ofFloat("Speed", 5f),
            PropertyInfo.ofBoolean("TiedToPlane", true),
            PropertyInfo.ofEnum("SideRestriction", 3, "None", "Items", "Specials", "Both")
    ),
    HUNTER("Hunter",
            PropertyInfo.ofFloat("Speed", 1f),
            PropertyInfo.ofBoolean("TiedToPlane", false)
    );
    
    public static final @NonNull EnemyTemplate DEFAULT = NO_ENEMY;
    
    @Getter private final @NonNull String rollingcubeKey;
    private final @NonNull Map<String, PropertyInfo> properties;
    
    private EnemyTemplate(@NonNull String rollingcubeKey, @NonNull PropertyInfo... properties)
    {
        this.rollingcubeKey = rollingcubeKey;
        
        if(properties.length < 1)
            this.properties = Map.of();
        else
            this.properties = Map.copyOf(Stream.of(properties)
                    .map(Objects::requireNonNull)
                    .collect(Collectors.toMap(
                            PropertyInfo::getName,
                            Function.identity()
                    )));
    }
    
    @Override
    public final boolean isNull() { return this == NO_ENEMY; }
    public static boolean isNull(EnemyTemplate template) { return template == null || template.isNull(); }
    
    @Override
    public final @NonNull Map<String, PropertyInfo> getProperties() { return Map.copyOf(properties); }
    
    @Override
    public final boolean hasProperty(String name) { return properties.containsKey(name); }
    
    @Override
    public final @NonNull PropertyInfo getProperty(String name)
    {
        if(!properties.containsKey(name))
            throw new IllegalArgumentException("'" + name + "' property not found");
        
        return properties.get(name);
    }
    
    @Override
    public final String toString() { return getRollingcubeKey(); }
    
    
    private static final @NonNull Map<String, EnemyTemplate> MAP = Stream.of(values())
            .flatMap(th -> Stream.of(Pair.of(th.getRollingcubeKey().toLowerCase(), th)))
            .collect(Collectors.toMap(Pair::first, Pair::second, MapUtils.DuplicatedCriteria.alwaysFirst()));
    
    public static @NonNull EnemyTemplate fromRollingcubeKey(String key)
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
    
    
    
    public static final class Serializer extends StdSerializer<EnemyTemplate>
    {
        public Serializer() { super(EnemyTemplate.class); }
        public Serializer(Class<EnemyTemplate> cls) { super(cls); }

        @Override
        public void serialize(EnemyTemplate value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(isNull(value))
                gen.writeString("");
            else
                gen.writeString(value.toString());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<EnemyTemplate>
    {
        public Deserializer() { super(EnemyTemplate.class); }
        public Deserializer(Class<EnemyTemplate> cls) { super(cls); }

        @Override
        public @NonNull EnemyTemplate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isTextual())
                return DEFAULT;
            
            return fromRollingcubeKey(node.asText());
        }
    }
}
