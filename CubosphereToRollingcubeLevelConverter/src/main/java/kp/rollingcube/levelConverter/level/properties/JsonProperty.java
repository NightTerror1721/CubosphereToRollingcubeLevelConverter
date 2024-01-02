package kp.rollingcube.levelConverter.level.properties;

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
@Value(staticConstructor = "of")
public final class JsonProperty
{
    public static final @NonNull JsonProperty NULL = of("");
    
    private final @NonNull String value;
    
    @Override
    public final String toString() { return value; }
    
    
    public static final class Serializer extends StdSerializer<JsonProperty>
    {
        public Serializer() { super(JsonProperty.class); }
        public Serializer(Class<JsonProperty> cls) { super(cls); }

        @Override
        public void serialize(JsonProperty value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeString("");
            else
                gen.writeString(value.toString());
        }
    }
    
    public static final class Deserializer extends StdDeserializer<JsonProperty>
    {
        public Deserializer() { super(JsonProperty.class); }
        public Deserializer(Class<JsonProperty> cls) { super(cls); }

        @Override
        public @NonNull JsonProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
        {
            JsonNode node = p.getCodec().readTree(p);
            if(!node.isTextual())
                return NULL;
            
            return of(node.asText());
        }
    }
}
