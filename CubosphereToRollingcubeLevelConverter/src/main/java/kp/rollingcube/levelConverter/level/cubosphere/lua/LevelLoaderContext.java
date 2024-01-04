package kp.rollingcube.levelConverter.level.cubosphere.lua;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import kp.rollingcube.levelConverter.level.BlockId;
import kp.rollingcube.levelConverter.level.SideId;
import kp.rollingcube.levelConverter.level.SideTag;
import kp.rollingcube.levelConverter.level.cubosphere.CubosphereLevel;
import kp.rollingcube.levelConverter.level.cubosphere.CubospherePropertyValue;
import kp.rollingcube.levelConverter.level.cubosphere.EnemyId;
import kp.rollingcube.levelConverter.level.cubosphere.ItemId;
import kp.rollingcube.levelConverter.utils.IOUtils;
import lombok.NonNull;
import org.classdump.luna.ByteString;
import org.classdump.luna.StateContext;
import org.classdump.luna.Table;
import org.classdump.luna.Variable;
import org.classdump.luna.compiler.CompilerChunkLoader;
import org.classdump.luna.env.RuntimeEnvironments;
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.exec.DirectCallExecutor;
import org.classdump.luna.impl.DefaultTable;
import org.classdump.luna.impl.StateContexts;
import org.classdump.luna.lib.StandardLibrary;
import org.classdump.luna.load.LoaderException;

/**
 *
 * @author Marc
 */
public class LevelLoaderContext
{
    private CubosphereLevel level;
    private Table env;
    
    private final @NonNull CompilerChunkLoader loader = CompilerChunkLoader.of("Cubosphere_LevelLoader"); 
    private final @NonNull Map<Object, Object> functions = initFunctions();
    
    public @NonNull CubosphereLevel loadLevel(@NonNull InputStream in) throws IOException
    {
        return loadLevel(IOUtils.readAll(in));
    }
    
    public @NonNull CubosphereLevel loadLevel(@NonNull Reader reader) throws IOException
    {
        return loadLevel(IOUtils.readAll(reader));
    }
    
    public @NonNull CubosphereLevel loadLevel(@NonNull Path levelFilePath) throws IOException
    {
        return loadLevel(IOUtils.readAllFromFile(levelFilePath));
    }
    
    public @NonNull CubosphereLevel loadLevel(@NonNull String levelCode)
    {
        final var lv = new CubosphereLevel();
        level = lv;
        try
        {
            var state = StateContexts.newDefaultInstance();
            env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);
            prepareFunctions(env);

            var main = loader.loadTextChunk(new Variable(env), "CubosphereLevel", levelCode);

            DirectCallExecutor.newExecutor().call(state, main);
            callLuaLevelFunction(state);
        }
        catch(InterruptedException | CallException | CallPausedException | LoaderException ex)
        {
            ex.printStackTrace(System.err);
        }
        finally
        {
            level = null;
            env = null;
        }
        
