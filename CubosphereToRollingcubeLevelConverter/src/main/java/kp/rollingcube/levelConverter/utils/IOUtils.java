package kp.rollingcube.levelConverter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Marc
 */
@UtilityClass
public class IOUtils
{
    public String CUBOSPHERE_LEVEL_FORMAT = "ldef";
    public String ROLLINGCUBE_LEVEL_FORMAT = "json";
    
    public String CUBOSPHERE_LEVEL_DOT_FORMAT = "." + CUBOSPHERE_LEVEL_FORMAT;
    public String ROLLINGCUBE_LEVEL_DOT_FORMAT = "." + ROLLINGCUBE_LEVEL_FORMAT;
    
    
    public @NonNull String readAll(@NonNull Reader reader) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[8192];
        int len;
        
        while((len = reader.read(buffer, 0, buffer.length)) > 0)
            sb.append(buffer, 0, len);
        
        return sb.toString();
    }
    
    public @NonNull String readAll(@NonNull InputStream in) throws IOException
    {
        return readAll(new InputStreamReader(in));
    }
    
    public @NonNull String readAllFromFile(@NonNull Path filePath) throws IOException
    {
        try(var reader = Files.newBufferedReader(filePath))
        {
            return readAll(reader);
        }
    }
    
    
    public @NonNull Path getUserDirectory()
    {
        return Path.of(System.getProperty("user.dir")).toAbsolutePath();
    }
    
    public @NonNull String getFileName(@NonNull Path file)
    {
        var name = file.getFileName().toString();
        int index = name.lastIndexOf('.');
        return index < 0 ? name : name.substring(0, index);
    }
    
    public @NonNull String getFileExtension(@NonNull Path file)
    {
        var name = file.getFileName().toString();
        int index = name.lastIndexOf('.');
        return index < 0 ? "" : name.substring(index + 1);
    }
    
    public @NonNull Path concatElementAtPathEnd(@NonNull Path path, @NonNull String element)
    {
        if(path.getParent() == null)
            return Paths.get(path.getFileName().toString() + element);
        return path.getParent().resolve(path.getFileName().toString() + element);
    }
    
    public @NonNull URL getClasspathResourceUrl(@NonNull String path)
    {
        if(!path.startsWith("/"))
            path = "/" + path;
        return IOUtils.class.getResource(path);
    }
    
    public @NonNull InputStream getClasspathResourceAsStream(@NonNull String path)
    {
        if(!path.startsWith("/"))
            path = "/" + path;
        return IOUtils.class.getResourceAsStream(path);
    }
}
