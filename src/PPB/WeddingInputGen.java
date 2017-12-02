import java.util.*;
import java.io.*;

// generates input of a chosen size for the wedding.java file

// prints out generated input to console
class WeddingInputGen {
  public static int[] genInput() {
    Random rand = new Random();
    Scanner input = new Scanner(System.in);

    // Takes input for n, m and s
    int n = input.nextInt();
    if (n > 50000) n = 50000;

    int m = input.nextInt();
    if (m > 600000) m = 600000;

    int s = input.nextInt();
    if (s > n) s = n;

    int[] in = new int[3 + 3*m];
    in[0] = n;
    in[1] = m;
    in[2] = s;

    // generates random routes between the cities
    for (int i = 0; i < m; i++) {
      in[3 + i*3] = rand.nextInt(n) + 1;  // U
      in[4 + i*3] = rand.nextInt(n) + 1;  // V

      while (in[4 + i*3] == in[3 + i*3]) {
        in[4 + i*3] = rand.nextInt(n) + 1;  // V cannot equal U
      }

      in[5 + i*3] = rand.nextInt(5000); // D
    }

    return in;
  }




  public static void main(String[] args) {
    Random rand = new Random();
    Scanner input = new Scanner(System.in);

    try(BufferedWriter bw = new BufferedWriter(new FileWriter("input.txt"))){

    // Takes input for n, m and s
    int n = input.nextInt();
    if (n > 50000) n = 50000;

    int m = input.nextInt();
    if (m > 600000) m = 600000;

    int s = input.nextInt();
    if (s > n) s = n;

    int[] in = new int[3 + 3*m];
    in[0] = n;
    in[1] = m;
    in[2] = s;

    // generates random routes between the cities
    for (int i = 0; i < m; i++) {
      in[3 + i*3] = rand.nextInt(n) + 1;  // U
      in[4 + i*3] = rand.nextInt(n) + 1;  // V

      while (in[4 + i*3] == in[3 + i*3]) {
        in[4 + i*3] = rand.nextInt(n) + 1;  // V cannot equal U
      }

      in[5 + i*3] = rand.nextInt(5000) + 1; // D
    }

    String result = "";

    for (int i = 3; i < 3 + 3*m; ++i) {
      if (i % 3 == 2) {
        //result += in[i];
        //result += "\n";
        bw.write(in[i] + "\n");
        //System.out.println(in[i]);
      }
      else {
        //result += in[i];
        //result += " ";
        bw.write(in[i] + " ");
        //System.out.print(in[i] + " ");
      }
    }
    //bw.write(result);

    //System.out.println(result);
  }
  catch(IOException e) {
    e.printStackTrace();
  }
}
}
