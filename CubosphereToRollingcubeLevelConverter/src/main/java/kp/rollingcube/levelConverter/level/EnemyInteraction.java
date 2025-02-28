package kp.rollingcube.levelConverter.level;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.util.Collection;

@Getter
public final class EnemyInteraction
{
    private int flags = 0;

    @JsonIgnore
    public boolean isEmpty() { return flags == 0; }

    public void disableAllInteractions() { flags = 0; }

    public boolean hasInteraction(@NonNull EnemyInteractionType type) { return EnemyInteractionType.isSet(flags, type); }

    @JsonIgnore
    public void setInteraction(@NonNull EnemyInteractionType type, boolean enabled)
    {
        flags = EnemyInteractionType.setTo(flags, type, enabled);
    }

    @JsonIgnore
    public void setInteractions(boolean enabled, @NonNull EnemyInteractionType @NonNull ... types)
    {
        flags = EnemyInteractionType.setTo(flags, enabled, types);
    }

    @JsonIgnore
    public void setInteractions(@NonNull Collection<EnemyInteractionType> types, boolean enabled)
    {
        flags = EnemyInteractionType.setTo(flags, types, enabled);
    }

    @JsonIgnore
    public void setInitialInteractions(@NonNull EnemyInteractionType @NonNull ... types)
    {
        disableAllInteractions();
        setInteractions(true, types);
    }
    @JsonIgnore
    public void setInitialInteractions(@NonNull Collection<EnemyInteractionType> types)
    {
        disableAllInteractions();
        setInteractions(types, true);
    }

    @JsonIgnore
    public void set(@NonNull EnemyInteraction other) { flags = other.flags; }


    public static final class Serializer extends StdSerializer<EnemyInteraction>
    {
        public Serializer() { super(EnemyInteraction.class); }
        public Serializer(Class<EnemyInteraction> cls) { super(cls); }

        @Override
        public void serialize(EnemyInteraction value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            if(value == null)
                gen.writeNumber(0);
            else
                gen.writeNumber(value.getFlags());
        }
    }
}
