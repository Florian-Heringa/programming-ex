module MM

where
	import Data.List
	import Data.Function (on)
	import Control.Monad (replicateM)

	data Color = Red | Yellow | Blue | Green | Orange | Purple
				deriving (Eq,Show,Bounded,Enum)

	type Pattern = [Color]
					-- white, black
	type Feedback = (Int, Int)

	reaction :: Pattern -> Pattern -> Feedback
	reaction q z = ((almost q z), (correct q z))

	fromBool :: Bool -> Int
	fromBool bool 
		| (bool == True)  = 1
		| (bool == False) = 0
		| otherwise 	  = 0

	-- Black
	correct :: Pattern -> Pattern -> Int
	correct p1 p2 = 5
	--correct p1 (p:ps) = foldr (\a b -> fromBool b) (scanl (\q r -> q == r) p p1) p1

	-- White
	almost :: Pattern -> Pattern -> Int
	almost p1 p2 = 5