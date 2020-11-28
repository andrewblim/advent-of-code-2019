from collections import deque, namedtuple
import sys


Opcode = namedtuple('Opcode', ['op', 'mode1', 'mode2', 'mode3'])

def opcode_parser(code):
    return Opcode(
        op=code % 100,
        mode1=int((code % 1000) / 100),
        mode2=int((code % 10000) / 1000),
        mode3=int(code / 10000),
    )


def get_value(intcode, pos, mode):
    if mode == 0:
        return intcode[intcode[pos]]
    elif mode == 1:
        return intcode[pos]
    else:
        raise RuntimeError(f"unrecognized mode {mode}")


def intcode_parser(intcode, pos, input, output):
    # input/output assumed to be deques
    opcode = opcode_parser(intcode[pos])
    if opcode.op == 1:
        x1 = get_value(intcode, pos+1, opcode.mode1)
        x2 = get_value(intcode, pos+2, opcode.mode2)
        if opcode.mode3 != 0:
            raise RuntimeError(f"Got non-position mode for a write argument {opcode}")
        intcode[intcode[pos+3]] = x1 + x2
        next_pos = pos + 4
    elif opcode.op == 2:
        x1 = get_value(intcode, pos+1, opcode.mode1)
        x2 = get_value(intcode, pos+2, opcode.mode2)
        if opcode.mode3 != 0:
            raise RuntimeError(f"Got non-position mode for a write argument {opcode}")
        intcode[intcode[pos+3]] = x1 * x2
        next_pos = pos + 4
    elif opcode.op == 3:
        if opcode.mode1 != 0:
            raise RuntimeError(f"Got non-position mode for a write argument {opcode}")
        intcode[intcode[pos+1]] = input.popleft()
        next_pos = pos + 2
    elif opcode.op == 4:
        x1 = get_value(intcode, pos+1, opcode.mode1)
        output.append(x1)
        next_pos = pos + 2
    elif opcode.op == 5:
        x1 = get_value(intcode, pos+1, opcode.mode1)
        x2 = get_value(intcode, pos+2, opcode.mode2)
        if x1 != 0:
            next_pos = x2
        else:
            next_pos = pos + 3
    elif opcode.op == 6:
        x1 = get_value(intcode, pos+1, opcode.mode1)
        x2 = get_value(intcode, pos+2, opcode.mode2)
        if x1 == 0:
            next_pos = x2
        else:
            next_pos = pos + 3
    elif opcode.op == 7:
        x1 = get_value(intcode, pos+1, opcode.mode1)
        x2 = get_value(intcode, pos+2, opcode.mode2)
        if opcode.mode3 != 0:
            raise RuntimeError(f"Got non-position mode for a write argument {opcode}")
        if x1 < x2:
            intcode[intcode[pos+3]] = 1
        else:
            intcode[intcode[pos+3]] = 0
        next_pos = pos + 4
    elif opcode.op == 8:
        x1 = get_value(intcode, pos+1, opcode.mode1)
        x2 = get_value(intcode, pos+2, opcode.mode2)
        if opcode.mode3 != 0:
            raise RuntimeError(f"Got non-position mode for a write argument {opcode}")
        if x1 == x2:
            intcode[intcode[pos+3]] = 1
        else:
            intcode[intcode[pos+3]] = 0
        next_pos = pos + 4
    elif opcode.op == 99:
        next_pos = None  # signal to halt
    else:
        raise RuntimeError(f"Unrecognized opcode {opcode}")
    return next_pos


def run_intcode(intcode, pos, starting_input):
    input = deque(starting_input)
    output = deque()
    while pos is not None:
        pos = intcode_parser(intcode, pos, input, output)
    return output


if __name__ == "__main__":
    with open(sys.argv[1], "r") as fp:
        intcode = [int(x) for x in fp.readline().rstrip().split(",")]
    print("Part 1:")
    output = run_intcode([x for x in intcode], 0, [1])
    print(output)
    print("Part 2:")
    output = run_intcode([x for x in intcode], 0, [5])
    print(output)
