import os
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

# Создаем папку для сохранения графиков, если ее нет
os.makedirs("results/plots", exist_ok=True)

# Загружаем данные из CSV
df = pd.read_csv("results/results.csv")

# Настройка визуального стиля
plt.style.use('seaborn-v0_8-whitegrid' if 'seaborn-v0_8-whitegrid' in plt.style.available else 'default')

def format_log_axes():
    """Настройка логарифмического масштаба и степенного формата для осей X и Y"""
    ax = plt.gca()
    ax.set_xscale('log')
    ax.set_yscale('log')

    # Форматирование подписей осей в вид 10^k
    ax.xaxis.set_major_formatter(ticker.LogFormatterMathtext())
    ax.yaxis.set_major_formatter(ticker.LogFormatterMathtext())

def save_plot(filename):
    plt.tight_layout()
    plt.savefig(f"results/plots/{filename}", dpi=300)
    plt.close()
    print(f"График сохранен: results/plots/{filename}")

# --- 1. Workload 1 (W1): Random Access ---
w1_df = df[df['workload'] == 'W1']

plt.figure(figsize=(8, 5))
for struct in w1_df['structure'].unique():
    data = w1_df[w1_df['structure'] == struct]
    plt.plot(data['n'], data['time_ms'], marker='o', linewidth=2, label=struct)

format_log_axes()
plt.xlabel('n (Log Scale, $10^k$)')
plt.ylabel('Time in ms (Log Scale, $10^k$)')
plt.title('W1: Random Access - Execution Time vs n')
plt.legend()
save_plot('w1_time_vs_n.png')

plt.figure(figsize=(8, 5))
for struct in w1_df['structure'].unique():
    data = w1_df[w1_df['structure'] == struct]
    plt.plot(data['n'], data['steps'], marker='s', linewidth=2, label=f"{struct} Steps")

plt.gca().set_xscale('log')
plt.gca().set_yscale('log')
plt.gca().xaxis.set_major_formatter(ticker.LogFormatterMathtext())
plt.gca().yaxis.set_major_formatter(ticker.LogFormatterMathtext())

plt.xlabel('n (Log Scale, $10^k$)')
plt.ylabel('Steps Count (Log Scale, $10^k$)')
plt.title('W1: Random Access - Memory Steps vs n')
plt.legend()
save_plot('w1_steps_vs_n.png')

# --- 2. Workload 2 (W2): Search (contains) ---
w2_df = df[df['workload'] == 'W2']

plt.figure(figsize=(8, 5))
for struct in w2_df['structure'].unique():
    data = w2_df[w2_df['structure'] == struct]
    plt.plot(data['n'], data['time_ms'], marker='o', linewidth=2, label=struct)

format_log_axes()
plt.xlabel('n (Log Scale, $10^k$)')
plt.ylabel('Time in ms (Log Scale, $10^k$)')
plt.title('W2: Search (contains) - Execution Time vs n')
plt.legend()
save_plot('w2_time_vs_n.png')

# --- 3. Workload 3 (W3): Insert & Remove (Head vs Middle) ---
w3_df = df[df['workload'] == 'W3']

plt.figure(figsize=(8, 5))
for variant in ['head', 'middle']:
    for struct in ['DynamicArray', 'MyLinkedList']:
        data = w3_df[(w3_df['variant'] == variant) & (w3_df['structure'] == struct)]
        plt.plot(data['n'], data['time_ms'], marker='o', linewidth=2, label=f"{struct} ({variant})")

format_log_axes()
plt.xlabel('n (Log Scale, $10^k$)')
plt.ylabel('Time in ms (Log Scale, $10^k$)')
plt.title('W3: Insert & Remove - Time vs n (Head vs Middle)')
plt.legend()
save_plot('w3_time_vs_n.png')

# --- 4. Workload 4 (W4): MinHeap Priority Processing ---
w4_df = df[df['workload'] == 'W4']

fig, ax1 = plt.subplots(figsize=(8, 5))

color = 'tab:blue'
ax1.set_xscale('log')
ax1.set_yscale('log')
ax1.xaxis.set_major_formatter(ticker.LogFormatterMathtext())
ax1.yaxis.set_major_formatter(ticker.LogFormatterMathtext())

ax1.set_xlabel('n (Log Scale, $10^k$)')
ax1.set_ylabel('Time in ms (Log Scale, $10^k$)', color=color)
ax1.plot(w4_df['n'], w4_df['time_ms'], marker='o', color=color, linewidth=2, label='Time (ms)')
ax1.tick_params(axis='y', labelcolor=color)

ax2 = ax1.twinx()
ax2.set_yscale('log')
ax2.yaxis.set_major_formatter(ticker.LogFormatterMathtext())

color = 'tab:red'
ax2.set_ylabel('Comparisons Count (Log Scale, $10^k$)', color=color)
ax2.plot(w4_df['n'], w4_df['comparisons'], marker='^', color=color, linestyle='--', linewidth=2, label='Comparisons')
ax2.tick_params(axis='y', labelcolor=color)

plt.title('W4: MinHeap Operations & Scaling (Log-Log Scale)')
save_plot('w4_minheap_performance.png')

print("Все графики с логарифмической осью времени (10^k) сгенерированы в results/plots/")