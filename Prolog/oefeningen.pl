:- use_module(library(clpfd)).

my_append([], Y, Y).
my_append([Hx|Tx], Y, [Hx|Tz]) :- my_append(Tx, Y, Tz).

palindrome([]).
palindrome([Hx|Tx]) :- reverse(Tx, [Hx|Tnext]), palindrome(Tnext).

sudoku(Puzzle, Solution) :-

	Puzzle = [[X11, X12, X13, X14],
			  [X21, X22, X23, X24],
			  [X31, X32, X33, X34],
			  [X41, X42, X43, X44]],

	Row1 = [X11, X12, X13, X14],
	Row2 = [X21, X22, X23, X24],
	Row3 = [X31, X32, X33, X34],
	Row4 = [X41, X42, X43, X44],

	Col1 = [X11, X21, X31, X41],
	Col2 = [X12, X22, X32, X42],
	Col3 = [X13, X23, X33, X43],
	Col4 = [X14, X24, X34, X44], 

	Square1 = [X11, X12, X21. X22],
	Square2 = [X13, X14, X23, X24],
	Square3 = [X31, X32, X41, X42],
	Square4 = [X33, X34, X43, X44],

	correctSolution([Row1, Row2, Row3, Row4,
					 Col1, Col2, Col3 , Col4,
					 Square1, Square2, Square3, Square4]),

	Solution = Puzzle.


correctSolution([]).
correctSolution([[]|Ts]) :- correctSolution(Ts).
correctSolution([[Hr|Tr]|Ts]) :-
	Vals = [1,2,3,4],			% define domain of sudoku
	member(Hr, Vals),			% check if the members of rows are in domain
	not(member(Hr, Tr)),		% Check if members are the only ones in row/column
	append([Tr], Ts, Tnext),	% redefine the next list to use
	correctSolution(Tnext).		% Into recursion
