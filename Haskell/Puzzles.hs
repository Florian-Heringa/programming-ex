module Puzzles

where

	thrd :: (a, b, c) -> c
	thrd (a, b, c) = c

	test :: [a] -> a
	test (x:xs) = x

	addone :: Num a => t -> a -> a
	addone _ n = n + 1

	length' :: [a] -> Integer
	length' = foldr addone 0 

	or' :: [Bool] -> Bool
	or' = foldr (\a b -> a || b) False

	myOr :: [Bool] -> Bool
	myOr (x:xs)
		| (xs==[False]) = False
		| (x==True)     = True
		| (x/=True)     = myOr xs

	elem' :: Eq a => a -> [a] -> Bool
	elem' el lst = foldr (\a b -> (el == a) || b) False lst
	
	map' :: (a -> b) -> [a] -> [b]
	map' f = foldr (\element acc -> f element : acc) []

	append' :: [a] -> [a] -> [a]
	append' lst1 lst2 = foldr (\l1 l2 -> l1 : l2) lst1 lst2

	reverse' :: [a] -> [a]
	reverse' = foldr (\el acc -> acc ++ [el]) []

	reverse'' :: [a] -> [a]
	reverse'' = foldl (\acc el -> el : acc) []

	--index' :: [a] -> Int -> a
	--index' lst ind = foldl indexF

	isPalindrome :: Eq a => [a] -> Bool
	isPalindrome lst = (lst == (reverse' lst))

	giveMeFibs :: (Enum a, Num a) => Int -> [a]
	giveMeFibs num = take num (fibsScanl)

	fibsScanl :: (Num a) => [a]
	fibsScanl = scanl (\acc el -> acc + el) 1 ([0] ++ fibsScanl)
