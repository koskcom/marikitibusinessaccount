package ics.co.ke.businessaccount.pojo;

public class Sends {
    private double balance;
    private double wdValue;

    //set current balance
    public void setBalance(double b) {

        balance = b;

    }//end setBalance
    //set withdraw value entered by user
    public void setSends (double wd) {


        wdValue = wd;
    }//end setWithdraw
    //calculate and return new balance
    public double getNewBalance() {

        return balance - wdValue;


    }//end getNewBalance
}
