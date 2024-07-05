package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;

import java.io.*;
import java.util.ArrayList;

/**
 * A class responsible for reading from and writing to a file of User objects using JSON format.
 */
public class UserReaderWriter extends ReaderWriter<User> {

    /**
     * Parses a JSON string into a User object using Jackson ObjectMapper.
     * @param json The JSON string to parse.
     * @return The parsed User object.
     * @throws IOException If there is an error during parsing.
     */
    private static User parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User obj = mapper.reader().readValue(json, User.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    /**
     * Writes a User object to a file in JSON format.
     * @param obj The User object to write.
     * @param filename The name of the file to write to.
     */
    @Override
    public void write(User obj, String filename)  {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString()); // Assuming User has a sensible toString() method
            bw.newLine();
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    /**
     * Reads User objects from a file in JSON format.
     * @param fr The FileReader object initialized with the file to read from.
     * @param file The File object representing the file to read from.
     * @return An ArrayList of User objects read from the file.
     */
    @Override
    public ArrayList<User> read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        ArrayList<User> users = new ArrayList<>();
        try {
            readByte = fr.read();
            sb.append((char) readByte);
            User user;
            while (readByte > -1) {
                if ((char) readByte == '\n') {
                    // Parse the JSON string into a User object and add to the list
                    user = UserReaderWriter.parse(sb.substring(0, sb.length() - 1));
                    users.add(user);
                    sb = new StringBuilder();
                }
                readByte = fr.read();
                sb.append((char) readByte);
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return users;
    }
}