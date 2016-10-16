import java.util.*;

public  class  BJGame 
{/* Controls the Game Flow.
*/
	
	public  Vector mainDeckPILE = new Vector(1);
	
	public  Player player1 = new Player();//Player 1 with TLU algo.
	public  Dealer dealer1 = new Dealer();//Dealer 1 with simple algorithm.
	public int playerDollars;	//Total Player amount after 100 hands
	public boolean picked = false;	//If the thread ID was chosen to amoung top 20.
	
	public double weights[][] = new double[10][2];
	public double sigma[][] = new double[10][2];
	//To indicate the player/Dealer has an ACE card picked.
	private boolean playerAceCard = false, dealerAceCard = false;
	
	//-----------Constructor----------------
	public  BJGame (double wghtParams[][], double sigmaParams[][], Vector mdPILE, Vector pPILE, Vector dPILE)//constructor
	{	/*******************************************************************************************   
	     * Function: Initializes this Thread and at start of thread runs 100 hands 
		 * Inputs: { ( weights of Dealer's Up Card, Player's Total Card Value, 
		 * 			Ace value (if 1 or 11), Action (Stand/Hit)) & its corresponding Sigma Values }
		 ********************************************************************************************/
		//this.shuffleLoadVectors();
		this.mainDeckPILE = (Vector) mdPILE.clone();
		this.player1.playerPILE = (Vector) pPILE.clone();
		this.dealer1.dealerPILE = (Vector) dPILE.clone();
		for (int j =0; j < 10; j++)
		{
			System.arraycopy(wghtParams[j],0,weights[j],0,wghtParams[j].length);
			System.arraycopy(sigmaParams[j],0,sigma[j],0,sigmaParams[j].length);
		}
	}
	//	----------Overloaded constructor------------
	public BJGame ()
	{	/*******************************************************************************************  
		 * Function: Shuffles & loads Player, Dealer Cards.
		 * Inputs: none.
		 ******************************************************************************************* */
		this.shuffleLoadVectors();
	}
	public void setGameParams(double wghtParams[][], double sigmaParams[][], Vector mdPILE, Vector pPILE, Vector dPILE)//constructor
	{
		/*******************************************************************************************  
		 * Function: Set the Main Deck, Player Cards, Dealer Cards and the Weights.
		 * Inputs: weight[3], sigma[3],MainDeckPile, Player's Pile, Dealer's pile.
		 ******************************************************************************************* */
		this.mainDeckPILE = (Vector) mdPILE.clone();
		this.player1.playerPILE = (Vector) pPILE.clone();
		this.dealer1.dealerPILE = (Vector) dPILE.clone();
		for (int j =0; j < 10; j++)
		{
		System.arraycopy(wghtParams[j],0,weights[j],0,wghtParams[j].length);
		System.arraycopy(sigmaParams[j],0,sigma[j],0,sigmaParams[j].length);
		
		}
	}
	
