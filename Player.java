
import java.util.Vector;

public class Player  {
		public  Vector playerPILE = new Vector(1);
		private int dealerValue;
		private int playerValue;
		public int aceValue;
		private int actionValue;
		
		private double weights[][] = new double[10][2];
		private double sigma[][] = new double [10][2];
		private TLUNode tlu_Node1;
		
	public Player ()
	{
		tlu_Node1 = new TLUNode(0,0,true );
	//	System.out.println( " Evaluvation\t" + tlu_Node1.tluEvaluvation(0.02, 0.1, 1, 1, true));
	}
	
	public void setParams(int dealerValue,  double weights[][], double sigma[][], int aceValue )
	{/*Function: Sets the Parameters for the Players.
		  *Returns: void.
		  *Inputs: Dealer Last Card Value,  weights(4), sigma(4), ace
		  *Ace Value is always set to true (to train for the worst-case).
		  */
		this.playerValue =0;
		for (int i = 0; i < playerPILE.size(); i++)
			this.playerValue += showCardValue((Card)playerPILE.get(i));
			
		this.dealerValue = dealerValue;
		
		for (int j =0; j < 10; j++)
		{
			System.arraycopy(weights[j],0,this.weights[j],0,weights[j].length);
			System.arraycopy(sigma[j],0,this.sigma[j],0,sigma[j].length);
		}
		this.aceValue = aceValue;

	}
	public void setAceValue( int aceVal )
	{//Resets the AceValue during Score Calculation.
			this.aceValue = aceVal;
		
	}
	public  static  int showCardValue(Card pCard)
	{//Returns the Value of the Card
		if (pCard.getCardNumber() > 10)
			return 10;

		else 
			return pCard.getCardNumber();
	}
	
	public  int getScore()
	{//Returns the Total Value of the Player Score.
		int playerScore = 0;
		int score;
		
		for (int i = 0; i < playerPILE.size(); i++)
		{
			score = showCardValue( (Card)playerPILE.get(i));
			if( score == 1)
			{//If the card was an ACE then check which value to use.
				if ( this.aceValue == 1)
					score = 11;
				//else
					//score = 1;
			}
			//----------------------------------------------------
			playerScore += score;
		
		}	
		return playerScore;
	
	}
	public  int getAction()
	{	  /*Function: Passes the weighted Value into the TLU & Calls the TLU.
		  *Returns: 0: Stand, 1: Hit
		  *Inputs: None.
		  */
		
		//actionValue = 1;//But TLU must check for both actions.
		this.playerValue = getScore();
		
		if (this.playerValue > 10 && this.playerValue <= 20)
			return ( tlu_Node1.tluEvaluvation((double)(dealerValue * weights[dealerValue - 1 ][0] * sigma[dealerValue - 1][0]), 
					(double)(playerValue * weights[this.playerValue -11][1] * sigma[this.playerValue -11][1]), true) );
		
		else if (this.playerValue > 20)
			return 0;// If the Player's Score is greater than 17 then always prefers to Stand.
		
		else
			return 1;// If the Player's Score is less than 11 then always prefers to HIT.
	}
	public void showPlayerCards()
	{	/*Function: Displays the Players cards.
		  *Returns: void.
		  *Inputs: none.
		  */
		Card pCard = new Card(0,0);
		System.out.println("-------Player Cards------");
		for (int i = 0; i < playerPILE.size(); i ++)
		{
			pCard = (Card)(playerPILE.get(i));//initialize the Card Object to pCard.
	       	System.out.println( "Player Cards:\t" + specificCard( pCard.getCardNumber()  ,  pCard.getSuit() ) );
		}
	}
	 private String specificCard( int crdNum, int suitNum)
	  {/* returns a specific card.*/
	    	String outCard;
	    	
	    	outCard = " " + crdNum + "  ";
	    	
	    	if ( suitNum == 0)
	    		outCard += " Hearts";
	    	else if ( suitNum  == 1)
	    		outCard += " Diamonds";
	    	else if ( suitNum  == 2)
	    		outCard += " Clubs";
	    	else if ( suitNum  == 3)
	    		outCard += " Spades";
	    	
	    return outCard;
	    }
	
}
