package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Room;

import java.io.*;
import java.util.ArrayList;

/**
 * This class provides functionality to read from and write to a file
 * containing Room objects in JSON format.
 */
public class RoomReaderWriter extends ReaderWriter<Room> {

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
     * Parses a JSON string representation of a Room object using ObjectMapper.
     * @param json JSON string representing a Room object.
     * @return Parsed Room object.
     * @throws IOException If there is an error during JSON parsing.
     */
    private static Room parse(String json) throws IOException {
        ObjectMapper mapper = createObjectMapper();
        Room obj = mapper.readValue(json, Room.class);
        if (obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    /**
     * Writes a Room object to a file.
     * @param obj Room object to write.
     * @param filename Name of the file to write to.
     */
    @Override
    public void write(Room obj, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString()); // Writes the string representation of the Room object
            bw.newLine(); // Writes a new line after each object
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    /**
     * Reads Room objects from a file.
     * @param fr FileReader instance for the file to read from.
     * @param file File instance representing the file to read from.
     * @return ArrayList of Room objects read from the file.
     */
    @Override
    public ArrayList<Room> read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        ArrayList<Room> rooms = new ArrayList<>();
        try {
            readByte = fr.read();
            sb.append((char) readByte); // Append the first character read
            Room room;
            while (readByte > -1) { // Continue reading until end of file
                if ((char) readByte == '\n') { // Check for end of line
                    room = RoomReaderWriter.parse(sb.substring(0, sb.length() - 1)); // Parse the JSON string
                    rooms.add(room); // Add parsed Room object to the ArrayList
                    sb = new StringBuilder(); // Reset StringBuilder for the next line
                }
                readByte = fr.read(); // Read the next byte
                sb.append((char) readByte); // Append the character to StringBuilder
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return rooms; // Return the ArrayList of Room objects read from the file
    }
}