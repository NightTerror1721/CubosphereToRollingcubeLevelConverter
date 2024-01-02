package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public final class Side extends LevelElement<BlockTemplate>
{
    @JsonIgnore @Getter private final @NonNull Block block;
    @JsonIgnore @Getter private final @NonNull SideTag tag;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter private Item item = null;
    
    private Side(@NonNull Block block, @NonNull SideTag tag, @NonNull BlockTemplate template)
    {
        super(template);
        this.block = block;
        this.tag = tag;
    }
    
    static @NonNull Side create(@NonNull Block block, @NonNull SideTag tag, BlockTemplate template)
    {
        if(template == null)
            template = BlockTemplate.NULL;
        
        return new Side(block, tag, template);
    }
    
    public void setItem(Item item)
    {
        if(item == null || !canHasItem())
            this.item = null;
        else
            this.item = item;
    }
    
    public final boolean hasItem() { return item != null; }
    
    @JsonIgnore
    public final BlockTemplate getSideTemplate()
    {
        if(hasNullTemplate())
            return block.getTemplate();
        return getTemplate();
    }
    
    @JsonIgnore
    public final boolean canHasItem() { return getSideTemplate().isCanHasItem(); }
    
    @JsonIgnore
    public final boolean mayAppearInJson()
    {
        return !hasNullTemplate() || hasItem() || hasAnyProperty();   
    }
    
    @JsonIgnore
    public final @NonNull Side createResetCopy(@NonNull BlockTemplate newTemplate)
    {
        return create(block, tag, newTemplate);
    }
}