        return lv;
    }
    
    private void prepareFunctions(@NonNull Table env)
    {
        functions.forEach((key, val) -> env.rawset(key, val));
    }
    
    private void callLuaLevelFunction(@NonNull StateContext state) throws CallException, CallPausedException, InterruptedException
    {
        Object fn = env.rawget("Level");
        if(fn == null)
            return;
        
        DirectCallExecutor.newExecutor().call(state, fn);
    }
    
    private Map<Object, Object> initFunctions()
    {
        return Map.copyOf(new HashMap<>()
        {
            {
                put("VECTOR_New", LuaFunction.of((Number x, Number y, Number z) -> {
                    var vector = DefaultTable.factory().newTable(3, 0);
                    vector.rawset(1, x);
                    vector.rawset(2, y);
                    vector.rawset(3, z);
                    return vector;
                }));
                
                put("GLOBAL_GetVar", LuaFunction.of((ByteString varname) -> {
                    return switch(varname.toString())
                    {
                        case "EditorMode" -> 0;
                        case "ThemeOverride" -> "";
                        default -> null;
                    };
                }));
                
                put("GLOBAL_SetVar", LuaFunction.of((ByteString varname, Object value) -> {
                    switch(varname.toString())
                    {
                        case "StartTime" -> {
                            if(value instanceof Number n)
                                level.setInitialTime(n.intValue());
                        }
                        case "MaxTime" -> {
                            if(value instanceof Number n)
                                level.setMaxTime(n.intValue());
                        }
                        case "LevelMusic" -> {
                            if(value instanceof ByteString s)
                                level.setMusic(s.toString());
                        }
                    }
                }));
                
                put("LEVEL_LastBlock", LuaFunction.of(() -> level.getLastBlock().getId().getCode()));
                
                put("AddBall", LuaFunction.of((ByteString template, Table positionTable, Number startSide, Number startDirection) -> {
                    setBallInitialPosition(positionTable);
                    level.setBallTemplate(template.toString());
                    level.setBallInitialSide(SideTag.fromCubosphereSideId(startSide.intValue()));
                    level.setBallInitialDirection(startDirection.intValue(), level.getBallInitialSide());
                }));
                
                put("THEME_Load", LuaFunction.of((ByteString template) -> level.setTheme(template.toString())));
                
                put("LEVEL_AddBlock", LuaFunction.of((Number x, Number y, Number z, ByteString template) -> {
                    var block = level.createNewBlock(template.toString());
                    block.setX(x.intValue());
                    block.setY(y.intValue());
                    block.setZ(z.intValue());
                }));
                
                put("LEVEL_AddItem", LuaFunction.of((Number blockIdNum, ByteString sideStr, ByteString template) -> {
                    var blockId = BlockId.of(blockIdNum.intValue());
                    if(blockId.isInvalid())
                        return ItemId.INVALID.getCode();
                    
                    var sideTag = strToSideTag(sideStr);
                    var sideId = SideId.of(blockId, sideTag);
                    if(sideId.isInvalid())
                        return ItemId.INVALID.getCode();
                    
                    var item = level.createNewItem(sideId, template.toString());
                    if(item == null)
                        return ItemId.INVALID.getCode();
                    
                    return item.getId().getCode();
                }));
                
                put("ENEMY_New", LuaFunction.of((ByteString template) -> {
                    var enemy = level.createNewEnemy(template.toString());
                    if(enemy == null)
                        return EnemyId.INVALID.getCode();
                    return enemy.getId().getCode();
                }));
                
                put("LEVEL_ChangeSide", LuaFunction.of((Number blockIdNum, ByteString sideStr, ByteString template) -> {
                    var blockId = BlockId.of(blockIdNum.intValue());
                    if(blockId.isInvalid())
                        return;
                    
                    var sideTag = strToSideTag(sideStr);
                    var sideId = SideId.of(blockId, sideTag);
                    if(sideId.isInvalid())
                        return;
                    
                    var block = level.getBlock(blockId);
                    if(block == null)
                        return;
                    
                    block.changeSide(sideId, template.toString());
                }));
                
                put("BLOCK_SetVar", LuaFunction.of((Number blockIdNum, ByteString varname, Object value) -> {
                    var blockId = BlockId.of(blockIdNum.intValue());
                    if(blockId.isInvalid())
                        return;
                    
                    var block = level.getBlock(blockId);
                    if(block == null)
                        return;
                    
                    block.setProperty(varname.toString(), CubospherePropertyValue.of(value));
                }));
                
                put("SIDE_SetVar", LuaFunction.of((Number sideIdNum, ByteString varname, Object value) -> {
                    var sideId = decodeSideId(sideIdNum);
                    if(sideId.isInvalid())
                        return;
                    
                    var side = level.getSide(sideId);
                    if(side == null)
                        return;
                    
                    side.setProperty(varname.toString(), CubospherePropertyValue.of(value));
                }));
                
                put("ITEM_SetVar", LuaFunction.of((Number itemIdNum, ByteString varname, Object value) -> {
                    var itemId = ItemId.of(itemIdNum.intValue());
                    if(itemId.isInvalid())
                        return;
                    
                    var item = level.getItem(itemId);
                    if(item == null)
                        return;
                    
                    item.setProperty(varname.toString(), CubospherePropertyValue.of(value));
                }));
                
                put("ACTOR_SetVar", LuaFunction.of((Number enemyIdNum, ByteString varname, Object value) -> {
                    var enemyId = EnemyId.of(enemyIdNum.intValue());
                    if(enemyId.isInvalid())
                        return;
                    
                    var enemy = level.getEnemy(enemyId);
                    if(enemy == null)
                        return;
                    
                    enemy.setProperty(varname.toString(), CubospherePropertyValue.of(value));
                }));
                
                put("ACTOR_SetStart", LuaFunction.of((Number enemyIdNum, Number sideIdNum, Number rotation) -> {
                    var enemyId = EnemyId.of(enemyIdNum.intValue());
                    if(enemyId.isInvalid())
                        return;
                    
                    var sideId = decodeSideId(sideIdNum);
                    if(sideId.isInvalid())
                        return;
                    
                    var enemy = level.getEnemy(enemyId);
                    if(enemy == null)
                        return;
                    
                    var side = level.getSide(sideId);
                    if(side == null)
                        return;
                    
                    var block = side.getBlock();
                    
                    enemy.setX(block.getX());
                    enemy.setY(block.getY());
                    enemy.setZ(block.getZ());
                    enemy.setSide(sideId.getSideTag());
                    enemy.setCubosphereDirection(rotation.intValue(), sideId.getSideTag());
                }));
                
                
                put("InitLevel", LuaFunction.withContext(context -> {
                    var initActors = env.rawget("InitActors");
                    if(initActors != null)
                    {
                        try
                        {
                            DirectCallExecutor.newExecutor().call(context, initActors);
                        }
                        catch(InterruptedException | CallException | CallPausedException ex)
                        {
                            ex.printStackTrace(System.err);
                        }
                    }
                    else
                    {
                        findInitialBallData();
                    }
                    
                    return null;
                }));
                
                /* Dummy Functions */
                put("INCLUDE", dummyFunction());
                put("INCLUDEABSOLUTE", dummyFunction());
                put("BackwardCompatibility", dummyFunction());
                put("LEVEL_LoadSky", dummyFunction());
                put("BASIS_Set", dummyFunction());
            }
        });
    }
    
    private void setBallInitialPosition(Number[] positions)
    {
        level.setBallInitialX(positions[0].intValue() / 20);
        level.setBallInitialY(positions[1].intValue() / 20);
        level.setBallInitialZ(positions[2].intValue() / 20);
    }
    
    private void setBallInitialPosition(Table positions)
    {
        setBallInitialPosition(decodeVector(positions));
    }
    
    private Table readTableFromEnv(@NonNull String name)
    {
        var obj = env.rawget(name);
        if(obj == null)
            return null;
        
        if(obj instanceof Table t)
            return t;
        return null;
    }
    
    private Number readNumberFromEnv(@NonNull String name)
    {
        var obj = env.rawget(name);
        if(obj == null)
            return null;
        
        if(obj instanceof Number n)
            return n;
        return null;
    }
    
    private String readStringFromEnv(@NonNull String name)
    {
        var obj = env.rawget(name);
        if(obj == null)
            return null;
        
        if(obj instanceof ByteString bs)
            return bs.toString();
        return null;
    }
    
    private void findInitialBallData()
    {        
        var startBlockPos = readTableFromEnv("startblockpos");
        if(startBlockPos != null)
            setBallInitialPosition(startBlockPos);
        
        var ballType = readStringFromEnv("BallType");
        if(ballType != null)
            level.setBallTemplate(ballType);
        
        Number data = readNumberFromEnv("startside");
        if(data != null)
            level.setBallInitialSide(SideTag.fromCubosphereSideId(data.intValue()));
        
        data = readNumberFromEnv("startrotation");
        if(data != null)
            level.setBallInitialDirection(data.intValue(), level.getBallInitialSide());
    }
    
    private static Object dummyFunction()
    {
        return LuaFunction.of(() -> {});
    }
    
    private static @NonNull Number[] decodeVector(Table table)
    {
        Number[] vector = new Number[] { 0, 0, 0 };
        if(table == null)
            return vector;
        
        vectorIndexSet(table, vector, 0);
        vectorIndexSet(table, vector, 1);
        vectorIndexSet(table, vector, 2);
        
        return vector;
    }
    
    private static void vectorIndexSet(Table table, Number[] vector, int vectorIndex)
    {
        int tableIndex = vectorIndex + 1;
        if(table.rawlen() <= tableIndex)
            return;
        
        var value = table.rawget(tableIndex);
        if(value instanceof Number n)
            vector[vectorIndex] = n;
    }
    
    private static @NonNull SideTag strToSideTag(ByteString str)
    {
        if(str == null)
            return SideTag.UP;
        
        return switch(str.toString().toLowerCase())
        {
            case "up" -> SideTag.UP;
            case "down" -> SideTag.DOWN;
            case "left" -> SideTag.RIGHT;
            case "right" -> SideTag.LEFT;
            case "front" -> SideTag.FRONT;
            case "back" -> SideTag.BACK;
            default -> SideTag.UP;
        };
    }
    
    private static @NonNull SideId decodeSideId(Number sideIdNum)
    {
        if(sideIdNum == null)
            return SideId.INVALID;
        
        return SideId.fromCubosphereId(sideIdNum.intValue());
    }
}
