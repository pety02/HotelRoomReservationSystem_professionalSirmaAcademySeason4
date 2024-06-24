package readersWriters;

import interfaces.IReadableWritable;
import models.DebitCard;

import java.io.File;
import java.io.FileReader;

public class DebitCardReaderWriter implements IReadableWritable<DebitCard> {
    @Override
    public void write(DebitCard obj, String filename) {

    }

    @Override
    public DebitCard read(FileReader fr, File file) {
        return null;
    }
}