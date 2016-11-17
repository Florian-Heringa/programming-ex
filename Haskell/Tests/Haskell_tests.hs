module Haskell_tests

where

	thrd :: (a, b, c) -> c
	thrd (a, b, c) = c

	test :: [a] -> a
	test (x:xs) = x

	addone :: Num a => t -> a -> a
	addone _ n = n + 1

	myOr :: [Bool] -> Bool
	myOr (x:xs)
		| (xs==[False]) = False
		| (x==True)     = True
		| (x/=True)     = myOr xs
