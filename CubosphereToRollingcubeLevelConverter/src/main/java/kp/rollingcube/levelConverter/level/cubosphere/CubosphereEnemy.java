package kp.rollingcube.levelConverter.level.cubosphere;

import java.util.stream.Collectors;
import kp.rollingcube.levelConverter.level.Direction;
import kp.rollingcube.levelConverter.level.Enemy;
import kp.rollingcube.levelConverter.level.EnemyTemplate;
import kp.rollingcube.levelConverter.level.PositionAndSideAndDirection;
import kp.rollingcube.levelConverter.level.SideTag;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Marc
 */
public class CubosphereEnemy extends CubosphereLevelElement
{
    @Getter private final @NonNull EnemyId id;
    
    @Getter @Setter private int x = 0;
    @Getter @Setter private int y = 0;
    @Getter @Setter private int z = 0;
    @Getter @Setter private @NonNull SideTag side = SideTag.UP;
    @Getter @Setter private @NonNull Direction direction = Direction.NORTH;
    
    private CubosphereEnemy(@NonNull EnemyId id, String template)
    {
        super(template);
        this.id = id;
    }
    
    static @NonNull CubosphereEnemy create(@NonNull EnemyId id, String template)
    {
        return new CubosphereEnemy(id, template);
    }
    
    
    public final void setCubosphereDirection(int direction, @NonNull SideTag sideTag)
    {
        var directionId = CubosphereUtils.toRollingcubeDirection(direction, sideTag);
        setDirection(Direction.fromId(directionId));
    }
    
    
    
    public final Enemy toRollingcubeEnemy(@NonNull CubosphereLevelConversionData data)
    {
        if(hasInvalidTemplate())
            return null;
        
        var enemy = switch(getTemplate().toLowerCase())
        {
            case "anomaly", "longspiked", "rhombus", "speeder2", "spiked", "tutorialball" -> {
                var renemy = Enemy.create(EnemyTemplate.GYRO);
                renemy.setPropertyString("Path", filterPath(getPropertyString("Movement"), "flr"));
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                yield renemy;
            }
            case "gear" -> {
                var renemy = Enemy.create(EnemyTemplate.GEAR);
                renemy.setPropertyString("Path", filterPath(getPropertyString("Movement"), "flr"));
                renemy.setPropertyFloat("ForwardSpeed", getPropertyFloat("Speed"));
                renemy.setPropertyFloat("RotateSpeed", getPropertyFloat("RotationSpeed"));
                yield renemy;
            }
            case "hunter" -> {
                var renemy = Enemy.create(EnemyTemplate.HUNTER);
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                renemy.setPropertyBoolean("TiedToPlane", getPropertyBoolean("TiedToPlane"));
                yield renemy;
            }
            case "jumper" -> unknown(data);
            case "randomwalker" -> {
                var renemy = Enemy.create(EnemyTemplate.RANDOM_WALKER);
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                renemy.setPropertyBoolean("TiedToPlane", getPropertyBoolean("TiedToPlane"));
                renemy.setPropertyEnumOrdinal("SideRestriction", 0);
                yield renemy;
            }
            default -> unknown(data);
        };
        
        if(enemy != null)
        {
            enemy.setInitialPosition(PositionAndSideAndDirection.builder()
                    .x(CubosphereUtils.toRollingcubePositionX(x))
                    .y(CubosphereUtils.toRollingcubePositionY(y))
                    .z(CubosphereUtils.toRollingcubePositionZ(z))
                    .side(side)
                    .direction(direction)
                    .build()
            );
        }
        
        return enemy;
    }
    
    
    private static @NonNull String filterPath(@NonNull String path, @NonNull CharSequence availableSigns)
    {
        var availables = availableSigns.chars()
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toSet());
        return path.chars()
                .mapToObj(Integer::valueOf)
                .filter(availables::contains)
                .map(cd -> Character.toString((char) cd.intValue()))
                .collect(Collectors.joining());
    }
    
    private Enemy unknown(@NonNull CubosphereLevelConversionData data)
    {
        data.warn("Enemy '%s' not exists in Rollingcube. Replaced by no-enemy", getTemplate().toLowerCase());
        return null;
    }
}
