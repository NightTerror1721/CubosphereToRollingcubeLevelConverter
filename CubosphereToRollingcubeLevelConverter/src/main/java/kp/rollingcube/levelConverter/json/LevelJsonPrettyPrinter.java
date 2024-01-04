package kp.rollingcube.levelConverter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import java.io.IOException;

/**
 *
 * @author Marc
 */
public class LevelJsonPrettyPrinter extends DefaultPrettyPrinter
{
    public LevelJsonPrettyPrinter()
    {
        super();
    }

    public LevelJsonPrettyPrinter(DefaultPrettyPrinter base)
    {
        super(base);
    }

    @Override
    public void writeStartObject(JsonGenerator g) throws IOException
    {
        _objectIndenter.writeIndentation(g, _nesting);
        super.writeStartObject(g);
    }

    @Override
    public void writeStartArray(JsonGenerator g) throws IOException
    {
        if (!_arrayIndenter.isInline())
        {
            ++_nesting;
            g.writeRaw('[');
            _arrayIndenter.writeIndentation(g, _nesting);
        }
        else
            g.writeRaw('[');
    }

    @Override
    public DefaultPrettyPrinter createInstance()
    {
        return new LevelJsonPrettyPrinter(this);
    }
}
