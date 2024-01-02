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
public final class SideId
{
    public static final SideId INVALID = new SideId(BlockId.INVALID, SideTag.UP);
    
    private final BlockId blockId;
    private final SideTag sideTag;
    
    private SideId(@NonNull BlockId blockId, @NonNull SideTag sideTag)
    {
        this.blockId = blockId;
        this.sideTag = sideTag;
    }
    
    public static @NonNull SideId of(@NonNull BlockId blockId, @NonNull SideTag sideTag)
    {
        if(blockId.isInvalid())
            return INVALID;
        
        return new SideId(blockId, sideTag);
    }
    public static @NonNull SideId of() { return INVALID; }
    
    public static @NonNull SideId of(int sideId)
    {
        if(sideId <= 0)
            return INVALID;
        
        int blockId = ((sideId - 1) / 6) + 1;
        SideTag sideTag = SideTag.fromSideId(sideId);
        return of(BlockId.of(blockId), sideTag);
    }
    
    public int getCode() { return sideTag.computeSideId(blockId.getCode()); }
    
    public final boolean isValid() { return blockId.isValid(); }
    public final boolean isInvalid() { return blockId.isInvalid(); }
    
    public final int toInt() { return getCode(); }
    

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + getCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(obj == null)
            return false;
        
        if(obj instanceof SideId sideId)
            return blockId.equals(sideId.blockId) && sideTag == sideId.sideTag;
        
        return false;
    }
    
    
    public static final class Serializer extends StdSerializer<SideId>
    {
        public Serializer() { super(SideId.class); }
        public Serializer(Class<SideId> cls) { super(cls); }

        @Override
        public void serialize(SideId value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeNumber(INVALID.toInt());
            else
                gen.writeNumber(value.toInt());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<SideId>
    {
        public Deserializer() { super(SideId.class); }
        public Deserializer(Class<SideId> cls) { super(cls); }

        @Override
        public @NonNull SideId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isNumber())
                return INVALID;
            
            return of(node.asInt());
        }
    }
}
