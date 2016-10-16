import java.util.Vector;


public class Dealer {
	
	public  Vector dealerPILE = new Vector(1);
	public int aceValue;
	
	// Constructor
	public  Dealer()
	{
		aceValue = 1;//Set Ace value to 1; to consider ACE as 11 by default.
	}
	
	public int getAction() 
	{/******* Returns Hit/Stand depending on the score*******/
		if ( getScore() > 17)
			return 0;// Stand
		else
			return 1;// hit
	}
	
	public void setAceValue( int aceVal)
	{/***************Set the ACE value 0 or 1*****************/
		this.aceValue = aceVal;
	}
	public int getScore()
	{/******Returns the Total Value of the Dealer Score*******/
		int dealerScore = 0;
		int score;
		
		for (int i = 0; i < dealerPILE.size(); i++)
		{
			score = showCardValue( (Card)dealerPILE.get(i));
			if( score == 1)
			{//If the card was an ACE then check which value to use.
				if ( this.aceValue == 1)
					score = 11;
				//else
				//	score = 1;
			}			
			//-------------------------------------------------------
			dealerScore += score;
		}	
		return dealerScore;
	
	}
	public void showDealerCards()
	{	/****************************************************
			*Function: Displays the Players cards.
			*Returns: void.
			*Inputs: none.
		  ****************************************************/
		Card pCard = new Card(0,0);
		System.out.println("-----Dealer Cards-----");
		for (int i = 0; i < dealerPILE.size(); i ++)
		{
			pCard = (Card)(dealerPILE.get(i));//initialize the Card Object to pCard.
	       	System.out.println( "Dealer Cards:\t" + specificCard( pCard.getCardNumber()  ,  pCard.getSuit() ) );
		}
	}
	 public String specificCard( int crdNum, int suitNum)
	  {/***************** returns a specific card**********************/
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
	/**
	 * @return
	 */
	public  static  int showCardValue(Card pCard)
	{/********************Returns the Value of the Card**********************/
		if (pCard.getCardNumber() > 10)
			return 10;
		else 
			return pCard.getCardNumber();
	}
	
}
