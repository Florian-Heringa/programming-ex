:- use_module(library(clpfd)).

my_append([], Y, Y).
my_append([Hx|Tx], Y, [Hx|Tz]) :- my_append(Tx, Y, Tz).

palindrome([]).
palindrome([Hx|Tx]) :- reverse(Tx, [Hx|Tnext]), palindrome(Tnext).

sudoku42(Puzzle, Solution) :-
	Solution = Puzzle,

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

	correctSolution([Row1, Row2, Row3, Row4, Col1, Col2, Col3 , Col4]).


correctSolution([]).
correctSolution([[]|Ts]) :- correctSolution(Ts).
correctSolution([[Hr|Tr]|Ts]) :-
	Vals = [1,2,3,4],			% define domain of sudoku
	member(Hr, Vals),			% check if the members of rows are in domain
	not(member(Hr, Tr)),		% Check if members are the only ones in row/column
	append([Tr], Ts, Tnext),	% redefine the next list to use
	correctSolution(Tnext).		% Into recursion
	




sudoku4(Puzzle, Solution) :-
  Solution = Puzzle,
  
  Puzzle = [S11, S12, S13, S14,
            S21, S22, S23, S24,
            S31, S32, S33, S34,
            S41, S42, S43, S44],
  
  fd_domain(Solution, 1, 4),
  
  Row1 = [S11, S12, S13, S14],
  Row2 = [S21, S22, S23, S24],
  Row3 = [S31, S32, S33, S34],
  Row4 = [S41, S42, S43, S44],      
  
  Col1 = [S11, S21, S31, S41],
  Col2 = [S12, S22, S32, S42],
  Col3 = [S13, S23, S33, S43],
  Col4 = [S14, S24, S34, S44],      
  
  Square1 = [S11, S12, S21, S22],
  Square2 = [S13, S14, S23, S24],
  Square3 = [S31, S32, S41, S42],
  Square4 = [S33, S34, S43, S44],      
  
  valid([Row1, Row2, Row3, Row4,
         Col1, Col2, Col3, Col4,
         Square1, Square2, Square3, Square4]).
 
valid([]).
valid([Head | Tail]) :- fd_all_different(Head), valid(Tail).



















pattern([1,2,3,4]).
pattern([1,2,4,3]).
pattern([1,3,2,4]).
pattern([1,3,4,2]).
pattern([1,4,2,3]).
pattern([1,4,3,2]).

pattern([2,1,3,4]).
pattern([2,1,4,3]).
pattern([2,3,1,4]).
pattern([2,3,4,1]).
pattern([2,4,1,3]).
pattern([2,4,3,1]).

pattern([3,1,2,4]).
pattern([3,1,4,2]).
pattern([3,2,1,4]).
pattern([3,2,4,1]).
pattern([3,4,1,2]).
pattern([3,4,2,1]).

pattern([4,1,2,3]).
pattern([4,1,3,2]).
pattern([4,2,1,3]).
pattern([4,2,3,1]).
pattern([4,3,1,2]).
pattern([4,3,2,1]).

sudoku([[x1, x2, x3, x4], [x5, x6, x7, x8], [x9, x10, x11, x12], [x13, x14, x15, x16]], [[x1, x2, x3, x4], [x5, x6, x7, x8], [x9, x10, x11, x12], [x13, x14, x15, x16]]) :- 
	pattern([x1, x2, x3, x4]),
	pattern([x5, x6, x7, x8]),
	pattern([x9, x10, x11, x12]),
	pattern([x13, x14, x15, x16]),
	pattern([x1, x5, x9, x13]),
	pattern([x2, x6, x10, x14]),
	pattern([x3, x7, x11, x15]),
	pattern([x4, x8, x12, x16]).