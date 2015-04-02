import java.io.*;
import java.util.Arrays;

class HMM
{

  //HMM description
  public int T;
  public int N;
  public String[] states;
  public String[] outputs;


  //Probabilities
  public double[] pi;//initialStates
  public double[][] A;//stateTransitions
  public double[][] B;//stateOutputs

  //Others
  // public double[][] alphas


  public void initHMM(int N, int M, int t)
  {
    T = t;
    this.N = N;
    states = new String[N];
    outputs = new String[M];

    pi = new double[N];
    A = new double[N][N];
    B = new double[N][M];
  }

  public int[] translateObservations (String[] obs)
  {
    int N = obs.length;
    int[] obsIndexes = new int[N];

    for(int i = 0; i < N; i++)
    {
      for(int j = 0; j < outputs.length; j++)
      {
        if (obs[i].equals(outputs[j]))
        {
          obsIndexes[i] = j;
          break;
        }
      }
    }

    return obsIndexes;

  }

  public void outputHMM(PrintWriter out)
  {

    out.printf("%d %d %d\n", states.length, outputs.length, T);

    //States
    for(int i = 0; i < states.length; i++)
    {
      out.print(states[i] + " ");
    }
    out.println();

    //Outputs
    for(int i = 0; i < outputs.length; i++)
    {
      out.print(outputs[i] + " ");
    }
    out.println();

    //A
    out.println("a:");
    for(int i = 0; i < A.length; i++)
    {
      for (int j = 0; j < A[i].length; j++)
      {
        out.printf("%.6f ", A[i][j]);
      }
      out.println();
    }

    //B
    out.println("b:");
    for(int i = 0; i < B.length; i++)
    {
      for (int j = 0; j < B[i].length; j++)
      {
        out.printf("%.6f ", B[i][j]);
      }
      out.println();
    }

    //Pi
    out.println("pi:");
    for(int i = 0; i < pi.length; i++)
    {
      out.printf("%.6f ", pi[i]);
    }
    out.println();


  }


  //==========================================================================
  //======== The HMM Methods
  //==========================================================================


  public void recognize(String[] observationStrings) {

    int T2 = observationStrings.length;
    int[] observations  = translateObservations(observationStrings);

    //forward procedure
    double[][] alpha = new double[T2][N];
    double prob = 0;

    //initialization
    for (int i = 0; i < N; i++) {
      alpha[0][i] = pi[i]*B[i][observations[0]];
    }

    //induction
    for (int t = 0; t < T2 - 1; t++) {
      for (int j = 0; j < N; j++) {
        double alpSum = 0;
        for (int i = 0; i < N; i++) {
          alpSum += (alpha[t][i]*A[i][j]);
        }
        alpha[t+1][j] = alpSum * B[j][observations[t+1]];
      }
    }

    //termination
    for (int i = 0; i < N; i++) {
      prob += alpha[T2-1][i];
    }

    System.out.println(prob);
  }


  public double statepath(String[] observationStrings)
  {

    //==========================================================================
    //======== Initilization
    //==========================================================================

    int T2 = observationStrings.length;
    int [] observations  = translateObservations(observationStrings);
    int[][] paths= new int [T][N];
    double[][] bestScores = new double [T][N];

    //Initilization step
    for (int n = 0; n < N; n++)
    {
      bestScores[0][n] = pi[n] * B[n][observations[0]];
      paths[0][n] = 0;
    }

    //==========================================================================
    //======== Recursion
    //==========================================================================

    double max;
    int argMax;
    double score;

    for (int t = 1; t < T2; t++)
    {

      for(int j = 0; j < N; j++)
      {

        max = 0;
        argMax = 0;

        for(int i = 0; i < N; i++)
        {
          score = bestScores[t - 1][i] * A[i][j];

          if (score > max)
          {
            max = score;
            argMax = i;
          }

        }

        bestScores[t][j] = max * B[j][observations[t]];
        paths[t][j] =  argMax;

      }

    }

    //==========================================================================
    //======== Termination
    //==========================================================================

    max = 0;
    argMax= 0;

    int[] path = new int[T2];

    for(int i = 0; i < N; i++)
    {
      score = bestScores[T2-1][i];

      if ( score > max )
      {
        max = score;
        argMax = i;
      }

    }

    double probT = max;
    path[T2-1] = argMax;

    //==========================================================================
    //======== Path Backtracking
    //==========================================================================

    for(int t = T2-2; t >= 0; t--)
    {
      path[t] = paths[t+1][path[t+1]];
    }

    //==========================================================================
    //======== Print Result
    //==========================================================================

    System.out.print(probT);

    if(probT > 0)
    {

      for(int t = 0; t < T2; t++)
      {
        System.out.printf(" %s", states[path[t]]);
      }

    }

    System.out.println();




    return 0;

  }

