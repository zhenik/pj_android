package woact.android.zhenik.pj.model;

public class Invitation {
    private long id;
    private long sendById;
    private long receivedById;
    private long groupId;

    private User sendBy;
    private User receivedBy;
    private Group group;

    public Invitation() {}

    public Invitation(long sendById, long receivedById, long groupId) {
        this.sendById = sendById;
        this.receivedById = receivedById;
        this.groupId = groupId;
    }

    public Invitation(long id, long sendById, long receivedById, long groupId) {
        this.id = id;
        this.sendById = sendById;
        this.receivedById = receivedById;
        this.groupId = groupId;
    }

    public Invitation(long id, long sendById, long receivedById, long groupId, User sendBy, User receivedBy) {
        this.id = id;
        this.sendById = sendById;
        this.receivedById = receivedById;
        this.groupId = groupId;
        this.sendBy = sendBy;
        this.receivedBy = receivedBy;
    }

    public Invitation(long id, long sendById, long receivedById, long groupId, User sendBy, User receivedBy, Group group) {
        this.id = id;
        this.sendById = sendById;
        this.receivedById = receivedById;
        this.groupId = groupId;
        this.sendBy = sendBy;
        this.receivedBy = receivedBy;
        this.group = group;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public long getSendById() {return sendById;}

    public void setSendById(long sendById) {this.sendById = sendById;}

    public long getReceivedById() {return receivedById;}

    public void setReceivedById(long receivedById) {this.receivedById = receivedById;}

    public long getGroupId() {return groupId;}

    public void setGroupId(long groupId) {this.groupId = groupId;}

    public User getSendBy() {
        return sendBy;
    }

    public void setSendBy(User sendBy) {
        this.sendBy = sendBy;
    }

    public User getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(User receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "id=" + id +
                ", sendById=" + sendById +
                ", receivedById=" + receivedById +
                ", groupId=" + groupId +
                '}';
    }
}
