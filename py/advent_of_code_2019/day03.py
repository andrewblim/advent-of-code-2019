import sys


def manhattan(p):
    return abs(p[0]) + abs(p[1])


class WireSegment:

    def __init__(self, start, direction, steps, prior_steps):
        assert direction in ["U", "D", "L", "R"]
        assert steps > 0
        self._start = start
        self._direction = direction
        self._steps = steps
        self._prior_steps = prior_steps
    
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
    def from_desc(cls, start, desc, prior_steps):
        return cls(start=start, direction=desc[0], steps=int(desc[1:]), prior_steps=prior_steps)

    @property
    def is_horizontal(self):
        return self.direction in ["L", "R"]
    
    @property
    def is_vertical(self):
        return self.direction in ["U", "D"]

    @property
    def first_point(self):
        if self.direction == "U":
            return (self.start[0], self.start[1] + 1)
        elif self.direction == "D":
            return (self.start[0], self.start[1] - 1)
        elif self.direction == "L":
            return (self.start[0] - 1, self.start[1])
        elif self.direction == "R":
            return (self.start[0] + 1, self.start[1])

    @property
    def last_point(self):
        if self.direction == "U":
            return (self.start[0], self.start[1] + self.steps)
        elif self.direction == "D":
            return (self.start[0], self.start[1] - self.steps)
        elif self.direction == "L":
            return (self.start[0] - self.steps, self.start[1])
        elif self.direction == "R":
            return (self.start[0] + self.steps, self.start[1])

    @property
    def minmax_points(self):
        return sorted([self.first_point, self.last_point])

    def intersection_closest_to_origin(self, other):
        p1, p2 = self.minmax_points
        q1, q2 = other.minmax_points
        if self.is_horizontal and other.is_horizontal and p1[1] == q1[1]:
            if p1 < q1:
                start_x = max(p2[0], q1[0])
                end_x = min(p2[0], q2[0])
                if end_x >= start_x:
                    return (min(range(start_x, end_x + 1), key=abs), p1[1])
            else:
                start_x = max(q2[0], p1[0])
                end_x = min(q2[0], p2[0])
                if end_x >= start_x:
                    return (min(range(start_x, end_x + 1), key=abs), p1[1])
        elif self.is_vertical and other.is_vertical and p1[0] == q1[0]:
            if p1 < q1:
                start_y = max(p2[1], q1[1])
                end_y = min(p2[1], q2[1])
                if end_y >= start_y:
                    return (p1[0], min(range(start_y, end_y + 1), key=abs))
            else:
                start_y = max(q2[1], p1[1])
                end_y = min(q2[1], p2[1])
                if end_y >= start_y:
                    return (p1[0], min(range(start_y, end_y + 1), key=abs))
        elif self.is_horizontal and other.is_vertical and \
                p1[0] <= q1[0] <= p2[0] and q1[1] <= p1[1] <= q2[1]:
            return (q1[0], p1[1])
        elif self.is_vertical and other.is_horizontal and \
                p1[1] <= q1[1] <= p2[1] and q1[0] <= p1[0] <= q2[0]:
            return (p1[0], q1[1])


def wire_from_desc(start, wire_desc):
    wire = []
    pos = start
    cumulative_steps = 0
    for wire_seg_desc in wire_desc.split(","):
        wire_seg = WireSegment.from_desc(pos, wire_seg_desc, cumulative_steps)
        wire.append(wire_seg)
        pos = wire_seg.last_point
        cumulative_steps += wire_seg.steps
    return wire


def intersection_closest_to_origin(wire1, wire2):
    closest = None
    for wire_seg1 in wire1:
        for wire_seg2 in wire2:
            intersection = wire_seg1.intersection_closest_to_origin(wire_seg2)
            if intersection is not None:
                if closest is None or manhattan(closest) > manhattan(intersection):
                    closest = intersection
    return closest


if __name__ == "__main__":
    with open(sys.argv[1], "r") as fp:
        wire1 = wire_from_desc((0,0), fp.readline().rstrip())
        wire2 = wire_from_desc((0,0), fp.readline().rstrip())
    print("Part 1:")
    isect = intersection_closest_to_origin(wire1, wire2)
    print(isect)
    print(manhattan(isect))
