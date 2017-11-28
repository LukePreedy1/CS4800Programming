import java.util.*;

public class wedding {
    static int N;
    static int M;
    static int S;

    static ArrayList<Route> routes;
    static ArrayList<City> cities;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        N = input.nextInt();        // N = number of cities
        M = input.nextInt();        // M = number of train routes
        S = input.nextInt();        // S = city number of the capital

        routes = new ArrayList<>(M);    // an ArrayList of all the routes that exist

        //TODO make cities a binary heap

        //cities = new ArrayList<>(N);    // an ArrayList of all the cities

        for (int i = 0; i < N; i++) {   // adds N cities to the ArrayList
            cities.add(new City(i));
        }

        // adds all the new routes that are given to the ArrayList routes
        for (int i = 0; i < M; i++) {
            int f = input.nextInt();
            int t = input.nextInt();
            // need to subtract 1, since indexing SHOULD START AT ZERO
            Route r = new Route(cities.get(f - 1), cities.get(t - 1), input.nextInt());
            routes.add(r);
        }

        int[] results = new int[N];

        // finds the distance from each city to the capital,
        // then adds each result to the
        for (City c : cities) {
            results[c.num] = c.distanceToCapital(N, S, routes);
        }

        String res = "";

        // prints the results
        for (int i = 0; i < N; i++) {
            res += Integer.toString(results[i]);
            res += " ";
        }

        System.out.print(res);
    }
}

class City {
    int num;    // number of the city
    int val;

    City(int num) {         // can initialize the city with no leaving routes
        this.num = num;
        this.val = 0;
    }

    // returns the distance from the given city to the capital
    // Will use a modified Bellman-Ford algorithm to solve
    // Runs in O(cities * routes) time
    int distanceToCapital(int N, int cap, ArrayList<Route> routes) {
        if (this.num == (cap - 1)) {  // quickly check if this is the capital
            return 0;                 // if so, just return 0, skip a few steps
        }                             // But, still need to subtract by 1, since APPARENTLY ARRAYS DON'T START AT ZERO

        int[] distance = new int[N];
        //City[] predecessor = new City[N];

        // each element in the array represents a city, and its distance from the given city
        for (int i = 0; i < N; i++) {
            distance[i] = 12345;
            //predecessor[i] = null;
        }
        distance[this.num] = 0; // set the source distance to 0

        for (int i = 0; i < N; i++) {
            for (Route r : routes) {
                // if following this route will result in a shorter path to
                // the end of the path than what it currently has, replace the values
                if (distance[r.from.num] + r.days < distance[r.to.num] || distance[r.to.num] < 0) {
                    distance[r.to.num] = distance[r.from.num] + r.days;
                    //predecessor[r.to.num] = r.from;
                }
            }
        }

        // if the distance is the default large value, it means
        // that it could not be reached, so we return -1
        if (distance[cap - 1] == 12345) {
            return -1;
        }

        // returns the distance from the capital city
        // Also, need to subtract 1, again, since indices SHOULD START AT ZERO
        // HINT HINT
        return distance[cap - 1];
    }
}

class Route {
    City from;   // City the route leaves from
    City to;     // City the route goes to
    int days;    // number of days the route takes to travel

    Route(City from, City to, int days) {
        this.from = from;
        this.to = to;
        this.days = days;
    }
}

// represents a binary heap of cities
class CityBinaryHeap {
  private City[] cities;
  public int[] costs;
  private int size;

  CityBinaryHeap(int N) {
    this.cities = new City[N];
    this.costs = new int[N];
  }

  public void initialize() {
    Arrays.fill(this.costs, 12345); // sets the costs to start at ~infinity
    this.size = 0;
  }

  public boolean isEmpty() {
    return (this.size == 0);
  }

  public void insert(City c) {
    this.cities[size] = c;  // put the given city at the bottom of the heap
    this.size++;            // increment the size
    this.bubbleUp(this.size-1); // will bubble up from the given index
  }

  public void bubbleUp(int index) {
    // if the value at the given index is greater than the city above it, swap
    if (this.cities[index].val < this.cities[index/2].val) {
      this.swap(index, index/2);
      this.bubbleUp(index/2); // recur on the new position of the item to bubble up
    }
    // otherwise, do nothing
  }

  public void bubbleDown(int index) {
    //TODO
  }

  // swaps the cities and their respective costs at the given two indices
  public void swap(int i1, int i2) {
    if (i1 >= size || i2 >= size) return; // will not run if given indices that are out of bounds
    
    City temp1 = this.cities[i1];
    City temp2 = this.cities[i2];
    int cost1 = this.costs[i1];
    int cost2 = this.costs[i2];
    this.cities[i1] = temp2;
    this.cities[i2] = temp1;
    this.costs[i1] = cost2;
    this.costs[i2] = cost1;
  }

  // since the min will always be at the beginning of the heap, just return [0],
  // then bubble down to fill in the blanks
  public City extractMin() {
    City result = this.cities[0];       // result will be the beginning
    this.size--;                        // decrement the size
    this.cities[0] = this.cities[size]; // put the final value at the beginning
    this.bubbleDown(0);                 // bubble down from the beginning
  }
}
