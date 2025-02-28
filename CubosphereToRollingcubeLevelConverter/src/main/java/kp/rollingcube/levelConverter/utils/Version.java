package kp.rollingcube.levelConverter.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.regex.Pattern;
import kp.rollingcube.levelConverter.exception.JsonParsingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Version implements Comparable<Version>
{
    private static final Pattern PATTERN = Pattern.compile("^[0-9]+(.[0-9]+){0,3}$");
    
    public static final Version APP_VERSION = of(0, 3, 0);
    public static final Version CURRENT_LEVEL_VERSION = of(1, 1);
    
    private int major;
    private int minor;
    private int patch;
    private int build;
    
    public static @NonNull Version of() { return new Version(); }
    public static @NonNull Version of(int major) { return new Version(major, 0, 0, 0); }
    public static @NonNull Version of(int major, int minor) { return new Version(major, minor, 0, 0); }
    public static @NonNull Version of(int major, int minor, int patch) { return new Version(major, minor, patch, 0); }
    public static @NonNull Version of(int major, int minor, int patch, int build) { return new Version(major, minor, patch, build); }
    
    public static @NonNull Version zero() { return of(); }
    
    public boolean isZero() { return major == 0 && minor == 0 && patch == 0 && build == 0; }
    
    @Override
    public String toString()
    {
        var sb = new StringBuilder();
        sb.append(major).append('.').append(minor);
        if(patch > 0)
        {
            sb.append('.').append(patch);
            if (build > 0)
                sb.append('.').append(build);
        }
        else if(build > 0)
            sb.append(".0.").append(build);
        return sb.toString();
    }

    @Override
    public int compareTo(Version other)
    {
        if(other == null)
            return 1;
        
        int cmp = Integer.compare(major, other.major);
        if(cmp != 0) return cmp;

        cmp = Integer.compare(minor, other.minor);
        if(cmp != 0) return cmp;

        cmp = Integer.compare(patch, other.patch);
        if(cmp != 0) return cmp;

        return Integer.compare(build, other.build);
    }
    
    public static @NonNull Version parse(@NonNull String text) throws JsonParsingException
    {
        text = text.trim();
        if(text.isBlank())
            throw new JsonParsingException("Invalid Version format");
        
        if(!PATTERN.matcher(text).matches())
            throw new JsonParsingException("Invalid Version format");
        
        try
        {
            var parts = text.split("\\.");
            if(parts == null || parts.length < 1)
                throw new JsonParsingException("Invalid Version format");
            
            var v = new Version();
            v.setMajor(Integer.parseUnsignedInt(parts[0]));
            if(parts.length > 1)
                v.setMinor(Integer.parseInt(parts[1]));
            if(parts.length > 2)
                v.setPatch(Integer.parseInt(parts[2]));
            if(parts.length > 3)
                v.setBuild(Integer.parseInt(parts[3]));
            
            return v;
        }
        catch(NumberFormatException ex)
        {
            throw new JsonParsingException("Invalid Version format", ex);
        }
    }
    
    
    public static final class Serializer extends StdSerializer<Version>
    {
        public Serializer() { super(Version.class); }
        public Serializer(Class<Version> cls) { super(cls); }

        @Override
        public void serialize(Version value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeString("");
            else
                gen.writeString(value.toString());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<Version>
    {
        public Deserializer() { super(Version.class); }
        public Deserializer(Class<Version> cls) { super(cls); }

        @Override
        public @NonNull Version deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isTextual())
                return Version.zero();
            
            try
            {
                return Version.parse(node.asText());
            }
            catch(JsonParsingException ex)
            {
                return Version.zero();
            }
        }
    }
}
