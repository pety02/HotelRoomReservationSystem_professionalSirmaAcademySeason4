import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class UserReaderWriter implements IReadableWritable<User> {
    public UserReaderWriter() {

    }

    private static User parse(String json) {
        User currentUser = new User();
        StringBuilder sb = new StringBuilder();

        String subJson = json.substring(11);
        StringBuilder userField = new StringBuilder();
        int index = 0;
        while(subJson.charAt(index) != ',') {
            userField.append(subJson.charAt(index++));
        }
        currentUser.setId(Integer.parseInt(userField.toString()));
        userField = new StringBuilder();
        subJson = json.substring(index + 23);
        while(subJson.charAt(index) != '\"') {
            userField.append(subJson.charAt(index++));
        }
        currentUser.setUsername(userField.toString());
        userField = new StringBuilder();
        subJson = json.substring(index + 24);
        while(subJson.charAt(index) != '\"') {
            userField.append(subJson.charAt(index++));
        }
        currentUser.setEmail(userField.toString());
        userField = new StringBuilder();
        subJson = json.substring(index + 21);
        while(subJson.charAt(index) != '\"') {
            userField.append(subJson.charAt(index++));
        }
        try {
            currentUser.setPassword(userField.toString(), false);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            ex.fillInStackTrace();
            System.out.println("Cannot set this password!");
        }

        return currentUser;
    }

    @Override
    public void write(User obj, String filename)  {
        try (FileWriter bw = new FileWriter(filename)) {
            bw.write(obj.toString());
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in file with name %s!%n", filename);
        }
    }

    @Override
    public ArrayList<User> read(String filename) {
        try(FileReader fr = new FileReader(filename)) {
            StringBuilder sb = new StringBuilder();
            int readByte = fr.read();
            sb.append((char)readByte);
            ArrayList<User> users = new ArrayList<>();
            while (readByte > -1) {
                readByte = fr.read();
                if((char) readByte == '}') {
                    users.add(UserReaderWriter.parse(sb.toString()));
                    sb = new StringBuilder();
                    continue;
                }
                sb.append((char)readByte);
            }
            return users;
        } catch (IOException ex) {
            ex.fillInStackTrace();
            return null;
        }
    }
}