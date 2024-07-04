package interfaces;

public interface IAdminManageable {
    void showAllHotels();
    void viewAllBookings(int hotelId);
    double getTotalIncome(int hotelId) throws RuntimeException;
    double getCancellationFees(int hotelId);
}