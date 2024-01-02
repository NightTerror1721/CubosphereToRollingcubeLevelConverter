package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public final class Enemy extends LevelElement<EnemyTemplate>
{
    @Getter private final PositionAndSideAndDirection initialPosition = new PositionAndSideAndDirection();
    
    private Enemy(@NonNull EnemyTemplate template)
    {
        super(template);
    }
    
    public static @NonNull Enemy create(EnemyTemplate template)
    {
        if(template == null)
            template = EnemyTemplate.NO_ENEMY;
        
        return new Enemy(template);
    }
    
    
    public final void setInitialPosition(@NonNull PositionAndSideAndDirection initialPosition)
    {
        this.initialPosition.copyFrom(initialPosition);
    }
    
    @JsonIgnore
    public final boolean mayAppearInJson()
    {
        return !hasNullTemplate();   
    }}
