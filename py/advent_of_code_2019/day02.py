import sys


def intcode_parser(intcode, pos):
    opcode = intcode[pos]
    if opcode == 1:
        intcode[intcode[pos+3]] = intcode[intcode[pos+1]] + intcode[intcode[pos+2]]
        shift = 4
    elif opcode == 2:
        intcode[intcode[pos+3]] = intcode[intcode[pos+1]] * intcode[intcode[pos+2]]
        shift = 4
    elif opcode == 99:
        return intcode
    else:
        raise RuntimeError(f"Unrecognized opcode {opcode}")
    return intcode_parser(intcode, pos + shift)


def intcode_result(intcode, noun, verb, start_pos=0):
    start_intcode = list(intcode)
    start_intcode[1] = noun
    start_intcode[2] = verb
    final_intcode = intcode_parser(start_intcode, start_pos)
    return final_intcode[0]


def try_intcodes(intcode, range1, range2, target, start_pos=0):
    for noun in range1:
        for verb in range2:
            if intcode_result(intcode, noun, verb, start_pos=start_pos) == target:
                return noun, verb


if __name__ == "__main__":
    with open(sys.argv[1], "r") as fp:
        input = [int(x) for x in fp.readline().rstrip().split(",")]
    print("Part 1:")
    print(intcode_result(input, 12, 2))
    print("Part 2:")
    result = try_intcodes(input, range(100), range(100), 19690720)
    print(result)
    print(100*result[0] + result[1])
