// It finally all works!  Got a perfect score!

// Will take in a list of integers:
//    First, two integers, the number of cities, then the number of routes
//    Then, takes in number of routes lines of two integers:
//      The first is the city the route leaves from, the second is the city the route goes to
//    Will then print out an integer for the number of cities that can be reached by
//    every other city
//    Will work flawlessly :)

// Surprisingly only took ~4 hours to complete.  I expected it to take much longer.

import java.util.*;

public class CapitalRelocation {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    final int N = input.nextInt();  // number of cities
    final int M = input.nextInt();  // number of routes


    City[] cities = new City[N];
    City[] reversedCities = new City[N];

    for (int i = 0; i < N; i++) {
      cities[i] = new City(i);
      reversedCities[i] = new City(i);
    }

    for (int i = 0; i < M; i++) {
      int f = input.nextInt();
      int t = input.nextInt();

      // need to subtract 1 because ARRAYS SHOULD START AT ZERO!!!
      cities[f-1].outRoutes.add(new Route(cities[f-1], cities[t-1]));
      reversedCities[t-1].outRoutes.add(new Route(reversedCities[t-1], reversedCities[f-1]));
    }

    int result = numFullyConnected(cities, reversedCities, N);

    System.out.print(result);
  }

  // DFS through the array of cities, but only from the given source
  // will mark each city with its parent City
  // also places them in the stack in the order that they were finished,
  //  so the last finished will be the first popped
  static void DFS(City[] cities, int source, Stack stack) {
    cities[source].visited = true;

    for (Route r : cities[source].outRoutes) {
      // if the city at the end of the route is unvisited
      if (r.to.visited == false) {
        r.to.parent = source;
        DFS(cities, r.to.num, stack);
      }
    }
    stack.push(new Integer(cities[source].num));
  }

  // will put all the connected vertices via dfs into a SCC and return that
  static SCC DFSSCC(City[] cities, int source, SCC result) {
    cities[source].visited = true;
    for (Route r : cities[source].outRoutes) {
      if (r.to.visited == false) {
        r.to.parent = cities[source].parent;

        DFSSCC(cities, r.to.num, result);
      }
      else if (r.to.parent != cities[source].parent){
        // if it is already visited and not in this SCC, it is in another SCC, so it is out
        result.outRoutes.add(r);
      }
    }
    result.cities.add(cities[source]);
    return result;
  }

  // returns number of cities that are fully connected
  static int numFullyConnected(City[] cities, City[] reversedCities, int N) {
    Stack stack = new Stack();
    SCC[] SCCs = new SCC[N];  // represents all the SCC's in the cities

    // will iterate through all the cities, placing them in the stack based on their finishing times
    for (City c : reversedCities) {
      if (c.visited == false) {
        // will result in the stack having a list of numbers of cities
        // in the order that they were finished, so the last finished will
        // be the first popped, etc.
        // also, they will have their parents named, but that won't matter here
        DFS(reversedCities, c.num, stack);
      }
    }

    // while there are still elements left in the stack
    while(stack.empty() == false) {
      int c = (int)stack.pop(); // the number of the next city to DFS through
      if (cities[c].visited == false) { // if it has not been visited, do this
        // the stack doesn't matter here, so we'll just make a new one, might get rid of later if causing performance issues
        // we do care about the parents, though, since that will determine the SCC's
        cities[c].parent = c;

        // SCCs indices are like this: it will have a size = N, but only the indices
        // of parent cities will have anything in them, so you can search through it
        // by looking for the index of the parent city
        SCCs[c] = DFSSCC(cities, c, new SCC());
      }
    }

    for (int i = 0; i < N; i++) {
      if (SCCs[i] != null) {
        SCCs[i].assignParent();
        SCCs[i].assignAdjacentTo();
      }
    }

    Queue<Integer> queue = new LinkedList<Integer>();

    // this should result in the stack being in the order of the SCCs, when they are sorted
    for (int i = 0; i < N; i++) {
      // skip this index if there is nothing there
      if (SCCs[i] == null) continue;
      // if this SCC (which is now a vertex in a new graph) is unvisited, then do something
      if (SCCs[i].visited == false) {
        topSort(SCCs, i, queue);
      }
    }

    // the stack should now be sorted in topological order, with the first popped at the beginning
    int first = (int) queue.remove();

    // This will be the result if there is a path from first to all others.
    int result = SCCs[first].size();

    while (queue.size() != 0) {
      // if there is not a path from the first to every other SCC, then return 0.
      if (SCCs[(int) queue.remove()].pathTo(first, SCCs, new boolean[N]) == false) {
        return 0;
      }
    }

    // else, return the size of the first in the queue as your answer
    return result;
  }

  // Will sort the sets topologically
  static void topSort(SCC[] SCCs, int source, Queue queue) {
    SCCs[source].visited = true;

    // will iterate through all the routes out of
    for (Route r : SCCs[source].outRoutes) {
      // if the SCC that the route points to has not yet been visited, recur
      if (SCCs[r.to.parent].visited == false) {
        topSort(SCCs, r.to.parent, queue);
      }
    }
    queue.add(new Integer(source));
  }
}

