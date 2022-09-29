# Scala Scoreboard Creator for matches

## How to build
```sh
sbt pack
cd ./target/pack; make install
```

## Some notes
In your example below you in specified the last place as 5. which i found odd.
I have 
```text
Sample input:
Lions 3, Snakes 3
Tarantulas 1, FC Awesome 0
Lions 1, FC Awesome 1
Tarantulas 3, Snakes 1
Lions 4, Grouches 0

Expected output:
1. Tarantulas, 6 pts
2. Lions, 5 pts
3. FC Awesome, 1 pt
3. Snakes, 1 pt
5. Grouches, 0 pts
```