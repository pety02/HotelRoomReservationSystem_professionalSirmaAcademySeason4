package interfaces;

import models.Hotel;

public interface ITransaction {
    void makeTransaction(int debitCardId, double moneyToBePayed);
}