  public void optimize (String[] observationStrings)
  {
    int N = pi.length;
    int T2 = observationStrings.length;
    int [] observations  = translateObservations(observationStrings);

    double[][] alphas = new double[T2][N];
    double[][] betas = new double[T2][N];

    double sum, prob_O;

    //==========================================================================
    //======== Forward part
    //==========================================================================

    //Init
    for (int i = 0; i < N; i++)
    {
      alphas[0][i] = pi[i] * B[i][observations[0]];
    }


    //Induction
    for (int t = 1; t < T2; t++)
    {

      for (int j = 0; j < N; j++)
      {

        sum = 0;

        for (int i = 0; i < N; i++)
        {
          sum += alphas[t - 1][i] * A[i][j];
        }

        alphas[t][j] = sum * B[j][observations[t]];

      }

    }


    //Termination
    sum = 0;
    for (int i = 0; i < N; i++)
    {
      sum += alphas[T2-1][i];
    }

    prob_O = sum;


    System.out.printf("%.6f ", prob_O);


    //==========================================================================
    //======== Backward part
    //==========================================================================

    //Init
    for (int i = 0; i < N; i++)
    {
      betas[T2-1][i] = 1;
    }

    //Induction
    for (int t = T2-2; t >= 0; t--)
    {

      for (int i = 0; i < N; i++)
      {

        sum = 0;

        for (int j = 0; j < N; j++)
        {
          sum += A[i][j] * B[j][observations[t+1]] * betas[t+1][j];
        }

        betas[t][i] = sum;
      }

    }

    System.out.println("Beats: \n " + Arrays.deepToString(betas));



    //==========================================================================
    //======== Reestimation part
    //==========================================================================

    double[][][] xi = new double[T2][N][N];
    double[][] gammas = new double[T2][N];
    double[] probs_O = new double[T2];
    double sum2 = 0;


    //calculate probs
    for (int t = 0; t < T2 - 1; t++)
    {

      sum2 = 0;

      for (int i = 0; i < N; i++)
      {

        sum = 0;

        for (int j = 0; j < N; j++)
        {
          // System.out.println("t is " + t + " i is " + i + " j is " + j);
          sum += alphas[t][i] * A[i][j] * B[j][observations[t + 1]] * betas[t+1][j];
        }

        sum2 += sum;

      }

      probs_O[t] = sum2;

    }


    double p = 0;
    for (int t = 0; t < T2; t++)
    {
      p += probs_O[t];
    }



    //calculate Xi's and Gamma's

    for (int t = 0; t < T2 - 1; t++)
    {

      for (int i = 0; i < N; i++)
      {

        sum = 0;

        for (int j = 0; j < N; j++)
        {

          if(probs_O[t] != 0)
          {
            xi[t][i][j] = (alphas[t][i] * A[i][j] * B[j][observations[t + 1]] * betas[t+1][j]) / probs_O[t];
            sum += xi[t][i][j];
          }

        }

        gammas[t][i] = sum;

      }



    }

    System.out.println("Xis: \n " + Arrays.deepToString(xi));



    //Reestimate pi
    for (int i = 0; i < N; i++)
    {
      System.out.println("gamma is " + gammas[0][i]);
      pi[i] = gammas[0][i];

    }



    //Reestimate A
    boolean gamma = false;
    for (int i = 0; i < N; i++)
    {

      sum2 = 0;//for gammas


      for (int j = 0; j < N; j++)
      {

        sum = 0;

        for (int t = 0; t < T-1; t++)
        {
          sum += xi[t][i][j];

          if (gamma == false)
          {
            sum2 += gammas[t][i];
          }
        }

        gamma = true;
        if (sum2 != 0)
        {
          A[i][j] = sum / sum2;
        }
        else
        {
          A[i][j] = 0;
        }
      }

      gamma = false;
    }


    //Reestimate B

    for (int i = 0; i < N; i++)
    {

      sum = 0;//Numerator
      sum2 = 0;//Denominator

      for (int k = 0; k < outputs.length; k++)
      {

        for (int t = 0; t < T; t++)
        {

          if(B[i][k] == observations[t])
          {
            sum += gammas[t][i];
          }

          sum2 += gammas[t][i];
        }

        if (sum2 != 0)
        {
          B[i][k] = sum / sum2;
        }
        else
        {
          B[i][k] = 0;
        }
      }


    }



    recognize(observationStrings);

  }




}
