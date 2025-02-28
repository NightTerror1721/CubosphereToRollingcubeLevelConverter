package kp.rollingcube.levelConverter.level.cubosphere;

import java.util.stream.Collectors;
import kp.rollingcube.levelConverter.level.Enemy;
import kp.rollingcube.levelConverter.level.EnemyTemplate;
import kp.rollingcube.levelConverter.level.PositionAndSideAndDirection;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public class CubosphereEnemy extends CubosphereActor<EnemyId>
{   
    private CubosphereEnemy(@NonNull EnemyId id, String template)
    {
        super(id, template);
    }
    
    static @NonNull CubosphereEnemy create(@NonNull EnemyId id, String template)
    {
        return new CubosphereEnemy(id, template);
    }
    
    
    
    public final Enemy toRollingcubeEnemy(@NonNull CubosphereLevelConversionData data)
    {
        if(hasInvalidTemplate())
            return null;
        
        var enemy = switch(getTemplate().toLowerCase())
        {
            case "longspiked", "spiked", "tutorialball" -> {
                var renemy = Enemy.create(EnemyTemplate.GYRO);
                renemy.setPropertyString("Path", filterPath(getPropertyString("Movement"), "flr"));
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                yield renemy;
            }
            case "anomaly" -> {
                var renemy = Enemy.create(EnemyTemplate.ANOMALY);
                renemy.setPropertyString("Path", filterPath(getPropertyString("Movement"), "flrujshwpm"));
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                yield renemy;
            }
            case "gear" -> {
                var renemy = Enemy.create(EnemyTemplate.GEAR);
                renemy.setPropertyString("Path", filterPath(getPropertyString("Movement"), "flr"));
                renemy.setPropertyFloat("ForwardSpeed", getPropertyFloat("Speed"));
                renemy.setPropertyFloat("RotateSpeed", getPropertyFloat("RotationSpeed", 1f));
                yield renemy;
            }
            case "hunter" -> {
                var renemy = Enemy.create(EnemyTemplate.HUNTER);
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                renemy.setPropertyBoolean("TiedToPlane", getPropertyBoolean("TiedToPlane"));
                yield renemy;
            }
            case "jumper" -> {
                var renemy = Enemy.create(EnemyTemplate.JUMPER);
                renemy.setPropertyString("Path", filterPath(getPropertyString("Movement"), "flrujsh"));
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                yield renemy;
            }
            case "rhombus" -> {
                var renemy = Enemy.create(EnemyTemplate.RHOMBUS);
                renemy.setPropertyString("Path", filterPath(getPropertyString("Movement"), "flrw"));
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                yield renemy;
            }
            case "speeder2" -> {
                var renemy = Enemy.create(EnemyTemplate.RHOMBUS);
                renemy.setPropertyString("Path", filterPath(getPropertyString("Movement"), "flrpm"));
                renemy.setPropertyFloat("Speed", getPropertyFloat("Speed"));
                yield renemy;
            }
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
                    .x(CubosphereUtils.toRollingcubePositionX(getX()))
                    .y(CubosphereUtils.toRollingcubePositionY(getY()))
                    .z(CubosphereUtils.toRollingcubePositionZ(getZ()))
                    .side(getSide())
                    .direction(getDirection())
                    .build()
            );

            CubosphereUtils.parseAndSetInteractions(getPropertyString("Interaction"), enemy.getInteractions());
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
                .map(cd -> cd == 'd' ? 's' : cd)
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
