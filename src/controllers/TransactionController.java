package controllers;

import interfaces.ITransaction;
import models.DebitCard;
import models.Hotel;
import readersWriters.DebitCardReaderWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TransactionController implements ITransaction {
    private static final String debitCardsFilename = "debitCards.txt";
    private Hotel hotel;

    public TransactionController(Hotel hotel) {
        this.setHotel(hotel);
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public boolean makeTransaction(int debitCardId, double moneyToBePayed) {
        DebitCardReaderWriter drw = new DebitCardReaderWriter();
        ArrayList<DebitCard> readDebitCards;
        File file = new File(TransactionController.debitCardsFilename);
        try(FileReader fr = new FileReader(file)) {
            readDebitCards = drw.read(fr, file);
            for(DebitCard card : readDebitCards) {
                if(card.getId() == debitCardId) {
                    double newBalance = card.getBalance() - moneyToBePayed;
                    card.setBalance(newBalance);
                    System.out.printf("You successfully have paid %.2f$!%n", card.getBalance());
                    double newHotelIncomes = this.hotel.getIncomes() + moneyToBePayed;
                    this.hotel.setIncomes(newHotelIncomes);
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            return false;
        }

        return false;
    }
}