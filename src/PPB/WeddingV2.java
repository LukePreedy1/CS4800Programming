import java.util.*;

public class WeddingV2 {
  static int N;
  static int M;
  static int S;

  static ArrayList<Route> routes;
  static City[] cities;

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    N = input.nextInt();        // N = number of cities
    M = input.nextInt();        // M = number of train routes
    S = input.nextInt();        // S = city number of the capital

    routes = new ArrayList<>(M);    // an ArrayList of all the routes that exist
    cities = new City[N];           // an array of all the cities in the kingdom, useful later

    for (int i = 0; i < N; i++) {   // adds N cities to the ArrayList
      cities[i] = new City(i);
    }

    // adds all the new routes that are given to the ArrayList routes
    for (int i = 0; i < M; i++) {
      int f = input.nextInt();
      int t = input.nextInt();
      // need to subtract 1, since indexing SHOULD START AT ZERO
      Route r = new Route(cities[f - 1], cities[t - 1], input.nextInt());
      routes.add(r);
      cities[f - 1].outRoutes.add(r); // adds the route to the city it leaves
    }

    // finds the distance from each city to the capital,
    // then puts the results into the array
    for (City c : cities) {
      // initializes the heap to be a copy of the array of cities
      // will also be fresh for each
      CityBinaryHeap heap = new CityBinaryHeap(N, cities);
      System.out.print(c.distanceToCapital(N, S, routes, heap) + " ");
    }
  }
}

class City {
  int num;    // number of the city
  ArrayList<Route> outRoutes;
  int indexInHeap;

  City(int num) {         // can initialize the city with no leaving routes
    this.num = num;
    this.outRoutes = new ArrayList<>(50);
    this.indexInHeap = num;
  }

  // returns the distance from the given city to the capital
  // Will use a Dijkstra to solve
  // the heap will begin initialized
  int distanceToCapital(int N, int cap, ArrayList<Route> routes, CityBinaryHeap heap) {
    // will quickly solve if this city is the capital
    if (this.num == (cap - 1)) {
      return 0;
    }

    int[] parents = new int[N]; // an array of all city's parents number, index corresponding to city number
    Arrays.fill(parents, -1);

    parents[this.num] = this.num;

    heap.updateCost(this.num, 0);

    // while the heap is not empty:
    while(!heap.isEmpty()) {
      City c = heap.extractMin();
      for (Route r : c.outRoutes) {
        // if the distance from the source on r.to's current path is greater than
        // a path from c to r.to, then replace
        if (r.to.indexInHeap != -1
            && heap.getCost(r.to.num) > heap.getCost(c.num) + r.days) {
          // updates the cost if it is lower
          heap.updateCost(r.to.indexInHeap, heap.getCost(c.num) + r.days);
          parents[r.to.num] = parents[c.num];
        }
      }
    }
    // if the capital cannot be traced back to the source, return -1
    if (parents[cap-1] == -1) return -1;

    return heap.getCost(cap-1);
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

  CityBinaryHeap(int N, City[] cities) {
    for (City c : cities) {
      c.indexInHeap = c.num;
    }
    this.cities = new City[N];
    System.arraycopy(cities, 0, this.cities, 0, cities.length);

    // costs will not be based on position in heap, but on the number of the city
    this.costs = new int[N];
    Arrays.fill(this.costs, 12345);
    this.size = N;
  }

  public int getCost(int index) {
    return this.costs[index];
  }

  public boolean isEmpty() {
    return (this.size == 0);
  }

  // given index in heap, new cost, replaces old cost and bubbles accordingly
  public void updateCost(int index, int newCost) {
    this.costs[this.cities[index].num] = newCost; // updates the cost
    // if it has children and one of them is less than the new cost, bubble down
    if ((index*2 + 1 < this.size) && (newCost > this.costs[this.cities[index*2 + 1].num]
    || (index*2 + 2 < this.size && newCost > this.costs[this.cities[index*2 + 2].num]))) {
      this.bubbleDown(index);
    }
    else {
      this.bubbleUp(index);
    }
  }

/*
  public void insert(City c) {
    this.cities[size] = c;  // put the given city at the bottom of the heap
    this.size++;            // increment the size
    this.bubbleUp(this.size-1); // will bubble up from the given index
  }
*/

  public void bubbleUp(int index) {
    // if the value at the given index is greater than the city above it, swap
    if (this.costs[this.cities[index].num] < this.costs[this.cities[(index-1)/2].num]) {
      this.swap(index, (index-1)/2);
      this.bubbleUp((index-1)/2); // recur on the new position of the item to bubble up
    }
    // otherwise, do nothing
  }

  // bubbles down from the given index until it reaches its sorted position
  public void bubbleDown(int index) {
    int leftIndex = index*2 + 1;        // index of the left child of the given index
    int rightIndex = leftIndex + 1; // index of the right child of the given index

    // do nothing if the given index has no children
    if (leftIndex >= this.size) return;
    // else if there is no right child and the left is less than the given, swap and return
    else if (rightIndex == this.size && this.costs[this.cities[leftIndex].num] < this.costs[this.cities[index].num]) {
      this.swap(index, leftIndex);
      return;
    }

    // is the left child the min?
    boolean leftIsMin = (this.costs[this.cities[leftIndex].num] < this.costs[this.cities[rightIndex].num]);

    // if the city at leftIndex is greater than the city at rightIndex,
    // and is greater than the city at index
    if (leftIsMin && this.costs[this.cities[leftIndex].num] < this.costs[this.cities[index].num]) {
      this.swap(index, leftIndex);
      this.bubbleDown(leftIndex);
    }
    // else if the city at rightIndex is less than the given index, swap and bubble again
    else if (this.costs[this.cities[rightIndex].num] < this.costs[this.cities[index].num]) {
      this.swap(index, rightIndex);
      this.bubbleDown(rightIndex);
    }
  }

  // swaps the cities and their respective costs at the given two indices
  public void swap(int i1, int i2) {
    if (i1 >= size || i2 >= size) return; // will not run if given indices that are out of bounds

    City temp1 = this.cities[i1];
    City temp2 = this.cities[i2];
    this.cities[i1] = temp2;
    this.cities[i2] = temp1;
    
    this.cities[i1].indexInHeap = i1;
    this.cities[i2].indexInHeap = i2;
  }

  // since the min will always be at the beginning of the heap, just return [0],
  // then bubble down to fill in the blanks
  public City extractMin() {
    City result = this.cities[0];       // result will be the beginning
    this.size--;                        // decrement the size
    this.cities[0] = this.cities[size]; // put the final value at the beginning
    this.bubbleDown(0);                 // bubble down from the beginning
    result.indexInHeap = -1;
    return result;
  }
}
