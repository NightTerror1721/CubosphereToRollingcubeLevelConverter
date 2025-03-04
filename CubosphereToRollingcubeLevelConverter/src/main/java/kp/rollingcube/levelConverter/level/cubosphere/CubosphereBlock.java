package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.level.Block;
import kp.rollingcube.levelConverter.level.BlockId;
import kp.rollingcube.levelConverter.level.BlockTemplate;
import kp.rollingcube.levelConverter.level.SideId;
import kp.rollingcube.levelConverter.level.SideTag;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Marc
 */
public class CubosphereBlock extends CubosphereLevelElement
{
    @Getter private final @NonNull BlockId id;
    
    @Getter @Setter private int x = 0;
    @Getter @Setter private int y = 0;
    @Getter @Setter private int z = 0;
    
    @Getter private @NonNull CubosphereSide up;
    @Getter private @NonNull CubosphereSide down;
    @Getter private @NonNull CubosphereSide left;
    @Getter private @NonNull CubosphereSide right;
    @Getter private @NonNull CubosphereSide front;
    @Getter private @NonNull CubosphereSide back;
    
    private CubosphereBlock(@NonNull BlockId id, String template)
    {
        super(template);
        this.id = id;
        if(id.isInvalid())
            throw new IllegalArgumentException("id");
        
        this.up = createNewSide(SideTag.UP);
        this.down = createNewSide(SideTag.DOWN);
        this.left = createNewSide(SideTag.LEFT);
        this.right = createNewSide(SideTag.RIGHT);
        this.front = createNewSide(SideTag.FRONT);
        this.back = createNewSide(SideTag.BACK);
    }
    
    static @NonNull CubosphereBlock create(@NonNull BlockId id, String template)
    {
        if(template == null || template.isBlank())
            template = "normal1";
        
        return new CubosphereBlock(id, template);
    }
    
    
    public final @NonNull CubosphereSide getSide(@NonNull SideTag sideTag)
    {
        return switch(sideTag)
        {
            case UP -> up;
            case DOWN -> down;
            case LEFT -> left;
            case RIGHT -> right;
            case FRONT -> front;
            case BACK -> back;
        };
    }
    
    public final @NonNull CubosphereSide getSide(@NonNull SideId id)
    {
        return getSide(id.getSideTag());
    }
    
    public final void changeUpSide(String newTemplate) { up = changeSide(up, newTemplate); }
    public final void changeDownSide(String newTemplate) { down = changeSide(down, newTemplate); }
    public final void changeLeftSide(String newTemplate) { left = changeSide(left, newTemplate); }
    public final void changeRightSide(String newTemplate) { right = changeSide(right, newTemplate); }
    public final void changeFrontSide(String newTemplate) { front = changeSide(front, newTemplate); }
    public final void changeBackSide(String newTemplate) { back = changeSide(back, newTemplate); }
    
    public final void changeSide(@NonNull SideTag sideTag, String newTemplate)
    {
        switch(sideTag)
        {
            case UP -> changeUpSide(newTemplate);
            case DOWN -> changeDownSide(newTemplate);
            case LEFT -> changeLeftSide(newTemplate);
            case RIGHT -> changeRightSide(newTemplate);
            case FRONT -> changeFrontSide(newTemplate);
            case BACK -> changeBackSide(newTemplate);
        }
    }
    
    public final void changeSide(@NonNull SideId id, String newTemplate)
    {
        changeSide(id.getSideTag(), newTemplate);
    }
    
    
    private CubosphereSide createNewSide(@NonNull SideTag sideTag)
    {
        return CubosphereSide.create(this, sideTag, null);
    }
    
