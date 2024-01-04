package kp.rollingcube.levelConverter.level.cubosphere;

import lombok.NonNull;
import org.classdump.luna.ByteString;

/**
 *
 * @author Marc
 */
public abstract sealed class CubospherePropertyValue permits
        CubospherePropertyValue.NullValue,
        CubospherePropertyValue.IntValue,
        CubospherePropertyValue.FloatValue,
        CubospherePropertyValue.BooleanValue,
        CubospherePropertyValue.StringValue
{
    public abstract int toInt();
    public abstract float toFloat();
    public abstract boolean toBoolean();
    @Override public abstract @NonNull String toString();
    
    public static final CubospherePropertyValue zero() { return of(); }
    
    public static final CubospherePropertyValue of() { return NullValue.INSTANCE; }
    public static final CubospherePropertyValue of(int value) { return new IntValue(value); }
    public static final CubospherePropertyValue of(float value) { return new FloatValue(value); }
    public static final CubospherePropertyValue of(boolean value) { return value ? BooleanValue.TRUE_INSTANCE : BooleanValue.FALSE_INSTANCE; }
    public static final CubospherePropertyValue of(String value) { return value == null ? NullValue.INSTANCE : new StringValue(value); }
    
    public static final CubospherePropertyValue of(Number value)
    {
        return switch(value)
        {
            case Byte b -> of(b.intValue());
            case Short s -> of(s.intValue());
            case Integer i -> of(i.intValue());
            case Long l -> of(l.intValue());
            case null -> NullValue.INSTANCE;
            default -> of(value.floatValue());
        };
    }
    
    public static final CubospherePropertyValue of(ByteString value) { return value == null ? NullValue.INSTANCE : new StringValue(value.toString()); }
    
    public static final CubospherePropertyValue of(Object value)
    {
        return switch(value)
        {
            case Number n -> of(n);
            case Boolean b -> of(b.booleanValue());
            case String s -> of(s);
            case ByteString bs -> of(bs);
            case null -> NullValue.INSTANCE;
            default -> NullValue.INSTANCE;
        };
    }
    
    
    
    
    
    static final class NullValue extends CubospherePropertyValue
    {
        private static final NullValue INSTANCE = new NullValue();
        
        private NullValue() {}
        
        @Override
        public int toInt() { return 0; }

        @Override
        public float toFloat() { return 0; }

        @Override
        public boolean toBoolean() { return false; }

        @Override
        public @NonNull String toString() { return ""; }
    }
    
    static final class IntValue extends CubospherePropertyValue
    {
        private final int value;
        
        private IntValue(int value) { this.value = value; }
        
        @Override
        public int toInt() { return value; }

        @Override
        public float toFloat() { return value; }

        @Override
        public boolean toBoolean() { return value != 0; }

        @Override
        public @NonNull String toString() { return Integer.toString(value); }
    }
    
    static final class FloatValue extends CubospherePropertyValue
    {
        private final float value;
        
        private FloatValue(float value) { this.value = value; }
        
        @Override
        public int toInt() { return (int) value; }

        @Override
        public float toFloat() { return value; }

        @Override
        public boolean toBoolean() { return value != 0; }

        @Override
        public @NonNull String toString() { return Float.toString(value); }
    }
    
    static final class BooleanValue extends CubospherePropertyValue
    {
        private static final BooleanValue TRUE_INSTANCE = new BooleanValue(true);
        private static final BooleanValue FALSE_INSTANCE = new BooleanValue(false);
        
        private final boolean value;
        
        private BooleanValue(boolean value) { this.value = value; }
        
        @Override
        public int toInt() { return value ? 1 : 0; }

        @Override
        public float toFloat() { return value ? 1 : 0; }

        @Override
        public boolean toBoolean() { return value; }

        @Override
        public @NonNull String toString() { return value ? "true" : "false"; }
    }
    
    static final class StringValue extends CubospherePropertyValue
    {
        private final @NonNull String value;
        
        private StringValue(@NonNull String value) { this.value = value; }
        
        @Override
        public int toInt()
        {
            try { return Integer.parseInt(value); }
            catch(NumberFormatException ex) { return 0; }
        }

        @Override
        public float toFloat()
        {
            try { return Float.parseFloat(value); }
            catch(NumberFormatException ex) { return 0; }
        }

        @Override
        public boolean toBoolean()
        {
            return switch(value.toLowerCase())
            {
                case "true" -> true;
                default -> false;
            };
        }

        @Override
        public @NonNull String toString() { return value; }
    }
}
