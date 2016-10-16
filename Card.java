

public class Card {
	/********************************************************************************
	*CLASS: Class Structure representing a single card.							 	*
	*********************************************************************************/	
	private int suit;	//0 - hearts, 1: diamonds, 2: clubs, 3: spades
	private int cardNumber;	//1-10 11: jack, 12: queen, 13: king

	//constructor
	public Card ( int theSuit, int theCardNumber)
	{
		setCardValues (theSuit, theCardNumber);
	}
	
	//set Card values
	private void setCardValues( int theSuit, int theCardNumber)
	{
		suit = theSuit;
		cardNumber = theCardNumber;
//		location = theLocation;
	}
	// return values
	public int getSuit()
	{
		return suit;
	}
	
	public int getCardNumber()
	{
		return cardNumber;
	}

}

