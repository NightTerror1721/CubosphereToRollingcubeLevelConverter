package kp.rollingcube.levelConverter.level;

import lombok.NonNull;
import lombok.Value;

/**
 *
 * @author Marc
 */
@Value
public final class SideId
{
    public static final SideId INVALID = new SideId(BlockId.INVALID, SideTag.UP);
    
    private final BlockId blockId;
    private final SideTag sideTag;
    
    private SideId(@NonNull BlockId blockId, @NonNull SideTag sideTag)
    {
        this.blockId = blockId;
        this.sideTag = sideTag;
    }
    
    public static @NonNull SideId of(@NonNull BlockId blockId, @NonNull SideTag sideTag)
    {
        if(blockId.isInvalid())
            return INVALID;
        
        return new SideId(blockId, sideTag);
    }
    public static @NonNull SideId of() { return INVALID; }
    
    public static @NonNull SideId of(int sideId)
    {
        if(sideId <= 0)
            return INVALID;
        
        int blockId = ((sideId - 1) / 6) + 1;
        SideTag sideTag = SideTag.fromSideId(sideId);
        return of(BlockId.of(blockId), sideTag);
    }
    
    public static @NonNull SideId fromCubosphereId(int cubosphereId)
    {
        int rawBlockId = cubosphereId / 6;
        int rawSideId = cubosphereId % 6;
        
        return of(BlockId.of(rawBlockId), SideTag.fromCubosphereSideId(rawSideId));
    }
    
    public int getCode() { return sideTag.computeSideId(blockId.getCode()); }
    
    public final boolean isValid() { return blockId.isValid(); }
    public final boolean isInvalid() { return blockId.isInvalid(); }
    
    public final int toInt() { return getCode(); }
    

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + getCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(obj == null)
            return false;
        
        if(obj instanceof SideId sideId)
            return blockId.equals(sideId.blockId) && sideTag == sideId.sideTag;
        
        return false;
    }
    
    @Override
    public String toString() { return Integer.toString(getCode()); }
}
