package sample;

public class searchTableEntry{
    private String name;
    private String accountType;
    private String email;
    private String contact;
    private String s_no;
    private String cid,accountNum,balance;

    public searchTableEntry(String s_no, String cID,String name,String accountNum, String accountType, String balance,String email, String contact) {
        this.s_no = s_no;
        this.name = name;
        this.accountType = accountType;
        this.email = email;
        this.contact = contact;
        this.cid = cID;
        this.accountNum = accountNum;
        this.balance = balance;
    }

    public String getName() { return name; }
    public String getCid() { return cid; }
    public String getS_no() { return s_no; }
    public String getAccountType() {
        return accountType;
    }
    public String getEmail() {
        return email;
    }
    public String getContact() {
        return contact;
    }
    public String getAccountNum() {
        return accountNum;
    }
    public String getBalance() {
        return balance;
    }

}