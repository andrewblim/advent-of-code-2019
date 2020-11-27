import math
import sys


def fuel_required(mass):
    return math.floor(mass / 3) - 2


def fuel_required2(mass):
    fuel = fuel_required(mass)
    if fuel <= 0:
        return 0
    else:
        return fuel + fuel_required2(fuel)
    

if __name__ == "__main__":
    with open(sys.argv[1], "r") as fp:
        input = fp.readlines()
    print("Part 1:")
    print(sum([fuel_required(int(x)) for x in input]))
    print("Part 2:")
    print(sum([fuel_required2(int(x)) for x in input]))
