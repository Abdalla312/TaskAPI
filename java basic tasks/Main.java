import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your name:");
        String name = input.nextLine();
        System.out.println("Please enter your age:");
        int age = input.nextInt();
        System.out.println("Your name is:" + name + ", Your age is: " + age);
        input.close();

    }
}
