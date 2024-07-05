package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Hotel;

import java.io.*;
import java.util.ArrayList;

/**
 * This class provides functionality to read from and write to a file
 * containing Hotel objects in JSON format.
 */
public class HotelReaderWriter extends ReaderWriter<Hotel> {

    /**
     * Parses a JSON string representation of a Hotel object using ObjectMapper.
     * @param json JSON string representing a Hotel object.
     * @return Parsed Hotel object.
     * @throws IOException If there is an error during JSON parsing.
     */
    private static Hotel parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Hotel obj = mapper.reader().readValue(json, Hotel.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    /**
     * Writes a Hotel object to a file.
     * @param obj Hotel object to write.
     * @param filename Name of the file to write to.
     */
    @Override
    public void write(Hotel obj, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString()); // Writes the string representation of the Hotel object
            bw.newLine(); // Writes a new line after each object
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    /**
     * Reads Hotel objects from a file.
     * @param fr FileReader instance for the file to read from.
     * @param file File instance representing the file to read from.
     * @return ArrayList of Hotel objects read from the file.
     */
    @Override
    public ArrayList<Hotel> read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        ArrayList<Hotel> hotels = new ArrayList<>();
        try {
            readByte = fr.read();
            sb.append((char)readByte); // Append the first character read
            Hotel hotel;
            while (readByte > -1) { // Continue reading until end of file
                if((char) readByte == '\n') { // Check for end of line
                    hotel = HotelReaderWriter.parse(sb.substring(0,sb.length() - 1)); // Parse the JSON string
                    hotels.add(hotel); // Add parsed Hotel object to the ArrayList
                    sb = new StringBuilder(); // Reset StringBuilder for the next line
                }
                readByte = fr.read(); // Read the next byte
                sb.append((char)readByte); // Append the character to StringBuilder
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            ex.printStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return hotels; // Return the ArrayList of Hotel objects read from the file
    }
}