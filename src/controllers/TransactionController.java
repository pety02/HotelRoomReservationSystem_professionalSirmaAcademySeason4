package controllers;

import interfaces.ITransactionManageable;
import models.DebitCard;
import models.Hotel;
import readersWriters.DebitCardReaderWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The TransactionController class implements the ITransactionManageable interface
 * and provides functionality for handling transactions related to debit cards and hotel incomes.
 */
public class TransactionController implements ITransactionManageable {
    private static final String debitCardsFilename = "debitCards.txt";
    private Hotel hotel;

    /**
     * Constructor to initialize the TransactionController with a hotel instance.
     *
     * @param hotel the hotel associated with this controller
     */
    public TransactionController(Hotel hotel) {
        this.setHotel(hotel);
    }

    /**
     * Gets the hotel associated with this controller.
     *
     * @return the hotel
     */
    public Hotel getHotel() {
        return hotel;
    }

    /**
     * Sets the hotel associated with this controller.
     *
     * @param hotel the hotel to set
     */
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    /**
     * Processes a transaction by deducting the specified amount from the debit card balance
     * and adding it to the hotel's incomes.
     *
     * @param debitCardId the ID of the debit card to be charged
     * @param moneyToBePayed the amount of money to be deducted
     * @return true if the transaction is successful, false otherwise
     */
    @Override
    public boolean makeTransaction(int debitCardId, double moneyToBePayed) {
        DebitCardReaderWriter drw = new DebitCardReaderWriter();
        ArrayList<DebitCard> readDebitCards;
        File file = new File(TransactionController.debitCardsFilename);
        try (FileReader fr = new FileReader(file)) {
            readDebitCards = drw.read(fr, file);

            // Iterate through the list of debit cards to find the matching card by ID
            for (DebitCard card : readDebitCards) {
                if (card.getId() == debitCardId) {
                    // Deduct the specified amount from the card's balance
                    double newBalance = card.getBalance() - moneyToBePayed;
                    card.setBalance(newBalance);
                    System.out.printf("You successfully have paid %.2f$!%n", card.getBalance());

                    // Update the hotel's total incomes
                    double newHotelIncomes = this.hotel.getIncomes() + moneyToBePayed;
                    this.hotel.setIncomes(newHotelIncomes);
                    return true; // Transaction successful
                }
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
            return false; // Transaction failed due to an I/O error
        }

        return false; // Transaction failed if card not found or insufficient balance
    }
}