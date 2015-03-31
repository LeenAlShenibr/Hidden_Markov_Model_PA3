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




}
