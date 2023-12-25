package kp.rollingcube.levelConverter.level;

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
public enum Theme
{
    EGYPT("Egypt", "egypt"),
    BONUS("Bonus"),
    CLOUDY("Cloudy", "cloudy"),
    INCA("Inca", "inca"),
    STORM("Storm", "stormy"),
    ARTIC("Artic", "alpine"),
    VALLEY("Valley", "valley"),
    FIELD("Field_Remastered"),
    CHINA("China"),
    ATLANTIS("Atlantis_Remastered"),
    HAZE("Haze_Remastered"),
    MARS("Mars_Remastered", "mars"),
    CHESS("Chess", "chess"),
    NIGHT("Night", "night");
    
    public static final @NonNull Theme DEFAULT = EGYPT;
    
    @Getter private final @NonNull String rollingcubeKey;
    private final @NonNull Set<String> cubosphereKeys;
    
    private Theme(@NonNull String rollingcubeKey, String... cubosphereKeys)
    {
        this.rollingcubeKey = rollingcubeKey;
        if(cubosphereKeys == null || cubosphereKeys.length < 1)
            this.cubosphereKeys = Set.of();
        else
            this.cubosphereKeys = Set.of(cubosphereKeys);
    }
    
    public final @NonNull Set<String> getCubosphereKeys() { return Set.copyOf(cubosphereKeys); }
    
    
    private static final @NonNull Map<String, Theme> MAP = Stream.of(values())
            .flatMap(th -> th.cubosphereKeys.stream().map(ck -> Pair.of(ck, th)))
            .collect(Collectors.toMap(Pair::first, Pair::second));
    
    public static @NonNull Theme fromCubosphereKey(String key)
    {
        return MAP.getOrDefault(key, DEFAULT);
    }
}
