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
import lombok.Value;

/**
 *
 * @author Marc
 */
@Value
public final class BlockId
{
    public static final BlockId INVALID = new BlockId(0);
    
    private final int code;
    
    private BlockId(int code)
    {
        this.code = code;
    }
    
    public static @NonNull BlockId of(int blockId)
    {
        if(blockId <= 0)
            return INVALID;
        
        return new BlockId(blockId);
    }
    public static @NonNull BlockId of() { return INVALID; }
    
    public final boolean isValid() { return code > 0; }
    public final boolean isInvalid() { return code <= 0; }
    
    public final int toInt() { return code; }
    
    
    public static final class Serializer extends StdSerializer<BlockId>
    {
        public Serializer() { super(BlockId.class); }
        public Serializer(Class<BlockId> cls) { super(cls); }

        @Override
        public void serialize(BlockId value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeNumber(INVALID.toInt());
            else
                gen.writeNumber(value.toInt());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<BlockId>
    {
        public Deserializer() { super(BlockId.class); }
        public Deserializer(Class<BlockId> cls) { super(cls); }

        @Override
        public @NonNull BlockId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isNumber())
                return INVALID;
            
            return of(node.asInt());
        }
    }
}
