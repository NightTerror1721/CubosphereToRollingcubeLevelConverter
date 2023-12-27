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
import kp.rollingcube.levelConverter.utils.Pair;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public enum Music
{
    EGYPT("Egypt", "01_-_hiro", "Ziggurat"),
    HILLS("Hills", "02_-_hills"),
    INCA("Inca", "03_-_inca", "Jungle Bed"),
    VALLEY("Valley", "The Sun Will Come"),
    ARTIC("Artic", "04_-_arctic", "Arctican"),
    COWBOY("Cowboy", "05_-_cowboy"),
    FIELD("Field", "06_-_field"),
    ATLANTIS("Atlantis", "07_-_atlantis"),
    HAZE("Haze", "08_-_haze"),
    MARS("Mars", "09_-_mars", "Melodies de Mars"),
    HELL("Hell", "10_-_hell"),
    STORM("Storm", "Ruin"),
    CHINA("China", "Streets of Asia"),
    CHESS("Chess", "Escape from the Citadel"),
    BONUS1("Bonus1", "11_-_bonus_1"),
    BONUS2("Bonus2", "12_-_bonus_2"),
    BONUS3("Bonus3", "13_-_bonus_3"),
    KULA_BETA("KulaBeta", "15_-_beta"),
    KULA_HIDDEN("KulaHidden", "14_-_hidden_track");
        
    public static final @NonNull Music DEFAULT = EGYPT;
    
    @Getter private final @NonNull String rollingcubeKey;
    private final @NonNull Set<String> cubosphereKeys;
    
    private Music(@NonNull String rollingcubeKey, String... cubosphereKeys)
    {
        this.rollingcubeKey = rollingcubeKey;
        if(cubosphereKeys == null || cubosphereKeys.length < 1)
            this.cubosphereKeys = Set.of();
        else
            this.cubosphereKeys = Set.of(cubosphereKeys);
    }
    
    public final @NonNull Set<String> getCubosphereKeys() { return Set.copyOf(cubosphereKeys); }
    
    @Override
    public final String toString() { return getRollingcubeKey(); }
    
    
    private static final @NonNull Map<String, Music> MAP = Stream.of(values())
            .flatMap(th -> Stream.concat(
                    th.cubosphereKeys.stream().map(ck -> Pair.of(ck.toLowerCase(), th)),
                    Stream.of(Pair.of(th.getRollingcubeKey().toLowerCase(), th))))
            .collect(Collectors.toMap(Pair::first, Pair::second));
    
    public static @NonNull Music fromKey(String key)
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
    
    
    
    public static final class Serializer extends StdSerializer<Music>
    {
        public Serializer() { super(Music.class); }
        public Serializer(Class<Music> cls) { super(cls); }

        @Override
        public void serialize(Music value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeString("");
            else
                gen.writeString(value.toString());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<Music>
    {
        public Deserializer() { super(Music.class); }
        public Deserializer(Class<Music> cls) { super(cls); }

        @Override
        public @NonNull Music deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isTextual())
                return DEFAULT;
            
            return Music.fromKey(node.asText());
        }
    }
}
