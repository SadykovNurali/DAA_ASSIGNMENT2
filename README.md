# Assignment 2: In-Memory Workload Engine

**Course:** Design and Analysis of Algorithms  
**Student:** Sadykov Nurali (Group: SE-2509)  

---

##  Overview
This project implements transparent, low-level data structures from scratch using primitive `int` values without relying on `java.util` collections. It measures exact physical operation counts (`steps`, `moves`, `comparisons`) and execution times across four distinct workloads, analyzing performance disparities caused by CPU cache locality, spatial prefetching, and object overhead.

---

## 🛠 Project Structure
```text
DAA_Assignment2_Sadykov_Nurali_SE2509/
├── pom.xml
├── README.md
├── REPORT.md
├── results/
│   ├── results.csv
│   └── plots/
│       ├── w1_time_vs_n.png
│       ├── w1_steps_vs_n.png
│       ├── w2_time_vs_n.png
│       ├── w3_time_vs_n.png
│       └── w4_minheap_performance.png
└── src/
    ├── main/
    │   └── java/
    │       └── org/example/
    │           ├── metrics/
    │           │   └── OperationCounter.java
    │           ├── ds/
    │           │   ├── DynamicArray.java
    │           │   ├── MyLinkedList.java
    │           │   └── MinHeap.java
    │           └── benchmark/
    │               └── BenchmarkRunner.java
    └── test/
        └── java/
            └── org/example/
                ├── DynamicArrayTest.java
                ├── MyLinkedListTest.java
                └── MinHeapTest.java
```
How to Build and Run1. Compile and Run Unit TestsRun the test suite to verify structural correctness and invariants:Bashmvn clean test
2. Run BenchmarksExecute the benchmark suite to generate results/results.csv:Bashmvn exec:java -Dexec.mainClass="org.example.benchmark.BenchmarkRunner"
   Alternatively, run BenchmarkRunner.main() directly from IntelliJ IDEA.3. Generate Benchmark PlotsGenerate log-log scale plots ($10^k$) in results/plots/:Bashpython results/generate_plots.py