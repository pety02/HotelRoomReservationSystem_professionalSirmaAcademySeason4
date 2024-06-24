package readersWriters;

import interfaces.IReadableWritable;
import models.Reservation;

import java.io.File;
import java.io.FileReader;

public class ReservationReaderWriter implements IReadableWritable<Reservation> {
    @Override
    public void write(Reservation obj, String filename) {

    }

    @Override
    public Reservation read(FileReader fr, File file) {
        return null;
    }
}