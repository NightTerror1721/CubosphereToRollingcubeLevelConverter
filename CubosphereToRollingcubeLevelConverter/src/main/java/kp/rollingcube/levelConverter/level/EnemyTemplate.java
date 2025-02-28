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
import java.util.*;
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
    NO_ENEMY("", List.of(), Set.of()),
    SPEEDER("Speeder",
            List.of(
                    PropertyInfo.ofInteger("ForwardSteps", 1),
                    PropertyInfo.ofInteger("BackwardSteps", 0),
                    PropertyInfo.ofFloat("Phase", 0),
                    PropertyInfo.ofFloat("Speed", 1)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, false),
                    Interaction.of(EnemyInteractionType.LASERS, false),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, false),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, false)
            )
    ),
    CAPTIVATOR("Captivator",
            List.of(
                    PropertyInfo.ofFloat("Phase", 0),
                    PropertyInfo.ofFloat("DelayTime", 1),
                    PropertyInfo.ofFloat("AttackTime", 1)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    GEAR("Gear",
            List.of(
                    PropertyInfo.ofString("Path", "f"),
                    PropertyInfo.ofFloat("ForwardSpeed", 2),
                    PropertyInfo.ofFloat("RotateSpeed", 1)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.ICE, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.BREAKING_BLOCKS, false),
                    Interaction.of(EnemyInteractionType.TRAMPS_AND_VENTS, false),
                    Interaction.of(EnemyInteractionType.MAGNETS, true),
                    Interaction.of(EnemyInteractionType.ROTATORS, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    GYRO("Gyro",
            List.of(
                    PropertyInfo.ofString("Path", "f"),
                    PropertyInfo.ofFloat("Speed", 1)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.ICE, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.BREAKING_BLOCKS, false),
                    Interaction.of(EnemyInteractionType.TRAMPS_AND_VENTS, false),
                    Interaction.of(EnemyInteractionType.MAGNETS, false),
                    Interaction.of(EnemyInteractionType.ROTATORS, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    SLOW_SPIKES("SlowSpikes",
            List.of(
                    PropertyInfo.ofFloat("Speed", 1.62f),
                    PropertyInfo.ofEnum("TurnDirection", 0, "Left", "Right"),
                    PropertyInfo.ofBoolean("TiedToPlane", true),
                    PropertyInfo.ofEnum("SideRestriction", 3, "None", "Items", "Specials", "Both")
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.ROTATORS, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    RANDOM_WALKER("RandomWalker",
            List.of(
                    PropertyInfo.ofFloat("Speed", 5f),
                    PropertyInfo.ofBoolean("TiedToPlane", true),
                    PropertyInfo.ofEnum("SideRestriction", 3, "None", "Items", "Specials", "Both")
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    HUNTER("Hunter",
            List.of(
                    PropertyInfo.ofFloat("Speed", 1f),
                    PropertyInfo.ofBoolean("TiedToPlane", false)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    JUMPER("Jumper",
            List.of(
                    PropertyInfo.ofString("Path", "f"),
                    PropertyInfo.ofFloat("Speed", 2.5f)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.ICE, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.BREAKING_BLOCKS, false),
                    Interaction.of(EnemyInteractionType.TRAMPS_AND_VENTS, false),
                    Interaction.of(EnemyInteractionType.MAGNETS, false),
                    Interaction.of(EnemyInteractionType.ROTATORS, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    RHOMBUS("Rhombus",
            List.of(
                    PropertyInfo.ofString("Path", "f"),
                    PropertyInfo.ofFloat("Speed", 3.25f)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.ICE, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.BREAKING_BLOCKS, false),
                    Interaction.of(EnemyInteractionType.TRAMPS_AND_VENTS, false),
                    Interaction.of(EnemyInteractionType.MAGNETS, false),
                    Interaction.of(EnemyInteractionType.ROTATORS, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    ANOMALY("Anomaly",
            List.of(
                    PropertyInfo.ofString("Path", "f"),
                    PropertyInfo.ofFloat("Speed", 3.25f)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.ICE, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.BREAKING_BLOCKS, false),
                    Interaction.of(EnemyInteractionType.TRAMPS_AND_VENTS, false),
                    Interaction.of(EnemyInteractionType.MAGNETS, false),
                    Interaction.of(EnemyInteractionType.ROTATORS, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    ),
    SMART_SPEEDER("SmartSpeeder",
            List.of(
                    PropertyInfo.ofString("Path", "f"),
                    PropertyInfo.ofFloat("Speed", 3f)
            ),
            Set.of(
                    Interaction.of(EnemyInteractionType.SPIKES, true),
                    Interaction.of(EnemyInteractionType.LASERS, true),
                    Interaction.of(EnemyInteractionType.TELEPORTS, true),
                    Interaction.of(EnemyInteractionType.BUTTONS, true),
                    Interaction.of(EnemyInteractionType.LIGHT_SENSORS, true),
                    Interaction.of(EnemyInteractionType.DIRECTION_RESTRICTIONS, false),
                    Interaction.of(EnemyInteractionType.ICE, false),
                    Interaction.of(EnemyInteractionType.FIRE, false),
                    Interaction.of(EnemyInteractionType.BREAKING_BLOCKS, false),
                    Interaction.of(EnemyInteractionType.TRAMPS_AND_VENTS, false),
                    Interaction.of(EnemyInteractionType.MAGNETS, false),
                    Interaction.of(EnemyInteractionType.ROTATORS, false),
                    Interaction.of(EnemyInteractionType.BALL_SHIELD, true),
                    Interaction.of(EnemyInteractionType.INVISIBLE_BLOCKS, true)
            )
    );
    
    public static final @NonNull EnemyTemplate DEFAULT = NO_ENEMY;
    
    @Getter private final @NonNull String rollingcubeKey;
    private final @NonNull Map<String, PropertyInfo> properties;
    private final @NonNull Set<EnemyInteractionType> availableInteractions;
    private final @NonNull Set<EnemyInteractionType> defaultInteractions;
    
    EnemyTemplate(
            @NonNull String rollingcubeKey,
            @NonNull Collection<PropertyInfo> properties,
            @NonNull Collection<Interaction> interactions
    )
    {
        this.rollingcubeKey = rollingcubeKey;
        
        if(properties.isEmpty())
            this.properties = Map.of();
        else
            this.properties = Map.copyOf(properties.stream()
                    .map(Objects::requireNonNull)
                    .collect(Collectors.toMap(
                            PropertyInfo::getName,
                            Function.identity()
                    )));

        if (interactions.isEmpty())
        {
            this.availableInteractions = Set.of();
            this.defaultInteractions = Set.of();
        }
        else {
            this.availableInteractions = Set.copyOf(
                    interactions.stream()
                            .map(Interaction::type)
                            .collect(Collectors.toSet())
            );
            this.defaultInteractions = Set.copyOf(
                    interactions.stream()
                            .filter(Interaction::isDefault)
                            .map(Interaction::type)
                            .collect(Collectors.toSet())
            );
        }
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

    public final @NonNull Set<EnemyInteractionType> getAvailableInteractions() { return Set.copyOf(availableInteractions); }
    public final @NonNull Set<EnemyInteractionType> getDefaultInteractions() { return Set.copyOf(defaultInteractions); }

    public final boolean hasInteractionAvailable(EnemyInteractionType type) { return availableInteractions.contains(type); }
    
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


    private record Interaction(EnemyInteractionType type, boolean isDefault)
    {
        public static Interaction of(EnemyInteractionType type, boolean isDefault) {
            return new Interaction(type, isDefault);
        }
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
