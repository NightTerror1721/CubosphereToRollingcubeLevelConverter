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
import kp.rollingcube.levelConverter.utils.Pair;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public enum BlockTemplate implements Template
{
    NO_BLOCK("", false, false),
    NORMAL("Normal", false, true),
    EXIT("Exit", false, false,
            PropertyInfo.ofString("NextLevel", ""),
            PropertyInfo.ofBoolean("Secret", false)
    ),
    STATIC_SPIKES("StaticSpikes", false, false),
    BREAKING("Breaking", true, false),
    COUNTER("Counter", true, false,
            PropertyInfo.ofEnum("Times", 4,
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9"
    )),
    RETRACTABLE_SPIKES("RetractableSpikes", false, false,
            PropertyInfo.ofFloat("Phase", 0),
            PropertyInfo.ofFloat("Speed", 1)
    ),
    PATTERN_SPIKES("PatternSpikes", false, false,
            PropertyInfo.ofString("Pattern", "iiiiooooiioo"),
            PropertyInfo.ofInteger("PatternIndex", 0),
            PropertyInfo.ofFloat("Speed", 4)
    ),
    FAR_INVISIBLE_BLOCK("FarInvisibleBlock", true, true),
    NEAR_INVISIBLE_BLOCK("NearInvisibleBlock", true, true),
    FULL_INVISIBLE_BLOCK("FullInvisibleBlock", true, true),
    TRAMPOLINE("Trampoline", false, false),
    VENT("Trampoline", false, false),
    BURNED("Burned", false, true),
    ICY("Icy", false, true),
    TIME_STOP("TimeStop", false, false),
    ROTATOR("Rotator", false, false,
            PropertyInfo.ofEnum("Direction", 0, "Left", "Right")
    ),
    ONE_WAY("OneWay", false, false,
            PropertyInfo.ofEnum("Direction", 0, "North", "East", "South", "West")
    ),
    SWITCH("Switch", false, false,
            colorProperty("Color", 0),
            PropertyInfo.ofEnum("Activated", 0, "Yes", "No")
    ),
    BUTTON("Button", false, false,
            colorProperty("Color", 0)
    ),
    PRESSURE_PLATE("PressurePlate", false, false,
            colorProperty("Color", 0)
    ),
    TELEPORT("Teleport", false, false,
            colorProperty("Color", 0),
            PropertyInfo.ofInteger("Order", 0),
            PropertyInfo.ofEnum("Activated", 0, "Yes", "No"),
            PropertyInfo.ofEnum("Direction", 0, "North", "East", "South", "West")
    ),
    ELEVATOR("Elevator", true, true,
            PropertyInfo.ofInteger("ForwardSteps", 3),
            PropertyInfo.ofInteger("BackwardSteps", 0),
            PropertyInfo.ofEnum("Direction", 0, "Up", "Down", "Left", "Right" , "Front", "Back"),
            PropertyInfo.ofFloat("Speed", 3),
            PropertyInfo.ofFloat("DelayTime", 0.82f)
    ),
    BLINKING("Blinking", true, false,
            PropertyInfo.ofFloat("TimeActivated", 1.2f),
            PropertyInfo.ofFloat("TimeDeactivated", 1.2f),
            PropertyInfo.ofFloat("TimeBlending", 0.7f),
            PropertyInfo.ofFloat("Phase", 0)
    ),
    LASER("Laser", false, false,
            colorProperty("Color", 0),
            PropertyInfo.ofBoolean("Activated", true),
            PropertyInfo.ofEnum("NumOfBeams", 0, "4", "5")
    ),
    TOGGLE("ToggleBlock", true, false,
            colorProperty("Color", 0),
            PropertyInfo.ofBoolean("Activated", true)
    ),
    SAND("Sand", false, true);
    
    public static final @NonNull BlockTemplate DEFAULT = NORMAL;
    public static final @NonNull BlockTemplate NULL = NO_BLOCK;
    
    @Getter private final @NonNull String rollingcubeKey;
    @Getter private final boolean blockOnly;
    @Getter private final boolean canHasItem;
    private final @NonNull Map<String, PropertyInfo> properties;
    
    private BlockTemplate(@NonNull String rollingcubeKey, boolean blockOnly, boolean canHasItem, @NonNull PropertyInfo... properties)
    {
        this.rollingcubeKey = rollingcubeKey;
        this.blockOnly = blockOnly;
        this.canHasItem = canHasItem;
        
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
    public final boolean isNull() { return this == NULL; }
    public static boolean isNull(BlockTemplate template) { return template == null || template.isNull(); }
    
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
    
    
    private static final @NonNull Map<String, BlockTemplate> MAP = Stream.of(values())
            .flatMap(th -> Stream.of(Pair.of(th.getRollingcubeKey().toLowerCase(), th)))
            .collect(Collectors.toMap(Pair::first, Pair::second));
    
    public static @NonNull BlockTemplate fromRollingcubeKey(String key)
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
    
    
    
    public static final class Serializer extends StdSerializer<BlockTemplate>
    {
        public Serializer() { super(BlockTemplate.class); }
        public Serializer(Class<BlockTemplate> cls) { super(cls); }

        @Override
        public void serialize(BlockTemplate value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(isNull(value))
                gen.writeString(DEFAULT.toString());
            else
                gen.writeString(value.toString());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<BlockTemplate>
    {
        public Deserializer() { super(BlockTemplate.class); }
        public Deserializer(Class<BlockTemplate> cls) { super(cls); }

        @Override
        public @NonNull BlockTemplate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isTextual())
                return DEFAULT;
            
            return fromRollingcubeKey(node.asText());
        }
    }
    
    
    
    private static PropertyInfo colorProperty(String name, int defaultIndex)
    {
        return PropertyInfo.ofEnum(name, defaultIndex,
                "Red",
                "Blue",
                "Green",
                "Yellow",
                "Cyan",
                "Orange",
                "Violet",
                "Magenta",
                "Turquoise",
                "Lime",
                "Salmon",
                "White"
        );
    }
}
