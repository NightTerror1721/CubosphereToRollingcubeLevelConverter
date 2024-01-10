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
public enum Direction
{
    NORTH,
    EAST,
    SOUTH,
    WEST;
    
    public final int getId() { return ordinal(); }
    
    
    public static Direction fromId(int directionId)
    {
        if(directionId <= 0)
            return NORTH;
        
        return switch(directionId % 4)
        {
            case 0 -> NORTH;
            case 1 -> EAST;
            case 2 -> SOUTH;
            case 3 -> WEST;
            default -> NORTH;
        };
    }
    
    
    
    public static final class Serializer extends StdSerializer<Direction>
    {
        public Serializer() { super(Direction.class); }
        public Serializer(Class<Direction> cls) { super(cls); }

        @Override
        public void serialize(Direction value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeNumber(NORTH.getId());
            else
                gen.writeNumber(value.getId());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<Direction>
    {
        public Deserializer() { super(Direction.class); }
        public Deserializer(Class<Direction> cls) { super(cls); }

        @Override
        public @NonNull Direction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isNumber())
                return NORTH;
            
            return fromId(node.asInt());
        }
    }
}
