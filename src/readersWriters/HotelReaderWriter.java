package readersWriters;

import interfaces.IReadableWritable;
import models.Hotel;

import java.io.File;
import java.io.FileReader;

public class HotelReaderWriter implements IReadableWritable<Hotel> {
    @Override
    public void write(Hotel obj, String filename) {

    }

    @Override
    public Hotel read(FileReader fr, File file) {
        return null;
    }
}