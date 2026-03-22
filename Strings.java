public class Strings {
    public static void main(String[] args) {
        System.out.print("test 1%");
        try {
            Thread.sleep(3000);
            System.out.print("\b\b\b\b\b\b\btest 3%");
            Thread.sleep(500);
            System.out.print("\b\b");
            System.out.print("6%");
            Thread.sleep(500);
            System.out.print("\b");
            Thread.sleep(500);
            System.out.print("\b ");
            Thread.sleep(500);
            System.out.print("\b ");
            Thread.sleep(500);
            System.out.print("\b\btest 5% ");
        } catch (InterruptedException e) {
            if (Thread.interrupted()){
            System.err.format("IO exception %s%n", e);
        }}
    }
}
