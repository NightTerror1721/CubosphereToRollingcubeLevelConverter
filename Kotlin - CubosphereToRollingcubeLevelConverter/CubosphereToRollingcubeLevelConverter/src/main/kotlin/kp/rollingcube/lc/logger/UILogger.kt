package kp.rollingcube.lc.logger

import java.awt.Color
import javax.swing.JTextPane
import javax.swing.text.BadLocationException
import javax.swing.text.Style
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

class UILogger(private val textPane: JTextPane)
{
    private val document: StyledDocument = textPane.styledDocument
    private val normalStyle = createNormalStyle("normal")
    private val warningStyle = createNormalStyle("warn", Color(210, 150, 0))
    private val errorStyle = createNormalStyle("err", Color(210, 0, 0))
    private val goodStyle = createNormalStyle("good", Color(0, 210, 0))

    fun clearText() { textPane.text = "" }

    @Synchronized
    fun write(text: String, level: UILoggerLevel?)
    {
        try {
            document.insertString(document.length, text, level.style)
        } catch (_: BadLocationException) {}
    }
    fun write(text: String) = write(text, null)

    fun println(text: String, level: UILoggerLevel?) = write(text + "\n", level)

    fun println(text: String) = write(text + "\n")

    fun format(level: UILoggerLevel?, text: String, vararg args: Any?) = write(String.format(text, *args), level)

    fun formatln(level: UILoggerLevel?, text: String, vararg args: Any?) = println(String.format(text, *args), level)

    private fun createNormalStyle(name: String): Style
    {
        val style = textPane.addStyle(name, null)
        StyleConstants.setFontSize(style, 12)
        return style
    }

    private fun createNormalStyle(name: String, color: Color): Style
    {
        val style = textPane.addStyle(name, null)
        StyleConstants.setFontSize(style, 12)
        StyleConstants.setForeground(style, color);
        return style
    }

    private val UILoggerLevel?.style: Style get() = when (this) {
        UILoggerLevel.ERROR -> errorStyle
        UILoggerLevel.WARNING -> warningStyle
        UILoggerLevel.NORMAL -> normalStyle
        UILoggerLevel.GOOD -> goodStyle
        null -> normalStyle
    }



    companion object
    {
        fun UILogger?.info(text: String, vararg args: Any?) = this?.formatln(UILoggerLevel.NORMAL, text, *args)
        fun UILogger?.success(text: String, vararg args: Any?) = this?.formatln(UILoggerLevel.GOOD, text, *args)
        fun UILogger?.warn(text: String, vararg args: Any?) = this?.formatln(UILoggerLevel.WARNING, text, *args)
        fun UILogger?.error(text: String, vararg args: Any?) = this?.formatln(UILoggerLevel.ERROR, text, *args)
    }
}