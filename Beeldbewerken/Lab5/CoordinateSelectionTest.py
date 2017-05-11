import matplotlib.pyplot as plt
import numpy as np

# Plot two images next to each other

f1 = plt.imread('nachtwacht1.jpg')/255
plt.subplot(121) 
plt.imshow(f1)
plt.axis('off')

f2 = plt.imread('nachtwacht2.jpg')/255
plt.subplot(122)
plt.imshow(f2)
plt.axis('off')

plt.show()

# Select 6 points in the left image
xy = np.array([[ 157, 32],
               [ 211, 37],
               [ 222,107],
               [ 147,124]])


# Points in right image:
xaya = np.array([[  6, 38],
                 [ 56, 31],
                 [ 82, 87],
                 [ 22,118]])


plt.subplot(121); plt.imshow(f1); plt.axis('off');
plt.scatter(xy[:,0], xy[:,1], marker='x');
plt.subplot(122); plt.imshow(f2); plt.axis('off');
plt.scatter(xaya[:,0], xaya[:,1], marker='x');
plt.show()


P = perspectiveTransform(xy.astype(np.float32), xaya.astype(np.float32))

f_stitched = warp(f2, P, output_shape=(300,450))

M, N = f1.shape[:2]

f_stitched[0:M, 0:N, :] = f1

plt.imshow(f_stitched)
plt.axis('off')
plt.show()