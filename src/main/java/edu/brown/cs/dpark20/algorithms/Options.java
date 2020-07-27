package edu.brown.cs.dpark20.algorithms;

import java.util.*;


/**
 * Options class for building algorithms for information processing.
 *
 */
public class Options {

	String company;
	String currPrice;
	String interest;
	String volatility;
	String time;


	/**
	 * Constructor for Options.
	 */
	public Options(String cpny, String cp, String in, String vol, String tm) {
		company = cpny;
		currPrice = cp;
		interest = in;
		volatility = vol;
		time = tm;

		String[] low = new String[3];
		String[] mid = new String[3];
		String[] high = new String[3];
		low[0] = "4";
		low[1] = "10";
		low[2] = "20";
		mid[0] = "2";
		mid[1] = "20";
		mid[2] = "24.88";
		high[0] = "4";
		high[1] = "24.88";
		high[2] = "29.12";
		List<String[]> input = new ArrayList<>();
		input.add(low);
		input.add(mid);
		input.add(high);
		List<String[]> res = getOptions(input, 0.00);
		for (String[] s : res){
			for (String item : s){
				System.out.println(item);
			}
		}
		List<double[]> coords = getGraphPoints(res);
		for (double[] d : coords){
			System.out.println(d[0] + " " + d[1]);
		}
		System.out.println(company + " " + currPrice);

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

		double s = getDouble(currPrice);
		double r = getDouble(interest);
		double sigma = getDouble(volatility);
		double t = getDouble(time);


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

		int tot = 0;
		if (low != 0){
			tot += 1;
		}
		if (mid != 0) {
			tot += 1;
		}
		if (high != 0) {
			tot += 1;
		}

		if (tot == 1){
			// Long position
			portfolio.add(createOption("Buy", "Stock", currPrice, 0.0));
		} else if (tot == 2){
			//Handle possibilities
			if (low > mid){
				portfolio.add(createOption("Buy", "Put", lowRange[1], blackScholes(s, getDouble(lowRange[1]), r, sigma, t, false)));

			} else if (mid > low){
				portfolio.add(createOption("Buy", "Call", lowRange[1], blackScholes(s, getDouble(lowRange[1]), r, sigma, t, true)));
			}

		} else if (tot == 3){
			if (low > mid) {
				if (high > mid){
					if (high > low){
						//high low mid
						if (high - low >= 2) {
							portfolio.add(createOption("Buy", "Call", highRange[0], blackScholes(s, getDouble(highRange[0]), r, sigma, t, true)));
						} else  if (low - mid >= 2){
							String middle = avgString(midRange[0], midRange[1]);
							portfolio.add(createOption("Buy", "Call", middle, blackScholes(s, getDouble(middle), r, sigma, t, true)));
							portfolio.add(createOption("Buy", "Put", middle, blackScholes(s, getDouble(middle), r, sigma, t, false)));
						} else {
							portfolio.add(createOption("Sell", "Call", midRange[1], blackScholes(s, getDouble(midRange[1]), r, sigma, t, true)));
							portfolio.add(createOption("Buy", "Put", midRange[0], blackScholes(s, getDouble(midRange[0]), r, sigma, t, false)));
						}

					} else {
						//low high mid
						if (low - high >= 2) {
							portfolio.add(createOption("Buy", "Put", lowRange[1], blackScholes(s, getDouble(lowRange[1]), r, sigma, t, false)));
						} else  if (high - mid >= 2){
							String middle = avgString(midRange[0], midRange[1]);
							portfolio.add(createOption("Buy", "Call", middle, blackScholes(s, getDouble(middle), r, sigma, t, true)));
							portfolio.add(createOption("Buy", "Put", middle, blackScholes(s, getDouble(middle), r, sigma, t, false)));
						} else {
							portfolio.add(createOption("Sell", "Call", midRange[1], blackScholes(s, getDouble(midRange[1]), r, sigma, t, true)));
							portfolio.add(createOption("Buy", "Put", midRange[0], blackScholes(s, getDouble(midRange[0]), r, sigma, t, false)));
						}
					}
				}
				else {
					//low mid high
					String middle = avgString(midRange[0], midRange[1]);
					portfolio.add(createOption("Buy", "Put", middle, blackScholes(s, getDouble(middle), r, sigma, t, false)));
				}

			} else if (mid > low) {
				if (high > low){
					if (high > mid){
						// high mid low
						portfolio.add(createOption("Buy", "Stock", currPrice, 0.0));
					} else {
						// mid high low
						if (mid - high >= 2) {
							portfolio.add(createOption("Sell", "Put", currPrice, blackScholes(s, getDouble(currPrice), r, sigma, t, false)));
							portfolio.add(createOption("Buy", "Put", midRange[0], blackScholes(s, getDouble(midRange[0]), r, sigma, t, false)));
							portfolio.add(createOption("Sell", "Call", currPrice, blackScholes(s, getDouble(currPrice), r, sigma, t, true)));
							portfolio.add(createOption("Buy", "Call", midRange[1], blackScholes(s, getDouble(midRange[1]), r, sigma, t, true)));
						} else  if (high - low >= 2){
							portfolio.add(createOption("Buy", "Stock", currPrice, 0.0));
							if (high - low < 3){
								portfolio.add(createOption("Buy", "Put", midRange[0], blackScholes(s, getDouble(midRange[0]), r, sigma, t, false)));
							}
						} else {
							portfolio.add(createOption("Sell", "Put", midRange[0], blackScholes(s, getDouble(midRange[0]), r, sigma, t, false)));
							portfolio.add(createOption("Buy", "Put", avgString(lowRange[0], lowRange[1]), blackScholes(s, getDouble(avgString(lowRange[0], lowRange[1])), r, sigma, t, false)));
							portfolio.add(createOption("Sell", "Call", midRange[1], blackScholes(s, getDouble(midRange[1]), r, sigma, t, true)));
							portfolio.add(createOption("Buy", "Call", avgString(highRange[0], highRange[1]), blackScholes(s, getDouble(avgString(highRange[0], highRange[1])), r, sigma, t, true)));
						}
					}
				}
				else {
					//mid low high
					if (mid - low >= 2) {
						portfolio.add(createOption("Sell", "Put", currPrice, blackScholes(s, getDouble(currPrice), r, sigma, t, false)));
						portfolio.add(createOption("Buy", "Put", midRange[0], blackScholes(s, getDouble(midRange[0]), r, sigma, t, false)));
						portfolio.add(createOption("Sell", "Call", currPrice, blackScholes(s, getDouble(currPrice), r, sigma, t, true)));
						portfolio.add(createOption("Buy", "Call", midRange[1], blackScholes(s, getDouble(midRange[1]), r, sigma, t, true)));
					} else  if (low - high >= 2){
						String middle = avgString(midRange[0], midRange[1]);
						portfolio.add(createOption("Buy", "Put", middle, blackScholes(s, getDouble(middle), r, sigma, t, false)));
					} else {
						portfolio.add(createOption("Sell", "Put", midRange[0], blackScholes(s, getDouble(midRange[0]), r, sigma, t, false)));
						portfolio.add(createOption("Buy", "Put", avgString(lowRange[0], lowRange[1]), blackScholes(s, getDouble(avgString(lowRange[0], lowRange[1])), r, sigma, t, false)));
						portfolio.add(createOption("Sell", "Call", midRange[1], blackScholes(s, getDouble(midRange[1]), r, sigma, t, true)));
						portfolio.add(createOption("Buy", "Call", avgString(highRange[0], highRange[1]), blackScholes(s, getDouble(avgString(highRange[0], highRange[1])), r, sigma, t, true)));
					}
				}
			}
		}

		return applyMaxLoss(portfolio, maxLoss);
	}