// Represents a City, with routes leaving to other cities
class City {
  int num;                    // The number used to identify this city
  Boolean visited;            // Has this city been visited by DFS.  Will only be used once,
                              // and it's easier to have it here than an array in the method
  ArrayList<Route> outRoutes; // ArrayList of all the routes that leave this city
  int parent;                 // city number of the parent city.  Will be useful for making SCCs later

  City(int num) {
    this.num = num;
    this.visited = false;
    this.outRoutes = new ArrayList<>(50); // The assignment says that there will be a max of 50 routes
  }                                       // connected to each city, but this ArrayList could grow if necessary

  // prints out details about the city, useful for troubleshooting
  public void printCity() {
    System.out.println(this.num + ", visited = " + this.visited + ", parent = " + this.parent);
  }
}

// represents a strongly connected component in the graph
class SCC {
  ArrayList<City> cities;     // all the cities that are strongly connected
  ArrayList<Route> outRoutes; // all the routes that connect this SCC with another SCC
  int parent;                 // city number of the city that is the parent to all cities in this SCC
  boolean visited;            // will be used when top-sorting
  Set<Integer> adjacentTo;    // a set of all the parent numbers of the SCCs that this SCC is adjacent to

  SCC() {
    this.cities = new ArrayList<>();
    this.outRoutes = new ArrayList<>();
    this.visited = false;
    this.adjacentTo = new HashSet<Integer>();
  }

  // will only be called when the SCC is complete, so doesn't need any args
  // fills the adjacency set, so don't need to check each route more than once
  void assignAdjacentTo() {
    for (Route r : outRoutes) {
      this.adjacentTo.add(new Integer(r.to.parent));
    }
  }

  // will only be called when the SCC is complete, so doesn't need any args
  public void assignParent() {
    this.parent = this.cities.get(0).parent;
  }

  // returns the size of the SCC
  public int size() {
    return this.cities.size();
  }

  // does this SCC have a path to the given SCC
  public boolean pathTo(int to, SCC[] SCCs, boolean[] visited) {
    if (this.adjacentTo(to)) return true;

    visited[this.parent] = true;

    for (Integer i : this.adjacentTo) {
      // if the SCC at the end of the route is not visted, recur on it
      if (visited[i] == false) {
        return pathTo(i, SCCs, visited);
      }
    }
    return false;
  }

  // is this SCC adjacent to the given SCC index?
  public boolean adjacentTo(int to) {
    for (Integer i : this.adjacentTo) {
      if (i == to) {
        return true;
      }
    }
    return false;
  }

  // prints out details about this SCC.  Useful for troubleshooting
  public void printSCC() {
    System.out.println("printing an SCC's cities:");
    for (City c : this.cities) {
      c.printCity();
    }
    System.out.println();
    System.out.println("Printing an SCC's routes:");
    for (Route r : this.outRoutes) {
      r.printRoute();
    }
    System.out.println();
  }
}

// Represents a route from one city to another
class Route {
  City from;
  City to;

  Route(City from, City to) {
    this.from = from;
    this.to = to;
  }

  // prints out details about the Route.  Useful for troubleshooting
  public void printRoute() {
    System.out.println("from: " + this.from.num + ", to: " + this.to.num);
  }
}
