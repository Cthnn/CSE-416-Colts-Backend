import random
import json
from disjoint_set import DisjointSet
import sys
import cProfile


class Precinct:
    precinct_id: None
    total: None
    vap_total: None
    neighbor_precincts: []


class District:
    def __init__(self, p=None):
        self.district_precincts = [p]
        if not p:
            self.district_precincts = []
        self.edges = []
        self.is_acceptable = False


def find_sub_graph(precinct_id):
    return district_dict[precinct_id]


def rehash(district):
    for precinct in district.district_precincts:
        district_dict[precinct.precinct_id] = district


# returns a random neighbor sub-graph
def find_neighbor_sub_graph(district):
    ids = [d.precinct_id for d in district.district_precincts]
    for precinct in random.sample(district.district_precincts,
                                  len(district.district_precincts)):  # choose random precinct
        for neighbor in random.sample(precinct.neighbor_precincts,
                                      len(precinct.neighbor_precincts)):  # choose a random neighbor
            if neighbor not in ids:  # neighbor is part of different sub_graph
                return find_sub_graph(neighbor)
    return None  # edge case where the district has all the precincts and there are no neighbors


# given a sub-graph with incomplete edges, fill in missing edges
def add_edges(district):
    # print(district)
    ids = [precinct.precinct_id for precinct in district.district_precincts]
    for precinct in district.district_precincts:
        for neighbor in precinct.neighbor_precincts:
            edge = {precinct.precinct_id, neighbor}
            if neighbor in ids and edge not in district.edges:
                district.edges.append(edge)


def combine_sub_graphs(district1, district2):
    d = District()
    d.district_precincts = district1.district_precincts + district2.district_precincts
    d.edges = district1.edges + district2.edges
    return d


def have_common_edge(graph1, graph2):
    for x in graph1:
        for y in graph2:
            if x == y:
                return True
    return False


def generate_spanning_tree(district):  # use modified Kruskal's Algorithm
    add_edges(district)
    edges = random.sample(district.edges, len(district.edges))
    nodes = [precinct.precinct_id for precinct in district.district_precincts]
    spanning_tree = []
    ds = DisjointSet()
    for node in nodes:
        ds.find(node)
    for e in edges:
        edge = list(e)
        u = edge[0]
        v = edge[1]
        x = ds.find(u)
        y = ds.find(v)
        if x == y:
            continue
        else:
            spanning_tree.append(edge)
            ds.union(x, y)

        if len(spanning_tree) == len(district.district_precincts) - 1:
            break
    # print('spanning tree:', len(spanning_tree), spanning_tree)
    return spanning_tree


# takes in a list of precinct ids and returns a district
def build_sub_graph(nodes):
    d = District()
    for node in nodes:
        d.district_precincts.append(precinct_dict[node])
    # print('checking for duplicates in subgraphs:')
    # print('dups:', [item for item, count in collections.Counter(d.district_precincts).items() if count > 1])
    return d


# returns a tuple containing the two sub-graphs when edge is cut
def cut_edge(cut_edge, spanning_tree, district):
    nodes = [precinct.precinct_id for precinct in district.district_precincts]
    ds = DisjointSet()
    for node in nodes:
        ds.find(node)
    # print(list(ds))
    for e in spanning_tree:
        if e != cut_edge:
            edge = list(e)
            ds.union(edge[0], edge[1])
    list_ds = list(ds)
    nodes1 = []
    nodes2 = []
    parent1 = list_ds[0][1]
    parent2 = None
    for member in list_ds:
        if member[1] != parent1:
            parent2 = member[1]
            break
    for member in list_ds:
        if member[1] == parent1:
            nodes1.append(member[0])
        elif member[1] == parent2:
            nodes2.append(member[0])
        else:
            print('MEMBER UNACCOUNTED FOR THIS IS REALLY BAD!!!!!!!!')
    sub_graph1 = build_sub_graph(nodes1)
    sub_graph2 = build_sub_graph(nodes2)
    return sub_graph1, sub_graph2


# calculates the ratio between the number of edge nodes and total nodes
def calculate_compactness(district):
    num_nodes = len(district.district_precincts)
    edge_nodes = []
    for precinct in district.district_precincts:
        for neighbor in precinct.neighbor_precincts:
            if find_sub_graph(neighbor) != district and precinct not in edge_nodes:
                edge_nodes.append(precinct)
    num_edge_nodes = len(edge_nodes)
    # print('compactness:', num_edge_nodes, num_nodes)
    return num_edge_nodes / num_nodes


def calculate_district_total_population(district):
    total = 0
    for precinct in district.district_precincts:
        total += precinct.vap_total
    return total


# returns true if district conforms to compactness and population deviation
def is_acceptable(district):
    population1 = calculate_district_total_population(district)
    compactness1 = calculate_compactness(district)
    return compactness1 <= compactness and\
        population_range[0] < population1 <= population_range[1]

target_districts = None
max_iterations = None
number_of_plans = None
compactness = None
population_deviation = None

precinct_dict = {}  # maps precinct ids to precincts
district_dict = {}  # maps precincts ids to districts

population_range = None

target_districts = int(sys.argv[1])
max_iterations = int(sys.argv[2])
number_of_plans = int(sys.argv[3])
compactness = float(sys.argv[4])
population_deviation = float(sys.argv[5])
input_file_name = sys.argv[6]
output_file_name = sys.argv[7]
print(sys.argv)


