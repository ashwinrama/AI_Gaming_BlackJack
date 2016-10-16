import java.util.*;
import java.io.*;
import java.text.*;
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
	private static double weightArray [][][] = new double [40][10][2];//40 Networks of 10 Single i/s representing 
	private static double sigmaArray [][][] = new double [40][10][2];	//Dealer and Player.
	
	private static int maxAmount = 0, minAmount = 0;
	private static double mean =0, stdDev = 0;
	private static String outFN = null;
	private static int  currGen = 10000 ;
	
	public static void main(String[] args) 
	{
		
		System.out.println("Running trainPA3 with Sigmoid...");
		if (args.length == 0 || args.length < 2)
		{
			System.out.println("Program Exits.  Restart the program with 'numGen' argument and the 'fileName' argument.");
			System.exit(0);
		}
		//-----Create Objects-----------------
		BJGame thread_BJ[] = new BJGame [40];
		int pDollarArray[] = new int [40];//Array of Player's Dollars from all the 40 NN's 
		int sumAmount = 0;
		double prev_WeightArray [][][] = new double [40][10][2];//40 Networks of 4 inputs each
		double prev_SigmaArray [][][] = new double [40][10][2];

		List list;
		
		//Load the File Name from the argument.
		outFN = args[1];
		//--------------------------------------------------------------------------------
		//if (args.length > 2)
			//readInitialPopulation(args[2]);//read the Initial Population from the file
		//else
			generateInitialPopulation();//Generate Initial Population
		/*-------------------------------------------------------------------------------
		 *A Shuffled set of initial Main Deck and the Player's , Dealer's set of Cards
		  are initialized.  This same set of cards will be provided to the 40 NNs.*/
			BJGame beginBJGame = new BJGame();
		//***********************************************************************	
		long startTime = System.currentTimeMillis();
		//--------Begin Generations------------------------------------------------------------
		for (int generation =1; generation <= Integer.parseInt(args[0]) ; generation++)
		{												//Args[0]= numGen
			generateOffSpring();
			//----------------------100 HANDS-------------------------------------------------
			for (int hands =1; hands <=100; hands ++)
			{
				//**************PLAY A HAND********************************************	
				for (int i = 0; i<40; i++)
				{
					if (generation == 1 && hands == 1)
						thread_BJ[i] = new BJGame(weightArray[i],sigmaArray[i], beginBJGame.mainDeckPILE 
												,beginBJGame.player1.playerPILE, beginBJGame.dealer1.dealerPILE);
					else
						thread_BJ[i].setGameParams(weightArray[i],sigmaArray[i], beginBJGame.mainDeckPILE 
												,beginBJGame.player1.playerPILE, beginBJGame.dealer1.dealerPILE);
					//-------Start the Hand------------//
					thread_BJ[i].startHand();
					
				}
				//*********************************************************************	
				beginBJGame.shuffleLoadVectors();
			}
			//----------------------End of 100 hands------------------------------------------
				for (int i=0; i < 40; i++)
				{
					pDollarArray[i] = thread_BJ[i].playerDollars;
				}
				Arrays.sort(pDollarArray);
				sumAmount = 0;		
				//---------Select the Top 20 best Weighted Values------------
				for (int i = 39; i >= 20; i--)
				{
					for (int j = 0; j < 40; j++)
					{
						if ( (pDollarArray[i] == thread_BJ[j].playerDollars) && thread_BJ[j].picked == false )
						{
							weightArray[39 - i] = thread_BJ[j].weights;
							sigmaArray [39 - i] = thread_BJ[j].sigma;
							//System.out.println("thread\t" + j + "\t" + thread_BJ[j].weights[0][0] + "\t" + thread_BJ[j].sigma[0]);
							thread_BJ[j].picked = true;
							break;
						}
						
					}
					sumAmount += pDollarArray[i];// Total Sum.
				}
				//--------Max.--------Min.--------Mean-------Std.Dev.-------------------------
				mean = sumAmount / 20;
				//Skip the generation if the results are poor.i.e < -8
				if (mean >= -5)
				{
					maxAmount = pDollarArray[39];
					minAmount = pDollarArray[20];
					
					stdDev = calculateStdDev(mean, pDollarArray);
					sumAmount = 0;
					
					//	System.out.println(currGen)	;
					//System.out.println("Args" + args[1]);
					logStatistics(Integer.parseInt(args[0]));
					
					if (generation % 10000 == 0)//For Every 10000 generation put in a new file.
						currGen += 10000;
					//Copy the best weights
					for (int i =0; i < 40; i++)
					{
						for (int j =0; j < 10; j ++)
						{
							for (int k =0; j < 10; j ++)
							{
							weightArray[i][j][k]=prev_WeightArray[i][j][k];
							sigmaArray[i][j][k]= prev_SigmaArray[i][j][k];
							}
						}
					}
					//*********************SAVE the Last Generation ******************************
					if ( generation == Integer.parseInt(args[0]) )
					{	
						saveGeneration(generation);
					}
				}
				else
				{
					generation --;//Skip the generation since the results were poor
					//use the Old best Weights to the current weights.
					for (int i =0; i < 40; i++)
					{
						for (int j =0; j < 10; j ++)
						{
							for (int k =0; j < 10; j ++)
							{
							prev_WeightArray[i][j][k]=weightArray[i][j][k];
							prev_SigmaArray[i][j][k]=sigmaArray[i][j][k];
							}
						}
					}
				}
				//****************************************************************************
				for (int i =0; i < 40; i++)
				{
					//System.out.println(" Sorted Array:\t" + pDollarArray[i]);
					thread_BJ[i].picked = false;
					thread_BJ[i].playerDollars = 0;//Reset for next Generation
				//	thread_BJ[i].destroy();
					
				}
		}
		long stopTime = System.currentTimeMillis();
		
		System.out.println("Total Time Taken:\t" + (stopTime - startTime)/1000 + " Seconds");
		//-----------End of Generations---------------------------------------------
		
	}
	private static void logStatistics(int gen)
	{	/*Function: Prints: Mean; Max; Min; stdDev in the Stats_'gen'.txt file
		  *Returns: none.
		  *Inputs: the no. of the generations.
		  */
		
		try
		{
			
			//------------------------------------------------------------------------
			FileOutputStream outFileStream = new FileOutputStream( "stats_"+ currGen + outFN,true);
			DataOutputStream logFile = new DataOutputStream( outFileStream  );
			//System.out.print("\r" + currGen++ );
			DecimalFormat x = new DecimalFormat("#00.0000");
			//-----Write the Max------------	
			{//System.out.println("mean\t" + x.format(mean));
			logFile.writeUTF(x.format(mean) );//Mean value of the best 20 elements- frequency of winning
			logFile.writeChars(";");
			logFile.writeUTF(Integer.toString(maxAmount).trim());//Max. frequency of winning for 100 hands
			logFile.writeChars(";");
			logFile.writeUTF(Integer.toString(minAmount).trim());//Min. frequency of winning for 100 hands
			logFile.writeChars(";");
			logFile.writeUTF( x.format(stdDev) );//Std Dev. of winning for 100 hands
			logFile.writeChars("\n");
			}
			logFile.close();
			//--------------------------------------------------------------------------
			
		//appendToFile
		}
		catch (IOException io)
		{
			System.out.println("The File could not be opened");
		}
		
	}
	private static void saveGeneration(int gen)
	{
		try
		{
			//------------------------------------------------------------------------
			FileOutputStream outFileStream = new FileOutputStream( outFN   ,true);
			DataOutputStream logFile = new DataOutputStream( outFileStream  );
			DecimalFormat x = new DecimalFormat("#0.00000");
			//-----Write the Max------------	
			for (int i =0; i < 20; i++)
			{
				for (int j =0; j < 10; j++)
				{
					for (int k =0; k < 2; k++)
					{
					logFile.writeUTF(x.format(weightArray[i][j][k]));//Mean value of the best 20 elements- frequency of winning
					//System.out.print( "j "+ j + "i " + k +" "+ weightArray[i][j][k] + "\t");
					logFile.writeChars(";");
					}
					//logFile.writeChars("\n");
				}
				//logFile.writeChars("\n");
				for (int j =0; j < 10; j++)
				{
					for (int k =0; k < 2; k++)
					{
					logFile.writeUTF(x.format(sigmaArray[i][j][k]));//Mean value of the best 20 elements- frequency of winning
					//System.out.print(sigmaArray[i][j][k] + "\t");
					logFile.writeChars(";");
					}
					//logFile.writeChars("\n");
				}
				logFile.writeChars("\n");
			}
			logFile.close();
			//--------------------------------------------------------------------------

		//appendToFile
		}
		catch (IOException io)
		{
			System.out.println("The File could not be opened");
		}
	}
	private static float calculateStdDev(double mean, int[] pDollarArray)
	{	/*********************************************************************************
	      *Function: Calculates the Standard Deviation.
		  *Returns: the Calculated StDev.
		  *Inputs: Mean of the Dollar Amount, the top 20 elements of the playerDollars.
		  *********************************************************************************/
		double variance = 0;
		//-----------------Calculate Variance------------------------------
		//System.out.println(" Mean "+ mean);
		for (int i = 39; i >= 20; i--)
			variance += (float)Math.pow( (pDollarArray[i] - mean), 2 );//Square value.

		//------------------------------------------------------------------
		return ((float) Math.sqrt(variance)/ 19 );//Return Standard Deviation.
	}
	private static void generateInitialPopulation()
	{	/********************************************************************************
			*Function: Generates initial population of 20. weights & sigmas.
			*Returns: void.
			*Inputs: none.
		  ********************************************************************************/
		Random rnd = new Random ();// Uniform distribution 0.0 to 1.0
		double uniRndDis;
		for (int i = 0; i <= 19; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				for (int k =0; k < 2; k++)
				{
				uniRndDis = rnd.nextDouble();
				//-------weight of NN -0.2 to 0.2--------
				if ( uniRndDis < 0.5 )
					weightArray[i][j][k] = -0.2 * uniRndDis;
				else
					weightArray[i][j][k] = 0.2 * uniRndDis;
				//---------------------------------------
				//sigma set to 0.05.
				sigmaArray[i][j][k] = 0.05;
				//System.out.println("\t"+weightArray[i][j][k]);
				}
			}
		}
	}
	
	private static void generateOffSpring()
	{
		/***********************************************************************************
		  *Function: Generates 20 Off Springs of weights & sigmas using Gaussian Function.
		  *Returns: void.
		  *Inputs: none.
		  **************************************************************************************/
		Random rnd = new Random ();// Random distribution 
		final  double  gamma = 0.000025;// Gamma = 1/(2*(sqrt(no. of weights))), where no. of weights = 4.
		
		for (int i = 20; i <= 39; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				for (int k =0; k < 2; k++)
				{
				//New sigma = oldSigma * e^(gamma * gauss()).
				sigmaArray[i][j][k] = (double)( sigmaArray[i - 20][j][k] * ( Math.exp( gamma * rnd.nextGaussian() ) ) );
				//New weight of NN = oldWeight + newSigma( * gauss() ) )
				weightArray[i][j][k] = weightArray[i - 20][j][k] + (double) ( sigmaArray[i][j][k] * ( rnd.nextGaussian())  );
				}
			}
		}
	}

	
}
