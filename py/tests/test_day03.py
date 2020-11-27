import pytest
from .context import advent_of_code_2019
from advent_of_code_2019.day03 import *


def test_intersection_closest():
    wire1 = wire_from_desc((0,0), "R8,U5,L5,D3")
    wire2 = wire_from_desc((0,0), "U7,R6,D4,L4")
    isect = intersection_closest_to_origin(wire1, wire2)
    assert isect == (3,3)

    wire1 = wire_from_desc((0,0), "R75,D30,R83,U83,L12,D49,R71,U7,L72")
    wire2 = wire_from_desc((0,0), "U62,R66,U55,R34,D71,R55,D58,R83")
    isect = intersection_closest_to_origin(wire1, wire2)
    assert manhattan(isect) == 159
    
    wire1 = wire_from_desc((0,0), "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
    wire2 = wire_from_desc((0,0), "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
    isect = intersection_closest_to_origin(wire1, wire2)
    assert manhattan(isect) == 135
