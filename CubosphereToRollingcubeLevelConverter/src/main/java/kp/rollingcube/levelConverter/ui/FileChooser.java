package kp.rollingcube.levelConverter.ui;

import java.awt.Component;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import kp.rollingcube.levelConverter.utils.IOUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Marc
 */
@UtilityClass
public class FileChooser
{    
    private JFileChooser INPUTS;
    private @NonNull JFileChooser inputs()
    {
        if(INPUTS == null)
        {
            INPUTS = new JFileChooser(IOUtils.getUserDirectory().toFile());
            INPUTS.setAcceptAllFileFilterUsed(false);
            INPUTS.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            INPUTS.setMultiSelectionEnabled(false);
            INPUTS.setFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File f) { return f.isDirectory() || f.getName().endsWith(IOUtils.CUBOSPHERE_LEVEL_DOT_FORMAT); }

                @Override
                public String getDescription()
                {
                    return "Cubosphere Level (" + IOUtils.CUBOSPHERE_LEVEL_DOT_FORMAT + ") or folder";
                }
            });
        }
        return INPUTS;
    }
    
    private JFileChooser OUTPUTS;
    private @NonNull JFileChooser outputs()
    {
        if(OUTPUTS == null)
        {
            OUTPUTS = new JFileChooser(IOUtils.getUserDirectory().toFile());
            OUTPUTS.setAcceptAllFileFilterUsed(false);
            OUTPUTS.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            OUTPUTS.setMultiSelectionEnabled(false);
            OUTPUTS.setFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File f) { return f.isDirectory(); }

                @Override
                public String getDescription()
                {
                    return "Folder";
                }
            });
        }
        return OUTPUTS;
    }
    
    private @NonNull Path insertFormat(@NonNull Path path, @NonNull String format)
    {
        path = path.toAbsolutePath();
        if(!path.getFileName().toString().endsWith(format))
            path = IOUtils.concatElementAtPathEnd(path, format);
        return path;
    }
    
    public @NonNull Optional<Path> showInputs(Component parent)
    {
        JFileChooser chooser = inputs();
        if(chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION)
            return Optional.empty();
        
        return Optional.of(chooser.getSelectedFile().toPath());
    }
    
    public @NonNull Optional<Path> showOutputs(Component parent)
    {
        JFileChooser chooser = outputs();
        if(chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION)
            return Optional.empty();
        return Optional.of(chooser.getSelectedFile().toPath());
    }
}
