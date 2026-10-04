package org.example.benchmark;

import org.example.ds.DynamicArray;
import org.example.ds.MyLinkedList;
import org.example.ds.MinHeap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Locale;
import java.io.Writer;


public class BenchmarkRunner {
    private static final int SEED = 42;
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 5;

    public static void main(String[] args) throws IOException {
        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                // warm-up: результаты выбрасываются
                for (int i = 0; i < 5; i++) {
                    PrintWriter dummy = new PrintWriter(Writer.nullWriter());
                    runW1(dummy, n);
                    runW2(dummy, n);
                    runW3(dummy, n, "head");
                    runW3(dummy, n, "middle");
                    runW4(dummy, n);
                }
                // настоящий замер: идёт в CSV
                runW1(writer, n);
                runW2(writer, n);
                runW3(writer, n, "head");
                runW3(writer, n, "middle");
                runW4(writer, n);
            }
        }
        System.out.println("Benchmark Success results/results.csv");
    }

    private static void runW1(PrintWriter writer, int n) {
        double[] timesDA = new double[RUNS];
        long stepsDA = 0, movesDA = 0, compsDA = 0;

        for (int r = 0; r < RUNS; r++) {
            Random rand = new Random(SEED);
            DynamicArray da = new DynamicArray();
            for (int i = 0; i < n; i++) da.add(rand.nextInt());
            da.counter.reset();

            long start = System.nanoTime();
            for (int i = 0; i < 10000; i++) da.get(rand.nextInt(n));
            timesDA[r] = (System.nanoTime() - start) / 1e6;
            if (r == RUNS - 1) { stepsDA = da.counter.steps; movesDA = da.counter.moves; compsDA = da.counter.comparisons; }
        }
        writeCsv(writer, "W1", "-", "DynamicArray", n, getMedian(timesDA), stepsDA, movesDA, compsDA);

        double[] timesLL = new double[RUNS];
        long stepsLL = 0, movesLL = 0, compsLL = 0;
        for (int r = 0; r < RUNS; r++) {
            Random rand = new Random(SEED);
            MyLinkedList list = new MyLinkedList();
            for (int i = 0; i < n; i++) list.add(rand.nextInt());
            list.counter.reset();

            long start = System.nanoTime();
            for (int i = 0; i < 10000; i++) list.get(rand.nextInt(n));
            timesLL[r] = (System.nanoTime() - start) / 1e6;
            if (r == RUNS - 1) { stepsLL = list.counter.steps; movesLL = list.counter.moves; compsLL = list.counter.comparisons; }
        }
        writeCsv(writer, "W1", "-", "MyLinkedList", n, getMedian(timesLL), stepsLL, movesLL, compsLL);
    }

    private static void runW2(PrintWriter writer, int n) {
        double[] timesDA = new double[RUNS];
        long stepsDA = 0, movesDA = 0, compsDA = 0;

        for (int r = 0; r < RUNS; r++) {
            Random rand = new Random(SEED);
            DynamicArray da = new DynamicArray();
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = rand.nextInt();
                da.add(values[i]);
            }
            da.counter.reset();

            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                int query = (i % 2 == 0) ? values[rand.nextInt(n)] : rand.nextInt();
                da.contains(query);
            }
            timesDA[r] = (System.nanoTime() - start) / 1e6;
            if (r == RUNS - 1) { stepsDA = da.counter.steps; movesDA = da.counter.moves; compsDA = da.counter.comparisons; }
        }
        writeCsv(writer, "W2", "-", "DynamicArray", n, getMedian(timesDA), stepsDA, movesDA, compsDA);

        double[] timesLL = new double[RUNS];
        long stepsLL = 0, movesLL = 0, compsLL = 0;
        for (int r = 0; r < RUNS; r++) {
            Random rand = new Random(SEED);
            MyLinkedList list = new MyLinkedList();
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = rand.nextInt();
                list.add(values[i]);
            }
            list.counter.reset();

            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                int query = (i % 2 == 0) ? values[rand.nextInt(n)] : rand.nextInt();
                list.contains(query);
            }
            timesLL[r] = (System.nanoTime() - start) / 1e6;
            if (r == RUNS - 1) { stepsLL = list.counter.steps; movesLL = list.counter.moves; compsLL = list.counter.comparisons; }
        }
        writeCsv(writer, "W2", "-", "MyLinkedList", n, getMedian(timesLL), stepsLL, movesLL, compsLL);
    }

    private static void runW3(PrintWriter writer, int n, String variant) {
        double[] timesDA = new double[RUNS];
        int targetIdx = variant.equals("head") ? 0 : n / 2;
        long stepsDA = 0, movesDA = 0, compsDA = 0;

        for (int r = 0; r < RUNS; r++) {
            Random rand = new Random(SEED);
            DynamicArray da = new DynamicArray();
            for (int i = 0; i < n; i++) da.add(rand.nextInt());
            da.counter.reset();

            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) da.add(targetIdx, 999);
            for (int i = 0; i < 1000; i++) da.remove(targetIdx);
            timesDA[r] = (System.nanoTime() - start) / 1e6;
            if (r == RUNS - 1) { stepsDA = da.counter.steps; movesDA = da.counter.moves; compsDA = da.counter.comparisons; }
        }
        writeCsv(writer, "W3", variant, "DynamicArray", n, getMedian(timesDA), stepsDA, movesDA, compsDA);

        double[] timesLL = new double[RUNS];
        long stepsLL = 0, movesLL = 0, compsLL = 0;
        for (int r = 0; r < RUNS; r++) {
            Random rand = new Random(SEED);
            MyLinkedList list = new MyLinkedList();
            for (int i = 0; i < n; i++) list.add(rand.nextInt());
            list.counter.reset();

            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) list.add(targetIdx, 999);
            for (int i = 0; i < 1000; i++) list.remove(targetIdx);
            timesLL[r] = (System.nanoTime() - start) / 1e6;
            if (r == RUNS - 1) { stepsLL = list.counter.steps; movesLL = list.counter.moves; compsLL = list.counter.comparisons; }
        }
        writeCsv(writer, "W3", variant, "MyLinkedList", n, getMedian(timesLL), stepsLL, movesLL, compsLL);
    }

    private static void runW4(PrintWriter writer, int n) {
        double[] times = new double[RUNS];
        long steps = 0, moves = 0, comps = 0;

        for (int r = 0; r < RUNS; r++) {
            Random rand = new Random(SEED);
            MinHeap heap = new MinHeap();
            for (int i = 0; i < n; i++) heap.insert(rand.nextInt());
            heap.counter.reset();

            long start = System.nanoTime();
            int prev = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int curr = heap.extractMin();
                if (curr < prev) throw new AssertionError("Heap violation!");
                prev = curr;
            }
            times[r] = (System.nanoTime() - start) / 1e6;
            if (r == RUNS - 1) { steps = heap.counter.steps; moves = heap.counter.moves; comps = heap.counter.comparisons; }
        }
        writeCsv(writer, "W4", "-", "MinHeap", n, getMedian(times), steps, moves, comps);
    }

    private static double getMedian(double[] arr) {
        Arrays.sort(arr);
        return arr[arr.length / 2];
    }

    private static void writeCsv(PrintWriter w, String wl, String var, String ds, int n, double time, long s, long m, long c) {
        w.printf(Locale.US, "%s,%s,%s,%d,%.4f,%d,%d,%d%n", wl, var, ds, n, time, s, m, c);
    }
}