    private static CubosphereSide changeSide(@NonNull CubosphereSide side, String newTemplate)
    {
        return side.createResetCopy(newTemplate);
    }
    
    
    public final @NonNull Block toRollingcubeBlock(@NonNull CubosphereLevelConversionData data)
    {
        if(hasInvalidTemplate())
            return unknown(data);
        
        Block rblock = switch(getTemplate().toLowerCase())
        {
            case "aircondition" -> unknown(data);
            case "breakclock" -> unknown(data);
            case "breaking1" -> Block.create(BlockTemplate.BREAKING);
            case "button" -> Block.create(BlockTemplate.BUTTON);
            case "counter" -> {
                var blk = Block.create(BlockTemplate.COUNTER);
                blk.setPropertyEnumOrdinal("Times", Math.clamp(getPropertyInteger("Counter"), 1, 9) - 1);
                yield blk;
            }
            case "elevator" -> {
                var blk = Block.create(BlockTemplate.ELEVATOR);
                
                int amplitude = Math.max(1, (int) getPropertyFloat("Amplitude"));
                float speed = Math.max(0.1f, getPropertyFloat("Speed"));
                float phase = Math.clamp(getPropertyFloat("Phase"), 0, 1);
                int direction = Math.abs(getPropertyInteger("Direction")) % 3;
                float delayTime = Math.max(0, getPropertyFloat("DelayTime"));
                
                int steps = ((amplitude % 2) != 0 ? amplitude + 1 : amplitude) / 2;
                int movementMode = delayTime > 0 ? 0 : 1;
                
                blk.setPropertyInteger("ForwardSteps", steps);
                blk.setPropertyInteger("BackwardSteps", steps);
                blk.setPropertyEnumOrdinal("Direction", CubosphereUtils.toRollingcubeElevatorDirection(direction, phase));
                blk.setPropertyFloat("Speed", speed);
                blk.setPropertyFloat("DelayTime", delayTime);
                blk.setPropertyEnumOrdinal("MovementMode", movementMode);
                
                yield blk;
            }
            case "exit1", "exit1KulaMod" -> Block.create(BlockTemplate.EXIT);
            case "exit2", "exit2KulaMod" -> Block.create(BlockTemplate.EXIT);
            case "fire" -> Block.create(BlockTemplate.BURNED);
            case "fspikes" -> Block.create(BlockTemplate.STATIC_SPIKES);
            case "ghost" -> Block.create(BlockTemplate.FULL_INVISIBLE_BLOCK);
            case "hspikes" -> unknown(data);
            case "icy1" -> Block.create(BlockTemplate.ICY);
            case "invis1" -> Block.create(BlockTemplate.FAR_INVISIBLE_BLOCK);
            case "invis2" -> Block.create(BlockTemplate.NEAR_INVISIBLE_BLOCK);
            case "knife" -> Block.create(BlockTemplate.PATTERN_SPIKES);
            case "laser" -> Block.create(BlockTemplate.LASER);
            case "lightbarrier" -> unknown(data);
            case "magnet" -> Block.create(BlockTemplate.MAGNET);
            case "normal1" -> Block.create(BlockTemplate.NORMAL);
            case "oil" -> Block.create(BlockTemplate.SAND);
            case "onedir" -> Block.create(BlockTemplate.ONE_WAY);
            case "onedirtoggle" -> unknown(data);
            case "phaser" -> {
                var blk = Block.create(BlockTemplate.BLINKING);
                
                float timeActivated = Math.max(0, getPropertyFloat("ActiveTime"));
                float timeDeactivated = Math.max(0, getPropertyFloat("DeactiveTime"));
                float timeBlending = Math.max(0, getPropertyFloat("BlendTime"));
                float phase = Math.clamp(getPropertyFloat("Phase"), 0, 1);
                
                float totalTime = timeActivated + timeBlending + timeDeactivated + timeBlending;
                phase *= totalTime;
                
                blk.setPropertyFloat("TimeActivated", timeActivated);
                blk.setPropertyFloat("TimeDeactivated", timeDeactivated);
                blk.setPropertyFloat("TimeBlending", timeBlending);
                blk.setPropertyFloat("Phase", phase);
                
                yield blk;
            }
            case "plate" -> Block.create(BlockTemplate.PRESSURE_PLATE);
            case "power" -> unknown(data);
            case "rotate" -> Block.create(BlockTemplate.ROTATOR);
            case "sand" -> Block.create(BlockTemplate.SAND);
            case "spikes" -> Block.create(BlockTemplate.RETRACTABLE_SPIKES);
            case "switch" -> Block.create(BlockTemplate.SWITCH);
            case "tele_target" -> unknown(data);
            case "teleport" -> Block.create(BlockTemplate.TELEPORT);
            case "timestop" -> Block.create(BlockTemplate.TIME_STOP);
            case "toggleblock" -> {
                var blk = Block.create(BlockTemplate.TOGGLE);
                
                boolean activated = getPropertyBoolean("StartActive");
                int color = CubosphereUtils.toRollingcubeColorId(getPropertyInteger("Color"));
                
                blk.setPropertyBoolean("Activated", activated);
                blk.setPropertyEnumOrdinal("Color", color);
                
                yield blk;
            }
            case "tramp" -> Block.create(BlockTemplate.TRAMPOLINE);
            case "tramphigh" -> Block.create(BlockTemplate.VENT);
            case "tspikes" -> unknown(data);
            case "warptunnel" -> {
                var blk = Block.create(BlockTemplate.WARP);
                
                String nextLevel = getPropertyString("Next_Level");
                
                blk.setPropertyBoolean("RequireKeys", false);
                if(!nextLevel.isBlank() && !nextLevel.equals("nextlevel"))
                    blk.setPropertyString("NextLevel", nextLevel);
                
                yield blk;
            }
            default -> unknown(data);
        };
        
        rblock.setX(CubosphereUtils.toRollingcubePositionX(x));
        rblock.setY(CubosphereUtils.toRollingcubePositionY(y));
        rblock.setZ(CubosphereUtils.toRollingcubePositionZ(z));
        
        up.toRollingcubeSide(rblock, data);
        down.toRollingcubeSide(rblock, data);
        left.toRollingcubeSide(rblock, data);
        right.toRollingcubeSide(rblock, data);
        front.toRollingcubeSide(rblock, data);
        back.toRollingcubeSide(rblock, data);
        
        return rblock;
    }
    
    private @NonNull Block unknown(@NonNull CubosphereLevelConversionData data)
    {
        data.warn("Block template '%s' not exists in Rollingcube. Replaced by '%s'", getTemplate().toLowerCase(), BlockTemplate.DEFAULT);
        return Block.create(BlockTemplate.DEFAULT);
    }
}
