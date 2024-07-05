package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;

import java.io.*;
import java.util.ArrayList;

/**
 *
 */
public class UserReaderWriter extends ReaderWriter<User> {

    private static User parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User obj = mapper.reader().readValue(json, User.class);
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
    public void write(User obj, String filename)  {
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
    public ArrayList<User> read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        ArrayList<User> users = new ArrayList<>();
        try {
            readByte = fr.read();
            sb.append((char)readByte);
            User user = new User();
            while (readByte > -1) {
                if((char) readByte == '\n') {
                    user = UserReaderWriter.parse(sb.substring(0,sb.length() - 1));
                    users.add(user);
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

        return users;
    }
}