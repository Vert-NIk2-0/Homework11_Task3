import java.util.LinkedList;
import java.util.Queue;

public class ThreadClass {
    private static final short LIMIT = 10;
    private static Queue<Integer> queue = new LinkedList<>();
    private static volatile int counter = 0;

    public static void main(String[] args) {
        ThreadClass tc = new ThreadClass();
        Thread thread1 = new Thread(tc::produce);
        Thread thread2 = new Thread(tc::consume);

        thread1.start();
        thread2.start();
    }

    public void produce() {
        while (true) {
            synchronized (this) {
                while (queue.size() == LIMIT) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                queue.add(++counter);
                System.out.println("+ " + counter);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                notify();
            }
        }
    }

    public void consume() {
        while (true) {
            synchronized (this) {
                while (queue.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("- " + queue.poll());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                counter--;
                notify();
            }
        }
    }
}
