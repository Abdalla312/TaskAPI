package domain_practice;

public interface Borrowable {
    boolean isAvailable();
    void borrow(User user);
    void giveBack();
}
