package domain_practice;

public class Package {
    public static void main(String[] args) {
        User user = new User("Abdalla", "Test@test.com");
        Book book = new Book("1984", "George Orwell");
        Task task = new Task("read chapter 1", "finish the first ch this week.");

        user.borrowBook(book);
        user.assignTask(task);
        task.start();
        task.complete();

        System.out.println(user);
        System.out.println(book);
        System.out.println(task);
        System.out.println("user role" + user.getRole());
        System.out.println("book available" + book.isAvailable());
        user.returnBook(book);
        System.out.println("book available " + book.isAvailable());

    }
}
