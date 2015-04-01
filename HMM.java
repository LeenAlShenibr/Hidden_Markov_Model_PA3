import java.io.*;
import java.util.Arrays;

class HMM
{

  //HMM description
  public int T;
  public String[] states;
  public String[] outputs;


  //Probabilities
  public double[] pi;//initialStates
  public double[][] A;//stateTransitions
  public double[][] B;//stateOutputs


  public void initHMM(int N, int M, int t)
  {
    T = t;
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


  public double statepath(String[] observationStrings)
  {

    //==========================================================================
    //======== Initilization
    //==========================================================================


    int N = pi.length;
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




}
