from numpy import histogram, interp, cumsum, diff, array
import scipy.ndimage as scimg
import matplotlib.pyplot as plt

BINS = 255
IM_SIZE = 255

def histogramEqualization(f, bins=BINS):
	his, be = histogram(f, range=(0,255), bins=bins)
	# normalise to range [0, 255]
	hist = (his.astype('uint8') / sum(his)) * 255
	cdf = cumsum(hist)

	return lookupTable(cdf)[f.ravel()], his, be

def lookupTable(cdf):

	table = [0 for n in range(0, IM_SIZE)]

	for v in range(0, IM_SIZE):
		table[v] = (cdf[v] / IM_SIZE**2) * BINS
		print(table[v])

	table = array(table)

	return table

#Read figure and equalise
img = scimg.imread("cameraman1.png")
img_eq, his, be = histogramEqualization(img)

print(img_eq)

f, axarr = plt.subplots(2, 2)

#plot histogram of original data
axarr[1, 0].bar((be[:-1] + be[1:]) / 2, his, align='center', width=diff(be))
axarr[1, 0].set_xticks([be[x] for x in range(0, len(be), int(BINS/10))])

# Show original image
axarr[0, 0].imshow(img, cmap='gray')

#plot histogram of equalised image
his, be = histogram(img_eq, range=(0,255), bins=BINS)
axarr[1, 1].bar((be[:-1] + be[1:]) / 2, his, align='center', width=diff(be))
axarr[1, 1].set_xticks([be[x] for x in range(0, len(be), int(BINS/10))])

# Show equalised image
axarr[0, 1].imshow(img_eq, cmap='gray')

plt.show()