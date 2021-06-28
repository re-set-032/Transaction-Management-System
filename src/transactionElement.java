package sample;

public class transactionElement {
    private String s_no;
    private String from_acc;
    private String type;
    private String to_acc;
    private String amount;
    private String date;
    private String time;

    public transactionElement(String s_no, String from_acc, String type, String to_acc, String amount, String date, String time) {
        this.s_no = s_no;
        this.from_acc = from_acc;
        this.type = type;
        this.to_acc = to_acc;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }

    public String getS_no() { return s_no; }
    public String getFrom_acc() { return from_acc; }
    public String getType() { return type; }
    public String getTo_acc() { return to_acc; }
    public String getAmount() { return amount; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}
