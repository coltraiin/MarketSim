package coltonPayne;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;

//TODO AI can sell assets it doesnt have

public class Sim extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Swing Objects
	JLabel timeLeft;
	JTextArea console;
	JTextField textField1;
	JButton button1;
	JButton button2;
	JLabel info;
	JButton button3;
	JButton button4;
	JButton button5;
	JButton button6;
	JButton button7;
	JLabel roundNum;
	
	Timer timer = new Timer(5000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			decideBuyOrSell();
		}
	});

	// Player and AI Objects
	Player player = new Player();

	AI a = new AI(1);
	AI b = new AI(2);
	AI c = new AI(3);
	AI d = new AI(4);
	AI e = new AI(5);
	AI f = new AI(6);
	AI g = new AI(7);
	AI h = new AI(8);
	AI i = new AI(9);

	// Global vars

	public static long time = 0;
	public boolean advance = false;
	public static int round = 1;

	public static int highBuy = 0;
	public static int lowSell = 90000;
	public static int sellID = 0;
	public static int buyID = 0;
	public static int sellOfferID = 0;
	public static int buyOfferID = 0;
	
	public static int counter = 0;

	public Sim() {
		this.setSize(800, 800);

		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Econ Simulation");

		JPanel thePanel = new JPanel();

		console = new JTextArea(30, 60);
		console.setText("");

		thePanel.add(console);

		console.setLineWrap(true);
		console.setWrapStyleWord(true);

		JScrollPane scrollbar1 = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		new SmartScroller(scrollbar1);
		thePanel.add(scrollbar1);

		button1 = new JButton("View Inventory");
		OpenInventory inventory = new OpenInventory();
		button1.addActionListener(inventory);

		thePanel.add(button1);

		
		button3 = new JButton("Accept Buy Offer");
		BuyAsset buyAsset = new BuyAsset();
		button3.addActionListener(buyAsset);

		thePanel.add(button3);

		button4 = new JButton("Make Buy Offer");
		MakeBuyOffer makeBuyOffer = new MakeBuyOffer();
		button4.addActionListener(makeBuyOffer);

		thePanel.add(button4);

		button5 = new JButton("Accept Sell Offer");
		SellAsset sellAsset = new SellAsset();
		button5.addActionListener(sellAsset);

		thePanel.add(button5);

		button6 = new JButton("Make Sell Offer");
		MakeSellOffer makeSellOffer = new MakeSellOffer();
		button6.addActionListener(makeSellOffer);
		
		thePanel.add(button6);
		
		button7 = new JButton("End round");
		EndRound endRound = new EndRound();
		button7.addActionListener(endRound);
		
		thePanel.add(button7);

		timeLeft = new JLabel("================\nTime left: \n==============");

		thePanel.add(timeLeft);
		
		JLabel roundNum = new JLabel("Round : " + round + " out of 15");
		
		thePanel.add(roundNum);

		this.add(thePanel);

		this.setVisible(true);

	}

	public static void main(String[] args) {
		Sim sim = new Sim();

		sim.getUserInfo();
		sim.startTutorial();
		sim.setAIPriceRange();
		sim.startSim();

	}

	public void startSim() {

		console.setText("");	

		if (round != 1)
			if(round != 15) {
			payoutAssets();
			}
			resetBuyVal();
			resetSellVal();

		if (round <= 15) {

			writeInfo.saveToFile("ROUND " + round + " STARTED ");
			timer.start();
			JOptionPane.showMessageDialog(console, "Starting round " + round + ".");
			time(true);

			console.append("Click on a button to do stuff.\n");
			setAIValuations();
			setAIBuyAndSellPrices();
			decideBuyOrSell();
			decideBuyOrSell();
		}

		else {
			JOptionPane.showMessageDialog(console, "Simulation over.  Your net worth is " + player.getJewels() + " Jewels.");
			writeInfo.saveToFile("TOTAL JEWELS: " + player.getJewels());
			

		}
	}
	
	public void startTutorial() {
		JOptionPane.showMessageDialog(console, "Click to begin the tutorial.");
		JOptionPane.showMessageDialog(console, "Welcome to my market simulation.  Any personal information will not be shared in the Regional Science Fair.");
		JOptionPane.showMessageDialog(console, "The aim of the simulation is to earn as many Jewels as possible through the fifteen rounds of play.  Each round will last one minute long.");
		JOptionPane.showMessageDialog(console, "You and nine computers begin the simulation with 1000 Jewels and four unknown assets.");
		JOptionPane.showMessageDialog(console, "Each asset will pay the user a random dividend at the end of the round between 100 and zero jewels.");
		JOptionPane.showMessageDialog(console, "This is a simulation of a live marketplace.  Both players and computers have the ability to make offers to buy and sell assets each round.");
		JOptionPane.showMessageDialog(console, "If you wish to accept a computer's buy or sell offer, click the specified button.");
		JOptionPane.showMessageDialog(console, "You must place competitive buy or sell offers.  This means that if you want to buy an asset, you must offer more money than the current high buy offer.");
		JOptionPane.showMessageDialog(console, "Conversely, if you want to sell an asset, it must be at a lower price than the current sell offer.");
		JOptionPane.showMessageDialog(console, "Also, note that the clock only updates when you click a button.");
		JOptionPane.showMessageDialog(console, "Click the END ROUND button if you are done making trades for a round and want to continue to the next round.");
		JOptionPane.showMessageDialog(console, "Good luck!  I will be around if you have any questions - Colton ");
		
		if (JOptionPane.showConfirmDialog(null, "Click YES to begin the simulation or NO to re read the tutorial",
				"WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			return;
		} else {
			startTutorial();
		}
		
		
	}

	public void resetBuyVal() {
		highBuy = 0;
		if(round > 1) {
		console.append("highBuy reset to 0.");
		}
	}

	public void resetSellVal() {
		lowSell = 99999;
		if(round > 1) {
		console.append("lowSell reset to " + lowSell);
		}
	}

	public void payoutAssets() {
		Random rand = new Random();
		int payout = rand.nextInt(100) + 1;

		int earnings = player.getAssets() * payout;
		player.setJewels(player.getJewels() + earnings);

		JOptionPane.showMessageDialog(console,
				"You have " + player.getAssets() + " assets.  Asset dividend payment is " + payout
						+ " jewels. You earned " + earnings + " jewels from your assets.  You now have "
						+ player.getJewels() + " jewels.");

		a.payoutAssets(payout);
		b.payoutAssets(payout);
		c.payoutAssets(payout);
		d.payoutAssets(payout);
		e.payoutAssets(payout);
		f.payoutAssets(payout);
		g.payoutAssets(payout);
		h.payoutAssets(payout);
		i.payoutAssets(payout);

	}

	public void inventory() {

		if (time(false)) {
			round++;
			startSim();
			return;
		}

		console.append("Player Assets: " + player.getAssets() + "\n");
		console.append("Player Jewels: " + player.getJewels() + "\n");
	}

	public void marketplace() {
		console.append("Welcome to the marketplace!  Click on buttons to do things.\n");

		if (time(false)) {
			round++;
			startSim();
			return;
		}

	
		;

	}

	public void decideBuyOrSell() {
		
		counter++;
		System.out.println("Counter: " + counter);

		if (counter % 2 == 0) {
			makeBuyDes();
		} else {
			makeSellDes();
		}
	}

	public void makeBuyDes() {
		int id = rollForAI();
		System.out.println("ID: " + id);

		switch (id) {

		case 1:
			System.out.println("A buy Price: " + a.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (a.buyPrice > lowSell) {
				if (a.getJewelsAI() > lowSell) {
					buyID = a.id;
					buyAssetAI();
				}
			} else {
				if (a.buyPrice > highBuy) {
					if (a.getJewelsAI() > a.buyPrice) {
						highBuy = a.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = a.id;
					}
				}
				System.out.println("A took no action.");
			}

			break;
		case 2:
			System.out.println("B buy Price: " + b.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (b.buyPrice > lowSell) {
				if (b.getJewelsAI() > lowSell) {
					buyID = b.id;
					buyAssetAI();
				}
			} else {
				if (b.buyPrice > highBuy) {
					if (b.getJewelsAI() > b.buyPrice) {
						highBuy = b.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = b.id;
					}
				}
				System.out.println("B took no action.");
			}

			break;
		case 3:
			System.out.println("C buy Price: " + c.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (c.buyPrice > lowSell) {
				if (c.getJewelsAI() > lowSell) {
					buyID = c.id;
					buyAssetAI();
				}
			} else {
				if (c.buyPrice > highBuy) {
					if (c.getJewelsAI() > c.buyPrice) {
						highBuy = c.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = c.id;
					}
				}
				System.out.println("C took no action.");
			}

			break;

		case 4:
			System.out.println("D buy Price: " + d.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (d.buyPrice > lowSell) {
				if (d.getJewelsAI() > lowSell) {
					buyID = d.id;
					buyAssetAI();
				}
			} else {
				if (d.buyPrice > highBuy) {
					if (d.getJewelsAI() > d.buyPrice) {
						highBuy = d.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = d.id;
					}
				}
				System.out.println("D took no action.");
			}

			break;

		case 5:
			System.out.println("E buy Price: " + e.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (e.buyPrice > lowSell) {
				if (e.getJewelsAI() > lowSell) {
					buyID = e.id;
					buyAssetAI();
				}
			} else {
				if (e.buyPrice > highBuy) {
					if (e.getJewelsAI() > e.buyPrice) {
						highBuy = e.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = e.id;
					}
				}
				System.out.println("E took no action.");
			}

			break;

		case 6:
			System.out.println("F buy Price: " + f.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (f.buyPrice > lowSell) {
				if (f.getJewelsAI() > lowSell) {
					buyID = f.id;
					buyAssetAI();
				}
			} else {
				if (f.buyPrice > highBuy) {
					if (f.getJewelsAI() > f.buyPrice) {
						highBuy = f.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = f.id;
					}
				}
				System.out.println("F took no action.");
			}

			break;

		case 7:
			System.out.println("G buy Price: " + g.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (g.buyPrice > lowSell) {
				if (g.getJewelsAI() > lowSell) {
					buyID = g.id;
					buyAssetAI();
				}
			} else {
				if (g.buyPrice > highBuy) {
					if (g.getJewelsAI() > g.buyPrice) {
						highBuy = g.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = g.id;
					}
				}
				System.out.println("G took no action.");
			}

			break;

		case 8:
			System.out.println("H buy Price: " + h.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (h.buyPrice > lowSell) {
				if (h.getJewelsAI() > lowSell) {
					buyID = h.id;
					buyAssetAI();
				}
			} else {
				if (h.buyPrice > highBuy) {
					if (h.getJewelsAI() > h.buyPrice) {
						highBuy = h.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = h.id;
					}
				}
				System.out.println("H took no action.");
			}

			break;

		case 9:
			System.out.println("I buy Price: " + i.getBuyPrice());
			System.out.println("HighBuy: " + highBuy);
			if (i.buyPrice > lowSell) {
				if (i.getJewelsAI() > lowSell) {
					buyID = i.id;
					buyAssetAI();
				}
			} else {
				if (i.buyPrice > highBuy) {
					if (i.getJewelsAI() > i.buyPrice) {
						highBuy = i.buyPrice;
						System.out.println("HighBuy has been set to " + highBuy);
						console.append("Current buy price: " + highBuy + "\n");
						buyOfferID = i.id;
					}
				}
				System.out.println("I took no action.");
			}

			break;

		}

	}

	public void buyAssetAI() {

		switch (sellOfferID) {

		case 1:

			if (a.getAssetsAI() > 0) {

				console.append("A sold the asset for " + lowSell + "\n");
				a.setAssetsAI(a.getAssetsAI() - 1);
				System.out.println("A assets: " + a.getAssetsAI());
				a.setJewelsAI(a.getJewelsAI() + lowSell);
				System.out.println("A Jewels: " + a.getJewelsAI());

				sellOfferID = 0;

			}

			break;

		case 2:

			if (b.getAssetsAI() > 0) {

				console.append("B sold the asset for " + lowSell + "\n");
				b.setAssetsAI(b.getAssetsAI() - 1);
				System.out.println("B assets: " + b.getAssetsAI());
				b.setJewelsAI(b.getJewelsAI() + lowSell);
				System.out.println("B Jewels: " + b.getJewelsAI());

				sellOfferID = 0;

			}

			break;

		case 3:

			if (c.getAssetsAI() > 0) {

				console.append("C sold the asset for " + lowSell + "\n");
				c.setAssetsAI(c.getAssetsAI() - 1);
				System.out.println("C assets: " + c.getAssetsAI());
				c.setJewelsAI(c.getJewelsAI() + lowSell);
				System.out.println("C Jewels: " + c.getJewelsAI());

				sellOfferID = 0;

			}

			break;

		case 4:

			if (d.getAssetsAI() > 0) {

				console.append("D sold the asset for " + lowSell + "\n");
				d.setAssetsAI(d.getAssetsAI() - 1);
				System.out.println("D assets: " + d.getAssetsAI());
				d.setJewelsAI(d.getJewelsAI() + lowSell);
				System.out.println("D Jewels: " + d.getJewelsAI());

				sellOfferID = 0;

			}

			break;

		case 5:

			if (e.getAssetsAI() > 0) {

				console.append("E sold the asset for " + lowSell + "\n");
				e.setAssetsAI(e.getAssetsAI() - 1);
				System.out.println("E assets: " + e.getAssetsAI());
				e.setJewelsAI(e.getJewelsAI() + lowSell);
				System.out.println("E Jewels: " + e.getJewelsAI());

				sellOfferID = 0;
			}

			break;

		case 6:

			if (f.getAssetsAI() > 0) {

				console.append("F sold the asset for " + lowSell + "\n");
				f.setAssetsAI(f.getAssetsAI() - 1);
				System.out.println("F assets: " + f.getAssetsAI());
				f.setJewelsAI(f.getJewelsAI() + lowSell);
				System.out.println("F Jewels: " + f.getJewelsAI());

				sellOfferID = 0;

			}

			break;

		case 7:

			if (g.getAssetsAI() > 0) {

				console.append("G sold the asset for " + lowSell + "\n");
				g.setAssetsAI(g.getAssetsAI() - 1);
				System.out.println("G assets: " + g.getAssetsAI());
				g.setJewelsAI(g.getJewelsAI() + lowSell);
				System.out.println("G Jewels: " + g.getJewelsAI());

				sellOfferID = 0;

			}

			break;

		case 8:

			if (h.getAssetsAI() > 0) {

				console.append("H sold the asset for " + lowSell + "\n");
				h.setAssetsAI(h.getAssetsAI() - 1);
				System.out.println("H assets: " + h.getAssetsAI());
				h.setJewelsAI(h.getJewelsAI() + lowSell);
				System.out.println("H Jewels: " + h.getJewelsAI());

				sellOfferID = 0;

			}

			break;

		case 9:

			if (i.getAssetsAI() > 0) {

				console.append("I sold the asset for " + lowSell + "\n");
				i.setAssetsAI(i.getAssetsAI() - 1);
				System.out.println("I assets: " + i.getAssetsAI());
				i.setJewelsAI(i.getJewelsAI() + lowSell);
				System.out.println("I Jewels: " + i.getJewelsAI());

				sellOfferID = 0;

			}

			break;

		case 10:
			if (player.getAssets() > 0) {

				console.append("Player sold the asset for " + lowSell + "\n");
				writeInfo.saveToFile("Player sold the asset for " + lowSell);
				player.setAssets(player.getAssets() - 1);
				System.out.println("I assets: " + player.getAssets());
				player.setJewels(player.getJewels() + lowSell);
				System.out.println("I Jewels: " + player.getJewels());
				
				JOptionPane.showMessageDialog(console, "Player has sold the asset for " + lowSell);

				sellOfferID = 0;

			}

			break;

		default:
			System.out.println("ERROR: INVALID SELLER");

		}

		switch (buyID) {

		case 1:
			if (a.getJewelsAI() >= lowSell) {

				console.append("A bought the asset for " + lowSell + "\n");
				a.setAssetsAI(a.getAssetsAI() + 1);
				System.out.println("A assets: " + a.getAssetsAI());
				a.setJewelsAI(a.getJewelsAI() - lowSell);
				System.out.println("A Jewels: " + a.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 2:
			if (b.getJewelsAI() >= lowSell) {

				console.append("B bought the asset for " + lowSell + "\n");
				b.setAssetsAI(b.getAssetsAI() + 1);
				System.out.println("B assets: " + b.getAssetsAI());
				b.setJewelsAI(b.getJewelsAI() - lowSell);
				System.out.println("B Jewels: " + b.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 3:
			if (c.getJewelsAI() >= lowSell) {

				console.append("C bought the asset for " + lowSell + "\n");
				c.setAssetsAI(c.getAssetsAI() + 1);
				System.out.println("C assets: " + c.getAssetsAI());
				c.setJewelsAI(c.getJewelsAI() - lowSell);
				System.out.println("C Jewels: " + c.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 4:
			if (d.getJewelsAI() >= lowSell) {

				console.append("D bought the asset for " + lowSell + "\n");
				d.setAssetsAI(d.getAssetsAI() + 1);
				System.out.println("D assets: " + d.getAssetsAI());
				d.setJewelsAI(d.getJewelsAI() - lowSell);
				System.out.println("D Jewels: " + d.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 5:
			if (e.getJewelsAI() >= lowSell) {

				console.append("E bought the asset for " + lowSell + "\n");
				e.setAssetsAI(e.getAssetsAI() + 1);
				System.out.println("E assets: " + e.getAssetsAI());
				e.setJewelsAI(e.getJewelsAI() - lowSell);
				System.out.println("E Jewels: " + e.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 6:
			if (f.getJewelsAI() >= lowSell) {

				console.append("F bought the asset for " + lowSell + "\n");
				f.setAssetsAI(f.getAssetsAI() + 1);
				System.out.println("F assets: " + f.getAssetsAI());
				f.setJewelsAI(f.getJewelsAI() - lowSell);
				System.out.println("F Jewels: " + f.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 7:
			if (g.getJewelsAI() >= lowSell) {

				console.append("G bought the asset for " + lowSell + "\n");
				g.setAssetsAI(g.getAssetsAI() + 1);
				System.out.println("G assets: " + g.getAssetsAI());
				g.setJewelsAI(g.getJewelsAI() - lowSell);
				System.out.println("G Jewels: " + g.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 8:
			if (h.getJewelsAI() >= lowSell) {

				console.append("H bought the asset for " + lowSell + "\n");
				h.setAssetsAI(h.getAssetsAI() + 1);
				System.out.println("H assets: " + h.getAssetsAI());
				h.setJewelsAI(h.getJewelsAI() - lowSell);
				System.out.println("H Jewels: " + h.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 9:
			if (i.getJewelsAI() >= lowSell) {

				console.append("I bought the asset for " + lowSell + "\n");
				i.setAssetsAI(i.getAssetsAI() + 1);
				System.out.println("I assets: " + i.getAssetsAI());
				i.setJewelsAI(i.getJewelsAI() - lowSell);
				System.out.println("I Jewels: " + i.getJewelsAI());

				resetSellVal();
				buyID = 0;

			}

			break;

		case 10:
			if (player.getJewels() >= lowSell) {

				console.append("Player bought the asset for " + lowSell + "\n");
				writeInfo.saveToFile("Player bought the asset for " + lowSell);
				player.setAssets(player.getAssets() + 1);
				System.out.println("Player assets: " + player.getAssets());
				player.setJewels(player.getJewels() - lowSell);
				System.out.println("Player Jewels: " + player.getJewels());
				
				JOptionPane.showMessageDialog(console, "Player has purchased the asset for " + lowSell);

				resetSellVal();
				buyID = 0;

			}

			break;

		default:
			System.out.println("ERROR: Invalid buyer (buy)");

		}

	}

	public void makeSellDes() {
		int id = rollForAI();
		System.out.println(id);

		switch (id) {
		case 1:
			System.out.println("A sell Price: " + a.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (a.getSellPrice() < highBuy) {
				if (a.getAssetsAI() > 0) {
					sellID = a.id;
					sellAssetAI();
				}
			} else {
				if (a.sellPrice < lowSell) {
					if (a.getAssetsAI() > 0) {
						lowSell = a.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = a.id;
					}
				} else {
					System.out.println("A took no sell action.");
				}
			}

			break;

		case 2:
			System.out.println("B sell Price: " + b.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (b.getSellPrice() < highBuy) {
				if (b.getAssetsAI() > 0) {
					sellID = b.id;
					sellAssetAI();
				}
			} else {
				if (b.sellPrice < lowSell) {
					if (b.getAssetsAI() > 0) {
						lowSell = b.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = b.id;
					}
				} else {
					System.out.println("B took no sell action.");
				}
			}

			break;

		case 3:
			System.out.println("C sell Price: " + c.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (c.getSellPrice() < highBuy) {
				if (c.getAssetsAI() > 0) {
					sellID = c.id;
					sellAssetAI();
				}
			} else {
				if (c.sellPrice < lowSell) {
					if (c.getAssetsAI() > 0) {
						lowSell = c.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = c.id;
					}
				} else {
					System.out.println("C took no sell action.");
				}
			}

			break;

		case 4:
			System.out.println("D sell Price: " + d.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (d.getSellPrice() < highBuy) {
				if (d.getAssetsAI() > 0) {
					sellID = d.id;
					sellAssetAI();
				}
			} else {
				if (d.sellPrice < lowSell) {
					if (d.getAssetsAI() > 0) {
						lowSell = d.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = d.id;
					}
				} else {
					System.out.println("D took no sell action.");
				}
			}

			break;

		case 5:
			System.out.println("E sell Price: " + e.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (e.getSellPrice() < highBuy) {
				if (e.getAssetsAI() > 0) {
					sellID = e.id;
					sellAssetAI();
				}
			} else {
				if (e.sellPrice < lowSell) {
					if (e.getAssetsAI() > 0) {
						lowSell = e.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = e.id;
					}
				} else {
					System.out.println("E took no sell action.");
				}
			}

			break;

		case 6:
			System.out.println("F sell Price: " + f.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (f.getSellPrice() < highBuy) {
				if (f.getAssetsAI() > 0) {
					sellID = f.id;
					sellAssetAI();
				}
			} else {
				if (f.sellPrice < lowSell) {
					if (f.getAssetsAI() > 0) {
						lowSell = f.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = f.id;
					}
				} else {
					System.out.println("F took no sell action.");
				}
			}

			break;

		case 7:
			System.out.println("G sell Price: " + g.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (g.getSellPrice() < highBuy) {
				if (g.getAssetsAI() > 0) {
					sellID = g.id;
					sellAssetAI();
				}
			} else {
				if (g.sellPrice < lowSell) {
					if (g.getAssetsAI() > 0) {
						lowSell = g.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = g.id;
					}
				} else {
					System.out.println("G took no sell action.");
				}
			}

			break;

		case 8:
			System.out.println("H sell Price: " + h.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (h.getSellPrice() < highBuy) {
				if (h.getAssetsAI() > 0) {
					sellID = h.id;
					sellAssetAI();
				}
			} else {
				if (h.sellPrice < lowSell) {
					if (h.getAssetsAI() > 0) {
						lowSell = h.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = h.id;
					}
				} else {
					System.out.println("H took no sell action.");
				}
			}

			break;

		case 9:
			System.out.println("I sell Price: " + i.getSellPrice());
			System.out.println("LowSell: " + lowSell);
			if (i.getSellPrice() < highBuy) {
				if (i.getAssetsAI() > 0) {
					sellID = i.id;
					sellAssetAI();
				}
			} else {
				if (i.sellPrice < lowSell) {
					if (i.getAssetsAI() > 0) {
						lowSell = i.sellPrice;
						System.out.println("lowSell has been set to " + lowSell);
						console.append("Current sell price: " + lowSell + "\n");
						sellOfferID = i.id;
					}
				} else {
					System.out.println("I took no sell action.");
				}
			}

			break;
		}

	}

	public void sellAssetAI() {
		switch (buyOfferID) {
		case 1:
			if (a.getJewelsAI() > highBuy) {
				System.out.println("A has purchased the asset." + "\n");
				a.setAssetsAI(a.getAssetsAI() + 1);
				System.out.println("A Assets: " + a.getAssetsAI());
				a.setJewelsAI(a.getJewelsAI() - highBuy);
				System.out.println("A Jewels: " + a.getJewelsAI());

				buyOfferID = 0;
			}

			break;

		case 2:
			if (b.getJewelsAI() > highBuy) {
				System.out.println("B has purchased the asset." + "\n");
				b.setAssetsAI(b.getAssetsAI() + 1);
				System.out.println("B Assets: " + b.getAssetsAI());
				b.setJewelsAI(b.getJewelsAI() - highBuy);
				System.out.println("B Jewels: " + b.getJewelsAI());

				buyOfferID = 0;
			}

			break;

		case 3:
			if (c.getJewelsAI() > highBuy) {
				System.out.println("C has purchased the asset." + "\n");
				c.setAssetsAI(c.getAssetsAI() + 1);
				System.out.println("C Assets: " + c.getAssetsAI());
				c.setJewelsAI(c.getJewelsAI() - highBuy);
				System.out.println("C Jewels: " + c.getJewelsAI());

				buyOfferID = 0;
			}

			break;

		case 4:
			if (d.getJewelsAI() > highBuy) {
				System.out.println("D has purchased the asset." + "\n");
				d.setAssetsAI(d.getAssetsAI() + 1);
				System.out.println("D Assets: " + d.getAssetsAI());
				d.setJewelsAI(d.getJewelsAI() - highBuy);
				System.out.println("D Jewels: " + d.getJewelsAI());

				buyOfferID = 0;
			}

			break;

		case 5:
			if (e.getJewelsAI() > highBuy) {
				System.out.println("E has purchased the asset." + "\n");
				e.setAssetsAI(e.getAssetsAI() + 1);
				System.out.println("E Assets: " + e.getAssetsAI());
				e.setJewelsAI(e.getJewelsAI() - highBuy);
				System.out.println("E Jewels: " + e.getJewelsAI());

				buyOfferID = 0;
			}

			break;

		case 6:
			if (f.getJewelsAI() > highBuy) {
				System.out.println("F has purchased the asset." + "\n");
				f.setAssetsAI(f.getAssetsAI() + 1);
				System.out.println("F Assets: " + f.getAssetsAI());
				f.setJewelsAI(f.getJewelsAI() - highBuy);
				System.out.println("F Jewels: " + f.getJewelsAI());

				buyOfferID = 0;
			}

			break;

		case 7:
			if (g.getJewelsAI() > highBuy) {
				System.out.println("G has purchased the asset." + "\n");
				g.setAssetsAI(g.getAssetsAI() + 1);
				System.out.println("G Assets: " + g.getAssetsAI());
				g.setJewelsAI(g.getJewelsAI() - highBuy);
				System.out.println("G Jewels: " + g.getJewelsAI());

				buyOfferID = 0;
			}

			break;

		case 8:
			if (h.getJewelsAI() > highBuy) {
				System.out.println("H has purchased the asset." + "\n");
				h.setAssetsAI(h.getAssetsAI() + 1);
				System.out.println("H Assets: " + h.getAssetsAI());
				h.setJewelsAI(h.getJewelsAI() - highBuy);
				System.out.println("H Jewels: " + h.getJewelsAI());

				buyOfferID = 0;
			}

			break;

		case 9:
			if (i.getJewelsAI() > highBuy) {
				System.out.println("I has purchased the asset." + "\n");
				i.setAssetsAI(i.getAssetsAI() + 1);
				System.out.println("I Assets: " + i.getAssetsAI());
				i.setJewelsAI(i.getJewelsAI() - highBuy);
				System.out.println("I Jewels: " + i.getJewelsAI());

				buyOfferID = 0;
			}

			break;

			
			// TODO add price in console logs
		case 10:
			if (player.getJewels() > highBuy) {
				console.append("Player has purchased the asset." + "\n");
				writeInfo.saveToFile("Player bought the asset for " + highBuy);
				player.setAssets(player.getAssets() + 1);
				System.out.println("Player Assets: " + player.getAssets());
				player.setJewels(player.getJewels() - highBuy);
				System.out.println("Player Jewels: " + player.getJewels());
				
				JOptionPane.showMessageDialog(console, "Player has purchased the asset for " + highBuy);

				buyOfferID = 0;
			}

			break;

		default:
			System.out.println("ERROR: INVALID BUYER (sell)");

		}

		switch (sellID) {

		case 1:
			if (a.getAssetsAI() > 0) {
				console.append("A sold asset." + "\n");
				a.setAssetsAI(a.getAssetsAI() - 1);
				System.out.println("A Assets: " + a.getAssetsAI());
				a.setJewelsAI(a.getJewelsAI() + highBuy);
				System.out.println("A Jewels: " + a.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 2:
			if (b.getAssetsAI() > 0) {
				console.append("B sold asset." + "\n");
				b.setAssetsAI(b.getAssetsAI() - 1);
				System.out.println("B Assets: " + b.getAssetsAI());
				b.setJewelsAI(b.getJewelsAI() + highBuy);
				System.out.println("B Jewels: " + b.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 3:
			if (c.getAssetsAI() > 0) {
				console.append("C sold asset." + "\n");
				c.setAssetsAI(c.getAssetsAI() - 1);
				System.out.println("C Assets: " + c.getAssetsAI());
				c.setJewelsAI(c.getJewelsAI() + highBuy);
				System.out.println("C Jewels: " + c.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 4:
			if (d.getAssetsAI() > 0) {
				console.append("D sold asset." + "\n");
				d.setAssetsAI(d.getAssetsAI() - 1);
				System.out.println("D Assets: " + d.getAssetsAI());
				d.setJewelsAI(d.getJewelsAI() + highBuy);
				System.out.println("D Jewels: " + d.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 5:
			if (e.getAssetsAI() > 0) {
				console.append("E sold asset." + "\n");
				e.setAssetsAI(e.getAssetsAI() - 1);
				System.out.println("E Assets: " + e.getAssetsAI());
				e.setJewelsAI(e.getJewelsAI() + highBuy);
				System.out.println("E Jewels: " + e.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 6:
			if (f.getAssetsAI() > 0) {
				console.append("F sold asset." + "\n");
				f.setAssetsAI(f.getAssetsAI() - 1);
				System.out.println("F Assets: " + f.getAssetsAI());
				f.setJewelsAI(f.getJewelsAI() + highBuy);
				System.out.println("F Jewels: " + f.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 7:
			if (g.getAssetsAI() > 0) {
				console.append("G sold asset." + "\n");
				g.setAssetsAI(g.getAssetsAI() - 1);
				System.out.println("G Assets: " + g.getAssetsAI());
				g.setJewelsAI(g.getJewelsAI() + highBuy);
				System.out.println("G Jewels: " + g.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 8:
			if (h.getAssetsAI() > 0) {
				console.append("H sold asset." + "\n");
				h.setAssetsAI(h.getAssetsAI() - 1);
				System.out.println("H Assets: " + h.getAssetsAI());
				h.setJewelsAI(h.getJewelsAI() + highBuy);
				System.out.println("H Jewels: " + h.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 9:
			if (i.getAssetsAI() > 0) {
				console.append("I sold asset." + "\n");
				i.setAssetsAI(i.getAssetsAI() - 1);
				System.out.println("I Assets: " + i.getAssetsAI());
				i.setJewelsAI(i.getJewelsAI() + highBuy);
				System.out.println("I Jewels: " + i.getJewelsAI());

				sellID = 0;
				resetBuyVal();
			}

			break;

		case 10:
			if (player.getAssets() > 0) {
				console.append("Player sold asset." + "\n");
				writeInfo.saveToFile("Player sold the asset for " + highBuy);
				player.setAssets(player.getAssets() - 1);
				System.out.println("Player Assets: " + player.getAssets());
				player.setJewels(player.getJewels() + highBuy);
				System.out.println("Player Jewels: " + player.getJewels());
				
				JOptionPane.showMessageDialog(console, "Player has sold the asset for " + highBuy);

				sellID = 0;
				resetBuyVal();
			}

			break;

		default:
			System.out.println("ERROR: INVALID SELLER (sell)");

		}
	}

	public void setAIPriceRange() {
		a.setRange(a.calcPriceRange());
		System.out.println("a Range: " + a.getRange());
		b.setRange(b.calcPriceRange());
		System.out.println("b Range: " + b.getRange());
		c.setRange(c.calcPriceRange());
		System.out.println("c Range: " + c.getRange());
		d.setRange(d.calcPriceRange());
		System.out.println("d Range: " + d.getRange());
		e.setRange(e.calcPriceRange());
		System.out.println("e Range: " + e.getRange());
		f.setRange(f.calcPriceRange());
		System.out.println("f Range: " + f.getRange());
		g.setRange(g.calcPriceRange());
		System.out.println("g Range: " + g.getRange());
		h.setRange(h.calcPriceRange());
		System.out.println("h Range: " + h.getRange());
		i.setRange(i.calcPriceRange());
		System.out.println("i Range: " + i.getRange());

	}

	public void setAIValuations() {
		a.setAssetValue(a.calcAssetValuation(round));
		System.out.print("A Value: " + a.getAssetValue() + "\n");

		b.setAssetValue(b.calcAssetValuation(round));
		System.out.print("b Value: " + b.getAssetValue() + "\n");

		c.setAssetValue(c.calcAssetValuation(round));
		System.out.print("c Value: " + c.getAssetValue() + "\n");

		d.setAssetValue(d.calcAssetValuation(round));
		System.out.print("d Value: " + d.getAssetValue() + "\n");

		e.setAssetValue(e.calcAssetValuation(round));
		System.out.print("e Value: " + e.getAssetValue() + "\n");

		f.setAssetValue(f.calcAssetValuation(round));
		System.out.print("f Value: " + f.getAssetValue() + "\n");

		g.setAssetValue(g.calcAssetValuation(round));
		System.out.print("g Value: " + g.getAssetValue() + "\n");

		h.setAssetValue(h.calcAssetValuation(round));
		System.out.print("h Value: " + h.getAssetValue() + "\n");

		i.setAssetValue(i.calcAssetValuation(round));
		System.out.print("i Value: " + i.getAssetValue() + "\n");

	}

	// TODO Improve formulas
	public void setAIBuyAndSellPrices() {
		a.setBuyPrice(a.getAssetValue() - a.getRange());
		a.setSellPrice(a.getAssetValue() + a.getRange());

		b.setBuyPrice(b.getAssetValue() - b.getRange());
		b.setSellPrice(b.getAssetValue() + b.getRange());

		c.setBuyPrice(c.getAssetValue() - c.getRange());
		c.setSellPrice(c.getAssetValue() + c.getRange());

		d.setBuyPrice(d.getAssetValue() - d.getRange());
		d.setSellPrice(d.getAssetValue() + d.getRange());

		e.setBuyPrice(e.getAssetValue() - e.getRange());
		e.setSellPrice(e.getAssetValue() + e.getRange());

		f.setBuyPrice(f.getAssetValue() - f.getRange());
		f.setSellPrice(f.getAssetValue() + f.getRange());

		g.setBuyPrice(g.getAssetValue() - g.getRange());
		g.setSellPrice(g.getAssetValue() + g.getRange());

		h.setBuyPrice(h.getAssetValue() - h.getRange());
		h.setSellPrice(h.getAssetValue() + h.getRange());

		i.setBuyPrice(i.getAssetValue() - i.getRange());
		i.setSellPrice(i.getAssetValue() + i.getRange());

	}

	public int rollForAI() {
		int num = 0;
		Random rand = new Random();
		num = rand.nextInt(9) + 1;

		return num;
	}

	public boolean time(boolean reset) {
		boolean expired;
		if (reset) {
			time = System.currentTimeMillis();

		}

		// The remaining time in the round is equal to 120 seconds subtracted by the
		// amount of time passed in the round
		long remaining = 60000 - (System.currentTimeMillis() - time);
		if (remaining > 0) {
			expired = false;
		} else {
			expired = true;
			JOptionPane.showMessageDialog(console, "Round is over! \n");
		}

		timeLeft.setText("================\nTime left: " + remaining / 1000 + " seconds\n==============");

		return expired;
	}

	public void getUserInfo() {
		String name = JOptionPane.showInputDialog("Please enter your name.");
		System.out.println("Name: " + name);

		writeInfo.saveToFile("Name: " + name);

		String econ = JOptionPane.showInputDialog("What econ class are you taking?.");
		System.out.println("Econ: " + econ);

		writeInfo.saveToFile("Econ: " + econ);

		String grade = JOptionPane.showInputDialog("What grade are you in?");
		System.out.println("Grade: " + grade);

		writeInfo.saveToFile("Grade: " + grade);
	}

	public class OpenInventory implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == button1) {
				inventory();
			}

		}

	}


	


	public class BuyAsset implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			if (time(false)) {
				round++;
				startSim();
				return;
			}

			if (JOptionPane.showConfirmDialog(null, "Would you like to purchase an asset for " + lowSell + " Jewels?\n",
					"WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

				if (player.getJewels() > lowSell) {
					
					writeInfo.saveToFile("Purchased asset for " + lowSell + " Jewels \n");
					
					player.setAssets(player.getAssets() + 1);
					player.setJewels(player.getJewels() - lowSell);

					switch (sellID) {
					case 1:
						a.setAssetsAI(a.getAssetsAI() - 1);
						a.setJewelsAI(a.getJewelsAI() + lowSell);

						break;
					case 2:
						b.setAssetsAI(b.getAssetsAI() - 1);
						b.setJewelsAI(b.getJewelsAI() + lowSell);

						break;

					case 3:
						c.setAssetsAI(c.getAssetsAI() - 1);
						c.setJewelsAI(c.getJewelsAI() + lowSell);

						break;
					case 4:
						d.setAssetsAI(d.getAssetsAI() - 1);
						d.setJewelsAI(d.getJewelsAI() + lowSell);

						break;
					case 5:
						e.setAssetsAI(e.getAssetsAI() - 1);
						e.setJewelsAI(e.getJewelsAI() + lowSell);

						break;
					case 6:
						f.setAssetsAI(f.getAssetsAI() - 1);
						f.setJewelsAI(f.getJewelsAI() + lowSell);

						break;
					case 7:
						g.setAssetsAI(g.getAssetsAI() - 1);
						g.setJewelsAI(g.getJewelsAI() + lowSell);

						break;

					case 8:
						h.setAssetsAI(h.getAssetsAI() - 1);
						h.setJewelsAI(h.getJewelsAI() + lowSell);

						break;
					case 9:
						i.setAssetsAI(i.getAssetsAI() - 1);
						i.setJewelsAI(i.getJewelsAI() + lowSell);

						break;
					}

				} else {
					JOptionPane.showMessageDialog(console, "You don't have enough Jewels.");
				}

				JOptionPane.showMessageDialog(console, "Purchase complete.");

			} else {
				JOptionPane.showMessageDialog(console, "Purchase canceled.");
			}
		}

	}

	public class MakeBuyOffer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (time(false)) {
				round++;
				startSim();
				return;
			}

			String inputValue = JOptionPane.showInputDialog("Please enter your buy offer.");
			Integer buyPrice = tryParse(inputValue);

			if (buyPrice == null) {
				JOptionPane.showMessageDialog(console, "Invalid buy Offer.");
				return;

			}

			if (buyPrice > player.getJewels()) {
				JOptionPane.showMessageDialog(console, "You don't have enough jewels.");
			}

			if (buyPrice <= highBuy) {
				JOptionPane.showMessageDialog(console,
						"A computer has offered a lower or equal buy price.  Riase your price to have a better chance at selling.");

			} else {
				buyOfferID = 10;
				highBuy = buyPrice;
				console.append("Current buy price: " + highBuy);
				
			}

		}

	}

	// Makes sure that an int can be parsed from the string. Method found on
	// StackOverflow:
	// https://stackoverflow.com/questions/1486077/good-way-to-encapsulate-integer-parseint
	public static Integer tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {

			return null;
		}
	}

	public class SellAsset implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			if (time(false)) {
				round++;
				startSim();
				return;
			}

			if (JOptionPane.showConfirmDialog(null, "Would you like to sell an asset for " + highBuy + " Jewels?\n",
					"WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

				if (player.getAssets() > 0) {
					
					writeInfo.saveToFile("Player sold an asset for " + highBuy + " Jewels. \n");

					
					player.setAssets(player.getAssets() - 1);
					player.setJewels(player.getJewels() + highBuy);

					switch (buyID) {
					case 1:
						a.setAssetsAI(a.getAssetsAI() + 1);
						a.setJewelsAI(a.getJewelsAI() - highBuy);

						break;
					case 2:
						b.setAssetsAI(b.getAssetsAI() + 1);
						b.setJewelsAI(b.getJewelsAI() - highBuy);

						break;

					case 3:
						c.setAssetsAI(c.getAssetsAI() + 1);
						c.setJewelsAI(c.getJewelsAI() - highBuy);

						break;
					case 4:
						d.setAssetsAI(d.getAssetsAI() + 1);
						d.setJewelsAI(d.getJewelsAI() - highBuy);

						break;
					case 5:
						e.setAssetsAI(e.getAssetsAI() + 1);
						e.setJewelsAI(e.getJewelsAI() - highBuy);

						break;
					case 6:
						f.setAssetsAI(f.getAssetsAI() + 1);
						f.setJewelsAI(f.getJewelsAI() - highBuy);

						break;
					case 7:
						g.setAssetsAI(g.getAssetsAI() + 1);
						g.setJewelsAI(g.getJewelsAI() - highBuy);

						break;

					case 8:
						h.setAssetsAI(h.getAssetsAI() + 1);
						h.setJewelsAI(h.getJewelsAI() - highBuy);

						break;
					case 9:
						i.setAssetsAI(i.getAssetsAI() + 1);
						i.setJewelsAI(i.getJewelsAI() - highBuy);

						break;
					}

				} else {
					JOptionPane.showMessageDialog(console, "You don't have any assets.");
				}

				JOptionPane.showMessageDialog(console, "Sale complete.");

			} else {
				JOptionPane.showMessageDialog(console, "Sale canceled.");
			}
		}

	}

	public class MakeSellOffer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (time(false)) {
				round++;
				startSim();
				return;
			}

			String inputValue = JOptionPane.showInputDialog("Please enter your sell price.");
			Integer sellPrice = tryParse(inputValue);

			if (sellPrice == null) {
				JOptionPane.showMessageDialog(console, "Invalid Sell Price.");
				return;

			}

			if (sellPrice >= lowSell) {
				JOptionPane.showMessageDialog(console,
						"A computer has offered a higher or equal sell price.  Lower your price to have a better chance at selling.");

			} else {
				sellOfferID = 10;
				lowSell = sellPrice;
				console.append("Current sell price: " + lowSell);
			}

		}

	}
	
	public class EndRound implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (JOptionPane.showConfirmDialog(null, "Would you like to advance to the next round? \n",
					"WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				round++;
				startSim();
			} else {
				JOptionPane.showMessageDialog(console, "You didnt end the round.");
			}
			
		}
		
	}

}
