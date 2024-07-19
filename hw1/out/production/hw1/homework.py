from collections import deque
from typing import List

class Homework:
    def __init__(self):
        pass

    class Node:
        def __init__(self, name, x, y, z):
            self.name = name
            self.x = x
            self.y = y
            self.z = z
            self.neighbors = []

    class Graph:
        def __init__(self):
            self.nodes = []
            self.algorithm = ""
            self.uphill_energy_limit = 0

    @staticmethod
    def read_input_file() -> Graph:
        with open("input.txt", "r") as file:
            algorithm = file.readline().strip()
            uphill_energy_limit = int(file.readline().strip())

            nodes_by_name = {}

            num_safe_locations = int(file.readline().strip())
            for _ in range(num_safe_locations):
                location_data = file.readline().split()
                name = location_data[0]
                x, y, z = map(int, location_data[1:])
                node = Homework.Node(name, x, y, z)
                nodes_by_name[name] = node

            num_safe_path_segments = int(file.readline().strip())
            for _ in range(num_safe_path_segments):
                path_data = file.readline().split()
                name_one, name_two = path_data
                node_one, node_two = nodes_by_name[name_one], nodes_by_name[name_two]
                node_one.neighbors.append(node_two)
                node_two.neighbors.append(node_one)

        graph = Homework.Graph()
        graph.nodes = list(nodes_by_name.values())
        graph.algorithm = algorithm
        graph.uphill_energy_limit = uphill_energy_limit
        return graph

    @staticmethod
    def general_search(graph: Graph, strategy: str) -> List[Node]:
        explored = []
        frontier = deque()
        # Implement the rest of the general_search function based on the Java code

    @staticmethod
    def main():
        result = []
        try:
            graph = Homework.read_input_file()
            if graph.algorithm == "BFS":
                result = Homework.general_search(graph, graph.algorithm)
            elif graph.algorithm == "UFS":
                pass  # Implement Uniform Cost Search
            else:
                pass  # Implement other search algorithms
        except IOError:
            print("Error reading input")

        try:
            with open("output.txt", "w") as output_file:
                output_file.write(str(result))
        except IOError:
            print("Error writing output.txt")


if __name__ == "__main__":
    Homework.main()