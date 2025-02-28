package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 *
 * @author Marc
 */
@Getter
public final class Enemy extends Actor<EnemyTemplate>
{
    private final EnemyInteraction interactions = new EnemyInteraction();

    private Enemy(@NonNull EnemyTemplate template)
    {
        super(template);
        interactions.setInitialInteractions(template.getDefaultInteractions());
    }

    public void setInteractions(@Nullable EnemyInteraction interactions)
    {
        if (interactions == null)
            this.interactions.disableAllInteractions();
        else
            this.interactions.set(interactions);
    }

    @JsonIgnore
    @Override
    public boolean mayAppearInJson()
    {
        return super.mayAppearInJson() && !interactions.isEmpty();
    }
    
    public static @NonNull Enemy create(EnemyTemplate template)
    {
        if(template == null)
            template = EnemyTemplate.NO_ENEMY;
        
        return new Enemy(template);
    }
}
