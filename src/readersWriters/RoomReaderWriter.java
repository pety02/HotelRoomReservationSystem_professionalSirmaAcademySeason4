package readersWriters;

import interfaces.IReadableWritable;
import models.Room;
import models.User;

import java.io.File;
import java.io.FileReader;

public class RoomReaderWriter implements IReadableWritable<Room> {
    @Override
    public void write(Room obj, String filename) {

    }

    @Override
    public User read(FileReader fr, File file) {
        return null;
    }
}