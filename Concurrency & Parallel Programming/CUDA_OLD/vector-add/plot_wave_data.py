import sys
import math
import matplotlib.pyplot as plt

#amount of points
n = int(sys.argv[1])
dx = (6 * math.pi) / n
x = 0

data = open("sin.txt", 'r')

for line in data:
	lst_x.append(int(line))
	lst_y.append(x)
	x += dx

plt.plot(lst_x, lst_y)
plt.show()

data.close()