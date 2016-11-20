edge(1, 2, 5).
edge(2, 1, 3).
edge(2, 3, 4).
edge(2, 4, 3).
edge(2, 5, 5).
edge(3, 1, 9).
edge(3, 2, 2).
edge(5, 1, 3).
edge(5, 4, 2).

path(From, To, Visited, [edge(From, To, Cost)|Visited]) :- 
	From =\= To,
	edge(From, To, Cost),! .

path(From, To, Visited, Path) :- 
	From =\= To,
	edge(From, Sub, Cost),
	path(Sub, To, [edge(From, Sub, Cost)|Visited], Path).

path(From, To, Path) :-
	path(From, To, [], RevPath),
	reverse(RevPath, Path).

connected(A, B, Cost) :- 
	edge(A, B, Cost).
connected(A, B, TotalCost) :- 
	edge(A, C, Cost1), 
	connected(C, B, PrevCost),
	TotalCost is PrevCost + Cost1.
	