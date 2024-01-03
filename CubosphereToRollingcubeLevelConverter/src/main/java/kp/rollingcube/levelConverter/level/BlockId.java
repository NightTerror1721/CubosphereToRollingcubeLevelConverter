package kp.rollingcube.levelConverter.level;

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
public final class BlockId extends ReferenceId
{
    public static final BlockId INVALID = new BlockId(0);
    
    private BlockId(int code) { super(code); }
    
    public static @NonNull BlockId of(int blockId)
    {
        if(blockId <= 0)
            return INVALID;
        
        return new BlockId(blockId);
    }
    public static @NonNull BlockId of() { return INVALID; }
    
    
    public static final class Generator extends BaseGenerator<BlockId>
    {
        @Override
        protected final @NonNull BlockId createNew(int code) { return of(code); }
    }
}
