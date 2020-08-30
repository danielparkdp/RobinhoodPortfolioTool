package edu.brown.cs.dpark20.algorithms;

import java.util.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.IOException;


public class Company {
  private String company;

  public Company(String c){
    company = c.trim().toUpperCase().replaceAll("\\s+","");
  }

  public double getPrice(){
    int count = 0;
    double val = 0.0;
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
    return val;
  }

  public double getVolatility(){

    return 0.0;
  }

}
