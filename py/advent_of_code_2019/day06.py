from collections import defaultdict
import sys


class OrbitSystem:

    def __init__(self):
        self.sat_to_hub = {}
        self.bodies = set()
    
    def add_orbit(self, hub, sat):
        self.sat_to_hub[sat] = hub
        self.bodies.add(sat)
        self.bodies.add(hub)
    
    def add_orbit_from_desc(self, desc):
        hub, sat = desc.split(")")
        self.add_orbit(hub, sat)
    
    def count_orbits(self, start, memo={}):
        if start not in memo:
            if start in self.sat_to_hub:
                memo[start] = 1 + self.count_orbits(self.sat_to_hub[start], memo)
            else:
                memo[start] = 0
        return memo[start]
    
    def count_all_orbits(self):
        orbits = 0
        memo = {}
        for body in self.bodies:
            orbits += self.count_orbits(body, memo)
        return orbits
    
    def orbit_trail(self, start, i):
        if start not in self.sat_to_hub:
            return []
        return [(start, i)] + self.orbit_trail(self.sat_to_hub[start], i+1)
    
    def transfers(self, x1, x2):
        trail1 = dict(self.orbit_trail(self.sat_to_hub[x1], 0))
        trail2 = dict(self.orbit_trail(self.sat_to_hub[x2], 0))
        common_bodies = set(trail1.keys()) & set(trail2.keys())
        min_dist = None
        for body in common_bodies:
            dist = trail1[body] + trail2[body]
            if min_dist is None or dist < min_dist:
                min_dist = dist
        return min_dist



if __name__ == "__main__":
    with open(sys.argv[1], "r") as fp:
        orbit_descs = [x.rstrip() for x in fp.readlines()]
    system = OrbitSystem()
    for orbit_desc in orbit_descs:
        system.add_orbit_from_desc(orbit_desc)
    print("Part 1:")
    print(system.count_all_orbits())
    print("Part 2:")
    print(system.transfers("YOU", "SAN"))
