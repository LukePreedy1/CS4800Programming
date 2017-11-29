import java.util.*;

public class CRInputGen {

// returns the results as an array of ints, rather than printing them
  public static int[] genInput() {
    Random rand = new Random();
    Scanner input = new Scanner(System.in);

    final int N = input.nextInt();
    final int M = input.nextInt();

    int[] in = new int[M*2 + 2];

    in[0] = N;
    in[1] = M;

    for (int i = 0; i < M * 2; i++) {
      in[i+2] = rand.nextInt(N) + 1;
    }
    return in;
  }

  public static void main(String[] args) {
    Random rand = new Random();
    Scanner input = new Scanner(System.in);

    final int N = input.nextInt();
    final int M = input.nextInt();

    int[] in = new int[M*2 + 2];

    in[0] = N;
    in[1] = M;

    for (int i = 0; i < M * 2; i++) {
      in[i+2] = rand.nextInt(N) + 1;
    }

    for (int i = 0; i < (M+1)*2; i++) {
      if (i%2 == 0) System.out.print(in[i] + " ");
      else System.out.println(in[i]);
    }
  }
}
