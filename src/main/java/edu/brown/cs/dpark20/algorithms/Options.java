package edu.brown.cs.dpark20.algorithms;

import java.util.*;


/**
 * Options class for building algorithms for information processing.
 *
 */
public class Options {

	String company;
	String currPrice;
	String volatility;

	/**
	 * Constructor for Options.
	 */
	public Options() {
		company = "";
		currPrice = "";
		volatility = "";

	}

	/**
	* Algorithm processing for optimization. 1. 1 to 5 for not likely at all to
	* very likely, 2. lower bound, 3. upper bound, maxLoss setting
	*/
	public List<String[]> getOptions(List<String[]> expectations, double maxLoss){

		List<String[]> portfolio = new ArrayList<String[]>();
		String[] lowRange = new String[2];
		String[] midRange = new String[2];
		String[] highRange = new String[2];
		int low=0;
		int mid=0;
		int high=0;



		for (int i = 0; i < expectations.size(); i++){
			String[] curr = expectations.get(i);
			if (i == 0){
				low = getInteger(curr[0]);
				lowRange[0] = curr[1];
				lowRange[1] = curr[2];
			} else if (i == 1){
				mid = getInteger(curr[0]);
				midRange[0] = curr[1];
				midRange[1] = curr[2];
			} else if (i == 2){
				high = getInteger(curr[0]);
				highRange[0] = curr[1];
				highRange[1] = curr[2];
			}
		}
		if (low == mid && low == high) {
			lowRange[1] = highRange[1];
			mid = 0;
			high = 0;
			midRange = new String[2];
			highRange = new String[2];
		}
		else if (low == mid) {
			lowRange[1] = midRange[1];
			mid = high;
			midRange[0] = highRange[0];
			midRange[1] = highRange[1];
			high = 0;
			highRange = new String[2];
		}
		else if (mid == high) {
			midRange[1] = highRange[1];
			high = 0;
			highRange = new String[2];
		}

		int tot = low + mid + high;
		if (tot == 1){
			// Long position
			portfolio.add(createOption("Buy", "Stock", currPrice, "BLACKSCHOLES"));
		} else if (tot == 2){
			//Handle possibilities
			if (low > mid){
				portfolio.add(createOption("Buy", "Put", lowRange[1], "BLACKSCHOLES"));

			} else if (mid > low){
				portfolio.add(createOption("Buy", "Call", lowRange[1], "BLACKSCHOLES"));
			}

		} else if (tot == 3){
			if (low > mid) {
				if (high > mid){
					if (high > low){
						//high low mid
						if (high - low >= 2) {
							portfolio.add(createOption("Buy", "Call", highRange[0], "BLACKSCHOLES"));
						} else  if (low - mid >= 2){
							String middle = avgString(midRange[0], midRange[1]);
							portfolio.add(createOption("Buy", "Call", middle, "BLACKSCHOLES"));
							portfolio.add(createOption("Buy", "Put", middle, "BLACKSCHOLES"));
						} else {
							portfolio.add(createOption("Sell", "Call", midRange[1], "BLACKSCHOLES"));
							portfolio.add(createOption("Buy", "Put", midRange[0], "BLACKSCHOLES"));
						}

					} else {
						//low high mid
						if (low - high >= 2) {
							portfolio.add(createOption("Buy", "Put", lowRange[1], "BLACKSCHOLES"));
						} else  if (high - mid >= 2){
							String middle = avgString(midRange[0], midRange[1]);
							portfolio.add(createOption("Buy", "Call", middle, "BLACKSCHOLES"));
							portfolio.add(createOption("Buy", "Put", middle, "BLACKSCHOLES"));
						} else {
							portfolio.add(createOption("Sell", "Call", midRange[1], "BLACKSCHOLES"));
							portfolio.add(createOption("Buy", "Put", midRange[0], "BLACKSCHOLES"));
						}
					}
				}
				else {
					//low mid high
					String middle = avgString(midRange[0], midRange[1]);
					portfolio.add(createOption("Buy", "Put", middle, "BLACKSCHOLES"));
				}

			} else if (mid > low) {
				if (high > low){
					if (high > mid){
						// high mid low
						portfolio.add(createOption("Buy", "Stock", currPrice, "BLACKSCHOLES"));
					} else {
						// mid high low
						if (mid - high >= 2) {
							portfolio.add(createOption("Sell", "Put", currPrice, "BLACKSCHOLES"));
							portfolio.add(createOption("Buy", "Put", midRange[0], "BLACKSCHOLES"));
							portfolio.add(createOption("Sell", "Call", currPrice, "BLACKSCHOLES"));
							portfolio.add(createOption("Buy", "Call", midRange[1], "BLACKSCHOLES"));
						} else  if (high - low >= 2){
							portfolio.add(createOption("Buy", "Stock", currPrice, "BLACKSCHOLES"));
							if (high - low < 3){
								portfolio.add(createOption("Buy", "Put", midRange[0], "BLACKSCHOLES"));
							}
						} else {
							portfolio.add(createOption("Sell", "Put", midRange[0], "BLACKSCHOLES"));
							portfolio.add(createOption("Buy", "Put", avgString(lowRange[0], lowRange[1]), "BLACKSCHOLES"));
							portfolio.add(createOption("Sell", "Call", midRange[1], "BLACKSCHOLES"));
							portfolio.add(createOption("Buy", "Call", avgString(highRange[0], highRange[1]), "BLACKSCHOLES"));
						}
					}
				}
				else {
					//mid low high
					if (mid - low >= 2) {
						portfolio.add(createOption("Sell", "Put", currPrice, "BLACKSCHOLES"));
						portfolio.add(createOption("Buy", "Put", midRange[0], "BLACKSCHOLES"));
						portfolio.add(createOption("Sell", "Call", currPrice, "BLACKSCHOLES"));
						portfolio.add(createOption("Buy", "Call", midRange[1], "BLACKSCHOLES"));
					} else  if (low - high >= 2){
						String middle = avgString(midRange[0], midRange[1]);
						portfolio.add(createOption("Buy", "Put", middle, "BLACKSCHOLES"));
					} else {
						portfolio.add(createOption("Sell", "Put", midRange[0], "BLACKSCHOLES"));
						portfolio.add(createOption("Buy", "Put", avgString(lowRange[0], lowRange[1]), "BLACKSCHOLES"));
						portfolio.add(createOption("Sell", "Call", midRange[1], "BLACKSCHOLES"));
						portfolio.add(createOption("Buy", "Call", avgString(highRange[0], highRange[1]), "BLACKSCHOLES"));
					}
				}
			}
		}

		return applyMaxLoss(portfolio, maxLoss);
	}

	private List<String[]> applyMaxLoss(List<String[]> portfolio, double maxLoss){
		return null;
	}

	private String[] createOption(String buySell, String callPut, String strike, String premium){
		String[] option = new String[4];
		option[0] = buySell;
		option[1] = callPut;
		option[2] = strike;
		option[3] = premium;
		return option;
	}

	public double blackScholes(double s, double k, double r, double t, boolean isCall){
		double cost = 0.0;

		int callOrPut = -1;
		if (isCall){
			callOrPut = 1;
		}
		return cost * callOrPut;
	}

	private double getDouble(String s){
		return Double.parseDouble(s);
	}

	private int getInteger(String s){
		return Integer.parseInt(s);
	}

	private String avgString(String a, String b){
		double c = getDouble(a) + getDouble(b);
		c = c/2.0;

		return Double.toString(c);
	}

}
