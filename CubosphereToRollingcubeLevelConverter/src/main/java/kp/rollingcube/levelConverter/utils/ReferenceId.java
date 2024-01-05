package kp.rollingcube.levelConverter.utils;

import java.util.Iterator;
import lombok.Data;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
@Data
public abstract class ReferenceId
{
    private final int code;
    
    protected ReferenceId(int code)
    {
        this.code = code;
    }
    
    public final boolean isValid() { return code > 0; }
    public final boolean isInvalid() { return code <= 0; }
    
    public final int toInt() { return code; }
    
    @Override
    public String toString() { return Integer.toString(code); }
    
    
    public static abstract class BaseGenerator<T extends ReferenceId> implements Iterator<T>
    {
        private int id = 1;

        @Override
        public boolean hasNext() { return true; }

        @Override
        public @NonNull T next() { return generate(); }
        
        public final @NonNull T generate() { return createNew(id++); }
        
        protected abstract @NonNull T createNew(int code);
    }
}
