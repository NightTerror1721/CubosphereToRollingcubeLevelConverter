package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.level.Block;
import kp.rollingcube.levelConverter.level.BlockTemplate;
import kp.rollingcube.levelConverter.level.Side;
import kp.rollingcube.levelConverter.level.SideId;
import kp.rollingcube.levelConverter.level.SideTag;
import kp.rollingcube.levelConverter.ui.UILogger;
import kp.rollingcube.levelConverter.utils.LoggerUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Marc
 */
public class CubosphereSide extends CubosphereLevelElement
{
    @Getter private final @NonNull CubosphereBlock block;
    @Getter private final @NonNull SideTag tag;
    @Getter private final @NonNull SideId id;
    
    @Getter @Setter private CubosphereItem item = null;
    
    private CubosphereSide(@NonNull CubosphereBlock block, @NonNull SideTag tag, String template)
    {
        super(template);
        
        this.block = block;
        this.tag = tag;
        this.id = SideId.of(block.getId(), tag);
    }
    
    static @NonNull CubosphereSide create(@NonNull CubosphereBlock block, @NonNull SideTag tag, String template)
    {
        return new CubosphereSide(block, tag, template);
    }
    
    
    public final boolean hasItem() { return item != null; }
    
    public final @NonNull String getSideTemplate()
    {
        if(hasInvalidTemplate())
            return block.getTemplate();
        return getTemplate();
    }
    
    public final boolean mayAppearInJson()
    {
        return !hasInvalidTemplate() || hasItem() || hasAnyProperty();   
    }
    
