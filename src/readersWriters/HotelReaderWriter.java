package readersWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Hotel;

import java.io.*;
import java.util.ArrayList;

/**
 *
 */
public class HotelReaderWriter extends ReaderWriter<Hotel>  {
    private static Hotel parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Hotel obj = mapper.reader().readValue(json, Hotel.class);
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
    public void write(Hotel obj, String filename) {
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
    public ArrayList<Hotel> read(FileReader fr, File file) {
        StringBuilder sb = new StringBuilder();
        int readByte;
        ArrayList<Hotel> hotels = new ArrayList<>();
        try {
            readByte = fr.read();
            sb.append((char)readByte);
            Hotel hotel = new Hotel();
            while (readByte > -1) {
                if((char) readByte == '\n') {
                    hotel = HotelReaderWriter.parse(sb.substring(0,sb.length() - 1));
                    hotels.add(hotel);
                    sb = new StringBuilder();
                }
                readByte = fr.read();
                sb.append((char)readByte);
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return hotels;
    }
}