def run_algorithm():
    districtings = []
    fit_critera = 0
    improvements = 0
    no_improvements = 0
    for runNumber in range(number_of_plans):
        iteratations = max_iterations
        print("starting run number ", runNumber)
        precincts = []  # list of all precincts
        districts = []  # list  of all sub-graphs/districts

        f = open(input_file_name, 'r')
        data = f.read()
        # print(data)
        data = json.loads(data)
        total_population = 0
        average_district_population = 0
        for precinct in data:
            p = Precinct()
            p.precinct_id = precinct['GEOID']
            p.total = precinct['TOTAL']
            p.vap_total = precinct['VAP_TOTAL']
            p.neighbor_precincts = precinct['NEIGHBORS']
            if len(p.neighbor_precincts) > 0:
                precincts.append(p)
                total_population += p.vap_total
        average_district_population = int(total_population / target_districts)
        deviation = int(average_district_population * population_deviation / 2)
        global population_range
        population_range = average_district_population - deviation, average_district_population + deviation + 1


        # make each precinct its own sub-graph
        for precinct in precincts:
            districts.append(District(precinct))
            precinct_dict[precinct.precinct_id] = precinct

        for district in districts:
            precinct = district.district_precincts[0]
            district_dict[precinct.precinct_id] = district


        # generate seed districting
        districts = random.sample(districts, len(districts))
        while len(districts) > target_districts:
            district = districts[0]
            neighbor_sub_graph = find_neighbor_sub_graph(district)
            # print(district)
            # print(neighbor_sub_graph)
            districts.remove(district)
            districts.remove(neighbor_sub_graph)
            combined_sub_graph = combine_sub_graphs(district, neighbor_sub_graph)
            districts.append(combined_sub_graph)
            rehash(combined_sub_graph)

        print('seed districts created')

        districting = []
        i = 0
        for district in districts:
            district.is_acceptable = is_acceptable(district)
            districting.append({})
            districting[i]['id'] = i + 1
            districting[i]['precincts'] = [precinct.precinct_id for precinct in district.district_precincts]
            i += 1

        # for district in districting:
        #     print(district)

        # improve districting
        running = True
        while running:
            found_feasible_edge = True
            i = random.randint(0, len(districts) - 1)  # choose a random district
            district = districts[i]
            neighbor_sub_graph = find_neighbor_sub_graph(district)
            combined_sub_graph = combine_sub_graphs(district, neighbor_sub_graph)
            # if district.is_acceptable and neighbor_sub_graph.is_acceptable:
            #     continue
            spanning_tree = generate_spanning_tree(combined_sub_graph)
            population_district = calculate_district_total_population(district)
            population_neighbor = calculate_district_total_population(neighbor_sub_graph)

            testArray = []
            for edge in random.sample(spanning_tree, len(spanning_tree)):
                if(edge in testArray):
                    print("DUPLICATE EDGE")
                else:
                    testArray.append(edge)
                sub_graphs = cut_edge(edge, spanning_tree, combined_sub_graph)
                sub_graph1 = sub_graphs[0]
                sub_graph2 = sub_graphs[1]
                rehash(sub_graph1)
                rehash(sub_graph2)

                population1 = calculate_district_total_population(sub_graph1)
                population2 = (population_district + population_neighbor) - population1

                compactness1 = calculate_compactness(sub_graph1)
                compactness2 = calculate_compactness(sub_graph2)
                if compactness1 <= compactness and compactness2 <= compactness \
                        and population_range[0] < population1 <= population_range[1] \
                        and population_range[0] < population2 <= population_range[1]:
                    print('subgraphs fit critera')
                    fit_critera += 1
                    districts.remove(district)
                    districts.remove(neighbor_sub_graph)
                    districts.append(sub_graph1)
                    districts.append(sub_graph2)
                    sub_graph1.is_acceptable = True
                    sub_graph2.is_acceptable = True
                    break
                if abs(population1 - average_district_population) <= \
                        abs(population_district - average_district_population) and \
                        abs(population2 - average_district_population)  <= \
                        abs(population_neighbor - average_district_population):
                    print('subgraphs improve but do not fit critera')
                    improvements += 1
                    districts.remove(district)
                    districts.remove(neighbor_sub_graph)
                    districts.append(sub_graph1)
                    districts.append(sub_graph2)
                    break
                rehash(district)
                rehash(neighbor_sub_graph)
                no_improvements += 1
                found_feasible_edge = False
                break

            # check for termination conditions
            running = False
            for district in districts:
                if not district.is_acceptable:
                    running = True

            if found_feasible_edge:
                iteratations -= 1
                print(iteratations)
            if iteratations == 0:
                running = False

        print(len(districts), districts)

        districting = {'districts': []}
        i = 0
        for district in districts:
            districting['districts'].append({})
            districting['districts'][i]['id'] = i + 1
            districting['districts'][i]['precincts'] = [precinct.precinct_id for precinct in district.district_precincts]
            i += 1

        districtings.append(districting)

        for district in districting['districts']:
            print(district)

    print('writing to file', output_file_name)
    with open(output_file_name, 'w+') as f:
        json.dump(districtings, f, indent=2)

    print("Edges that fit critera: ", fit_critera)
    print("Edges that improve: ", improvements)
    print("Edges that do not improve: ", no_improvements)
    print("Total: ", (fit_critera + improvements + no_improvements))


def main():
    run_algorithm()


if __name__ == '__main__':
    cProfile.run('main()')
