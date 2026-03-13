interface BicycleBehavior {
    void changeCadence(int newValue);

    void speedUp(int newValue);

    void changeGear(int newValue);

    void applyBrake(int decrement);
}

class Bicycle implements BicycleBehavior {
    int cadence = 0, speed = 0, gear = 1;

    @Override
    public void changeCadence(int newValue) {
        cadence = newValue;
    }

    @Override
    public void speedUp(int increament) {
        speed = speed + increament;
    }

    @Override
    public void changeGear(int newValue) {
        gear = newValue;
    }

    @Override
    public void applyBrake(int decrement) {
        speed = speed - decrement;
    }

    void printStates() {
        System.out.println(
                "The Speed: " + speed +
                        ", Gear " + gear +
                        ", Cadence: " + cadence);
    }
}

public class BicycleMain {
    public static void main(String[] args) {

        Bicycle bike1 = new Bicycle();
        Bicycle bike2 = new Bicycle();
        bike1.speedUp(20);
        bike2.speedUp(20);
        bike1.changeGear(2);
        bike1.changeCadence(3);
        bike1.applyBrake(10);
        bike2.applyBrake(5);
        bike1.printStates();
        bike2.printStates();
    }
}
