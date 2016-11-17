module Main
where

import MM

main :: IO()
main = do
    putStrLn "Naive algorithm:"
    print $ tester naive_algorithm
    putStrLn "Knuths algorithm:"
    print $ tester knuths_algorithm
