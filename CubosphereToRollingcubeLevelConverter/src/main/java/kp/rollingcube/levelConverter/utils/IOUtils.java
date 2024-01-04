package kp.rollingcube.levelConverter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Marc
 */
@UtilityClass
public class IOUtils
{
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
}
