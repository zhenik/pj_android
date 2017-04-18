package woact.android.zhenik.pj.model;

/**
 * Created by NIK on 17/04/2017.
 */

public class Group {
    private long id;
    private double totalMoney;
    private double availableMoney;
    private String groupName;

    public Group() {}

    public Group(double totalMoney, double availableMoney) {
        this.totalMoney = totalMoney;
        this.availableMoney = availableMoney;
    }

    public Group(long id, double totalMoney, double availableMoney) {
        this.id = id;
        this.totalMoney = totalMoney;
        this.availableMoney = availableMoney;
    }

    public Group(long id, double totalMoney, double availableMoney, String groupName) {
        this.id = id;
        this.totalMoney = totalMoney;
        this.availableMoney = availableMoney;
        this.groupName = groupName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(double availableMoney) {
        this.availableMoney = availableMoney;
    }

    public String getGroupName() {return groupName;}

    public void setGroupName(String groupName) {this.groupName = groupName;}
}
