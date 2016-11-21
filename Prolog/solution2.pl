
% Finds the cost of a path
cost([edge(_, _, Cost)], Cost).
cost([edge(_, _, Cost)|Tp], TotalCost) :-
	cost(Tp, PrevCost),
	TotalCost is Cost + PrevCost.

% find all paths possible and sorts them, then puts the first 
% sorted item in Hsort.

% Could not get it to work :(


shortestPath(From, To, Hsort) :-
	findall(Paths, path(From, To, Paths), AllPaths),
	%[Hsort|TSort] = 
	sort(AllPaths, [Hsort|Tsort]).