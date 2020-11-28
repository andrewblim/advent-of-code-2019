import pytest
from .context import advent_of_code_2019
from advent_of_code_2019.day07 import *


def test_intcode():
    intcode = [3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0]
    setting, signal = max_phase_settings(intcode, 0)
    assert setting == (4,3,2,1,0)
    assert signal == 43210

    intcode = [3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0]
    setting, signal = max_phase_settings(intcode, 0)
    assert setting == (0,1,2,3,4)
    assert signal == 54321

    intcode = [3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,
               1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0]
    setting, signal = max_phase_settings(intcode, 0)
    assert setting == (1,0,4,3,2)
    assert signal == 65210
