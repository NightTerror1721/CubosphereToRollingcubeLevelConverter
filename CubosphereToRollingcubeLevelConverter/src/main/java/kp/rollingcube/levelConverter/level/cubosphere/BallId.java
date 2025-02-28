package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.utils.ReferenceId;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 *
 * @author Marc
 */
@Value
@EqualsAndHashCode(callSuper = true)
public final class BallId extends ReferenceId
{
    public static final BallId INVALID = new BallId(0);
    
    private BallId(int code) { super(code); }
    
    public static @NonNull BallId of(int blockId)
    {
        if(blockId <= 0)
            return INVALID;
        
        return new BallId(blockId);
    }
    public static @NonNull BallId of() { return INVALID; }
    
    
    public static final class Generator extends BaseGenerator<BallId>
    {
        @Override
        protected final @NonNull BallId createNew(int code) { return of(code); }
    }
}
