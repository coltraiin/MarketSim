package coltonPayne;

import java.util.Random;

public class AI {

	public AI(int id) {
		this.id = id;

	}

	int buyPrice;
	int sellPrice;
	int assetValue;
	int assetsAI = 4;
	int jewelsAI = 1000;
	int id;
	int range;
	public boolean madePurchase;

	public void setAssetValue(int assetValue) {
		this.assetValue = assetValue;
	}

	public int getAssetValue() {
		return assetValue;
	}

	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}

	public int getBuyPrice() {
		return buyPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setJewelsAI(int jewelsAI) {
		this.jewelsAI = jewelsAI;
	}

	public int getJewelsAI() {
		return jewelsAI;
	}

	public void setAssetsAI(int assetsAI) {
		this.assetsAI = assetsAI;
	}

	public int getAssetsAI() {
		return assetsAI;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getRange() {
		return range;
	}

	public int calcAssetValuation(int round) {
		int buyVal;
		Random rand = new Random();
		int offset = rand.nextInt(50) + 1;
		int chance = rand.nextInt(2) + 1;

		if (chance % 2 == 0) {
			offset *= -1;
		}

		buyVal = assetValue(round) + offset;

		return buyVal;
	}

	public void payoutAssets(int payout) {
		
		

		jewelsAI += assetsAI * payout;
		
		
	}

	public int calcPriceRange() {
		int range = 0;
		Random rand = new Random();
		int num = rand.nextInt(5) + 1;

		switch (num) {
		case 1:
			range = 5;
			break;

		case 2:
			range = 10;
			break;

		case 3:
			range = 15;
			break;

		case 4:
			range = 20;
			break;

		case 5:
			range = 25;
			break;
		}

		return range;
	}

	public int assetValue(int round) {
		int price = 0;

		switch (round) {
		case 1:
			price = 490;
			break;

		case 2:
			price = 540;
			break;

		case 3:
			price = 600;
			break;

		case 4:
			price = 580;
			break;

		case 5:
			price = 555;
			break;

		case 6:
			price = 595;
			break;

		case 7:
			price = 615;
			break;

		case 8:
			price = 669;
			break;

		case 9:
			price = 742;
			break;

		case 10:
			price = 769;
			break;

		case 11:
			price = 680;
			break;

		case 12:
			price = 720;
			break;

		case 13:
			price = 400;
			break;

		case 14:;
			price = 150;
			break;

		case 15:
			round = 15;
			price = 0;
			break;
	

		}
		return price;
	}

}
