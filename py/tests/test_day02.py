import pytest
from .context import advent_of_code_2019
from advent_of_code_2019.day02 import *


def test_intcode_parser():
    intcode = [1,9,10,3,2,3,11,0,99,30,40,50]
    assert intcode_parser(intcode, 0) == \
        [3500,9,10,70,2,3,11,0,99,30,40,50]
    
    intcode = [1,0,0,0,99]
    assert intcode_parser(intcode, 0) == [2,0,0,0,99]

    intcode = [2,3,0,3,99]
    assert intcode_parser(intcode, 0) == [2,3,0,6,99]

    intcode = [2,4,4,5,99,0]
    assert intcode_parser(intcode, 0) == [2,4,4,5,99,9801]

    intcode = [1,1,1,4,99,5,6,0,99]
    assert intcode_parser(intcode, 0) == [30,1,1,4,2,5,6,0,99]
