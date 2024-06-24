package interfaces;

import models.User;

import java.io.File;
import java.io.FileReader;

public interface IReadableWritable<T> {
    void write(T obj, String filename);
    T read(FileReader fr, File file);
}