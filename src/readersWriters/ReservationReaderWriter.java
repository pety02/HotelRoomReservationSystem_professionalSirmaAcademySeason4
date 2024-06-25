package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.IReadableWritable;
import models.Reservation;

import java.io.*;

public class ReservationReaderWriter implements IReadableWritable<Reservation> {
    private static Reservation parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Reservation obj = mapper.reader().readValue(json, Reservation.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }
    @Override
    public void write(Reservation obj, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString());
            bw.newLine();
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    @Override
    public Reservation read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        try {
            readByte = fr.read();
            sb.append((char)readByte);
            Reservation reservation = new Reservation();
            while (readByte > -1) {
                if((char) readByte == '\n') {
                    reservation = ReservationReaderWriter.parse(sb.substring(0,sb.length() - 1));
                    readByte = fr.read();
                    sb.append((char)readByte);
                }
                readByte = fr.read();
                sb.append((char)readByte);
            }

            return reservation;
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
            return new Reservation();
        }
    }
}