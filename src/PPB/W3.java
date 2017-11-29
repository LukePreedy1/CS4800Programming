import java.util.*;

// TODO will try reversing all the routes, then searching from the capital backwards

public class W3 {
  static int N;
  static int M;
  static int S;

  static City[] cities;

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    //int[] inputnums = new WeddingInputGen().genInput();


    N = input.nextInt();
    M = input.nextInt();
    S = input.nextInt();

/*
    N = inputnums[0];//input.nextInt();        // N = number of cities
    M = inputnums[1];//input.nextInt();        // M = number of train routes
    S = inputnums[2];//input.nextInt();        // S = city number of the capital
*/

    cities = new City[N];           // an array of all the cities in the kingdom, useful later

    for (int i = 0; i < N; i++) {   // adds N cities to the ArrayList
      cities[i] = new City(i);
    }

    // adds all the new routes that are given to the ArrayList routes
    //for (int i = 3; i < M*3 + 3; i++) {
    for (int i = 0; i < M; i++) {

      int f = input.nextInt();
      int t = input.nextInt();

/*
      int f = inputnums[i];//input.nextInt();
      i++;
      int t = inputnums[i];//input.nextInt();
      i++;
*/
      // need to subtract 1, since indexing SHOULD START AT ZERO
      Route r = new Route(cities[t - 1], cities[f - 1], input.nextInt());
      //Route r = new Route(cities[t - 1], cities[f - 1], inputnums[i]);//input.nextInt());
      cities[t - 1].outRoutes.add(r); // adds the route to the city it leaves
    }

    CityBinaryHeap heap = new CityBinaryHeap(N, cities);

    int[] resultsInts = cities[S-1].distanceToAll(N, S, heap);

    String results = "";

    for (int i : resultsInts) {
      results += i;
      results += " ";
    }
    System.out.print(results);
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

  int[] distanceToAll(int N, int cap, CityBinaryHeap heap) {
    int[] parents = new int[N];
    Arrays.fill(parents, -1);

    heap.updateCost(this.num, 0);

    while(!heap.isEmpty()) {
      City c = heap.extractMin();
      for (Route r : c.outRoutes) {
        if (r.to.indexInHeap != -1 &&
            heap.getCost(r.to.num) > heap.getCost(c.num) + r.days) {
          // updates the cost if it is lower
          heap.updateCost(r.to.indexInHeap, heap.getCost(c.num) + r.days);
          parents[r.to.num] = parents[c.num];
        }
      }
    }
    for (int i = 0; i < N; i++) {
      if(heap.costs[i] == 999999999) heap.costs[i] = -1;
    }
    return heap.costs;
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
    this.cities = new City[N];
    this.costs = new int[N];
    for (int i = 0; i < N; i++) {
      cities[i].indexInHeap = cities[i].num;
      this.cities[i] = cities[i];

      // costs will not be based on position in heap, but on the number of the city
      this.costs[i] = 999999999;
    }

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

  public void bubbleUp(int index) {
    if (index > 0 && this.costs[this.cities[index].num] < this.costs[this.cities[(index-1)/2].num]) {
      this.swap(index, (index-1)/2);
      this.bubbleUp((index-1)/2);
    }
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
