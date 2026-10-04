# Assignment 2: In-Memory Workload Engine
Sadykov Nurali, group SE-2509. Repository: https://github.com/SadykovNurali/daa_assignment2 (branch main, tag v1.0).

## 1. Implementation summary
- **DynamicArray**: `int[]`, initial capacity 10, grows 2x when full.
- **MyLinkedList**: Doubly-linked list of nodes holding a primitive `int` value. The counters show that `get(i)` costs about $n/4$ hops on average (255,745 steps for 10,000 random gets at $n = 100$, 2,497,236 at $n = 1,000$), confirming that node traversal starts from the nearer end (`head` or `tail`).
- **MinHeap**: Array-based binary min-heap with bubble-up (`insert`) and bubble-down (`extractMin`).
- **Counters**, incremented inside the methods:
    - **steps**: one read of an array cell or one move to the next node;
    - **moves**: one element shifted in the array or one pointer update in the list;
    - **comparisons**: one comparison of two elements.
- **Invalid index**: `IndexOutOfBoundsException`. `peekMin` / `extractMin` on an empty heap: `IllegalStateException`.

---

## 2. Complexity table
$\Theta$ is used where the best and worst bounds coincide. Otherwise $\Omega$ gives the lower and $O$ the upper bound. $n$ is the current size, $h = \lfloor\log_2 n\rfloor$ is the heap height.

| Structure | Operation | Best | Average | Worst | Justification |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DynamicArray** | `get(i)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | direct index access |
| | `add(x)` | $\Theta(1)$ | $\Theta(1)$ amortized | $O(n)$ | the copy happens only when full; total copying for $n$ adds is $1+2+4+\dots+n < 2n$ |
| | `add(i, x)` | $\Omega(1)$ ($i = \text{size}$) | $\Theta(n)$ | $O(n)$ ($i = 0$) | shifts $\text{size} - i$ elements |
| | `remove(i)` | $\Omega(1)$ (last) | $\Theta(n)$ | $O(n)$ ($i = 0$) | shifts $\text{size} - i - 1$ elements |
| | `contains(x)` | $\Omega(1)$ (first) | $\Theta(n)$ | $O(n)$ | linear scan, absent value reads all $n$ cells |
| | *Auxiliary Space* | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | capacity up to $2n$; the grow step needs a second array of $2n$ |
| **MyLinkedList** | `get(i)` | $\Omega(1)$ (first/last) | $\Theta(n)$ | $O(n)$ (middle) | walks from the nearer end, average $n/4$ hops; no random access |
| | `add(x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | constant time append via tail pointer |
| | `add(i, x)` | $\Omega(1)$ (head) | $\Theta(n)$ | $O(n)$ | find node $i - 1$, then 2 pointer updates |
| | `remove(i)` | $\Omega(1)$ (head) | $\Theta(n)$ | $O(n)$ | find node $i - 1$, then 1 pointer update |
| | `contains(x)` | $\Omega(1)$ (first) | $\Theta(n)$ | $O(n)$ | linear traversal |
| | *Auxiliary Space* | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | one node object per element (header + value + references) |
| **MinHeap** | `peekMin()` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | returns `a[0]` |
| | `insert(x)` | $\Omega(1)$ | $O(1)$ on random data | $O(\log n)$ | bubble-up climbs at most $h$ levels; expected climb is constant |
| | `extractMin()` | $\Omega(1)$ (all equal) | $\Theta(\log n)$ | $\Theta(\log n)$ | moved last element usually sinks to the bottom, at most $h$ levels |
| | *Auxiliary Space* | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | `int[]` array with 2x growth strategy |

---

## 3. Loop invariant proofs

### 3.1 `DynamicArray.contains(x)`
```java
for (int i = 0; i < size; i++) {
    if (a[i] == x) return true;
}
return false;