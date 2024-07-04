package interfaces;

public interface ITransactionManageable {
    boolean makeTransaction(int debitCardId, double moneyToBePayed);
}