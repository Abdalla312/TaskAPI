package domain_practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User extends Person {
    private final List<Task> tasks = new ArrayList<>();
    private final List<Book> borrowedBooks = new ArrayList<>();

    public User(String name, String email) {
        super(name, email);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public List<Book> getBorrowedBooks() {
        return Collections.unmodifiableList(borrowedBooks);
    }

    public void assignTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be empty.");

        }
        task.assignTo(this);
    }

    // borrowBook
    public void borrowBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        book.borrow(this);
    }

    // return borrowed book
    public void returnBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (!borrowedBooks.contains(book)) {
            throw new IllegalStateException("This user did not borrow that book. ");
        }
        book.giveBack();
    }

    // add task internal
    void addTaskInternal(Task task) {
        if (!tasks.contains(task)) {
            tasks.add(task);
        }
    }

    // rem task internal
    void removeTaskInternal(Task task) {
        tasks.remove(task);
    }

    // add borrowed book internal
    void addBorrowedBookInternal(Book book) {
        if (!borrowedBooks.contains(book)) {
            borrowedBooks.add(book);
        }
    }

    // rem borrowed book internal
    void removeBorrowedBookInternal(Book book) {
        borrowedBooks.remove(book);
    }

    @Override
    public String getRole() {
        return "Standard User";
    }

    @Override
    public String toString() {
        return "User{name='" + getName() + "', email='" + getEmail() + "'}";
    }
}
