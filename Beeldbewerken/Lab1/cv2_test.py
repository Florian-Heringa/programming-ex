import cv2
import numpy as np
import matplotlib.pyplot as plt

x = np.arange(128)-64
X, Y = np.meshgrid(x,x)
f = (100*(np.hypot(X,Y)<32)).astype(np.uint8)
plt.clf()
plt.imshow(f);
plt.gray()
sift = cv2.xfeatures2d.SIFT_create()
kps, dscs = sift.detectAndCompute(f, mask=None)
ax = plt.gca()

for kp in kps:
	ax.add_artist( plt.Circle((kp.pt), kp.size/2, color='green', fill=False))

plt.show()

# conda install -c https://conda.binstar.org/menpo opencv
# florian-ubuntu@ubuntu:~/programming-ex/Beeldbewerken$ conda install -c https://conda.binstar.org/menpo opencv
# Fetching package metadata ...........
# Solving package specifications: .


# UnsatisfiableError: The following specifications were found to be in conflict:
#   - opencv -> numpy 1.9* -> python 2.6* -> openssl 1.0.1*
#   - python 3.6*
# Use "conda info <package>" to see the dependencies for each package.



# conda install -c menpo opencv=2.4.11
# 
# florian-ubuntu@ubuntu:~/programming-ex/Beeldbewerken$ conda install -c menpo opencv=2.4.11
# Fetching package metadata ...........
# Solving package specifications: .


# UnsatisfiableError: The following specifications were found to be in conflict:
#   - opencv 2.4.11* -> numpy 1.9* -> python 2.6* -> openssl 1.0.1*
#   - python 3.6*
# Use "conda info <package>" to see the dependencies for each package.

