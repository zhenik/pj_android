package woact.android.zhenik.pj.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.Invitation;
import woact.android.zhenik.pj.model.User;

import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_FULL_NAME;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_GROUP_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_PASSWORD;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_RECEIVED_BY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_SCORE;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_SEND_BY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_USER_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_USER_NAME;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_INVITATIONS;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USERS;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USER_GROUP;

public class InvitationDao {
    private static final String TAG = "InvitationDao:> ";
    private UserDao userDao;
    private GroupDao groupDao;

    public InvitationDao() {this(new UserDao(), new GroupDao());}

    public InvitationDao(UserDao userDao, GroupDao groupDao){
        this.userDao=userDao;
        this.groupDao=groupDao;
    }

    public long createInvitation(long userSender, long userReceiver, long groupId){
        long inviteId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SEND_BY_ID, userSender);
        values.put(KEY_RECEIVED_BY_ID, userReceiver);
        values.put(KEY_GROUP_ID, groupId);
        // Inserting Row
        inviteId = db.insert(TABLE_INVITATIONS, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return inviteId;
    }

    public Invitation getInvitationById(long inviteId){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(
                TABLE_INVITATIONS,
                new String[] {KEY_ID, KEY_SEND_BY_ID, KEY_RECEIVED_BY_ID, KEY_GROUP_ID},
                KEY_ID + "=?",
                new String[] {String.valueOf(inviteId)},
                null, null, null, null);

        if (cursor != null & cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;

        Invitation invitation = new Invitation();
        invitation.setId(cursor.getLong(0));
        invitation.setSendById(cursor.getLong(1));
        invitation.setSendBy(userDao.getUserById(cursor.getLong(1)));
        invitation.setReceivedById(cursor.getLong(2));
        invitation.setReceivedBy(userDao.getUserById(cursor.getLong(2)));
        invitation.setGroupId(cursor.getLong(3));
        invitation.setGroup(groupDao.getGroup(cursor.getLong(3)));

        return invitation;
    }


    public List<Invitation> getInvitationsListOfUser(long userId){
        List<Invitation> invitations = new ArrayList<Invitation>();
        String selectQuery = "SELECT * FROM " + TABLE_INVITATIONS
                + " WHERE "+KEY_RECEIVED_BY_ID+"=?";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.getCount()>0)
            cursor.moveToFirst();

        Invitation invitation=null;
        if (cursor.moveToFirst()) {
            do {
                invitation= new Invitation();
                invitation.setId(cursor.getLong(0));

                invitation.setSendById(cursor.getLong(1));
                invitation.setSendBy(userDao.getUserById(cursor.getLong(1)));

                invitation.setReceivedById(cursor.getLong(2));
                invitation.setReceivedBy(userDao.getUserById(cursor.getLong(2)));

                invitation.setGroupId(cursor.getLong(3));
                invitation.setGroup(groupDao.getGroup(cursor.getLong(3)));


                // Adding contact to list
                invitations.add(invitation);
            } while (cursor.moveToNext());
        }
        Log.d("DAO_INVITATION:> ", "amount of invitations: "+invitations.size());
        return invitations;
    }

    // Deleting single raw
    public int deleteRaw(long inviteId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        int rowsDeleted = db.delete(
                TABLE_INVITATIONS,
                KEY_ID + "=?",
                new String[] { String.valueOf(inviteId)}
        );
        DatabaseManager.getInstance().closeDatabase();
        return rowsDeleted;
    }





    /**
     * Only for development mode
     * TODO: Move to tests
     * */
    public void clearInvitationsTable() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_INVITATIONS, null, null);
        DatabaseManager.getInstance().closeDatabase();
        Log.d(TAG, "---clean db---");
    }

    public Invitation getInvitation(long senderId, long recieverId, long groupId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(
                TABLE_INVITATIONS,
                new String[] {KEY_ID, KEY_SEND_BY_ID, KEY_RECEIVED_BY_ID, KEY_GROUP_ID},
                KEY_SEND_BY_ID + "=? AND "+KEY_RECEIVED_BY_ID+"=? AND "+KEY_GROUP_ID+"=?",
                new String[] {String.valueOf(senderId),String.valueOf(recieverId),String.valueOf(groupId)},
                null, null, null, null);

        if (cursor != null & cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;

        Invitation invitation = new Invitation();
        invitation.setId(cursor.getLong(0));
        invitation.setSendById(cursor.getLong(1));
        invitation.setSendBy(userDao.getUserById(cursor.getLong(1)));
        invitation.setReceivedById(cursor.getLong(2));
        invitation.setReceivedBy(userDao.getUserById(cursor.getLong(2)));
        invitation.setGroupId(cursor.getLong(3));
        invitation.setGroup(groupDao.getGroup(cursor.getLong(3)));

        return invitation;
    }
}
