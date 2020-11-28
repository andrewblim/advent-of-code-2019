import pytest
from .context import advent_of_code_2019
from advent_of_code_2019.day06 import *


def test_orbit_count():
    orbit_descs = [
        "COM)B",
        "B)C",
        "C)D",
        "D)E",
        "E)F",
        "B)G",
        "G)H",
        "D)I",
        "E)J",
        "J)K",
        "K)L",
    ]
    system = OrbitSystem()
    for orbit_desc in orbit_descs:
        system.add_orbit_from_desc(orbit_desc)
    assert system.count_all_orbits() == 42


def test_transfers():
    orbit_descs = [
        "COM)B",
        "B)C",
        "C)D",
        "D)E",
        "E)F",
        "B)G",
        "G)H",
        "D)I",
        "E)J",
        "J)K",
        "K)L",
        "K)YOU",
        "I)SAN",
    ]
    system = OrbitSystem()
    for orbit_desc in orbit_descs:
        system.add_orbit_from_desc(orbit_desc)
    assert system.transfers("YOU", "SAN") == 4
    assert system.transfers("SAN", "YOU") == 4
