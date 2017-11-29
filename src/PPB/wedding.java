import java.util.*;

public class wedding {
    static int N;
    static int M;
    static int S;

    static ArrayList<Route> routes;
    static ArrayList<City> cities;

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

        routes = new ArrayList<>(M);    // an ArrayList of all the routes that exist
        cities = new ArrayList<>(N);    // an ArrayList of all the cities

        for (int i = 0; i < N; i++) {   // adds N cities to the ArrayList
            cities.add(new City(i));
        }

        // adds all the new routes that are given to the ArrayList routes
        //for (int i = 3; i < M*3 + 3; i++) {
        for (int i = 0; i < M; i++) {
          /*
            int f = inputnums[i];//input.nextInt();
            i++;
            int t = inputnums[i];//input.nextInt();
            i++;
            */
            int f = input.nextInt();
            int t = input.nextInt();
            // need to subtract 1, since indexing SHOULD START AT ZERO
            Route r = new Route(cities.get(f - 1), cities.get(t - 1), input.nextInt());
            //Route r = new Route(cities.get(f - 1), cities.get(t - 1), inputnums[i]);//input.nextInt());
            routes.add(r);
        }

        int[] results = new int[N];

        // finds the distance from each city to the capital,
        // then adds each result to the
        for (City c : cities) {
            System.out.print(c.distanceToCapital(N, S, routes) + " ");
        }
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
            distance[i] = 999999999;
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
        if (distance[cap - 1] == 999999999) {
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
