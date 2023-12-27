package kp.rollingcube.levelConverter.level;

import java.util.Set;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public enum BlockTemplate
{
    NORMAL("Normal");
    
    public static final @NonNull BlockTemplate DEFAULT = NORMAL;
    
    @Getter private final @NonNull String rollingcubeKey;
    private final @NonNull Set<String> cubosphereKeys;
    
    private BlockTemplate(@NonNull String rollingcubeKey, String... cubosphereKeys)
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
