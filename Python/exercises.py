

def checkSeq(l):
    """
    In => List of integers of length n
    Out => Checks if all integers from 1 to n are included in the list
    Gives the numbers that are missing and the numbers that are extra
    (outside of the range)
    """

    missing = [x for x in range(1,len(l)+1) if x not in l]
    extra = [x for x in l if x not in range(1, len(l)+1)]

    print("The numbers missing are: [%s]" % ', '.join(str(x) for x in missing))
    print("The extra numbers are: [%s]" % ', '.join(str(x) for x in extra))

def genMissing(L):
    """
    Generator for numbers missing from a list.
    In => List of integers of length n
    Out => All integers missing from the range [1, n]
    """

    for i in range(1, len(L) + 1):
        if i not in L:
            yield i

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

def printListOfInts(listIn):

    lineStr = ""

    for line in listIn:
        for ints in line:

            lineStr += "{} ".format(ints)

        print(lineStr)
        lineStr = ""

## Variables
inpList = [1,2,3,4,5,6]
inpList2 = [1,2,4,5,6]
inpList3 = [3,2,1,6,4,8,6]

fileName = "testInt.txt"
fileName2 = "sudoku_test.txt"

print("\nTest sequences\n")
checkSeq(inpList)
checkSeq(inpList2)
checkSeq(inpList3)

print("\nTest generator\n")
gen = genMissing(inpList3)

for generated in gen:
    print(generated)

print("\nTesting the to- and from-list functions\n")
listOfInts = readInts(fileName)
print(listOfInts)
printListOfInts(listOfInts)

print("\nTesting with a 4x4 sudoku-like grid\n")
sudoku_test = readInts(fileName2)
print(sudoku_test)
printListOfInts(sudoku_test)
