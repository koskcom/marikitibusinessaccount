package ics.co.ke.businessaccount.model;

public class Transaction {
    int id;
    private String transaction_id;
    private String username;
    private String national_id;
    private String phone_Number;
    private String user_Pin;
    private String account_type;
    private String bank_Name;
    private String bank_account_number;
    private String type_of_account;
    private String recipient_number;
    private String date;
    private String trader_id;
    private String trader_username;
    private String payment_mode;
    private String trader_number;
    private String agent_number;
    private String amount;
    private String credit_amount;
    private String debit_amount;
   private String credit_amount_Transaction_number;
    private String debit_amount_Transaction_number;

    public Transaction() {
    }

    public Transaction(int id, String transaction_id, String username, String national_id, String phone_Number, String user_Pin, String account_type, String bank_Name, String bank_account_number, String type_of_account, String recipient_number, String date, String trader_id, String trader_username, String payment_mode, String trader_number, String agent_number, String amount, String credit_amount, String debit_amount, String credit_amount_Transaction_number, String debit_amount_Transaction_number) {
        this.id = id;
        this.transaction_id = transaction_id;
        this.username = username;
        this.national_id = national_id;
        this.phone_Number = phone_Number;
        this.user_Pin = user_Pin;
        this.account_type = account_type;
        this.bank_Name = bank_Name;
        this.bank_account_number = bank_account_number;
        this.type_of_account = type_of_account;
        this.recipient_number = recipient_number;
        this.date = date;
        this.trader_id = trader_id;
        this.trader_username = trader_username;
        this.payment_mode = payment_mode;
        this.trader_number = trader_number;
        this.agent_number = agent_number;
        this.amount = amount;
        this.credit_amount = credit_amount;
        this.debit_amount = debit_amount;
        this.credit_amount_Transaction_number = credit_amount_Transaction_number;
        this.debit_amount_Transaction_number = debit_amount_Transaction_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public String getUser_Pin() {
        return user_Pin;
    }

    public void setUser_Pin(String user_Pin) {
        this.user_Pin = user_Pin;
    }

    public String getBank_Name() {
        return bank_Name;
    }

    public void setBank_Name(String bank_Name) {
        this.bank_Name = bank_Name;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getType_of_account() {
        return type_of_account;
    }

    public void setType_of_account(String type_of_account) {
        this.type_of_account = type_of_account;
    }

    public String getRecipient_number() {
        return recipient_number;
    }

    public void setRecipient_number(String recipient_number) {
        this.recipient_number = recipient_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrader_id() {
        return trader_id;
    }

    public void setTrader_id(String trader_id) {
        this.trader_id = trader_id;
    }

    public String getTrader_username() {
        return trader_username;
    }

    public void setTrader_username(String trader_username) {
        this.trader_username = trader_username;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getTrader_number() {
        return trader_number;
    }

    public void setTrader_number(String trader_number) {
        this.trader_number = trader_number;
    }

    public String getAgent_number() {
        return agent_number;
    }

    public void setAgent_number(String agent_number) {
        this.agent_number = agent_number;
    }

    public String getCredit_amount() {
        return credit_amount;
    }

    public void setCredit_amount(String credit_amount) {
        this.credit_amount = credit_amount;
    }

    public String getDebit_amount() {
        return debit_amount;
    }

    public void setDebit_amount(String debit_amount) {
        this.debit_amount = debit_amount;
    }

    public String getCredit_amount_Transaction_number() {
        return credit_amount_Transaction_number;
    }

    public void setCredit_amount_Transaction_number(String credit_amount_Transaction_number) {
        this.credit_amount_Transaction_number = credit_amount_Transaction_number;
    }

    public String getDebit_amount_Transaction_number() {
        return debit_amount_Transaction_number;
    }

    public void setDebit_amount_Transaction_number(String debit_amount_Transaction_number) {
        this.debit_amount_Transaction_number = debit_amount_Transaction_number;
    }
}
