package woact.android.zhenik.pj.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import woact.android.zhenik.pj.db.DatabaseManager;
import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.User;

import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_AVAILABLE_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_FULL_NAME;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_GROUP_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_INVESTMENT;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_LOAN;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_PASSWORD;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_TOTAL_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_USER_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_USER_NAME;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_GROUPS;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USERS;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USER_GROUP;

public class DummyDataFactory {

    private static final String TAG = "DummyDataFactory:> ";
    public DummyDataFactory() {}

    /**
     * ONLY for development mode
     * TODO: Move to tests
     * */
    public long addUserGroupDevMode(long id, long userId, long groupId, double investment, double loan) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_USER_ID, userId);
        values.put(KEY_GROUP_ID, groupId);
        values.put(KEY_INVESTMENT, investment);
        values.put(KEY_LOAN, loan);
        // Inserting Row
        long userGroupId = db.insert(TABLE_USER_GROUP, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return groupId;
    }

    /**
     * Only for development mode
     * TODO: Move to tests
     * */
    public long addGroupDevMode(Group group) {
        long groupId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, group.getId());
        values.put(KEY_TOTAL_MONEY, group.getTotalMoney());
        values.put(KEY_AVAILABLE_MONEY, group.getAvailableMoney());
        // Inserting Row
        groupId = db.insert(TABLE_GROUPS, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return groupId;
    }

    public long addUserDevMode(User user) {
        long userId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_PASSWORD, getHashWithSalt(user.getPassword()));
        values.put(KEY_FULL_NAME, user.getFullName());
        values.put(DatabaseHelper.KEY_MONEY, user.getMoney());
        values.put(DatabaseHelper.KEY_SCORE, user.getScore());
        // Inserting Row
        userId = db.insert(TABLE_USERS, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return userId;
    }

    /**
     * Simple security mechanism
     * Using hash + salt storing instead of clear password text
     * */
    private String getHashWithSalt(String password){
        String salt = "NIK";
        MessageDigest messageDigest = null;
        try {messageDigest = MessageDigest.getInstance("SHA-256");}
        catch (NoSuchAlgorithmException e) {e.printStackTrace();}
        messageDigest.update((password+salt).getBytes());
        String encryptedString = new String(messageDigest.digest());
        return encryptedString;
    }
}
