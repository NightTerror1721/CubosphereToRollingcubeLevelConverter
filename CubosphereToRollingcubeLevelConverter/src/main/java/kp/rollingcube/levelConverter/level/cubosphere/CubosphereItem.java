package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.level.Item;
import kp.rollingcube.levelConverter.level.ItemTemplate;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public class CubosphereItem extends CubosphereLevelElement
{
    @Getter private final @NonNull ItemId id;
    
    private CubosphereItem(@NonNull ItemId id, String template)
    {
        super(template);
        this.id = id;
    }
    
    static @NonNull CubosphereItem create(@NonNull ItemId id, String template)
    {
        return new CubosphereItem(id, template);
    }
    
    
    public Item toRollingcubeItem(@NonNull CubosphereLevelConversionData data)
    {
        if(hasInvalidTemplate())
        {
            data.warn("Invalid item. Replaced by no-item");
            return null;
        }
        
        return switch(getTemplate().toLowerCase())
        {
            case "apple" -> Item.create(ItemTemplate.FRUIT);
            case "banana" -> Item.create(ItemTemplate.FRUIT);
            case "bouncepill" -> Item.create(ItemTemplate.BOUNCING_PILL);
            case "cherries" -> Item.create(ItemTemplate.FRUIT);
            case "coin1" -> Item.create(ItemTemplate.BRONZE_COIN);
            case "coin2" -> Item.create(ItemTemplate.SILVER_COIN);
            case "coin3" -> Item.create(ItemTemplate.GOLD_COIN);
            case "cookie" -> Item.create(ItemTemplate.FRUIT);
            case "cube" -> unknown(data);
            case "death" -> unknown(data);
            case "diamond" -> Item.create(ItemTemplate.BLUE_DIAMOND);
            case "diamond2" -> Item.create(ItemTemplate.GREEN_DIAMOND);
            case "diamond3" -> Item.create(ItemTemplate.SPECIAL_DIAMOND);
            case "diamond4" -> Item.create(ItemTemplate.RED_DIAMOND);
            case "diamond5" -> Item.create(ItemTemplate.ORANGE_DIAMOND);
            case "diamond6" -> Item.create(ItemTemplate.WHITE_DIAMOND);
            case "diamond7" -> Item.create(ItemTemplate.MAGENTA_DIAMOND);
            case "dice" -> unknown(data);
            case "donut" -> Item.create(ItemTemplate.FRUIT);
            case "glasses" -> Item.create(ItemTemplate.GLASSES);
            case "goldenletter" -> unknown(data);
            case "gravity" -> {
                var ritem = Item.create(ItemTemplate.GRAVITY_ARROW);
                
                int direction = Math.abs(getPropertyInteger("Direction")) % 6;
                ritem.setPropertyEnumOrdinal("Direction", CubosphereUtils.toRollingcubeGravityDirection(direction));
                
                yield ritem;
            }
            case "hourglass" -> Item.create(ItemTemplate.HOURGLASS);
            case "icebility" -> unknown(data);
            case "invert" -> unknown(data);
            case "invulner" -> unknown(data);
            case "jump_minus" -> Item.create(ItemTemplate.JUMP_MINUS);
            case "jump_plus" -> Item.create(ItemTemplate.JUMP_MINUS);
            case "jumph_minus" -> unknown(data);
            case "jumph_plus" -> unknown(data);
            case "key" -> Item.create(ItemTemplate.KEY);
            case "lollipop" -> Item.create(ItemTemplate.FRUIT);
            case "loupe" -> unknown(data);
            case "minuscoin" -> unknown(data);
            case "mirror" -> unknown(data);
            case "nojump" -> Item.create(ItemTemplate.JUMP_BAN_PILL);
            case "pineapple" -> Item.create(ItemTemplate.FRUIT);
            case "pokal1" -> Item.create(ItemTemplate.GOBLET);
            case "pumpkin" -> Item.create(ItemTemplate.FRUIT);
            case "sandibility" -> unknown(data);
            case "sleepingpill" -> Item.create(ItemTemplate.SLEEPING_PILL);
            case "slowmo" -> Item.create(ItemTemplate.SLOW_MOTION);
            case "strawberry" -> Item.create(ItemTemplate.FRUIT);
            case "teleport" -> {
                var ritem = Item.create(ItemTemplate.PORTAL_ENTRANCE);
                
                int targetIndex = Math.abs(getPropertyInteger("Target") - 1) % CubosphereUtils.COLORS_COUNT;
                ritem.setPropertyEnumOrdinal("Color", targetIndex);
                
                yield ritem;
            }
            case "textout" -> unknown(data);
            case "time_minus" -> Item.create(ItemTemplate.TIME_MINUS);
            case "time_plus" -> Item.create(ItemTemplate.TIME_PLUS);
            case "watermelon" -> Item.create(ItemTemplate.FRUIT);
            default -> {
                data.warn("Invalid item. Replaced by no-item");
                yield null;
            }
        };
    }
    
    private Item unknown(@NonNull CubosphereLevelConversionData data)
    {
        data.warn("Item '%s' not exists in Rollingcube. Replaced by no-item", getTemplate().toLowerCase());
        return null;
    }
}
