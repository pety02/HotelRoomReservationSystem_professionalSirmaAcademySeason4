package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.DebitCard;

import java.util.*;
import java.io.*;

/**
 * This class provides functionality to read from and write to a file
 * containing DebitCard objects in JSON format.
 */
public class DebitCardReaderWriter extends ReaderWriter<DebitCard> {

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
     * Parses a JSON string representation of a DebitCard object using ObjectMapper.
     * @param json JSON string representing a DebitCard object.
     * @return Parsed DebitCard object.
     * @throws IOException If there is an error during JSON parsing.
     */
    private static DebitCard parse(String json) throws IOException {
        ObjectMapper mapper = createObjectMapper();
        DebitCard obj = mapper.reader().readValue(json, DebitCard.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    /**
     * Writes a DebitCard object to a file.
     * @param obj DebitCard object to write.
     * @param filename Name of the file to write to.
     */
    @Override
    public void write(DebitCard obj, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString()); // Writes the string representation of the DebitCard object
            bw.newLine(); // Writes a new line after each object
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    /**
     * Reads DebitCard objects from a file.
     * @param fr FileReader instance for the file to read from.
     * @param file File instance representing the file to read from.
     * @return ArrayList of DebitCard objects read from the file.
     */
    @Override
    public ArrayList<DebitCard> read(FileReader fr, File file) {
        ArrayList<DebitCard> cards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                DebitCard debitCard = parse(line); // Parse each line into a DebitCard object
                cards.add(debitCard); // Add parsed DebitCard object to the ArrayList
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return cards; // Return the ArrayList of DebitCard objects read from the file
    }
}