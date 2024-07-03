package readersWriters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import interfaces.IReadableWritable;
import models.DebitCard;

import java.util.*;
import java.io.*;

public class DebitCardReaderWriter extends ReaderWriter<DebitCard> {

    private static DebitCard parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        DebitCard obj = mapper.reader().readValue(json, DebitCard.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    @Override
    public void write(DebitCard obj, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(obj.toString());
            bw.newLine();
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot write in a file with the name %s!%n", filename);
        }
    }

    @Override
    public ArrayList<DebitCard> read(FileReader fr, File file) {
        ArrayList<DebitCard> cards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                DebitCard debitCard = parse(line);
                cards.add(debitCard);
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            ex.printStackTrace();
            System.out.printf("Cannot read from a file with the name %s", file.getName());
        }

        return cards;
    }
}