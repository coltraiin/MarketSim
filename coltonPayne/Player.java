package coltonPayne;

public class Player {
	int assets = 4;
	int jewels = 1000;
	
	public void setJewels(int jewels) {
		this.jewels = jewels;
	}
	
	public int getJewels() {
		return jewels;
	}
	
	public void setAssets(int assets) {
		this.assets = assets;
	}
	
	public int getAssets() {
		return assets;
	}
	
	public void payoutAssets(int payout) {

		System.out.println("You have " + assets + " assets.");
		System.out.println("Asset dividend payment for this round is " + payout + " jewels.");
		
		int earnings = assets * payout;
		jewels += earnings;
		
		System.out.println("You earned " + earnings + " jewels from your assets.");
		System.out.println("You now have " + jewels + " jewels.");
		
	}
	
}
