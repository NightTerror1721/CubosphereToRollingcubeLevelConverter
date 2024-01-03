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
public final class ItemId extends ReferenceId
{
    public static final ItemId INVALID = new ItemId(0);
    
    private ItemId(int code) { super(code); }
    
    public static @NonNull ItemId of(int blockId)
    {
        if(blockId <= 0)
            return INVALID;
        
        return new ItemId(blockId);
    }
    public static @NonNull ItemId of() { return INVALID; }
    
    
    public static final class Generator extends BaseGenerator<ItemId>
    {
        @Override
        protected final @NonNull ItemId createNew(int code) { return of(code); }
    }
}
