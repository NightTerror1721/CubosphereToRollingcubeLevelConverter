package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Marc
 */
public final class Block extends LevelElement<BlockTemplate>
{
    @Getter @Setter private int x = 0;
    @Getter @Setter private int y = 0;
    @Getter @Setter private int z = 0;
    
    private Side up;
    private Side down;
    private Side left;
    private Side right;
    private Side front;
    private Side back;
    
    
    private Block(BlockTemplate template)
    {
        super(template);
        
        this.up = createNewSide(SideTag.UP);
        this.down = createNewSide(SideTag.DOWN);
        this.left = createNewSide(SideTag.LEFT);
        this.right = createNewSide(SideTag.RIGHT);
        this.front = createNewSide(SideTag.FRONT);
        this.back = createNewSide(SideTag.BACK);
    }
    
    static @NonNull Block create(BlockTemplate template)
    {
        if(template == null)
            template = BlockTemplate.DEFAULT;
        
        return new Block(template);
    }
    
    @JsonGetter("up") public final Side getJsonUp() { return getJsonSide(up); }
    @JsonGetter("down") public final Side getJsonDown() { return getJsonSide(down); }
    @JsonGetter("left") public final Side getJsonLeft() { return getJsonSide(left); }
    @JsonGetter("right") public final Side getJsonRight() { return getJsonSide(right); }
    @JsonGetter("front") public final Side getJsonFront() { return getJsonSide(front); }
    @JsonGetter("back") public final Side getJsonBack() { return getJsonSide(back); }
    
    @JsonIgnore public final @NonNull Side getUp() { return up; }
    @JsonIgnore public final @NonNull Side getDown() { return down; }
    @JsonIgnore public final @NonNull Side getLeft() { return left; }
    @JsonIgnore public final @NonNull Side getRight() { return right; }
    @JsonIgnore public final @NonNull Side getFront() { return front; }
    @JsonIgnore public final @NonNull Side getBack() { return back; }
    
    @JsonIgnore public final @NonNull Side getSide(@NonNull SideTag sideTag)
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
    
    @JsonIgnore public final @NonNull Side getSide(@NonNull SideId id)
    {
        return getSide(id.getSideTag());
    }
    
    
    public final void changeUpSide(@NonNull BlockTemplate newTemplate) { up = changeSide(up, newTemplate); }
    public final void changeDownSide(@NonNull BlockTemplate newTemplate) { down = changeSide(down, newTemplate); }
    public final void changeLeftSide(@NonNull BlockTemplate newTemplate) { left = changeSide(left, newTemplate); }
    public final void changeRightSide(@NonNull BlockTemplate newTemplate) { right = changeSide(right, newTemplate); }
    public final void changeFrontSide(@NonNull BlockTemplate newTemplate) { front = changeSide(front, newTemplate); }
    public final void changeBackSide(@NonNull BlockTemplate newTemplate) { back = changeSide(back, newTemplate); }
    
    public final void changeSide(@NonNull SideTag sideTag, @NonNull BlockTemplate newTemplate)
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
    
    public final void changeSide(@NonNull SideId id, @NonNull BlockTemplate newTemplate)
    {
        changeSide(id.getSideTag(), newTemplate);
    }
    
    @JsonIgnore
    public final boolean mayAppearInJson()
    {
        return !hasNullTemplate();   
    }

    
    private Side createNewSide(@NonNull SideTag sideTag)
    {
        return Side.create(this, sideTag, BlockTemplate.NULL);
    }
    
    private static Side getJsonSide(@NonNull Side side)
    {
        return side.mayAppearInJson() ? side : null;
    }
    
    private static Side changeSide(@NonNull Side side, @NonNull BlockTemplate newTemplate)
    {
        return side.createResetCopy(newTemplate);
    }
}
