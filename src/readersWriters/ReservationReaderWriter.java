package readersWriters;

import interfaces.IReadableWritable;
import models.Reservation;
import models.User;

import java.io.File;
import java.io.FileReader;

public class ReservationReaderWriter implements IReadableWritable<Reservation> {
    @Override
    public void write(Reservation obj, String filename) {

    }

    @Override
    public User read(FileReader fr, File file) {
        return null;
    }
}