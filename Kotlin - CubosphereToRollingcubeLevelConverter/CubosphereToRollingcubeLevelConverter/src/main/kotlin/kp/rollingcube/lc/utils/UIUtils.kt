package kp.rollingcube.lc.utils

import java.awt.Image
import java.awt.Toolkit
import java.awt.Window
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException


object UIUtils
{
    val LOGO: Image? by lazy {
        try
        {
            ImageIO.read(IOUtils.getClasspathResourceUrl("/logo.png"))
        }
        catch (ex: IOException)
        {
            ex.printStackTrace()
            null
        }
    }

    fun useSystemLookAndFeel()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (ex: ClassNotFoundException) {
            ex.printStackTrace(System.err)
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace(System.err)
        } catch (ex: InstantiationException) {
            ex.printStackTrace(System.err)
        } catch (ex: UnsupportedLookAndFeelException) {
            ex.printStackTrace(System.err)
        }
    }

    fun Window.focus()
    {
        val screen = Toolkit.getDefaultToolkit().screenSize
        val window = size
        setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2)
    }

    fun JDialog.focus()
    {
        if(parent !is JDialog && parent !is JFrame)
        {
            (this as Window).focus()
            return
        }

        val loc = location
        val screen = parent.size
        val window = size

        loc.x = (screen.width - window.width) / 2
        loc.y = (screen.height - window.height) / 2

        location = loc
    }

    fun JFrame.installIcon()
    {
        val logo = LOGO
        if(logo != null)
            iconImage = logo
    }
}