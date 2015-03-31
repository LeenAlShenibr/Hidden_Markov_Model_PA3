import java.io.*;

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




}
