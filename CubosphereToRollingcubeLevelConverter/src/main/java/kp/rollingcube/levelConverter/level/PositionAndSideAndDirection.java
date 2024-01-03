package kp.rollingcube.levelConverter.level;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class PositionAndSideAndDirection
{
    @Builder.Default private int x = 0;
    @Builder.Default private int y = 0;
    @Builder.Default private int z = 0;
    @Builder.Default private @NonNull SideTag side = SideTag.UP;
    @Builder.Default private @NonNull Direction direction = Direction.NORTH;
    
    
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
