"""
Naam: Niek Kabel
Student ID: 11031174
Beschrijving:
"""

import fileinput
from pprint import pprint


if __name__ == '__main__':
    score = []
    for line in fileinput.input():
        (element, rank) = line.split()
        score.append((element, int(rank)))

    score.sort(key=lambda (element, rank): rank, reverse=True)
    highscore = score[:10]
    
    pprint(highscore)
