import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Lab14_Pino {

    // =========================================================
    // EXERCISE 1a — Runnable interface
    // =========================================================
    static class SumRunnable implements Runnable {
        private int result;

        @Override
        public void run() {
            int sum = 0;
            for (int i = 1; i <= 100; i++) sum += i;
            result = sum;
        }

        public int getResult() { return result; }
    }

    static void lab1a() throws InterruptedException {
        System.out.println("=== 1a: Runnable ===");
        SumRunnable task = new SumRunnable();
        Thread thread = new Thread(task);
        thread.start();
        thread.join(); // main waits for calculation to finish
        System.out.println("Result (Runnable): " + task.getResult());
    }

    // =========================================================
    // EXERCISE 1b — Extending Thread
    // =========================================================
    static class SumThread extends Thread {
        private int result;

        @Override
        public void run() {
            int sum = 0;
            for (int i = 1; i <= 100; i++) sum += i;
            result = sum;
        }

        public int getResult() { return result; }
    }

    static void lab1b() throws InterruptedException {
        System.out.println("\n=== 1b: Thread subclass ===");
        SumThread thread = new SumThread();
        thread.start();
        thread.join(); // main waits for calculation to finish
        System.out.println("Result (Thread subclass): " + thread.getResult());
    }

    // =========================================================
    // EXERCISE 2 — Shared Counter
    // =========================================================
    static class SharedCounter {
        private int count = 0;
        private final ReentrantLock lock = new ReentrantLock();

        // (a) synchronized — guarantees atomic increments
        public synchronized void increment() { count++; }

        // Lock-based alternative (equivalent to synchronized here)
        public void lockIncrement() {
            lock.lock();
            try { count++; } finally { lock.unlock(); }
        }

        // (d) unsafe — no synchronization; race conditions expected
        public void unsafeIncrement() { count++; }

        public int getCount() { return count; }
        public void reset()   { count = 0; }
    }

    static void lab2() throws InterruptedException {
        SharedCounter sc = new SharedCounter();

        // --- (a) Synchronized, all threads joined ---
        System.out.println("\n=== 2a: Synchronized + join all ===");
        sc.reset();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) sc.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join(); // join AFTER starting all threads
        System.out.println("Final count (should be 10000): " + sc.getCount());

        // --- (b) Join each thread immediately after start (sequential) ---
        System.out.println("\n=== 2b: Join inside start loop (sequential) ===");
        sc.reset();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 1000; j++) sc.increment();
            });
            t.start();
            t.join(); // blocks until this one thread finishes before starting the next
        }
        System.out.println("Final count (should be 10000): " + sc.getCount());

        // --- (c) No join — main may print before threads finish ---
        System.out.println("\n=== 2c: No join (main doesn't wait) ===");
        sc.reset();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) sc.increment();
            }).start();
        }
        // No join — count is likely NOT 10000 when printed
        System.out.println("Final count (likely < 10000): " + sc.getCount());

        // --- (d) Unsafe increment — race condition ---
        System.out.println("\n=== 2d: Unsafe (no sync) — race condition ===");
        sc.reset();
        Thread[] unsafe = new Thread[10];
        for (int i = 0; i < 10; i++) {
            unsafe[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) sc.unsafeIncrement();
            });
            unsafe[i].start();
        }
        for (Thread t : unsafe) t.join();
        System.out.println("Final count (likely < 10000 due to races): " + sc.getCount());
    }

    // =========================================================
    // EXERCISE 3 — Sum of squares: single-thread vs multi-thread
    // =========================================================
    static class SquareSumTask extends Thread {
        private final int[] arr;
        private final int start, end;
        private long partialSum = 0;

        SquareSumTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                partialSum += (long) arr[i] * arr[i];
            }
        }

        public long getPartialSum() { return partialSum; }
    }

    static void lab3(int n) throws InterruptedException {
        System.out.println("\n=== 3: Sum of squares, n = " + n + " ===");

        int[] data = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) data[i] = rand.nextInt(100) + 1;

        // --- Single-threaded ---
        long start1 = System.nanoTime();
        long totalSingle = 0;
        for (int x : data) totalSingle += (long) x * x;
        long timeSingle = System.nanoTime() - start1;

        // --- Multi-threaded (2 threads) ---
        long start2 = System.nanoTime();
        SquareSumTask t1 = new SquareSumTask(data, 0, n / 2);
        SquareSumTask t2 = new SquareSumTask(data, n / 2, n);
        t1.start(); t2.start();
        t1.join();  t2.join();
        long totalThreaded = t1.getPartialSum() + t2.getPartialSum();
        long timeThreaded = System.nanoTime() - start2;

        System.out.printf("  Single-thread : %,15d ns  (sum=%d)%n", timeSingle,  totalSingle);
        System.out.printf("  Multi-thread  : %,15d ns  (sum=%d)%n", timeThreaded, totalThreaded);
    }

    // =========================================================
    // MAIN — runs all exercises
    // =========================================================
    public static void main(String[] args) throws InterruptedException {
        lab1a();
        lab1b();
        lab2();

        // Exercise 3: run for increasing n values
        // Pass a single n via args[0], or run the full sweep if no arg given
        if (args.length > 0) {
            lab3(Integer.parseInt(args[0]));
        } else {
            int[] sizes = {100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000};
            for (int n : sizes) lab3(n);
        }
    }
}