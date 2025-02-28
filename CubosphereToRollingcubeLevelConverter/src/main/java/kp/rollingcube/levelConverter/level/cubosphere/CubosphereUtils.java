package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.level.Direction;
import kp.rollingcube.levelConverter.level.EnemyInteraction;
import kp.rollingcube.levelConverter.level.EnemyInteractionType;
import kp.rollingcube.levelConverter.level.SideTag;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Marc
 */
@UtilityClass
public final class CubosphereUtils
{   
    public int COLORS_COUNT = 12;
    
    public int toRollingcubeColorId(int cubosphereColorId)
    {
        return switch(cubosphereColorId)
        {
            case 1 -> 2;
            case 2 -> 1;
            default -> cubosphereColorId;
        };
    }
    
    public int toRollingcubeDirection(int cubosphereDirection, @NonNull SideTag sideTag)
    {
        cubosphereDirection = Math.abs(cubosphereDirection) % 4;
        return switch(sideTag)
        {
            case UP -> switch(cubosphereDirection)
            {
                case 0 -> Direction.NORTH.getId();
                case 1 -> Direction.WEST.getId();
                case 2 -> Direction.SOUTH.getId();
                case 3 -> Direction.EAST.getId();
                default -> Direction.NORTH.getId();
            };
            case DOWN -> switch(cubosphereDirection)
            {
                case 0 -> Direction.SOUTH.getId();
                case 1 -> Direction.EAST.getId();
                case 2 -> Direction.NORTH.getId();
                case 3 -> Direction.WEST.getId();
                default -> Direction.NORTH.getId();
            };
            case LEFT -> switch(cubosphereDirection)
            {
                case 0 -> Direction.NORTH.getId();
                case 1 -> Direction.WEST.getId();
                case 2 -> Direction.SOUTH.getId();
                case 3 -> Direction.EAST.getId();
                default -> Direction.NORTH.getId();
            };
            case RIGHT -> switch(cubosphereDirection)
            {
                case 0 -> Direction.SOUTH.getId();
                case 1 -> Direction.EAST.getId();
                case 2 -> Direction.NORTH.getId();
                case 3 -> Direction.WEST.getId();
                default -> Direction.NORTH.getId();
            };
            case FRONT -> switch(cubosphereDirection)
            {
                case 0 -> Direction.EAST.getId();
                case 1 -> Direction.NORTH.getId();
                case 2 -> Direction.WEST.getId();
                case 3 -> Direction.SOUTH.getId();
                default -> Direction.NORTH.getId();
            };
            case BACK -> switch(cubosphereDirection)
            {
                
                case 0 -> Direction.WEST.getId();
                case 1 -> Direction.SOUTH.getId();
                case 2 -> Direction.EAST.getId();
                case 3 -> Direction.NORTH.getId();
                default -> Direction.NORTH.getId();
            };
        };
    }
    
    public int toRollingcubeElevatorDirection(int cubosphereElevatorDirection, float cubosphereElevatorPhase)
    {
        cubosphereElevatorDirection = Math.abs(cubosphereElevatorDirection) % 3;
        if(cubosphereElevatorPhase >= 0.25f && cubosphereElevatorPhase <= 0.75f) return switch(cubosphereElevatorDirection)
        {
            case 0 -> 2;
            case 1 -> 1;
            case 2 -> 5;
            default -> 0;
        };
        
        return switch(cubosphereElevatorDirection)
        {
            case 0 -> 3;
            case 1 -> 0;
            case 2 -> 4;
            default -> 0;
        };
    }
    
    public int toRollingcubeGravityDirection(CubosphereSide side, int cubosphereGravityDirection)
    {
        if(side == null)
            return 0;

        cubosphereGravityDirection = Math.abs(cubosphereGravityDirection) % 6;
        return switch(side.getTag())
        {
            case UP -> switch(cubosphereGravityDirection)
            {
                case 0 -> 0;
                case 1 -> 3;
                case 2 -> 5;
                case 3 -> 2;
                case 4 -> 4;
                case 5 -> 1;
                default -> 0;
            };
            case DOWN -> switch(cubosphereGravityDirection)
            {
                case 0 -> 1;
                case 1 -> 2;
                case 2 -> 5;
                case 3 -> 3;
                case 4 -> 4;
                case 5 -> 0;
                default -> 0;
            };
            case RIGHT -> switch(cubosphereGravityDirection)
            {
                case 0 -> 3;
                case 1 -> 0;
                case 2 -> 4;
                case 3 -> 1;
                case 4 -> 5;
                case 5 -> 2;
                default -> 0;
            };
            case LEFT -> switch(cubosphereGravityDirection)
            {
                case 0 -> 2;
                case 1 -> 0;
                case 2 -> 5;
                case 3 -> 1;
                case 4 -> 4;
                case 5 -> 3;
                default -> 0;
            };
            case BACK -> switch(cubosphereGravityDirection)
            {
                case 0 -> 5;
                case 1 -> 0;
                case 2 -> 2;
                case 3 -> 1;
                case 4 -> 3;
                case 5 -> 4;
                default -> 0;
            };
            case FRONT -> switch(cubosphereGravityDirection)
            {
                case 0 -> 4;
                case 1 -> 0;
                case 2 -> 3;
                case 3 -> 1;
                case 4 -> 2;
                case 5 -> 5;
                default -> 0;
            };
        };
    }
    
    public int toRollingcubePositionX(int x) { return x; }
    public int toRollingcubePositionY(int y) { return y; }
    public int toRollingcubePositionZ(int z) { return -z; }

    private final Set<Character> CUBOSPHERE_INTERACTIONS = Set.of('s', 'i', 't', 'p', 'b', 'f', 'l', 'o');
    public Set<EnemyInteractionType> toRollingcubeInteraction(char interactionSymbol)
    {
        return switch(Character.toLowerCase(interactionSymbol))
        {
            case 's' -> Set.of(EnemyInteractionType.BUTTONS);
            case 'i' -> Set.of(EnemyInteractionType.ICE);
            case 't' -> Set.of(EnemyInteractionType.TRAMPS_AND_VENTS);
            case 'p' -> Set.of(EnemyInteractionType.SPIKES, EnemyInteractionType.TELEPORTS);
            case 'b' -> Set.of(EnemyInteractionType.BREAKING_BLOCKS);
            case 'f' -> Set.of(EnemyInteractionType.FIRE);
            case 'l' -> Set.of(EnemyInteractionType.LASERS);
            case 'o' -> Set.of(EnemyInteractionType.DIRECTION_RESTRICTIONS);
            default -> Set.of();
        };
    }

    public void parseAndSetInteractions(String cubosphereInteractions, EnemyInteraction rollingcubeInteractions)
    {
        if (cubosphereInteractions == null || cubosphereInteractions.isBlank())
            cubosphereInteractions = "";

        Set<Character> cubosphereInteractionSymbols = cubosphereInteractions.chars()
                .mapToObj(cp -> (char)cp)
                .collect(Collectors.toSet());

        for (var cubosphereInteractionSymbol : CUBOSPHERE_INTERACTIONS)
        {
            var interactionTypes = toRollingcubeInteraction(cubosphereInteractionSymbol);
            var enabled = cubosphereInteractionSymbols.contains(cubosphereInteractionSymbol);
            for (var type : interactionTypes)
                rollingcubeInteractions.setInteraction(type, enabled);
        }
    }
}
