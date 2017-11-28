// For each city, perform the strongly connected component thing
// If it is all in one component, then add 1 to the total, and do the rest
// If it splits into another component, then immediately return without adding 1, then do the rest

import java.util.*;

public class CapitalRelocation {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    final int N = input.nextInt();
    final int M = input.nextInt();

    ArrayList<Route> routes = new ArrayList<>();
    ArrayList<City> cities = new ArrayList<>();

    for (int i = 0; i < N; i++) {
      cities.add(new City(i));
    }

    for (int i = 0; i < M; i++) {
      routes.add(new Route(input.nextInt(), input.nextInt()));
    }

    // TODO find the number of cities that can reach every other city
    // Use the method demonstrated in the notes, said exactly how to do it

    int result = numFullyConnected(cities, routes);


    // Then print the results, which should be a single integer for the number
    // of cities that can reach every other city
  }

  // returns number of cities that are fully connected
  static int numFullyConnected(ArrayList<City> cities, ArrayList<Route> routes) {

  }
}

class City {
  int num;
  Boolean visited;

  City(int num) {
    this.num = num;
    this.visited = false;
  }
}

class Route {
  City from;
  City to;

  Route(City from, City to) {
    this.from = from;
    this.to = to;
  }

  public void printRoute() {
    System.out.println(this.from.num + " " + this.to.num);
  }
}
