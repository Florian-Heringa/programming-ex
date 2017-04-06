import scipy.ndimage as scimg
import matplotlib.pyplot as plt
import numpy as np

BINS = 100

IM_SIZE = 255

img = scimg.imread('cameraman1.png')
his, be = np.histogram(img, range=(0,255), bins=BINS)
cdf = np.cumsum(his)
print(his)
print(cdf)

table = np.interp(img, be[1:], cdf)

img_eq = table[img.ravel()]

plt.subplot(221)
plt.imshow(img, cmap='gray')

plt.subplot(222)
plt.bar((be[:-1] + be[1:]) / 2, his, width=np.diff(be), alpha=.5, color='blue')
plt.bar((be[:-1] + be[1:]) / 2, cdf, width=np.diff(be), alpha=.5, color='green')
plt.xticks(range(0, IM_SIZE + 1, 15))

plt.subplot(223)
plt.imshow(img_eq, cmap='gray')
plt.show()