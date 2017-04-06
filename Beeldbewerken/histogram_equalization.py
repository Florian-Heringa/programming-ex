from numpy import histogram, interp, cumsum, diff
import matplotlib.image as mpimg
import matplotlib.pyplot as plt

BINS = 100

def histogramEqualization(f, bins=BINS):
	his, be = histogram(f, range=(0,1), bins=bins)

	#plot
	#hist, bins = np.histogram(x, bins=bins)
	# width = diff(be)
	# center = (be[:-1] + be[1:]) / 2

	# fig, ax = plt.subplots(figsize=(8,3))
	# ax.bar(center, his, align='center', width=width)
	# ax.set_xticks(be)
	# plt.show()

	hist = his.astype(float)/sum(his)
	return interp(f, be[1:], cumsum(hist)), his, be


#Read figure and equalise
trui = mpimg.imread("trui.png")
trui_eq, his, be = histogramEqualization(trui)

f, axarr = plt.subplots(2, 2)

#plot histogram of original data
axarr[1, 0].bar((be[:-1] + be[1:]) / 2, his, align='center', width=diff(be))
axarr[1, 0].set_xticks(be)

axarr[0, 0].imshow(trui)

#plot histogram of equalised image
his, be = histogram(trui_eq, range=(0,1), bins=BINS)
axarr[1, 1].bar((be[:-1] + be[1:]) / 2, his, align='center', width=diff(be))
axarr[1, 1].set_xticks(be)

axarr[0, 1].imshow(trui_eq)

plt.show()











# plt.figure("final")
# f, axarr = plt.subplots(2, 2)

# axarr[1, 0].hist(trui.ravel(), bins=BINS, range=(0.0, 1.0), fc='k', ec='k')
# axarr[0, 0].imshow(trui, cmap="gray")


# plt.figure("final")
# axarr[1, 1].hist(trui_eq.ravel(), bins=BINS, range=(0.0, 1.0), fc='k', ec='k')
# axarr[0, 1].imshow(trui_eq, cmap="gray")
# plt.show()