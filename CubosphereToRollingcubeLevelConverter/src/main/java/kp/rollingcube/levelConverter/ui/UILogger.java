package kp.rollingcube.levelConverter.ui;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import lombok.NonNull;
import lombok.Synchronized;

/**
 *
 * @author Marc
 */
public final class UILogger
{
    private final @NonNull JTextPane textPane;
    private final @NonNull StyledDocument document;
    private final Style normalStyle;
    private final Style warningStyle;
    private final Style errorStyle;
    private final Style goodStyle;
    
    public UILogger(@NonNull JTextPane textPane)
    {
        this.textPane = textPane;
        this.document = textPane.getStyledDocument();
        
        normalStyle = createNormalStyle("normal");
        warningStyle = createStyle("warn", new Color(210, 150, 0));
        errorStyle = createStyle("err", new Color(210, 0, 0));
        goodStyle = createStyle("good", new Color(0, 210, 0));
    }
    
    public void clearText() { textPane.setText(""); }
    
    @Synchronized("document")
    public void write(@NonNull String text, Level level)
    {
        try
        {
            document.insertString(document.getLength(), text, levelToStyle(level));
        }
        catch(BadLocationException ex) {}
    }
    public void write(@NonNull String text) { write(text, null); }
    
    public void println(@NonNull String text, Level level) { write(text + "\n", level); }
    public void println(@NonNull String text) { write(text + "\n"); }
    
    public void format(Level level, @NonNull String text, Object... args)
    {
        write(String.format(text, args), level);
    }
    
    public void formatln(Level level, @NonNull String text, Object... args)
    {
        println(String.format(text, args), level);
    }
    
    private Style createStyle(String name, Color color)
    {
        var style = textPane.addStyle(name, null);
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setForeground(style, color);
        
        return style;
    }
    
    private Style createNormalStyle(String name)
    {
        var style = textPane.addStyle(name, null);
        StyleConstants.setFontSize(style, 12);
        
        return style;
    }
    
    private @NonNull Style levelToStyle(Level level)
    {
        return switch(level)
        {
            case ERROR -> errorStyle;
            case WARNING -> warningStyle;
            case NORMAL -> normalStyle;
            case GOOD -> goodStyle;
            case null -> normalStyle;
            default -> normalStyle;
        };
    }
    
    public enum Level
    {
        ERROR,
        WARNING,
        NORMAL,
        GOOD
    }
}
