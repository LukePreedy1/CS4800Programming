import java.util.*;

public class wedding {
    static int N;
    static int M;
    static int S;

    static ArrayList<Route> routes;
    static ArrayList<City> cities;

    public static void main(String[] args) {

      // TODO remove this comment when all is working find, this just controlls the input
        //Scanner input = new Scanner(System.in); TODO

        int[] input = genInput();

        N = input[0];//input.nextInt();  TODO      // N = number of cities
        M = input[1];//input.nextInt();  TODO      // M = number of train routes
        S = input[2];//input.nextInt();  TODO      // S = city number of the capital

        routes = new ArrayList<>(M);    // an ArrayList of all the routes that exist
        cities = new ArrayList<>(N);    // an ArrayList of all the cities

        for (int i = 0; i < N; i++) {   // adds N cities to the ArrayList
            cities.add(new City(i));
        }

        // adds all the new routes that are given to the ArrayList routes
        for (int i = 0; i < M; i++) {
            int f = input[3 + i*3];//input.nextInt(); TODO change this when done
            int t = input[4 + i*3];//input.nextInt(); TODO
            // need to subtract 1, since indexing SHOULD START AT ZERO
            Route r = new Route(cities.get(f - 1), cities.get(t - 1), input[5 + i*3]);//input.nextInt()); TODO
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

    static int[] genInput() {
      Random rand = new Random();

      int n = rand.nextInt(500) + 1;  // will be N
      int m = rand.nextInt(6000) + 1; // will be M
      int s = rand.nextInt(n) + 1;      // will be S

      int[] in = new int[3 + 3*m];
      in[0] = n;
      in[1] = m;
      in[2] = s;

      for (int i = 0; i < m; i++) {
        in[3 + i*3] = rand.nextInt(n) + 1;  // U
        in[4 + i*3] = rand.nextInt(n) + 1;  // V

        while (in[4 + i*3] == in[3 + i*3]) {
          in[4 + i*3] = rand.nextInt(n) + 1;  // V cannot equal U
        }

        in[5 + i*3] = rand.nextInt(5000) + 1; // D
      }

      return in;
    }
}

class City {
    int num;    // number of the city

    City(int num) {         // can initialize the city with no leaving routes
        this.num = num;
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
