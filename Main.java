import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

class Main
{

  public static String[][] observations;
  public static PrintWriter out;


  //==========================================================================
  //======== Test Data
  //==========================================================================


  public static void processHMM(String path, HMM hmm)
  {

    try
    {
      List<String> lines = Files.readAllLines(Paths.get(path));



      //Get N, M, T and init HMM
      String[] splitLine = lines.get(0).split(" ");
      hmm.initHMM(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]));

      //Get and Set State strings
      String[] states = lines.get(1).split(" ");
      for (int i = 0; i < states.length; i++)
      {
        hmm.states[i] = states[i];
      }

      //Get and set Output strings
      String[] outputs = lines.get(2).split(" ");
      for (int i = 0; i < outputs.length; i++)
      {
        hmm.outputs[i] = outputs[i];
      }

      //Get and Set A
      for(int i = 4; i < 8; i++)
      {
        String[] line = lines.get(i).split(" ");

        for(int j = 0; j < line.length; j++)
        {
          hmm.A[i-4][j] = Double.parseDouble(line[j]);
        }

      }

      //Get and Set B
      for(int i = 9; i < 13; i++)
      {
        String[] line = lines.get(i).split(" ");

        for(int j = 0; j < line.length; j++)
        {
          hmm.B[i-9][j] = Double.parseDouble(line[j]);
        }

      }


      //Get and Set Pi
      String[] initial = lines.get(14).split(" ");
      for(int i = 0; i < initial.length; i++)
      {

        hmm.pi[i] = Double.parseDouble(initial[i]);
      }

    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

  }

  public static void processObservations(String path)
  {
    try
    {
      List<String> lines = Files.readAllLines(Paths.get(path));

      int n = Integer.parseInt(lines.get(0));
      observations = new String[n][n];

      int index = 0;
      for(int i = 1; i < lines.size(); i+=2)
      {

        observations[index] = new String [Integer.parseInt(lines.get(i))];

        String[] splitLine = lines.get(i+1).split(" ");

        for(int j = 0; j < splitLine.length; j++)
        {
          observations[index][j] = splitLine[j];
        }

        index++;

      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

    //Check if observations are read correctly.
    // System.out.println("Observations: ");
    // System.out.println("\t" + Arrays.deepToString(observations));

  }

  private static void initWriter(String path)
  {
    File file = new File(path);
    try {
      out = new PrintWriter(file);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }



  //To verify the data was processed sucessfully.
  public static void printHMM(HMM hmm)
  {
    System.out.println("States: ");
    System.out.println("\t"+Arrays.toString(hmm.states));

    System.out.println("Outputs: ");
    System.out.println("\t"+Arrays.toString(hmm.outputs));

    System.out.println("Pi: ");
    System.out.println("\t"+Arrays.toString(hmm.pi));

    System.out.println("A: ");
    System.out.println("\t" + Arrays.deepToString(hmm.A));

    System.out.println("B: ");
    System.out.println("\t" + Arrays.deepToString(hmm.B));
  }

  public static void main(String[] args)
  {
    HMM hmm = new HMM();

    String hmmMethod = args[0]; //Which HMM method to run
    String pathHMM = args[1];
    String pathOBS = args[2];

    processHMM(pathHMM, hmm);
    // printHMM(hmm);

    processObservations(pathOBS);

    if (hmmMethod.equals("optimize"))
    {

      //prepare writer
      String writeFileName = args[3];
      initWriter(writeFileName);

      //Call optimization method

      //Write HMM
      hmm.outputHMM(out);
      out.close();
    }
    else if (hmmMethod.equals("statepath"))
    {

    }
    else if (hmmMethod.equals("recognize"))
    {

    }
    else
    {
      System.out.println(hmmMethod + " isn't a valid method.");
      System.out.println( "optimize, statepath, and recognize are valid methods.");

    }


  }

}
