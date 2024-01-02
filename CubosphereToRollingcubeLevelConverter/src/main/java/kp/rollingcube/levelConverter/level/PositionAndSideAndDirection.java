package kp.rollingcube.levelConverter.level;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
@Data
@NoArgsConstructor
public final class PositionAndSideAndDirection
{
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private @NonNull SideTag side = SideTag.UP;
    private @NonNull Direction direction = Direction.NORTH;
    
    
    public final @NonNull PositionAndSideAndDirection copyFrom(@NonNull PositionAndSideAndDirection other)
    {
        x = other.x;
        y = other.y;
        z = other.z;
        side = other.side;
        direction = other.direction;
        return this;
    }
    
    public final @NonNull PositionAndSideAndDirection copyTo(@NonNull PositionAndSideAndDirection other)
    {
        return other.copyFrom(this);
    }
}
