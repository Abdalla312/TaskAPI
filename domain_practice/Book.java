package domain_practice;

public class Book extends BaseEntity implements Borrowable {
    private String title;
    private String author;
    private User borrowedBy;

    Book(String title, String author) {

        setTitle(title);
        setAuthor(author);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public User getBorrowedBy() {
        return borrowedBy;
    }

    @Override
    public boolean isAvailable() {
        return borrowedBy == null;
    }

    @Override
    public void borrow(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (!isAvailable()) {
            throw new IllegalStateException("Book is already borrowed.");
        }
        borrowedBy = user;
        user.addBorrowedBookInternal(this);
    }

    @Override
    public void giveBack() {
        if (borrowedBy != null) {
            User previousBorrower = borrowedBy;
            borrowedBy = null;
            previousBorrower.removeBorrowedBookInternal(this);
        }
    }

    private void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.title = title;
    }

    private void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null.");
        }
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{title:'" + title + "', author:'" + author + "'}";
    }
}
