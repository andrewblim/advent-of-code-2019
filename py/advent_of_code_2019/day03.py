import sys


def manhattan(p):
    return abs(p[0]) + abs(p[1])


class WireSegment:

    def __init__(self, start, direction, steps):
        assert direction in ["U", "D", "L", "R"]
        assert steps > 0
        self._start = start
        self._direction = direction
        self._steps = steps
    
    def __repr__(self):
        return f"<WireSegment {self.start} {self.direction}{self.steps}"
    
    @property
    def start(self):
        return self._start
    
    @property
    def direction(self):
        return self._direction
    
    @property
    def steps(self):
        return self._steps
    
    @property
    def prior_steps(self):
        return self._prior_steps

    @classmethod
    def from_desc(cls, start, desc):
        return cls(start=start, direction=desc[0], steps=int(desc[1:]))

    @property
    def is_horizontal(self):
        return self.direction in ["L", "R"]
    
    @property
    def is_vertical(self):
        return self.direction in ["U", "D"]

    @property
    def points(self):
        if self.direction == "U":
            return ((self.start[0], self.start[1] + x, self.start[2] + x)
                    for x in range(1, self.steps + 1))
        elif self.direction == "D":
            return ((self.start[0], self.start[1] - x, self.start[2] + x)
                    for x in range(1, self.steps + 1))
        elif self.direction == "L":
            return ((self.start[0] - x, self.start[1], self.start[2] + x)
                    for x in range(1, self.steps + 1))
        elif self.direction == "R":
            return ((self.start[0] + x, self.start[1], self.start[2] + x)
                    for x in range(1, self.steps + 1))

    @property
    def last_point(self):
        if self.direction == "U":
            return (self.start[0], self.start[1] + self.steps, self.start[2] + self.steps)
        elif self.direction == "D":
            return (self.start[0], self.start[1] - self.steps, self.start[2] + self.steps)
        elif self.direction == "L":
            return (self.start[0] - self.steps, self.start[1], self.start[2] + self.steps)
        elif self.direction == "R":
            return (self.start[0] + self.steps, self.start[1], self.start[2] + self.steps)

    def intersections(self, other):
        isect = set([])
        # short-circuit clear non-intersections
        # could be more efficient but this isn't too bad to read/follow
        if self.is_horizontal and other.is_horizontal and self.start[1] != other.start[1]:
            return isect
        if self.is_vertical and other.is_vertical and self.start[0] != other.start[0]:
            return isect
        if self.is_horizontal and other.is_vertical and \
                ((self.start[0] < other.start[0] and self.last_point[0] < other.start[0]) or
                 (self.start[0] > other.start[0] and self.last_point[0] > other.start[0]) or
                 (other.start[1] < self.start[1] and other.last_point[1] < other.start[1]) or
                 (other.start[1] > self.start[1] and other.last_point[1] > other.start[1])):
            return isect
        if self.is_vertical and other.is_horizontal and \
                ((self.start[1] < other.start[1] and self.last_point[1] < other.start[1]) or
                 (self.start[1] > other.start[1] and self.last_point[1] > other.start[1]) or
                 (other.start[0] < self.start[0] and other.last_point[0] < other.start[0]) or
                 (other.start[0] > self.start[0] and other.last_point[0] > other.start[0])):
            return isect
        points = { (p[0], p[1]): p[2] for p in self.points }
        for q in other.points:
            if (q[0], q[1]) in points:
                isect.add((q[0], q[1], q[2] + points[(q[0], q[1])]))
        return isect


def wire_from_desc(start, wire_desc):
    wire = []
    pos = start
    for wire_seg_desc in wire_desc.split(","):
        wire_seg = WireSegment.from_desc(pos, wire_seg_desc)
        wire.append(wire_seg)
        pos = wire_seg.last_point
    return wire


def intersection_closest_to_origin(wire1, wire2):
    best = None
    for wire_seg1 in wire1:
        for wire_seg2 in wire2:
            isect = wire_seg1.intersections(wire_seg2)
            if len(isect) > 0:
                candidate = min(isect, key=manhattan)
                if best is None or manhattan(best) > manhattan(candidate):
                    best = candidate
    return best


def intersection_fewest_steps(wire1, wire2):
    best = None
    for wire_seg1 in wire1:
        for wire_seg2 in wire2:
            isect = wire_seg1.intersections(wire_seg2)
            if len(isect) > 0:
                candidate = min(isect, key=lambda x: x[2])
                if best is None or best[2] > candidate[2]:
                    best = candidate
    return best


if __name__ == "__main__":
    with open(sys.argv[1], "r") as fp:
        wire1 = wire_from_desc((0,0,0), fp.readline().rstrip())
        wire2 = wire_from_desc((0,0,0), fp.readline().rstrip())
    print("Part 1:")
    isect = intersection_closest_to_origin(wire1, wire2)
    print(isect)
    print(manhattan(isect))
    print("Part 2:")
    isect = intersection_fewest_steps(wire1, wire2)
    print(isect)
    print(isect[2])
