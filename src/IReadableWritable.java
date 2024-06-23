import java.io.File;
import java.io.FileReader;

public interface IReadableWritable<T> {
    void write(T obj, String filename);
    User read(FileReader fr, File file);
}