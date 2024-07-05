package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Reservation;

import java.io.*;
import java.util.ArrayList;

/**
 *
 */
public class ReservationReaderWriter extends ReaderWriter<Reservation> {
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
    private static Reservation parse(String json) throws IOException {
        ObjectMapper mapper = createObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Reservation obj = mapper.reader().readValue(json, Reservation.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    /**
     *
     * @param obj
     * @param filename
     */
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

    /**
     *
     * @param fr
     * @param file
     * @return
     */
    @Override
    public ArrayList<Reservation> read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        ArrayList<Reservation> reservations = new ArrayList<>();
        try {
            readByte = fr.read();
            sb.append((char)readByte);
            Reservation reservation = new Reservation();
            while (readByte > -1) {
                if((char) readByte == '\n') {
                    reservation = ReservationReaderWriter.parse(sb.substring(0,sb.length() - 1));
                    reservations.add(reservation);
                    sb = new StringBuilder();
                }
                readByte = fr.read();
                sb.append((char)readByte);
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            ex.printStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return reservations;
    }
}