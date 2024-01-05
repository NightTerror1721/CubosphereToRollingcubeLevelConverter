package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import kp.rollingcube.levelConverter.json.JsonMapper;
import kp.rollingcube.levelConverter.level.cubosphere.CubosphereBlock;
import kp.rollingcube.levelConverter.level.cubosphere.CubosphereEnemy;
import kp.rollingcube.levelConverter.ui.UILogger;
import kp.rollingcube.levelConverter.utils.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Marc
 */
@NoArgsConstructor
public class Level
{
    @Getter @Setter private @NonNull String name = "";
    @Getter @Setter private @NonNull Version version = Version.zero();
    @Getter @Setter private @NonNull Theme theme = Theme.DEFAULT;
    @Getter @Setter private @NonNull Music music = Music.DEFAULT;
    @Getter private int initialTime = 100;
    @Getter private int maxTime = 100;
    @Getter @Setter private @NonNull ThemeMode themeMode = ThemeMode.DEFAULT;
    
    @Getter @Setter private @NonNull BallTemplate ballTemplate = BallTemplate.DEFAULT;
    @Getter private final PositionAndSideAndDirection initialPosition = new PositionAndSideAndDirection();
    
    private final @NonNull List<Block> blocks = new LinkedList<>();
    private final @NonNull List<Enemy> enemies = new LinkedList<>();
    
    
    public final void setInitialTime(int initialTime) { this.initialTime = Math.max(1, initialTime); }
    public final void setMaxTime(int initialTime) { this.maxTime = Math.max(0, maxTime); }
    
    public final void setInitialPosition(@NonNull PositionAndSideAndDirection initialPosition)
    {
        this.initialPosition.copyFrom(initialPosition);
    }
    
    @JsonIgnore
    public final @NonNull List<Block> getBlocks() { return List.copyOf(blocks); }
    
    @JsonIgnore
    public final @NonNull List<Enemy> getEnemies() { return List.copyOf(enemies); }
    
    
    @JsonGetter("blocks")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_EMPTY)
    public final @NonNull List<Block> getJsonBlocks()
    {
        return blocks.stream().filter(Block::mayAppearInJson).toList();
    }
    
    @JsonGetter("enemies")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_EMPTY)
    public final @NonNull List<Enemy> getJsonEnemies()
    {
        return enemies.stream().filter(Enemy::mayAppearInJson).toList();
    }
    
    
    public final @NonNull Block createNewBlock(@NonNull BlockTemplate template)
    {
        if(BlockTemplate.isNull(template))
            template = BlockTemplate.DEFAULT;
        
        var block = Block.create(template);
        blocks.add(block);
        return block;
    }
    
    public final @NonNull Block createNewBlock(@NonNull CubosphereBlock cblock, UILogger logger)
    {
        var block = cblock.toRollingcubeBlock(logger);
        blocks.add(block);
        return block;
    }
    
    public final @NonNull Enemy createNewEnemy(@NonNull EnemyTemplate template)
    {
        var enemy = Enemy.create(template);
        enemies.add(enemy);
        return enemy;
    }
    
    public final Enemy createNewEnemy(@NonNull CubosphereEnemy cenemy, UILogger logger)
    {
        var enemy = cenemy.toRollingcubeEnemy(logger);
        if(enemy != null)
            enemies.add(enemy);
        return enemy;
    }
    
    
    
    public static void write(@NonNull OutputStream out, @NonNull Level level) throws IOException
    {
        JsonMapper.OBJECT_MAPPER.writeValue(out, level);
    }
    
    public static void write(@NonNull Path path, @NonNull Level level) throws IOException
    {
        try(var out = Files.newOutputStream(path))
        {
            write(out, level);
        }
    }
}
