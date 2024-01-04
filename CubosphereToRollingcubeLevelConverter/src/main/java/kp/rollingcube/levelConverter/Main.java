package kp.rollingcube.levelConverter;

import java.io.IOException;
import java.nio.file.Path;
import kp.rollingcube.levelConverter.level.Level;
import kp.rollingcube.levelConverter.level.cubosphere.lua.LevelLoaderContext;
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.load.LoaderException;

/**
 *
 * @author Marc
 */
public class Main
{
    public static void main(String[] args) throws LoaderException, CallException, CallPausedException, InterruptedException, IOException
    {
        /*var program = "print \"hello world\"";
        
        var state = StateContexts.newDefaultInstance();
        var env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);
        
        var loader = CompilerChunkLoader.of("hello_world");
        var main = loader.loadTextChunk(new Variable(env), "hello", program);
        
        DirectCallExecutor.newExecutor().call(state, main);*/
        
        /*final var levelCode = """
                              GLOBAL_SetVar("StartTime",90)
                              GLOBAL_SetVar("MaxTime",90)
                              GLOBAL_SetVar("GoggleTime",15)
                              GLOBAL_SetVar("LevelMusic","kula world hills")
        """.trim();
        
        var context = new LevelLoaderContext();
        context.loadLevel(levelCode);*/
        
        var context = new LevelLoaderContext();
        var levelTestPath = Path.of("leveltest.ldef");
        var clevel = context.loadLevel(levelTestPath);
        var level = clevel.toRollingcubeLevel("output");
        Level.write(Path.of("output.json"), level);
    }
}