	private List<String[]> applyMaxLoss(List<String[]> portfolio, double maxLoss){
		return portfolio;
	}

/*
* build graph
*/
	public List<double[]> getGraphPoints(List<String[]> portfolio){
		List<Double> strikes = new ArrayList<>();
		List<double[]> points = new ArrayList<>();
		strikes.add(0.0);
		for (String[] option : portfolio){
			double strk = getDouble(option[2]);
			strikes.add(strk);
		}
		double highest = getHighest(strikes);
		strikes.add(highest + 2*getDouble(currPrice));
		Collections.sort(strikes);
		Set<Double> visited = new HashSet<>();

		for (double d : strikes){
			if(visited.contains(d)){
				continue;
			}
			//calc price
			double total = 0.0;
			for (String[] option : portfolio){
				total += getProfit(d, option);
			}
			double[] coord = new double[2];
			coord[0] = d;
			coord[1] = total;
			points.add(coord);
			visited.add(d);
		}

		return points;
	}

/*
* profit from single option
*/
	private double getProfit(double price, String[] option){
		double ret = 0.0;
		//double curr = getDouble(currPrice);
		double strk = getDouble(option[2]);
		double prem = getDouble(option[3]);
		if (option[1].equals("Put")){
			ret = Double.max(strk - price - prem, -1*prem);
		}
		else if (option[1].equals("Call")){
			ret = Double.max(price - strk - prem, -1*prem);
		}
		else{
			ret = price - strk - prem;
		}
		//ret=getDouble(twoDecimalString(ret));
		//ret = (int)(ret*100+0.5) /100.0;

		if (option[0].equals("Sell")){
			return -1 * ret;
		}
		return ret;
	}

	private String[] createOption(String buySell, String callPut, String strike, double premium){
		String[] option = new String[4];
		option[0] = buySell;
		option[1] = callPut;
		option[2] = strike;
		option[3] = twoDecimalString(premium);
		return option;
	}

	public double blackScholes(double s, double x, double r, double sigma, double t, boolean isCall){

		double d1 = (Math.log(s/x) + (r + sigma * sigma/2) * t) / (sigma * Math.sqrt(t));
    double d2 = d1 - sigma * Math.sqrt(t);

		if (isCall){
			return s * Gaussian.cdf(d1) - x * Math.exp(-r*t) * Gaussian.cdf(d2);
		}
		return (s * Gaussian.cdf(-1*d1) - x * Math.exp(-r*t) * Gaussian.cdf(-1*d2)) * -1;
	}

	private double getDouble(String s){
		return Double.parseDouble(s);
	}

	private int getInteger(String s){
		return Integer.parseInt(s);
	}

	private String avgString(String a, String b){
		double c = getDouble(a) + getDouble(b);
		c = c/2.0d+0.0001;
		String s = Double.toString(c);
		if (s.contains(".")){
			int ind = s.indexOf('.');
			if (ind < s.length() - 2){
				s = s.substring(0, ind + 3);
			} else if (ind == s.length()-2){
				s = s.concat("0");
			} else {
				s = s.concat("00");
			}
		} else {
			s = s.concat(".00");
		}


		return s;
	}

	private String twoDecimalString(double d){
		String s = Double.toString(d+0.00001);
		if (s.contains(".")){
			int ind = s.indexOf('.');
			if (ind < s.length() - 2){
				s = s.substring(0, ind + 3);
			} else if (ind == s.length()-2){
				s = s.concat("0");
			} else {
				s = s.concat("00");
			}
		} else {
			s = s.concat(".00");
		}
		return s;
	}

	private double getHighest(List<Double> strikes){
		double highest = 0.0;
		for (double d : strikes){
			if (d>highest){
				highest = d;
			}
		}
		return highest;
	}

}
