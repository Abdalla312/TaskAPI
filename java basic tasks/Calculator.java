import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Input your first num: ");
        double x = input.nextDouble();
        System.out.println("Input your second num: ");
        double y = input.nextDouble();

        System.out.println("for\n1:+\t2:-\n3:*\t4:/  ");
        int inputSymbol = input.nextInt();
        while (inputSymbol > 4 || inputSymbol < 1) {
            System.out.print("Invalid input try again: ");
            inputSymbol = input.nextInt();
        }
        input.close();
        System.out.print("The result is :");
        switch (inputSymbol) {
            case 1 -> System.out.println(x + y);
            case 2 -> System.out.println(x - y);
            case 3 -> System.out.println(x * y);
            case 4 -> System.out.println(x / y);
        }

    }
}