    public final @NonNull CubosphereSide createResetCopy(String newTemplate)
    {
        return create(block, tag, newTemplate);
    }
    
    
    final void toRollingcubeSide(@NonNull Block rblock, UILogger logger)
    {
        switch(getTemplate().toLowerCase())
        {
            case "aircondition" -> unknown(rblock, logger);
            case "breakclock" -> unknown(rblock, logger);
            case "breaking1" -> changeToInvalidSide(rblock); // block only //
            case "button" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.BUTTON);
                rside.setPropertyEnumOrdinal("Color", CubosphereUtils.toRollingcubeColorId(getPropertyInteger("Color")));
            }
            case "counter" -> changeToInvalidSide(rblock); // block only //
            case "elevator" -> changeToInvalidSide(rblock); // block only //
            case "exit1", "exit1KulaMod" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.EXIT);
                
                String nextLevel = getPropertyString("Next_Level");
                
                rside.setPropertyBoolean("Secret", false);
                if(!nextLevel.isBlank() && !nextLevel.equals("nextlevel"))
                    rside.setPropertyString("NextLevel", nextLevel);
            }
            case "exit2", "exit2KulaMod" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.EXIT);
                
                String nextLevel = getPropertyString("Next_Level");
                
                rside.setPropertyBoolean("Secret", true);
                if(!nextLevel.isBlank() && !nextLevel.equals("nextlevel"))
                    rside.setPropertyString("NextLevel", nextLevel);
            }
            case "fire" -> rblock.changeSide(tag, BlockTemplate.BURNED);
            case "fspikes" -> rblock.changeSide(tag, BlockTemplate.STATIC_SPIKES);
            case "ghost" -> changeToInvalidSide(rblock); // block only //
            case "hspikes" -> unknown(rblock, logger);
            case "icy1" -> rblock.changeSide(tag, BlockTemplate.ICY);
            case "invis1" -> changeToInvalidSide(rblock); // block only //
            case "invis2" -> changeToInvalidSide(rblock); // block only //
            case "knife" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.PATTERN_SPIKES);
                rside.setPropertyString("Pattern", getPropertyString("Pattern"));
                rside.setPropertyInteger("PatternIndex", Math.max(0, getPropertyInteger("PatternIndex")));
                rside.setPropertyFloat("Speed", Math.max(0.01f, getPropertyFloat("Speed")));
            }
            case "laser" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.LASER);
                rside.setPropertyEnumOrdinal("Color", CubosphereUtils.toRollingcubeColorId(getPropertyInteger("Color")));
                rside.setPropertyBoolean("Activated", getPropertyBoolean("StartActive"));
                rside.setPropertyEnumOrdinal("NumOfBeams", 1);
            }
            case "lightbarrier" -> unknown(rblock, logger);
            case "magnet" -> unknown(rblock, logger);
            case "normal1" -> rblock.changeSide(tag, BlockTemplate.NORMAL);
            case "oil" -> unknown(rblock, logger);
            case "onedir" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.ONE_WAY);
                rside.setPropertyEnumOrdinal("Direction", CubosphereUtils.toRollingcubeDirection(getPropertyInteger("Rotation"), tag));
            }
            case "onedirtoggle" -> unknown(rblock, logger);
            case "phaser" -> changeToInvalidSide(rblock); // block only //
            case "plate" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.PRESSURE_PLATE);
                rside.setPropertyEnumOrdinal("Color", CubosphereUtils.toRollingcubeColorId(getPropertyInteger("Color")));
            }
            case "power" -> unknown(rblock, logger);
            case "rotate" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.ROTATOR);
                
                int direction = getPropertyBoolean("Clockwise") ? 1 : 0;
                
                rside.setPropertyEnumOrdinal("Direction", direction);
            }
            case "sand" -> rblock.changeSide(tag, BlockTemplate.SAND);
            case "spikes" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.RETRACTABLE_SPIKES);
                rside.setPropertyFloat("Phase", getPropertyFloat("Phase"));
                rside.setPropertyFloat("Speed", getPropertyFloat("Speed"));
            }
            case "switch" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.SWITCH);
                
                int activated = getPropertyBoolean("StartActive") ? 0 : 1;
                
                rside.setPropertyEnumOrdinal("Activated", activated);
                rside.setPropertyEnumOrdinal("Color", CubosphereUtils.toRollingcubeColorId(getPropertyInteger("Color")));
            }
            case "tele_target" -> unknown(rblock, logger);
            case "teleport" -> {
                var rside = rblock.changeSide(tag, BlockTemplate.TELEPORT);
                
                int activated = getPropertyBoolean("StartActive") ? 0 : 1;
                
                rside.setPropertyEnumOrdinal("Activated", activated);
                rside.setPropertyEnumOrdinal("Color", CubosphereUtils.toRollingcubeColorId(getPropertyInteger("Color")));
                rside.setPropertyEnumOrdinal("Direction", CubosphereUtils.toRollingcubeDirection(getPropertyInteger("DestRotation"), tag));
                rside.setPropertyInteger("Order", 0);
            }
            case "timestop" -> rblock.changeSide(tag, BlockTemplate.TIME_STOP);
            case "toggleblock" -> changeToInvalidSide(rblock); // block only //
            case "tramp" -> rblock.changeSide(tag, BlockTemplate.TRAMPOLINE);
            case "tramphigh" -> rblock.changeSide(tag, BlockTemplate.VENT);
            case "tspikes" -> unknown(rblock, logger);
            case "warptunnel" -> changeToInvalidSide(rblock); // block only //
            case "" -> changeToInvalidSide(rblock);
            default -> unknown(rblock, logger);
        }
        
        if(hasItem())
        {
            Side rside = rblock.getSide(tag);
            if(rside.canHasItem())
                rside.setItem(item.toRollingcubeItem(logger));
        }
    }
    
    private void changeToInvalidSide(@NonNull Block rblock)
    {
        Side rside = rblock.getSide(tag);
        if(!rside.hasNullTemplate())
            rblock.changeSide(tag, BlockTemplate.NULL);
    }
    
    private void unknown(@NonNull Block rblock, UILogger logger)
    {
        LoggerUtils.warn(logger, "Side template '%s' not exists in Rollingcube. Replaced by '%s'", getTemplate().toLowerCase(), rblock.getTemplate());
        changeToInvalidSide(rblock);
    }
}
