package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Reservation;

import java.io.*;
import java.util.ArrayList;

/**
 * This class provides functionality to read from and write to a file
 * containing Reservation objects in JSON format.
 */
public class ReservationReaderWriter extends ReaderWriter<Reservation> {

    /**
     * Creates and configures an ObjectMapper instance with JavaTimeModule.
     * @return Configured ObjectMapper instance.
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Parses a JSON string representation of a Reservation object using ObjectMapper.
     * @param json JSON string representing a Reservation object.
     * @return Parsed Reservation object.
     * @throws IOException If there is an error during JSON parsing.
     */
    private static Reservation parse(String json) throws IOException {
        ObjectMapper mapper = createObjectMapper();
        Reservation obj = mapper.reader().readValue(json, Reservation.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    /**
     * Writes a Reservation object to a file.
     * @param obj Reservation object to write.
     * @param filename Name of the file to write to.
     */
    @Override
    public void write(Reservation obj, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString()); // Writes the string representation of the Reservation object
            bw.newLine(); // Writes a new line after each object
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    /**
     * Reads Reservation objects from a file.
     * @param fr FileReader instance for the file to read from.
     * @param file File instance representing the file to read from.
     * @return ArrayList of Reservation objects read from the file.
     */
    @Override
    public ArrayList<Reservation> read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        ArrayList<Reservation> reservations = new ArrayList<>();
        try {
            readByte = fr.read();
            sb.append((char)readByte); // Append the first character read
            Reservation reservation;
            while (readByte > -1) { // Continue reading until end of file
                if((char) readByte == '\n') { // Check for end of line
                    reservation = ReservationReaderWriter.parse(sb.substring(0,sb.length() - 1)); // Parse the JSON string
                    reservations.add(reservation); // Add parsed Reservation object to the ArrayList
                    sb = new StringBuilder(); // Reset StringBuilder for the next line
                }
                readByte = fr.read(); // Read the next byte
                sb.append((char)readByte); // Append the character to StringBuilder
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return reservations; // Return the ArrayList of Reservation objects read from the file
    }
}