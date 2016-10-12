
import java.io.*;

/*
 * Created on Oct 25, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author aswinr
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MainApplication {
	private static double weightArray [] = new double [3];//40 Networks of 4 inputs each
	private static double sigmaArray [] = new double [3];
	
	public static void main(String[] args) 
	{
		final int dealerArray[] = {1,2,3,4,5,6,7,8,9,10};
		final int  playerArray[] = {21,20,19,18,17,16,15,14,13,12,11};
		int outputArray[][] = new int [playerArray.length][dealerArray.length];
		
		if (args.length == 0)
		{
			System.out.println("Program Exits.  Restart the program with 'inNN' argument and the 'game state' arguments.");
			System.exit(0);
		}
		
		//-----Set the Weight & Sigma Params---------------------------------
		readGameParams(args[0]);//The fileName specified in the command Line.
		
		Player player1 = new Player();
			
		//<<<<<<<<<<<Graph of Usable ACE & Non-Usable Ace>>>>>>>>>>>>>>>>>>>>>>>>>
		System.out.println("*********Non-usable ACE - Player Sum Vs Dealer Show Card********");
		for (int usableAce =0; usableAce <=1; usableAce ++)
		{
			for (int i = 0; i < playerArray.length; i++)
			{
				for (int j = 0; j < dealerArray.length; j++)
				{
					player1.setParams( dealerArray[j], playerArray[i], usableAce, weightArray, sigmaArray );
					outputArray[i][j] = player1.getAction();
					System.out.print(outputArray[i][j] + " ");
				}
				//-----------print the Y-Axis------------------
				System.out.print(" | " + playerArray[i]);
				System.out.print("\n");
			} 
			//-----------print the X-Axis--------------------
			for (int i = 0; i < dealerArray.length; i++)
			{
				System.out.print( dealerArray[i] + " ");
			}
			if (usableAce == 0)
			System.out.println("\n\n*******Usable ACE - Player Sum Vs Dealer Show Card*******");
		}
		
	
		player1.setParams( Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), weightArray, sigmaArray );
	
		System.out.println("\n\nOutput for given Input Game State:( 0 -> Stand, 1 -> Hit) = \t" + player1.getAction());
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	}
	private static void readGameParams(String fPath )
	{
		/*******************************************************************
		  *Method: Reads the specified file by line and stores the integers *
		  * in Numbers array												*
		  *Output: void                     								*
		  *Input: fileName object that is specified while Openning the file *
		  *******************************************************************/
		  double nDP[] = new double[4];//no. of cards in the deck pile.
		  int node = 0;
		  String stg = null;
		  String rString[] = new String [4];
		  File fileName;
		  boolean eof = false;
		  fileName = new File (fPath);
		   try
		   {
		   	//System.out.println("Executing 2...");
				//--------open file---------------------------------
		   	     
		   	     System.out.println("Reading from file path[TLU -> Sigmoid()]:\t" + fileName.getPath());
				 DataInputStream dataIn = new DataInputStream(new FileInputStream(fileName));
				 stg = dataIn.readLine();
				 while (stg != null )
				 {
					 stg.trim();
					 rString = (stg.split(";"));
			         //System.out.println("Reading lIne" + rString[5]);
			         for (int i =0; i < 3; i++)
			         {
			         	//System.out.println("rid" + Double.parseDouble(rString[i].trim()) );
			         	weightArray[i] = Double.parseDouble(rString[i].trim());
			         }
			         for (int i =3; i <  rString.length -1; i++)
			         {
			         	//System.out.println("rid" + Double.parseDouble(rString[i].trim()) );
			         	sigmaArray[i - 3] = Double.parseDouble(rString[i].trim());
			         }
			         node ++;
			         stg = dataIn.readLine();//Read the next Line.
				 }//while EOF is false
				 dataIn.close();
		     } 

		 catch (EOFException eo)
		 {
		 	eof = true;
		 }
		 catch(IOException e)
	      {
	      	 System.out.println("Reading from file path:\t" + fileName.getPath());
	         System.out.println("Problem finding file. Program exits.");
	         System.exit(0);
	      }	
	}
	
}
