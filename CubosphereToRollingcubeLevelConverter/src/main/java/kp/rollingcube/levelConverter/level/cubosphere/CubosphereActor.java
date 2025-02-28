package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.level.Direction;
import kp.rollingcube.levelConverter.level.SideTag;
import kp.rollingcube.levelConverter.utils.ReferenceId;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Marc
 * @param <IDT>
 */
public class CubosphereActor<IDT extends ReferenceId> extends CubosphereLevelElement
{
    @Getter private final @NonNull IDT id;
    
    @Getter @Setter private int x = 0;
    @Getter @Setter private int y = 0;
    @Getter @Setter private int z = 0;
    @Getter @Setter private @NonNull SideTag side = SideTag.UP;
    @Getter @Setter private @NonNull Direction direction = Direction.NORTH;
    
    CubosphereActor(@NonNull IDT id, String template)
    {
        super(template);
        this.id = id;
    }
    
    public final void setCubosphereDirection(int direction, @NonNull SideTag sideTag)
    {
        var directionId = CubosphereUtils.toRollingcubeDirection(direction, sideTag);
        setDirection(Direction.fromId(directionId));
    }
}
