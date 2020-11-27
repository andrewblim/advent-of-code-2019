import sys


def meets_criteria(x):
    return (
        x[0] == x[1] or
        x[1] == x[2] or
        x[2] == x[3] or
        x[3] == x[4] or
        x[4] == x[5]
    ) and (
        x[0] <= x[1] and
        x[1] <= x[2] and
        x[2] <= x[3] and
        x[3] <= x[4] and
        x[4] <= x[5]
    )


def meets_criteria2(x):
    return (
        (x[0] == x[1] and x[1] != x[2]) or
        (x[0] != x[1] and x[1] == x[2] and x[2] != x[3]) or
        (x[1] != x[2] and x[2] == x[3] and x[3] != x[4]) or
        (x[2] != x[3] and x[3] == x[4] and x[4] != x[5]) or
        (x[3] != x[4] and x[4] == x[5])
    ) and (
        x[0] <= x[1] and
        x[1] <= x[2] and
        x[2] <= x[3] and
        x[3] <= x[4] and
        x[4] <= x[5]
    )


def input_gen(start, end):
    return (
        (
            int(x / 100000),
            int((x % 100000) / 10000),
            int((x % 10000) / 1000),
            int((x % 1000) / 100),
            int((x % 100) / 10),
            int(x % 10),
        ) for x in range(start, end)
    )


def count_valid(start, end):
    i = 0
    for x in input_gen(start, end):
        if meets_criteria(x):
            i += 1
    return i


def count_valid2(start, end):
    i = 0
    for x in input_gen(start, end):
        if meets_criteria2(x):
            i += 1
    return i


if __name__ == "__main__":
    with open(sys.argv[1], "r") as fp:
        start = int(fp.readline().rstrip())
        end = int(fp.readline().rstrip())
    print("Part 1:")
    print(count_valid(start, end))
    print("Part 2:")
    print(count_valid2(start, end))
