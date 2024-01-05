package kp.rollingcube.levelConverter.level.cubosphere;

import java.util.HashMap;
import java.util.LinkedList;
import kp.rollingcube.levelConverter.level.BallTemplate;
import kp.rollingcube.levelConverter.level.BlockId;
import kp.rollingcube.levelConverter.level.Direction;
import kp.rollingcube.levelConverter.level.Level;
import kp.rollingcube.levelConverter.level.Music;
import kp.rollingcube.levelConverter.level.PositionAndSideAndDirection;
import kp.rollingcube.levelConverter.level.SideId;
import kp.rollingcube.levelConverter.level.SideTag;
import kp.rollingcube.levelConverter.level.Theme;
import kp.rollingcube.levelConverter.level.ThemeMode;
import kp.rollingcube.levelConverter.ui.UILogger;
import kp.rollingcube.levelConverter.utils.LoggerUtils;
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
public final class CubosphereLevel
{
    @Getter @Setter private @NonNull String theme = "";
    @Getter @Setter private @NonNull String music = "";
    @Getter private int initialTime = 100;
    @Getter private int maxTime = 100;
    
    @Getter @Setter private @NonNull String ballTemplate = "";
    @Getter @Setter private int ballInitialX = 0;
    @Getter @Setter private int ballInitialY = 0;
    @Getter @Setter private int ballInitialZ = 0;
    @Getter @Setter private @NonNull SideTag ballInitialSide = SideTag.UP;
    @Getter @Setter private @NonNull Direction ballInitialDirection = Direction.NORTH;
    
    private final @NonNull LinkedList<CubosphereBlock> blocks = new LinkedList<>();
    private final @NonNull LinkedList<CubosphereEnemy> enemies = new LinkedList<>();
    
    private final @NonNull BlockId.Generator blockIdGenerator = new BlockId.Generator();
    private final @NonNull ItemId.Generator itemIdGenerator = new ItemId.Generator();
    private final @NonNull EnemyId.Generator enemyIdGenerator = new EnemyId.Generator();
    
    private final @NonNull HashMap<ItemId, CubosphereItem> itemsMap = new HashMap<>();
    private final @NonNull HashMap<EnemyId, CubosphereEnemy> enemiesMap = new HashMap<>();
    
    
    public final void setInitialTime(int initialTime) { this.initialTime = Math.max(1, initialTime); }
    public final void setMaxTime(int initialTime) { this.maxTime = Math.max(0, maxTime); }
    
    public final void setBallInitialDirection(int direction, @NonNull SideTag sideTag)
    {
        var directionId = CubosphereUtils.toRollingcubeDirection(direction, sideTag);
        setBallInitialDirection(Direction.fromId(directionId));
    }
    
    public final @NonNull CubosphereBlock getFirstBlock() { return blocks.getFirst(); }
    public final @NonNull CubosphereBlock getLastBlock() { return blocks.getLast(); }
    public final CubosphereBlock getBlock(@NonNull BlockId id)
    {
        if(id.isInvalid())
            throw new IllegalArgumentException();
        
        int index = id.getCode() - 1;
        if(index > blocks.size())
            return null;
        
        return blocks.get(index);
    }
    
    public final CubosphereSide getSide(@NonNull SideId id)
    {
        var block = getBlock(id.getBlockId());
        if(block == null)
            return null;
        
        return block.getSide(id.getSideTag());
    }
    
    public final CubosphereItem getItem(@NonNull ItemId id)
    {
        return itemsMap.getOrDefault(id, null);
    }
    
    public final CubosphereEnemy getEnemy(@NonNull EnemyId id)
    {
        return enemiesMap.getOrDefault(id, null);
    }
    
    public final @NonNull CubosphereBlock createNewBlock(String template)
    {
        var block = CubosphereBlock.create(blockIdGenerator.generate(), template);
        blocks.add(block);
        return block;
    }
    
    public final CubosphereItem createNewItem(@NonNull SideId sideId, String template)
    {
        var side = getSide(sideId);
        if(side == null)
            return null;
        
        ItemId itemId;
        if(side.hasItem())
        {
            var item = side.getItem();
            itemId = item.getId();
        }
        else
            itemId = itemIdGenerator.generate();
        
        var item = CubosphereItem.create(itemId, template);
        if(item.hasInvalidTemplate())
        {
            itemsMap.remove(itemId);
            return null;
        }
        
        itemsMap.put(item.getId(), item);
        side.setItem(item);
        
        return item;
    }
    
    public final CubosphereEnemy createNewEnemy(String template)
    {
        var enemy = CubosphereEnemy.create(enemyIdGenerator.generate(), template);
        enemiesMap.put(enemy.getId(), enemy);
        enemies.add(enemy);
        return enemy;
    }
    
    
    public final @NonNull Level toRollingcubeLevel(UILogger logger, String levelName)
    {
        var level = toRollingcubeLevel(logger);
        if(levelName != null && !levelName.isBlank())
            level.setName(levelName);
        return level;
    }
    
    public final @NonNull Level toRollingcubeLevel(UILogger logger)
    {
        var level = new Level();
        level.setVersion(Version.CURRENT_LEVEL_VERSION);
        level.setTheme(parseTheme(logger));
        level.setMusic(parseMusic(logger));
        level.setInitialTime(initialTime);
        level.setMaxTime(maxTime);
        level.setThemeMode(ThemeMode.DEFAULT);
        
        level.setBallTemplate(parseBallTemplate(logger));
        level.setInitialPosition(PositionAndSideAndDirection.builder()
                .x(ballInitialX)
                .y(ballInitialY)
                .z(ballInitialZ)
                .side(ballInitialSide)
                .direction(ballInitialDirection)
                .build()
        );
        
        blocks.forEach(block -> level.createNewBlock(block, logger));
        enemies.forEach(enemy -> level.createNewEnemy(enemy, logger));
        
        return level;
    }
    
    private @NonNull Theme parseTheme(UILogger logger)
    {
        if(!Theme.existsKey(theme))
            LoggerUtils.warn(logger, "Theme '%s' not exists in Rollingcube. Replaced by '%s'", theme, Theme.DEFAULT);
        
        return Theme.fromKey(theme);
    }
    
    private @NonNull Music parseMusic(UILogger logger)
    {
        if(!Music.existsKey(music))
            LoggerUtils.warn(logger, "Music '%s' not exists in Rollingcube. Replaced by '%s'", music, Music.DEFAULT);
        
        return Music.fromKey(music);
    }
    
    private @NonNull BallTemplate parseBallTemplate(UILogger logger)
    {
        if(!BallTemplate.existsKey(ballTemplate))
            LoggerUtils.warn(logger, "Ball template '%s' not exists in Rollingcube. Replaced by '%s'", ballTemplate, BallTemplate.DEFAULT);
        
        return BallTemplate.fromKey(ballTemplate);
    }
}
