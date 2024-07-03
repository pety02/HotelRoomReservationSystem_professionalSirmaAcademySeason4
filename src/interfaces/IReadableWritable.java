package interfaces;

import models.User;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public interface IReadableWritable<T> {
    void write(T obj, String filename);
    ArrayList<T> read(FileReader fr, File file);
}