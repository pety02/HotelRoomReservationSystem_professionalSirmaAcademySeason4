package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import interfaces.IReadableWritable;
import models.DebitCard;
import models.Room;

import java.io.*;
import java.util.ArrayList;

public class RoomReaderWriter extends ReaderWriter<Room> {

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private static Room parse(String json) throws IOException {
        ObjectMapper mapper = createObjectMapper();
        Room obj = mapper.readValue(json, Room.class);
        if (obj == null) {
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
    public ArrayList<Room> read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        ArrayList<Room> rooms = new ArrayList<>();
        try {
            readByte = fr.read();
            sb.append((char) readByte);
            Room room = new Room();
            while (readByte > -1) {
                if ((char) readByte == '\n') {
                    room = RoomReaderWriter.parse(sb.substring(0, sb.length() - 1));
                    rooms.add(room);
                    sb = new StringBuilder();
                }
                readByte = fr.read();
                sb.append((char) readByte);
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return rooms;
    }
}