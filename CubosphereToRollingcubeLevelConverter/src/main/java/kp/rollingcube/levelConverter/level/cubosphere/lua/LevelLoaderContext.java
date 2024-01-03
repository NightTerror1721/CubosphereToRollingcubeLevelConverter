package kp.rollingcube.levelConverter.level.cubosphere.lua;

import java.util.HashMap;
import java.util.Map;
import kp.rollingcube.levelConverter.level.cubosphere.CubosphereLevel;
import lombok.NonNull;
import org.classdump.luna.ByteString;
import org.classdump.luna.Table;
import org.classdump.luna.Variable;
import org.classdump.luna.compiler.CompilerChunkLoader;
import org.classdump.luna.env.RuntimeEnvironments;
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.exec.DirectCallExecutor;
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
    
    private final @NonNull CompilerChunkLoader loader = CompilerChunkLoader.of("Cubosphere_LevelLoader"); 
    private final @NonNull Map<Object, Object> functions = initFunctions();
    
    public @NonNull CubosphereLevel loadLevel(@NonNull String levelCode)
    {
        final var lv = new CubosphereLevel();
        level = lv;
        try
        {
            var state = StateContexts.newDefaultInstance();
            var env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);
            prepareFunctions(env);

            var main = loader.loadTextChunk(new Variable(env), "CubosphereLevel", levelCode);

            DirectCallExecutor.newExecutor().call(state, main);
        }
        catch(InterruptedException | CallException | CallPausedException | LoaderException ex)
        {
            ex.printStackTrace(System.err);
        }
        
        return lv;
    }
    
    private void prepareFunctions(@NonNull Table env)
    {
        functions.forEach((key, val) -> env.rawset(key, val));
    }
    
    private Map<Object, Object> initFunctions()
    {
        return Map.copyOf(new HashMap<>()
        {
            {
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
                            if(value instanceof String s)
                                level.setMusic(s);
                        }
                    }
                }));
            }
        });
    }
}
