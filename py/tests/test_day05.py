import pytest
from .context import advent_of_code_2019
from advent_of_code_2019.day05 import *


def test_intcode():
    intcode = [1002,4,3,4,33]
    run_intcode(intcode, 0, [])
    assert intcode == [1002,4,3,4,99]

    intcode = [1101,100,-1,4,0]
    run_intcode(intcode, 0, [])
    assert intcode == [1101,100,-1,4,99]


def test_extended_intcode():
    output = run_intcode([3,9,8,9,10,9,4,9,99,-1,8], 0, [8])
    assert list(output) == [1]
    output = run_intcode([3,9,8,9,10,9,4,9,99,-1,8], 0, [9])
    assert list(output) == [0]

    output = run_intcode([3,9,7,9,10,9,4,9,99,-1,8], 0, [8])
    assert list(output) == [0]
    output = run_intcode([3,9,7,9,10,9,4,9,99,-1,8], 0, [7])
    assert list(output) == [1]

    output = run_intcode([3,3,1108,-1,8,3,4,3,99], 0, [8])
    assert list(output) == [1]
    output = run_intcode([3,3,1108,-1,8,3,4,3,99], 0, [9])
    assert list(output) == [0]

    output = run_intcode([3,3,1107,-1,8,3,4,3,99], 0, [8])
    assert list(output) == [0]
    output = run_intcode([3,3,1107,-1,8,3,4,3,99], 0, [7])
    assert list(output) == [1]

    output = run_intcode([3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9], 0, [0])
    assert list(output) == [0]
    output = run_intcode([3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9], 0, [1234])
    assert list(output) == [1]

    output = run_intcode([3,3,1105,-1,9,1101,0,0,12,4,12,99,1], 0, [0])
    assert list(output) == [0]
    output = run_intcode([3,3,1105,-1,9,1101,0,0,12,4,12,99,1], 0, [1234])
    assert list(output) == [1]

    intcode = [3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
               1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
               999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99]
    output = run_intcode([x for x in intcode], 0, [7])
    assert list(output) == [999]
    output = run_intcode([x for x in intcode], 0, [8])
    assert list(output) == [1000]
    output = run_intcode([x for x in intcode], 0, [9])
    assert list(output) == [1001]
