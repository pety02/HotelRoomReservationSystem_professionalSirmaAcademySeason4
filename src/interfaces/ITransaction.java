package interfaces;

import models.Hotel;

public interface ITransaction {
    boolean makeTransaction(int debitCardId, double moneyToBePayed);
}