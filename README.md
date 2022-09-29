# Scala Scoreboard Creator for matches

## How to build and install
```sh
sbt pack
cd ./target/pack; make install
```
## How to test
```sh
sbt test
```

or

```sh
make test
```

## How to run
### the easy way...
```sh
make
```
or the individual ones but remember to run `make build` before these will work.
```sh
make run-rankings-with-stdin
```
or
```sh
make run-rankings-with-file
```
or
```sh
make run-random
```

### the harder way...
```sh
make build
./target/pack/bin/scoreboard rankings ./data.txt --strict="true" --log_level="DEBUG"
```

## Some notes
In your example below you in specified the last place as 5. which i found odd.
I have added -s / --strict boolean flag to each command to indicate whether you 
want strictly ordered positions or not as I wanted ot match the spec but not sure 
if this is correct in hindsight this would be something I would ask but did not 
what to delay this submission any further.

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