import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        IO.println("Please enter your name:");
        String name = input.nextLine();
        IO.println("Please enter your age:");
        int age = input.nextInt();
        IO.println("Your name is:" + name + ", Your age is: " + age);
        input.close();

    }
}