	//----------Thread Starts, if called from the MainApplication----------------------
	public void startHand()
	{
		/*******************************************************************************************  
		 * Function: Plays the Black Jack.
		 * Inputs: none.
		 ******************************************************************************************* */
		int playerAction , dealerAction ;
		Card tempCard;
		//countGame +=1;
		//System.out.println("Starting Thread..." );
	//	for (int hands = 1; hands <= 100; hands ++)
		{
			playerAction = 1; dealerAction = 1;
			dealer1.aceValue = 0;
			player1.aceValue = 0;
			
			//--------------------New Set of Dealer, Player & Main Deck after first iteration-----
			player1.setParams( showCardValue((Card)dealer1.dealerPILE.lastElement()), 
									weights, sigma, 0 );//Ace Value set to 1 until the player goes burst.
			//------------------------------------------------------------------------------------
			while ( ((dealerAction == 1) || (playerAction == 1)) )//either of them stands;neither of them bursts.
			{//continue to player until both stands or either of them bursts.
				//-------------------------------------------------------------
				if ( playerAction != 0)// Player prefers to Stand
				{
					playerAction = player1.getAction();
					if ( playerAction == 1 )// Player chooses to hit
					{	//pick another card from the mainDeckPILE
						//System.out.println("----Player HITS----");
						player1.playerPILE.add((Card)mainDeckPILE.lastElement());
						mainDeckPILE.removeElement(mainDeckPILE.lastElement());
						tempCard = (Card)player1.playerPILE.lastElement();
						//------Ace Card picked; Set the Ace Flag.------
						if ( tempCard.getCardNumber() == 1)
						{	
							playerAceCard = true;
							player1.aceValue = 1;
						}
						
						//----------------------------------------------
						//System.out.println("Player Score:\t" + player1.getScore() + "hand:\t" + hands + this.getName() );
						
						// Player gets Bursted.
						if (player1.getScore() > 21)
						{
							if(( tempCard.getCardNumber() == 1  || playerAceCard == true ) &&  player1.aceValue == 1 )
							{	//Since the player's Score exceeds 21 using Ace as 11.
							//	System.out.println("*******Player SET ACE VALUE******\tScore" + player1.getScore());
								player1.aceValue = 0;
							}
							else
							{// bursts if the Ace Card is not considered as 11.
							//	System.out.println("Player Bursts!!" + tempCard.getCardNumber()  );
								playerDollars --;
								break;
							}
							 
						}
					}
				}// End of Player Action Choice.
				//------------------------------------------------------------
				if ( playerAction == 0)// If Player Stands then Dealer plays.
				{
					dealerAction = dealer1.getAction();
					if( dealerAction == 1 )// Dealer chooses to hit
					{
							//System.out.println("----Dealer HITS----");
							dealer1.dealerPILE.add((Card)mainDeckPILE.lastElement());
							mainDeckPILE.removeElement(mainDeckPILE.lastElement());
							tempCard = (Card)dealer1.dealerPILE.lastElement();

							//------Ace Card picked; Set the Ace Flag------
							if ( tempCard.getCardNumber() == 1)
							{
								dealer1.aceValue = 1;
								dealerAceCard = true ;
							}
							//----------------------------------------------
						
							//System.out.println("Dealer Score:\t" + dealer1.getScore() + "hand:\t" + hands + this.getName() );
						// Dealer gets Bursted.dealer1.getScore()
						if ( dealer1.getScore() > 21)
						{
							if ( (tempCard.getCardNumber() == 1 || dealerAceCard == true ) && dealer1.aceValue == 1 )
							{//Since the dealer's Score exceeds 21 using
								//dealer1.showDealerCards();
							//	System.out.println("*******SET ACE VALUE******\tScore" + dealer1.getScore());
								//player1.showPlayerCards();
								dealer1.aceValue = 0;
							}
							else 
							{//Does not bursts if the Ace Card is considered as 11.
							//	System.out.println("----Dealer Bursts----" );
								playerDollars ++;
								break;
							}
						}
					}// End of IF condition when Dealer chooses to hit
				}// End of Dealer Action Choice.
				
				// If both Stands.
				if ((dealerAction == 0) && (playerAction == 0))
					break;
				//--------------------------------------------------------------
			}// end of while
			
			if ((dealerAction == 0) && (playerAction == 0))
			{// If Both Stands then it breaks from the while loop and evaluvates the result.
			//	System.out.println("---Both Stands----");
			//	System.out.println("*******SET ACE VALUE******\tScore" + dealer1.getScore());
			//	System.out.println("*******Player SET ACE VALUE******\tScore" + player1.getScore());

				if (player1.getScore() >= dealer1.getScore())
					playerDollars ++;
				else
					playerDollars --;
			}
			
			
			//Re-initialize the AceCards Flags.
			playerAceCard = false; dealerAceCard = false;
		//	player1.showPlayerCards();
		//	dealer1.showDealerCards();
			//this.shuffleLoadVectors();
		}// End of For Loop
		//System.out.println("****End of Play:*****\t \tPlayer Dollars\t" + playerDollars + "\tThread Name:\t" + this.getName());
		//System.out.println("No. of Games:\t" + (countGame ) );
	}// End of Run
	
	public  static  int showCardValue(Card pCard)
	{/*******************************************************************************************  
		 * Function: Shows the Specific Card Value.
		 * Inputs: none.
		 ******************************************************************************************* */
		//-------Returns the Value of the Card-----------
		if (pCard.getCardNumber() > 10)
			return 10;
		else 
			return pCard.getCardNumber();
	}
	
	public void shuffleLoadVectors()
	{
		/*******************************************************************************************  
		 * Function: Shuffles & loads Player, Dealer Cards.
		 * Inputs: none.
		 ******************************************************************************************* */
		Card pCards[] = new Card[52];
		int count = 0;
		java.util.List list;
		
		for (int i = 1; i < 64; i++)
		{
			if ( (i % 16) != 0 && (i % 16) < 14 )
			{
				pCards[ count ] = new Card( i/16, i%16);
				count ++;
			}
		}
		list = Arrays.asList(pCards);
	//********Shuffle the Cards********************
		Collections.shuffle( list );
	//*********************************************
	//--------LOAD the shuffled cards back into the Object Array------------------
		pCards = (Card[])list.toArray(pCards);
		player1.playerPILE.clear();
		dealer1.dealerPILE.clear();
		mainDeckPILE.clear();
		//-----------------Loading...-----------------------------------------
		for (int i = 0; i < 2; i ++)// Add first 2 cards to Player 1
		{
			player1.playerPILE.add( pCards[i]);
			if (pCards[i].getCardNumber() == 1)
				playerAceCard = true;// Indicate an ACE card
				player1.aceValue = 1;
		}
		for (int i = 2; i < 4; i ++)// Add next 2 cards to Dealer 1
		{
			dealer1.dealerPILE.add( pCards[i]);
			if (pCards[i].getCardNumber() == 1)
				dealerAceCard = true;// Indicate an ACE card
				dealer1.aceValue = 1;
		}
		for (int i = 4; i < 52; i ++)// Add 21 cards to Main Deck
			mainDeckPILE.add( pCards[i]);
		//------------------------------------------------------------------------
		//System.out.println("**Initial Cards***");	
		//player1.showPlayerCards();
		//dealer1.showDealerCards();
	//---------end of loading----------------------------------------------------
	
		
	}	

	
}

