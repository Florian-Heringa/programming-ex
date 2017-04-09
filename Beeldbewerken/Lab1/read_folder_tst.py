from os import listdir, getcwd
from os.path import isfile, join
import numpy as np
from scipy.ndimage import imread
import skimage.color as skc

REL_PATH_ORG = 'SkinColor/FacePhoto/'
REL_PATH_MASK = 'SkinColor/GroundT_FacePhoto/'

# Reads image data from current directory (image data should be saved here)
def read_image_folder():
    
    mypath = getcwd()
    original = mypath + '/' + REL_PATH_ORG
    # Extract just 'filename.' so extensions can easily be changed
    original_list = [f[:-3] for f in listdir(original)]
    original_list.sort()
    
    data_vector = np.empty([0, 0])
    target_vector = np.empty([0, 0])

    colors = []
    targets = []
    
    for file in original_list:
        print(REL_PATH_ORG + file + 'jpg')
        f = imread(REL_PATH_ORG + file + 'jpg')
        m = imread(REL_PATH_MASK + file + 'png')
        
        # Convert image to lab color convention taking only the ab dimensions
        # and every 100th element to reduce sheer amount of data
        f_lab = skc.rgb2lab(f)[::100, ::, 1::]

        # Extract skin- and non-skin colors from image data
        sc_lab = f_lab[m[:,:,0]==255]
        nsc_lab = f_lab[m[:,:,0]==0]
        
        color = np.vstack((sc_lab, nsc_lab))
        target = np.concatenate((np.ones(len(sc_lab)),np.zeros(len(nsc_lab))))
        
        colors.append(color)
        targets.append(targets)
        
    colors = np.concatenate(colors)
    targets = np.concatenate(targets)
    
    return colors, targets


clrs, trgts = read_image_folder()