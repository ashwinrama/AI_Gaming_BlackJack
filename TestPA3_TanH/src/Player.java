

public class Player  {
		private int dealerValue;
		private int playerValue;
		public int aceValue;
		
		private double weights[] = new double [3];
		private double sigma[] = new double [3];
		private TLUNode tlu_Node1;
		
	public Player ()
	{
		tlu_Node1 = new TLUNode(0,0,0,0,true );
	}
	
	public void setParams(int dealerValue,  int playerValue, int aceValue, double weights[], double sigma[] )
	{	/***************************************************************************
		  *Function: Sets the Parameters for the Players.
		  *Returns: void.
		  *Inputs: Dealer Last Card Value,  weights(4), sigma(4), ace
		  *Ace Value is always set to true (to train for the worst-case).
		  **************************************************************************/
		
		this.playerValue = playerValue;	
		this.dealerValue = dealerValue;
		this.aceValue = aceValue;
		System.arraycopy(weights,0,this.weights,0,weights.length);
		System.arraycopy(sigma,0,this.sigma,0,sigma.length);
	}

	public int getAction()
	{	  /*************************************************************************
		  *Function: Passes the weighted Value into the TLU & Calls the TLU.
		  *Returns: 0: Stand, 1: Hit
		  *Inputs: None.
		  **************************************************************************/
		if (this.playerValue >= 10 && this.playerValue < 18)
			return ( tlu_Node1.tluEvaluvation((double)(dealerValue * weights[0] * sigma[0]), (double)(playerValue * weights[1] * sigma[1])
											, (double)(aceValue * weights[2] * sigma[2]), true) );
		else if (this.playerValue >= 18)
			return 0;// If the Player's Score is greater than 17 then always prefers to Stand.
		else
			return 1;// If the Player's Score is less than 11 then always prefers to HIT.
	}
}