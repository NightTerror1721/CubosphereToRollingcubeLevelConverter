package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
public enum SideTag
{
    UP,
    DOWN,
    LEFT,
    RIGHT,
    FRONT,
    BACK;
    
    public final int computeSideId(int blockId)
    {
        if(blockId <= 0)
            return 0;
        
        return ((blockId - 1) * 6) + ordinal() + 1;
    }
    
    public static SideTag fromSideId(int sideId)
    {
        if(sideId <= 0)
            return UP;
        
        return switch((sideId - 1) % 6)
        {
            case 0 -> UP;
            case 1 -> DOWN;
            case 2 -> LEFT;
            case 3 -> RIGHT;
            case 4 -> FRONT;
            case 5 -> BACK;
            default -> UP;
        };
    }
    
    public static SideTag fromCubosphereSideId(int cubosphereSideId)
    {
        if(cubosphereSideId <= 0)
            return UP;
        
        return switch(cubosphereSideId % 6)
        {
            case 0 -> UP;
            case 1 -> DOWN;
            case 2 -> RIGHT;
            case 3 -> LEFT;
            case 4 -> BACK;
            case 5 -> FRONT;
            default -> UP;
        };
    }
    
    public static @NonNull SideTag fromCubosphereString(String str)
    {
        if(str == null)
            return SideTag.UP;
        
        return switch(str.toLowerCase())
        {
            case "up" -> SideTag.UP;
            case "down" -> SideTag.DOWN;
            case "left" -> SideTag.RIGHT;
            case "right" -> SideTag.LEFT;
            case "front" -> SideTag.BACK;
            case "back" -> SideTag.FRONT;
            default -> SideTag.UP;
        };
    }
    
    
    
    public static final class Serializer extends StdSerializer<SideTag>
    {
        public Serializer() { super(SideTag.class); }
        public Serializer(Class<SideTag> cls) { super(cls); }

        @Override
        public void serialize(SideTag value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeNumber(UP.ordinal());
            else
                gen.writeNumber(value.ordinal());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<SideTag>
    {
        public Deserializer() { super(SideTag.class); }
        public Deserializer(Class<SideTag> cls) { super(cls); }

        @Override
        public @NonNull SideTag deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isNumber())
                return UP;
            
            return fromSideId(node.asInt());
        }
    }
}
