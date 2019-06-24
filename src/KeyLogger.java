import java.io.File;
import java.io.FileWriter;

public class KeyLogger {
    FileWriter fileWriter;

    public KeyLogger(File file) {
        file.setWritable(true, false);
        try {
            fileWriter = new FileWriter(file, true);
        }catch (Exception e) {

        }
    }

    public void logToFile(String keystroke) {
        try {
            fileWriter.write(keystroke);
            fileWriter.flush();
        }catch (Exception e) {

        }
    }
}





