package ics.co.ke.businessaccount.pojo;

import java.io.Serializable;

public class CreditPurchase implements Serializable  {
    private int id;
    private String customer_name;
    private String phone_number;
    private String amountCredited;
    private String amountDebited;
    private String trans_id;
    private String credittransaction_id;
    private String debittransaction_id;
    private String date;
    private String user_national_id;
    private String trans_type;

    private String marikiti_recipient_name;


    public CreditPurchase(int id, String customer_name, String phone_number, String amountCredited, String amountDebited, String trans_id, String credittransaction_id, String debittransaction_id, String date) {
        this.id = id;
        this.customer_name = customer_name;
        this.phone_number = phone_number;
        this.amountCredited = amountCredited;
        this.amountDebited = amountDebited;
        this.trans_id = trans_id;
        this.credittransaction_id = credittransaction_id;
        this.debittransaction_id = debittransaction_id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getAmountCredited() {
        return amountCredited;
    }

    public void setAmountCredited(String amountCredited) {
        this.amountCredited = amountCredited;
    }

    public String getAmountDebited() {
        return amountDebited;
    }

    public void setAmountDebited(String amountDebited) {
        this.amountDebited = amountDebited;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getCredittransaction_id() {
        return credittransaction_id;
    }

    public void setCredittransaction_id(String credittransaction_id) {
        this.credittransaction_id = credittransaction_id;
    }

    public String getDebittransaction_id() {
        return debittransaction_id;
    }

    public void setDebittransaction_id(String debittransaction_id) {
        this.debittransaction_id = debittransaction_id;
    }

    public String getUser_national_id() {
        return user_national_id;
    }

    public void setUser_national_id(String user_national_id) {
        this.user_national_id = user_national_id;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMarikiti_recipient_name() {
        return marikiti_recipient_name;
    }

    public void setMarikiti_recipient_name(String marikiti_recipient_name) {
        this.marikiti_recipient_name = marikiti_recipient_name;
    }
}
