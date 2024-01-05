package kp.rollingcube.levelConverter.ui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import kp.rollingcube.levelConverter.level.Level;
import kp.rollingcube.levelConverter.level.cubosphere.lua.LevelLoaderContext;
import kp.rollingcube.levelConverter.utils.IOUtils;
import kp.rollingcube.levelConverter.utils.LoggerUtils;
import kp.rollingcube.levelConverter.utils.UIUtils;
import kp.rollingcube.levelConverter.utils.Version;
import lombok.NonNull;
import lombok.Synchronized;

/**
 *
 * @author Marc
 */
public final class LevelConverterFrame extends JFrame
{
    private final @NonNull UILogger logger;
    private final @NonNull ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final @NonNull AtomicBoolean runningConversion = new AtomicBoolean(false);
    private final @NonNull Semaphore loggerSem = new Semaphore(1, true);
    private final Object sLock = new Object();
    
    
    private LevelConverterFrame()
    {
        initComponents();
        init();
        
        this.logger = new UILogger(logTextPane);
    }
    
    public static final void open()
    {
        var frame = new LevelConverterFrame();
        frame.setVisible(true);
    }
    
    private void init()
    {
        setTitle("CtR Level Converter - v" + Version.APP_VERSION);
        
        setResizable(false);
        UIUtils.focus(this);
        UIUtils.setIcon(this);
    }
    
    private void convert()
    {
        if(runningConversion.get())
            return;
        
        runningConversion.set(true);
        disableInteraction();
        
        logger.clearText();
        
        if(inputTextField.getText().isBlank())
        {
            error("Inputs Path cannot be empty", inputTextField.getText());
            runningConversion.set(false);
            enableInteraction();
            return;
        }
        
        if(outputTextField.getText().isBlank())
        {
            error("Output folder cannot be empty", inputTextField.getText());
            runningConversion.set(false);
            enableInteraction();
            return;
        }

        var inputsPath = Path.of(inputTextField.getText()).toAbsolutePath();
        if(!Files.exists(inputsPath))
        {
            error("Inputs Path '%s' not exists", inputsPath);
            runningConversion.set(false);
            enableInteraction();
            return;
        }

        var outputPath = Path.of(outputTextField.getText()).toAbsolutePath();
        if(!Files.isDirectory(outputPath))
        {
            error("Invalid Output folder '%s'", outputPath);
            runningConversion.set(false);
            enableInteraction();
            return;
        }

        info("Starting conversion...");
        conversionProcess(inputsPath, outputPath);
    }
    
    private void conversionProcess(@NonNull Path inputsPath, @NonNull Path outputFolder)
    {
        try
        {
            if(Files.isDirectory(inputsPath))
            {
                Files.list(inputsPath)
                        .filter(path -> path != null && Files.isRegularFile(path) && path.toString().endsWith(IOUtils.CUBOSPHERE_LEVEL_DOT_FORMAT))
                        .forEach(path -> convertLevel(path, outputFolder));
            }
            else
                convertLevel(inputsPath, outputFolder);
            
            success("Conversion DONE!");
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            error("Unexpected error during conversion process: %s", ex.getLocalizedMessage());
        }
        finally
        {
            runningConversion.set(false);
            enableInteraction();
        }
    }
    
    private void convertLevel(@NonNull Path levelPath, @NonNull Path folderDestPath)
    {
        try
        {
            info("-- Init conversion of '%s' --", levelPath.getFileName());

            var context = new LevelLoaderContext();
            var oclevel = context.loadLevel(levelPath, logger);
            if(oclevel.isEmpty())
            {
                error("Cannot finished conversion of '%s' because has some errors.", levelPath.getFileName());
                return;
            }
            
            var level = oclevel.get().toRollingcubeLevel(logger);
            
            Path destFilePath = folderDestPath.resolve(levelPath.getFileName());
            Level.write(destFilePath, level);

            info("-- Finished conversion of '%s' --", levelPath.getFileName());
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            error("Unexpected error during level conversion of '%s': %s", levelPath.getFileName(), ex.getLocalizedMessage());
        }
    }
    
    private void success(@NonNull String text, Object... args) { semAction(() -> LoggerUtils.success(logger, text, args)); }
    private void info(@NonNull String text, Object... args) { semAction(() -> LoggerUtils.info(logger, text, args)); }
    private void warn(@NonNull String text, Object... args) { semAction(() -> LoggerUtils.warn(logger, text, args)); }
    private void error(@NonNull String text, Object... args) { semAction(() -> LoggerUtils.error(logger, text, args)); }
    
    @Synchronized("sLock")
    private void semAction(@NonNull Runnable action)
    {
        try
        {
            loggerSem.acquire();
            action.run();
            loggerSem.release();
        }
        catch(InterruptedException ex) {}
    }
    
    private void disableInteraction() { setInteraction(false); }
    private void enableInteraction() { setInteraction(true); }
    
    private void setInteraction(boolean enabled)
    {
        inputTextField.setEditable(enabled);
        inputFindFile.setEnabled(enabled);
        outputTextField.setEditable(enabled);
        outputFindFile.setEnabled(enabled);
        convertButton.setEnabled(enabled);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        logScrollPane = new javax.swing.JScrollPane();
        logTextPane = new javax.swing.JTextPane();
        inputPanel = new javax.swing.JPanel();
        inputTextField = new javax.swing.JTextField();
        inputFindFile = new javax.swing.JButton();
        outputPanel = new javax.swing.JPanel();
        outputTextField = new javax.swing.JTextField();
        outputFindFile = new javax.swing.JButton();
        convertButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        logTextPane.setEditable(false);
        logTextPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Log"));
        logTextPane.setContentType("text/html"); // NOI18N
        logTextPane.setFocusable(false);
        logScrollPane.setViewportView(logTextPane);

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("File or Folder to Convert"));

        inputFindFile.setText("...");
        inputFindFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputFindFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputPanelLayout = new javax.swing.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputFindFile, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputFindFile))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Destination Folder"));

        outputFindFile.setText("...");
        outputFindFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputFindFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputFindFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outputFindFile))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logScrollPane)
                    .addComponent(outputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(inputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        convertButton.setText("Convert");
        convertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(convertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(convertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void outputFindFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputFindFileActionPerformed
        if(runningConversion.get())
            return;
        
        var result = FileChooser.showOutputs(this);
        if(result.isEmpty())
            return;
        
        outputTextField.setText(result.get().toAbsolutePath().toString());
    }//GEN-LAST:event_outputFindFileActionPerformed

    private void inputFindFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputFindFileActionPerformed
        if(runningConversion.get())
            return;
        
        var result = FileChooser.showInputs(this);
        if(result.isEmpty())
            return;
        
        inputTextField.setText(result.get().toAbsolutePath().toString());
    }//GEN-LAST:event_inputFindFileActionPerformed

    private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
        convert();
    }//GEN-LAST:event_convertButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton convertButton;
    private javax.swing.JButton inputFindFile;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JScrollPane logScrollPane;
    private javax.swing.JTextPane logTextPane;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton outputFindFile;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextField outputTextField;
    // End of variables declaration//GEN-END:variables
}
