package kp.rollingcube.levelConverter.level;

import lombok.NonNull;

/**
 *
 * @author Marc
 */
public final class Item extends LevelElement<ItemTemplate>
{
    private Item(@NonNull ItemTemplate template)
    {
        super(template);
    }
    
    public static @NonNull Item create(ItemTemplate template)
    {
        if(template == null)
            template = ItemTemplate.NO_ITEM;
        
        return new Item(template);
    }
}
