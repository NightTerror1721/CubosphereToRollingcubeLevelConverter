package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.level.Direction;
import kp.rollingcube.levelConverter.level.SideTag;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

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
                case 1 -> Direction.WEST.getId();
                case 2 -> Direction.NORTH.getId();
                case 3 -> Direction.EAST.getId();
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
    
    public int toRollingcubeGravityDirection(int cubosphereGravityDirection)
    {
        return switch(cubosphereGravityDirection)
        {
            case 0 -> 0;
            case 1 -> 3;
            case 2 -> 4;
            case 3 -> 2;
            case 4 -> 1;
            case 5 -> 5;
            default -> 0;
        };
    }
    
    public int toRollingcubePositionX(int x) { return x; }
    public int toRollingcubePositionY(int y) { return y; }
    public int toRollingcubePositionZ(int z) { return -z; }
}
