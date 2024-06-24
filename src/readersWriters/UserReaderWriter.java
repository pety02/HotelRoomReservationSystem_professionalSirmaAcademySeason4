package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.IReadableWritable;
import models.User;

import java.io.*;

public class UserReaderWriter implements IReadableWritable<User> {

    private static User parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User obj = mapper.reader().readValue(json, User.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

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

    @Override
    public User read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        try {
            readByte = fr.read();
            sb.append((char)readByte);
            User user = new User();
            while (readByte > -1) {
                if((char) readByte == '\n') {
                    user = UserReaderWriter.parse(sb.substring(0,sb.length() - 1));
                    readByte = fr.read();
                    sb.append((char)readByte);
                }
                readByte = fr.read();
                sb.append((char)readByte);
            }

            return user;
        } catch (IOException ex) {
            ex.fillInStackTrace();
            return new User();
        }
    }
}