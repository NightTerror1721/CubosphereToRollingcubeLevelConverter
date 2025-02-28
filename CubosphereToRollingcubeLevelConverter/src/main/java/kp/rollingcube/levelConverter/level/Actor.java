package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Marc
 * @param <T>
 */
@Getter
public class Actor<T extends Template> extends LevelElement<T>
{
    private final PositionAndSideAndDirection initialPosition = new PositionAndSideAndDirection();
    
    Actor(@NonNull T template)
    {
        super(template);
    }
    
    
    public final void setInitialPosition(@NonNull PositionAndSideAndDirection initialPosition)
    {
        this.initialPosition.copyFrom(initialPosition);
    }
    
    @JsonIgnore
    public boolean mayAppearInJson()
    {
        return !hasNullTemplate();   
    }
}
