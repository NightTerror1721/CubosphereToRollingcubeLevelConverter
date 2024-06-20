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
public enum ItemTemplate implements Template
{
    NO_ITEM(""),
    BRONZE_COIN("BronzeCoin"),
    SILVER_COIN("SilverCoin"),
    GOLD_COIN("GoldCoin"),
    PLATINUM_COIN("PlatinumCoin"),
    KEY("Key"),
    FRUIT("Fruit"),
    BLUE_DIAMOND("BlueDiamond"),
    RED_DIAMOND("RedDiamond"),
    GRAVITY_ARROW("GravityArrow",
            PropertyInfo.ofEnum("Direction", 0, "Up", "Down", "Left", "Right" , "Front", "Back")
    ),
    GREEN_DIAMOND("GreenDiamond"),
    ORANGE_DIAMOND("OrangeDiamond"),
    WHITE_DIAMOND("WhiteDiamond"),
    MAGENTA_DIAMOND("MagentaDiamond"),
    SPECIAL_DIAMOND("SpecialDiamond"),
    GOBLET("Goblet"),
    HOURGLASS("Hourglass"),
    TIME_PLUS("TimePlus",
            PropertyInfo.ofInteger("Seconds", 15)
    ),
    TIME_MINUS("TimeMinus",
            PropertyInfo.ofInteger("Seconds", 15)
    ),
    SLEEPING_PILL("SleepingPill"),
    BOUNCING_PILL("BouncingPill"),
    JUMP_BAN_PILL("JumpBanPill"),
    JUMP_PLUS("JumpPlus"),
    JUMP_MINUS("JumpMinus"),
    GLASSES("Glasses",
            PropertyInfo.ofInteger("Seconds", 15),
            PropertyInfo.ofBoolean("Additive", false)
    ),
    SHIELD("Shield",
            PropertyInfo.ofInteger("Seconds", 15),
            PropertyInfo.ofBoolean("Additive", false)
    ),
    SLOW_MOTION("SlowMotion"),
    PORTAL_ENTRANCE("PortalEntrance",
            colorProperty("Color", 0)
    );
    
    public static final @NonNull ItemTemplate DEFAULT = NO_ITEM;
    
    @Getter private final @NonNull String rollingcubeKey;
    private final @NonNull Map<String, PropertyInfo> properties;
    
    private ItemTemplate(@NonNull String rollingcubeKey, @NonNull PropertyInfo... properties)
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
    public final boolean isNull() { return this == NO_ITEM; }
    public static boolean isNull(ItemTemplate template) { return template == null || template.isNull(); }
    
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
    
    
    private static final @NonNull Map<String, ItemTemplate> MAP = Stream.of(values())
            .flatMap(th -> Stream.of(Pair.of(th.getRollingcubeKey().toLowerCase(), th)))
            .collect(Collectors.toMap(Pair::first, Pair::second, MapUtils.DuplicatedCriteria.alwaysFirst()));
    
    public static @NonNull ItemTemplate fromRollingcubeKey(String key)
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
    
    
    
    public static final class Serializer extends StdSerializer<ItemTemplate>
    {
        public Serializer() { super(ItemTemplate.class); }
        public Serializer(Class<ItemTemplate> cls) { super(cls); }

        @Override
        public void serialize(ItemTemplate value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(isNull(value))
                gen.writeString("");
            else
                gen.writeString(value.toString());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<ItemTemplate>
    {
        public Deserializer() { super(ItemTemplate.class); }
        public Deserializer(Class<ItemTemplate> cls) { super(cls); }

        @Override
        public @NonNull ItemTemplate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isTextual())
                return DEFAULT;
            
            return fromRollingcubeKey(node.asText());
        }
    }
    
    
    
    private static PropertyInfo colorProperty(String name, int defaultIndex)
    {
        return Template.Utils.colorProperty(name, defaultIndex);
    }
}
