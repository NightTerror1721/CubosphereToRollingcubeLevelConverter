package kp.rollingcube.levelConverter.level;

import lombok.NonNull;

/**
 *
 * @author Marc
 */
public final class Ball extends Actor<BallTemplate>
{
    private Ball(@NonNull BallTemplate template)
    {
        super(template);
    }
    
    public static @NonNull Ball create(BallTemplate template)
    {
        if(template == null)
            template = BallTemplate.NO_BALL;
        
        return new Ball(template);
    }
}
