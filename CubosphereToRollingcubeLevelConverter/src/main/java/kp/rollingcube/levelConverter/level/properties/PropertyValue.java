package kp.rollingcube.levelConverter.level.properties;

import lombok.NonNull;

/**
 *
 * @author Marc
 */
public final class PropertyValue
{
    private int _int;
    private float _float;
    private @NonNull String _str;
    
    private PropertyValue(int intValue, float floatValue, @NonNull String strValue)
    {
        this._int = intValue;
        this._float = floatValue;
        this._str = strValue;
    }
    
    public static @NonNull PropertyValue of() { return new PropertyValue(0, 0, ""); }
    public static @NonNull PropertyValue of(int value) { return new PropertyValue(value, 0, ""); }
    public static @NonNull PropertyValue of(float value) { return new PropertyValue(0, value, ""); }
    public static @NonNull PropertyValue of(boolean value) { return new PropertyValue(value ? 1 : 0, 0, ""); }
    public static @NonNull PropertyValue of(@NonNull String value) { return new PropertyValue(0, 0, value); }
    
    public static @NonNull PropertyValue zero() { return of(); }
    
    public static @NonNull PropertyValue fromStringAsInteger(String value) throws NumberFormatException
    {
        if(value == null)
            value = "0";
        
        return of(Integer.parseInt(value));
    }
    public static @NonNull PropertyValue fromStringAsFloat(String value) throws NumberFormatException
    {
        if(value == null)
            value = "0";
        
        return of(Float.parseFloat(value));
    }
    
    public static @NonNull PropertyValue fromStringAsBoolean(String value)
    {
        if(value == null)
            value = "false";
        
        return of(value.equalsIgnoreCase("true"));
    }
    
    public static @NonNull PropertyValue fromStringAsString(String value) { return of(value); }
    
    public final int getInteger() { return _int; }
    public final float getFloat() { return _float; }
    public final boolean getBoolean() { return _int != 0; }
    public final @NonNull String getString() { return _str; }
    public final int getEnumOrdinal() { return Math.abs(_int); }
    
    public void setInteger(int value) { _int = value; _float = 0; _str = ""; }
    public void setFloat(float value) { _int = 0; _float = value; _str = ""; }
    public void setBoolean(boolean value) { _int = value ? 1 : 0; _float = 0; _str = ""; }
    public void setString(@NonNull String value) { _int = 0; _float = 0; _str = value; }
    public void setEnumOrdinal(int value) { _int = Math.abs(value); _float = 0; _str = ""; }
    
    public @NonNull PropertyValue copy() { return new PropertyValue(_int, _float, _str); }
    
    public @NonNull PropertyValue copyFrom(@NonNull PropertyValue other)
    {
        _int = other._int;
        _float = other._float;
        _str = other._str;
        return this;
    }
    
    public @NonNull PropertyValue copyTo(@NonNull PropertyValue other)
    {
        return other.copyFrom(this);
    }
}
