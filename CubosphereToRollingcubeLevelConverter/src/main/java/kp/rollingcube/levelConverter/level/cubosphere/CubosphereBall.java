package kp.rollingcube.levelConverter.level.cubosphere;

import kp.rollingcube.levelConverter.level.Ball;
import kp.rollingcube.levelConverter.level.BallTemplate;
import kp.rollingcube.levelConverter.level.PositionAndSideAndDirection;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public class CubosphereBall extends CubosphereActor<BallId>
{
    private CubosphereBall(@NonNull BallId id, String template)
    {
        super(id, template);
    }
    
    static @NonNull CubosphereBall create(@NonNull BallId id, String template)
    {
        return new CubosphereBall(id, template);
    }
    
    
    
    public final Ball toRollingcubeEnemy(@NonNull CubosphereLevelConversionData data)
    {
        if(hasInvalidTemplate())
            return null;
        
        var ball = Ball.create(parseTemplate(data));
        ball.setInitialPosition(PositionAndSideAndDirection.builder()
                .x(CubosphereUtils.toRollingcubePositionX(getX()))
                .y(CubosphereUtils.toRollingcubePositionY(getY()))
                .z(CubosphereUtils.toRollingcubePositionZ(getZ()))
                .side(getSide())
                .direction(getDirection())
                .build()
        );
        
        return ball;
    }
    
    private @NonNull BallTemplate parseTemplate(@NonNull CubosphereLevelConversionData data)
    {
        var template = getTemplate();
        if(!BallTemplate.existsKey(template))
            data.warn("Ball template '%s' not exists in Rollingcube. Replaced by '%s'", template, BallTemplate.DEFAULT);
        
        return BallTemplate.fromKey(template);
    }
}
