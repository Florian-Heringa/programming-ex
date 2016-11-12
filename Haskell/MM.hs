module MM

where
	import Data.List
	import Data.Function (on)
	import Control.Monad (replicateM)

	-- This program is an implementation of mastermind, first, functions were created to
	-- give feedback on choices of input. Then a naive algorithm was added to try to crack the
	-- code. Finally, a benchmarking function was added to evaluate the average amount of
	-- steps taken to crack the code.

	data Color = Red | Yellow | Blue | Green | Orange | Purple
				deriving (Eq,Show,Bounded,Enum)

	type Pattern = [Color]
					-- white, black
	type Feedback = (Int, Int)

	-- Finds a reaction to two patterns in the form (white, black) where shite means correct color, incorrect position
	-- and black means correct color and correct position.
	---- pattern1 -> pattern2 -> how many correct
	reaction :: Pattern -> Pattern -> Feedback
	reaction q z = ((white q z), (black q z))

	-- Converts a boolena variable to an integer
	---- boolean -> integer (1 or 0)
	fromBool :: Bool -> Int
	fromBool bool 
		| (bool == True)  = 1
		| (bool == False) = 0
		| otherwise 	  = 0

	-- White pins, thus correct color, incorrect position. Calculated by taking the difference between the
	-- total amount of pins of correct color, and the amount of pins that also have the correct position
	---- pattern1 -> pattern2 -> number of correct color
	white :: Pattern -> Pattern -> Int
	white x y = almost_real x y - correct x y

	-- Black pins, correct color AND position simply a helper funcion to make the code more consistent in 
	---- naming convention
	black :: Pattern -> Pattern -> Int
	black = correct

	-- Calculate correct colours, on correct position using zipWith, converting a predicate solution to an integer
	---- pattern1 -> pattern2 -> amount of pins correct position
	correct :: Pattern -> Pattern -> Int
	correct p1 p2 = sum (zipWith toList p1 p2)
		where toList x y = fromBool (x == y)

	-- Calculate correct colour, wrong position. Recursively checks if a color is contained in the pattern.
	-- Because of the asymmetry of the function, a helper function is added (almost_real p1 p2) to ensure correct output
	---- pattern1 -> pattern2 -> amount of pins correct color
	almost :: Pattern -> Pattern -> Int
	almost [] _ = 0
	almost (p:ps) p2 = (fromBool (p `elem` p2)) + (almost ps p2)

	-- Helper function for almost, the real answer is the minimum of the two
	---- pattern1 -> pattern2 -> amount of pins correct color
	almost_real :: Pattern -> Pattern -> Int
	almost_real p1 p2 = min (almost p1 p2) (almost p2 p1)

	-- Creates a list of all possible patterns
	---- list of patterns
	store :: [Pattern]
	store = 
		let colors = [Red, Yellow, Blue, Green, Orange, Purple]
		in [[w, x, y, z] | w <- colors, x <- colors, y <- colors, z <- colors]

	-- Naive algorithm
	---- secret pattern -> list of guesses
	naive_algorithm :: Pattern -> [Pattern]
	naive_algorithm = naive_algorithm' store

	-- Naive algorithm, recursively finds correct pattern
	---- secret pattern -> store (initially) -> list of guesses
	naive_algorithm' :: [Pattern] -> Pattern -> [Pattern]
	naive_algorithm' (this_guess:rest) secret
				| reaction secret this_guess == (0,4) = [this_guess]
				| otherwise							  = this_guess : naive_algorithm' next_guesses secret
					where next_guesses = remove_incorrect rest this_guess secret

	-- Removes the patterns that are with certainty oncorrect from the list of potential guesse
	---- list of patterns remaining -> previous guess -> secret pattern -> list of potentially correct guesses
	remove_incorrect :: [Pattern] -> Pattern -> Pattern -> [Pattern]
	remove_incorrect ptrns guess secret = [y | y <- ptrns, reaction y guess == reaction secret guess]

	-- Test all possibilities and divides the total amount of steps taken by the total amount of patterns to get the
	-- average amount of steps taken / pattern
	---- algorithm wanted for use -> average amount of steps taken per pattern
	tester :: Fractional a => (Pattern -> [Pattern]) -> a
	tester algorithm = tryall algorithm / 1296

	-- Calculates the amount of steps taken for every single possibilitie when antered into the
	-- `algorithm` function (argument), gives the total amount of steps taken for ALL possibilities summed
	---- algorithm wanted for use -> amount of steps taken for ALL possibilities
	tryall :: Num a => (Pattern -> [Pattern]) -> a
	tryall algorithm = fromIntegral $ foldr (\n m -> m + (length $ algorithm n)) 0 store
