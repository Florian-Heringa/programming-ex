from numpy import histogram, interp, cumsum, diff
import matplotlib.image as mpimg
import matplotlib.pyplot as plt

BINS = 100

def histogramEqualization(f, bins=BINS):
	his, be = histogram(f, range=(0,1), bins=bins)
	hist = his.astype(float)/sum(his)
	return interp(f, be[1:], cumsum(hist)), his, be


#Read figure and equalise
trui = mpimg.imread("trui.png")
trui_eq, his, be = histogramEqualization(trui)

f, axarr = plt.subplots(2, 2)

#plot histogram of original data
axarr[1, 0].bar((be[:-1] + be[1:]) / 2, his, align='center', width=diff(be))
axarr[1, 0].set_xticks([be[x] for x in range(0, len(be), int(BINS/10))])

# Show original image
axarr[0, 0].imshow(trui)

#plot histogram of equalised image
his, be = histogram(trui_eq, range=(0,1), bins=BINS)
axarr[1, 1].bar((be[:-1] + be[1:]) / 2, his, align='center', width=diff(be))
axarr[1, 1].set_xticks([be[x] for x in range(0, len(be), int(BINS/10))])

# Show equalised image
axarr[0, 1].imshow(trui_eq)

plt.show()

