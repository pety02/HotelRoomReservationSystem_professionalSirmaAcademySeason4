import java.util.ArrayList;

public interface IReadableWritable<T> {
    void write(T obj, String filename);
    ArrayList<T> read(String filename);
}