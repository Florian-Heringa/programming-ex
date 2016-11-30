import sys
from math import sqrt

def readInts(filename):
    """
    In => A filename of a file containing lines of integers.
    Out => A list of lists. Every inner list containing one line of integers.
    """

    listOfInts = []
    lineList = []

    with open(filename) as f:

        # Read first line
        line = f.readline()

        # Loop until next line is NONE
        while line:

            lineList = [int(integer) for integer in line.split(" ")]

            # Add line to database, reset lineList
            listOfInts.append(lineList)
            lineList = []

            # Read next line
            line = f.readline()

    return listOfInts

def printGrid(grid):

    rowString = ""

    for row in grid:
        for num in row:

            rowString += "{} ".format(num)

        print(rowString)
        rowString = ""

# finds the transpose of a matrix
def transpose(grid):

    return [[row[i] for row in grid] for i in range(len(grid))]

# finds empty spaces (0) in a sudoku grid
def findEmpty(grid):

    n = len(grid)
    openSpots = [[i, j] for i in range(n) for j in range(n) if grid[i][j] == 0]

    return openSpots

# finds possible numbers for an open spot in the grid
def posNums(row, col, block):

    n = len(row) + 1
    possibilities = range(1, n)
    ans = []
    for pos in possibilities:
        if pos in row or pos in col or pos in block:
            continue
        else:
            ans.append(pos)

    return ans

def toBlock(grid):
    """ 
    In => an nxn sudoku grid
    Out => the grid with the lines represented in blocks (or vice versa)
    """

    n = len(grid[0])
    blocksize = int(sqrt(n))

    gridBlock = [[grid[i + s][j + t] for i in range(blocksize) for j in range(blocksize)]
                 for s in range(0, n, blocksize) for t in range(0, n, blocksize)]

    return gridBlock

def findSol2(grid, gridTr, block):

    openSpots = findEmpty(grid)
    tempgrid = grid
    lenList = []

    for spot in openSpots:
        # current position in grid
        n, m = spot
        row = grid[n]
        col = gridTr[m]
        # list of possible solutions for the chosen position
        lenList.append(len(posNums(row, col, block)))

    print(lenList)

   # while len(currentTree) != len(openSpots):





# Finds solution by iteratively trying out all possibilities per cell
def findSol(grid, gridTr, block):

    openSpots = findEmpty(grid)

    while len(openSpots) > 0:

        for spot in openSpots:
            # current position in grid
            n, m = spot
            row = grid[n]
            col = gridTr[m]
            # list of possible solutions for the chosen position
            posSol = posNums(row, col, block)
            # If there is only one possibility, add into the grid
            if len(posSol) == 1:

                grid[n][m] = posSol[0]
                openSpots.remove(spot)
            else:
                return 0


#########################################################

#Read filename from the terminal
fileName = sys.argv[1]

#MAke relevant grid variables
grid = readInts(fileName)
gridTr = transpose(grid)
gridBlock = toBlock(grid)
print("Welcome to sudoku solver, you entered the following grid:")
printGrid(grid)
print("\n")
openSpots = findEmpty(grid)
print("There are {} open spots:".format(len(openSpots)))
print(openSpots)

print("\nThe solution is\n")
findSol2(grid, gridTr, gridBlock)
printGrid(grid)
