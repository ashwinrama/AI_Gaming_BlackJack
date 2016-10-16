
public class TLUNode 
{
	/**CLASS: Second Layer of the NN
	 * INPUTS: Weighted Values of
	 * @param dealer_UpValue
	 * @param player_TotalValue
	 * @param aceVal
	 * @param actionVal
	 * @param bias
	 */
	private double dealer_UpValue;
	private double player_TotalValue;
	private double aceVal;
	private double actionVal;
	private boolean bias;
	
	public TLUNode( double dealer_UpValue,double player_TotalValue,  boolean bias )
	{
		/* 
		 * Function: Initializes the variables for the player Node.
		 * Inputs: ( Weighted VALUES ) Value of Dealer's Up Card, Player's Total Card Value, 
		 * 			Ace value (if 1 or 11), Action (Stand/Hit)
		 */
		setPlayerNode(dealer_UpValue,player_TotalValue , bias);
	}
	
	private void setPlayerNode( double dealer_UpValue,double player_TotalValue , boolean bias )
	{	/* Function: Sets the input node values to the private vars.
		 * Return: void
		 * Inputs: ( Weighted VALUES ) Value of Dealer's Up Card, Player's Total Card Value, 
		 * 			Ace value (if 1 or 11), Action (Stand/Hit) */ 
		this.dealer_UpValue = dealer_UpValue;
		this.player_TotalValue = player_TotalValue;
		//this.aceVal = aceVal;
		this.bias = bias;
	}
	
	public int tluEvaluvation(double dealer_UpValue,double player_TotalValue ,  boolean bias)
	{	/*	Function: Second Layer of NN -TLU NODE calls the sigmoid function.
			Returns: the value of the Non-linear sigmoid function.
			Inputs:  Weighted values after the input Layer of the NN.
		*/
		double weightedSum = 0;
		//double standValue, hitValue;
		if ( bias )//True
		{
			//Sum of all the Weighted Values.
			weightedSum = (dealer_UpValue);
			weightedSum += (player_TotalValue);
			//weightedSum += (aceVal);
			//weightedSum += actionVal;
		}
		//System.out.println(" Args: \t" + dealer_UpValue + " \t " + player_TotalValue + " \t " +  aceVal + " \t " +   actionVal + " \t " +  bias);
		//System.out.println(" Weighted Sum:\t " + weightedSum);
		
		weightedSum =tfSigmoid(weightedSum);// On Hit check the sigmoid function
		//standValue = tfSigmoid(weightedSum - actionVal);//On Stand action Value = 0
		//System.out.println(" Weighted Sum:\t " + weightedSum);
	//	System.out.println( "<< HitValue:\t" + hitValue + " standValue:\t" + standValue +">>");
		
		if (weightedSum > 0.0)//For Sigmoid Center is 0.5
			return 1;//Hit 
		else
			return 0;// Stand
	}
	
	private double tfSigmoid(double wSum)
	{/*Function: Transfer Function: a Sigmoid Function: 1/(1 + e^-(x))
	  *Returns: the value of the corresponding weighted Sum.
	  *Inputs: The weighted sum.
	  */
		final double a =1.716;
		final double b = (double)2/3;
		
	    try 
		{
	     // return (1/( (double) (1 + Math.exp(-( wSum ) ))));
	     // System.out.println  ( ( 2 * a /( 1 + Math.exp( -b * wSum) ) )  - a );
	      return ( ( 2 * a /( 1 + Math.exp( -b * wSum) ) )  - a );

	    }
	    catch (ArithmeticException e) 
		{
	      System.out.println("Error in sigmoid calculation.");
	      return 1d;
	    }
	}
}
