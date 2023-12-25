package kp.rollingcube.levelConverter.exception;

/**
 *
 * @author Marc
 */
public class JsonParsingException extends ConverterException
{
    public JsonParsingException(String message) { super(message); }
    public JsonParsingException(String message, Throwable cause) { super(message, cause); }
}
