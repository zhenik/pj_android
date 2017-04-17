package woact.android.zhenik.pj.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import woact.android.zhenik.pj.model.User;

import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_FULL_NAME;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_PASSWORD;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_SCORE;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_USER_NAME;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USERS;

public class UserDao {
    private static final String TAG = "# UserDao:> ";
    public UserDao() {}

    public long registerUser(User user) {
        long userId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_PASSWORD, getHashWithSalt(user.getPassword()));
        values.put(KEY_FULL_NAME, user.getFullName());
        // Inserting Row
        userId = db.insert(TABLE_USERS, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return userId;
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

    public User getUserByNameAndPassword(String name, String password){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[] {KEY_ID, KEY_USER_NAME, KEY_PASSWORD, KEY_FULL_NAME, KEY_MONEY, KEY_SCORE},
                KEY_USER_NAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[] {name, getHashWithSalt(password)},
                null, null, null, null);
        Log.d(TAG, "number of rows under cursor: "+cursor.getCount()+"");

        if (cursor != null & cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;

        User user = new User(Long.parseLong(cursor.getString(0)),
                             cursor.getString(1),
                             cursor.getString(2),
                             cursor.getString(3),
                             cursor.getDouble(4),
                             cursor.getLong(5));
        return user;
    }


    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUserName(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullName(cursor.getString(3));
                user.setMoney(cursor.getDouble(4));
                user.setScore(cursor.getLong(5));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        Log.d(TAG, "amount of users: "+userList.size());
//        Log.d(TAG, "Done2");
        return userList;
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


    public void clearUsersTable() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_USERS, null, null);
        DatabaseManager.getInstance().closeDatabase();
        Log.d(TAG, "---clean db---");
    }




}
