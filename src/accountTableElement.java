package sample;

public class accountTableElement{

    private String s_no;
    private String acc_no;
    private String type;
    private String balance;
    private String DOC;
    private String TOC;
    private String loanOutstanding;

    accountTableElement(String s_no,String acc_no,String type,String balance,String DOC,String TOC,String loanOutstanding)
    {
        this.acc_no = acc_no;
        this.balance = balance;
        this.DOC = DOC;
        this.TOC = TOC;
        this.type = type;
        this.loanOutstanding = loanOutstanding;
        this.s_no = s_no;
    }

    public String getS_no() { return s_no; }
    public String getAcc_no() { return acc_no; }
    public String getType() { return type; }
    public String getBalance() { return balance; }
    public String getDOC() { return DOC; }
    public String getTOC() { return TOC; }
    public String getLoanOutstanding() { return loanOutstanding; }

}
