package woact.android.zhenik.pj.model;

import java.io.Serializable;


public class User implements Serializable{

    private long id;
    private String userName;
    private String password;
    private String fullName;
    private double money;
    private long score;

    public User() {}

    public User(long id, String userName, String password, String fullName, double money, long score) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.money = money;
        this.score = score;
    }

    public User(String userName, String password, String fullName, double money, long score) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.money = money;
        this.score = score;
    }

    public User(String userName, String password, String fullName) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", money=" + money +
                ", score=" + score +
                '}';
    }
}

