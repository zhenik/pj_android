package woact.android.zhenik.pj.model;

/**
 * Created by NIK on 17/04/2017.
 */

public class Group {
    private long id;
    private double totalMoney;
    private double availableMoney;

    public Group() {}

    public Group(double totalMoney, double availableMoney) {
        this.totalMoney = totalMoney;
        this.availableMoney = availableMoney;
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
}
