import matplotlib.pyplot as plt

# Reading the data from the file
file_path = 'input.txt'  # Update this with the actual path to your file
with open(file_path, 'r') as file:
    lines = file.readlines()

# Extracting the sequence length and the sequence itself
sequence_length = int(lines[0].strip())
sequence = list(map(int, lines[1].strip().split()))

# Creating the line chart with sharper lines
plt.figure(figsize=(10, 6))
plt.plot(range(sequence_length), sequence, marker='o', linestyle='-', linewidth=0.5)
plt.title('Sequence Line Chart')
plt.xlabel('Index')
plt.ylabel('Value')
plt.grid(True)
plt.show()