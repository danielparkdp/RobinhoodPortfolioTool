package edu.brown.cs.dpark20.algorithms;

import java.util.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.IOException;


public class Company {
  private String company;
  private double pr;
  private double vol;

  public Company(String c){
    company = c.trim().toUpperCase().replaceAll("\\s+","");
    pr = 1.0;
    vol = 0.0;
  }

  public double getPrice(){
    int count = 0;
    double val = 0.0;
    if (company.trim().length() == 0){
      return 0.0;
    }
    try {
      Document doc = Jsoup.connect("https://ca.finance.yahoo.com/quote/".concat(company)).get();
      Elements prices = doc.getElementsByClass("Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)");

      for (Element e : prices){
        String temp = e.text();
        temp = temp.replaceAll("[^0-9.]", "");
        if (temp.trim().length() == 0){
          return -1.0;
        }
        val = Double.parseDouble(temp);
        count++;
      }
    } catch (IOException e) {
      e.printStackTrace();

    }

    if (count == 0){
      return -1.0;
    }
    pr = val;
    return pr;
  }

  public double getVolatility(){
    int count = 0;
    double val = 0.0;
    if (company.trim().length() == 0){
      return 0.0;
    }
    List<Double> pastEleven = new ArrayList<>();
    try {
      Document doc = Jsoup.connect("https://ca.finance.yahoo.com/quote/"+company+"/history?p="+ company).get();
      Elements prices = doc.getElementsByClass("Py(10px) Pstart(10px)");

      for (Element e : prices){
        count++;
        if (count % 6 != 5){
          continue;
        }
        String temp = e.text();
        temp = temp.replaceAll("[^0-9.]", "");
        if (temp.trim().length() == 0){
          return -1.0;
        }

        pastEleven.add(Double.parseDouble(temp));
        if(pastEleven.size() > 11){
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();

    }

    if (count == 0){
      return -1.0;
    }
    List<Double> logReturns = getLogReturns(pastEleven);
    //252 trading days assumed per year (annualized)
    val = Math.sqrt(variance(logReturns)) * Math.sqrt(252);
    if (pr < 0.01){
      return -1.0;
    }
    vol = val;
    return vol;
  }

  private List<Double> getLogReturns(List<Double> a){
    List<Double> toRet = new ArrayList<>();
    if (a == null || a.size() < 2){
      return toRet;
    }
    for (int i = a.size()-2; i >= 0; i--){
      toRet.add(Math.log(a.get(i)) - Math.log(a.get(i+1)));
    }
    Collections.reverse(toRet);
    return toRet;

  }

  private double variance(List<Double> a){
    // Compute mean (average of elements)
    double sum = 0.0;
    int n = a.size();
    if (n == 0){
      return 0.0;
    }

    for (int i = 0; i < n; i++){
      sum += a.get(i);
    }
    double mean = sum/(double)n;

    // Compute sum squared differences with
    // mean.
    double sqDiff = 0.0;
    for (int i = 0; i < n; i++){
      sqDiff += (a.get(i) - mean) * (a.get(i) - mean);
    }

    return (double)sqDiff/(double)n;
  }

}
