package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.IReadableWritable;
import models.DebitCard;

import java.io.*;

public class DebitCardReaderWriter implements IReadableWritable<DebitCard> {

    private static DebitCard parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DebitCard obj = mapper.reader().readValue(json, DebitCard.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    @Override
    public void write(DebitCard obj, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString());
            bw.newLine();
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    @Override
    public DebitCard read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        try {
            readByte = fr.read();
            sb.append((char)readByte);
            DebitCard debitCard = new DebitCard();
            while (readByte > -1) {
                if((char) readByte == '\n') {
                    debitCard = DebitCardReaderWriter.parse(sb.substring(0,sb.length() - 1));
                    readByte = fr.read();
                    sb.append((char)readByte);
                }
                readByte = fr.read();
                sb.append((char)readByte);
            }

            return debitCard;
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
            return new DebitCard();
        }
    }
}