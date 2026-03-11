import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        IO.println("Input your first num: ");
        double x = input.nextDouble();
        IO.println("Input your second num: ");
        double y = input.nextDouble();

        IO.println("for\n1:+\t2:-\n3:*\t4:/  ");
        int inputSymbol = input.nextInt();
        while (inputSymbol > 4 || inputSymbol < 1) {
            IO.print("Invalid input try again: ");
            inputSymbol = input.nextInt();
        }
        input.close();
        IO.print("The result is :");
        switch (inputSymbol) {
            case 1 -> IO.println(x + y);
            case 2 -> IO.println(x - y);
            case 3 -> IO.println(x * y);
            case 4 -> IO.println(x / y);
        }

    }
}
