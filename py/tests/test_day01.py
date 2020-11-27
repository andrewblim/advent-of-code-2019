import pytest
from .context import advent_of_code_2019
from advent_of_code_2019.day01 import *


def test_fuel():
    assert fuel_required(12) == 2
    assert fuel_required(14) == 2
    assert fuel_required(1969) == 654
    assert fuel_required(100756) == 33583

    assert fuel_required2(14) == 2
    assert fuel_required2(1969) == 966
    assert fuel_required2(100756) == 50346
