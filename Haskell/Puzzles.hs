module Puzzles

where

	-- length in terms of foldr
	length' :: [a] -> Integer
	length' = foldr (\_ n -> n + 1) 0 

	-- or in terms of foldr
	or' :: [Bool] -> Bool
	or' = foldr (||) False

	-- elem in terms of foldr
	elem' :: Eq a => a -> [a] -> Bool
	elem' el lst = foldr (\a b -> (el == a) || b) False lst
	
	-- map in terms of foldr
	map' :: (a -> b) -> [a] -> [b]
	map' f = foldr (\element acc -> f element : acc) []

	-- append in terms of foldr
	append' :: [a] -> [a] -> [a]
	append' = foldr (\l1 l2 -> l1 : l2)

	-- reverse in terms of foldr
	reverse' :: [a] -> [a]
	reverse' = foldr (\el acc -> acc ++ [el]) []

	-- reverse in terms of foldl
	reverse'' :: [a] -> [a]
	reverse'' = foldl (\acc el -> el : acc) []

	-- Index in terms of foldr, unfortunately the function just returns an empty list
	-- when given an index that is out of range. I tried to do error handling, but I could
	-- not get it to work.
	index' :: (Enum a, Eq a, Num a) => [a] -> a -> a
	index' lst ind = head $ foldl (indexF ind) [] $ zipWith (\n m -> [n, m]) [0,1..] lst

	indexF :: Eq a => a -> [a] -> [a] -> [a]
	indexF n sol (i:el)
		| n == i    = el ++ sol
		| otherwise = [] ++ sol


	-- Checks if a list is a palindrome by checking if it is equal to it's reverse
	isPalindrome :: Eq a => [a] -> Bool
	isPalindrome lst = (lst == (reverse' lst))

	-- gives the first n fibonacci numbers
	giveMeFibs :: (Enum a, Num a) => Int -> [a]
	giveMeFibs n = take n (fibsScanl)

	-- recursively generates an infinite list of fibonacci numbers
	fibsScanl :: (Num a) => [a]
	fibsScanl = scanl (\acc el -> acc + el) 1 ([0] ++ fibsScanl)
