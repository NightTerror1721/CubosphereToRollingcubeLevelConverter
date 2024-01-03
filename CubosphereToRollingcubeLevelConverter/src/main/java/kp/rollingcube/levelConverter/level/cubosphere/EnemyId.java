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
public final class EnemyId extends ReferenceId
{
    public static final EnemyId INVALID = new EnemyId(0);
    
    private EnemyId(int code) { super(code); }
    
    public static @NonNull EnemyId of(int blockId)
    {
        if(blockId <= 0)
            return INVALID;
        
        return new EnemyId(blockId);
    }
    public static @NonNull EnemyId of() { return INVALID; }
    
    
    public static final class Generator extends BaseGenerator<EnemyId>
    {
        @Override
        protected final @NonNull EnemyId createNew(int code) { return of(code); }
    }
}
