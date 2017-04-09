# Full library imports
import matplotlib.pyplot as plt
import numpy as np
import random
# Selective library imports
from scipy.ndimage import imread
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm

# Amount of samples taken in plotting 3D scatter
SAMPLES = 500

# Randomly takes n points from a numpy array
def sample(ar, n):

	i = 0
	sample = 0
	# Create empty holder array
	samples = np.zeros(n)

	while i < len(ar) and sample < n:

		# 50% chance to select item, not critical in this
		# implementation (just used for speeding up program)
		if random.random() > .5:

			samples[sample] = ar[i]
			sample += 1

		i += 1

	return samples

#Read images
f = imread('SkinColor/FacePhoto/0520962400.jpg')
m = imread('SkinColor/GroundT_FacePhoto/0520962400.png')

# Calculations
skincolors = f[m[:,:,0]==255]
print(skincolors.shape)
nonskincolors = f[m[:,:,0]==0]
print(nonskincolors.shape)

# Take a selection of SAMPLES points to plot in 3D plot for
# both skin- and non-skin colors
x, y, z = np.split(skincolors, 3, 1)
x = sample(x, SAMPLES)
y = sample(y, SAMPLES)
z = sample(z, SAMPLES)

t, u, v = np.split(nonskincolors, 3, 1)
t = sample(x, SAMPLES)
u = sample(y, SAMPLES)
v = sample(z, SAMPLES)

# 3D scatter plot of randomly selected points
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.scatter(x, y, z, s=30, c='green')
ax.scatter(t, u, v, s=30, c='red')
ax.set_xlabel('R')
ax.set_ylabel('G')
ax.set_zlabel('B')
plt.show()

# Plotting image and its skin color selector
plt.subplot(121)
plt.imshow(f)

plt.subplot(122)
plt.imshow(m)

plt.show()