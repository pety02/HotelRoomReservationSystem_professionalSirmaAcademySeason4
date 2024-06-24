package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.IReadableWritable;
import models.Room;

import java.io.*;

public class RoomReaderWriter implements IReadableWritable<Room> {
    private static Room parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Room obj = mapper.reader().readValue(json, Room.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }
    @Override
    public void write(Room obj, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString());
            bw.newLine();
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    @Override
    public Room read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        try {
            readByte = fr.read();
            sb.append((char)readByte);
            Room room = new Room();
            while (readByte > -1) {
                if((char) readByte == '\n') {
                    room = RoomReaderWriter.parse(sb.substring(0,sb.length() - 1));
                    readByte = fr.read();
                    sb.append((char)readByte);
                }
                readByte = fr.read();
                sb.append((char)readByte);
            }

            return room;
        } catch (IOException ex) {
            ex.fillInStackTrace();
            return new Room();
        }
    }
}