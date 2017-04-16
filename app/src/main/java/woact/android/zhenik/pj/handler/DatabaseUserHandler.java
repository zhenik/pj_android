package woact.android.zhenik.pj.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import woact.android.zhenik.pj.model.User;


public class DatabaseUserHandler extends SQLiteOpenHelper {

    public static final String TAG = "DB/DB_UserHandler: ";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "d";
    // User table name
    private static final String TABLE_USERS = "users";
    // User Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FULL_NAME = "name";

    public DatabaseUserHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_NAME + " TEXT NOT NULL UNIQUE,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_FULL_NAME + " TEXT)";

        db.execSQL(CREATE_USER_TABLE);
        Log.d(TAG, "db was created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        Log.d(TAG, "drop table if exists");
        onCreate(db);
    }

    // Adding new User
    public long addUser(User user) {
//        checkDb();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_PASSWORD, getHashWithSalt(user.getPassword()));
        values.put(KEY_FULL_NAME, user.getFullName());

        // Inserting Row
        long status = db.insert(TABLE_USERS, null, values);
        db.close();
//        Log.d(TAG, "userAdd status: " + status);
        return status;
    }

//    Get user by id
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[] {KEY_ID, KEY_USER_NAME, KEY_PASSWORD, KEY_FULL_NAME},
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        User user = new User(Long.parseLong(cursor.getString(0)),
                             cursor.getString(1),
                             cursor.getString(2),
                             cursor.getString(3));
//        Log.d(TAG, user.toString());
        return user;
    }


//     Getting All Users
    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUserName(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullName(cursor.getString(3));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        Log.d(TAG, "amount of users: "+userList.size());
        printList(userList);
//        Log.d(TAG, "Done2");
        return userList;
    }

    // Updating single user
    /**
     * TODO: Need to update 1 field by 1.
     * Be careful with password, don't update all with 1 transaction
     * */
    private long updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_FULL_NAME, user.getFullName());

        // updating row
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                         new String[] { String.valueOf(user.getId()) });
    }

    public User getUserByNameAndPassword(String name, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[] {KEY_ID, KEY_USER_NAME, KEY_PASSWORD, KEY_FULL_NAME},
                KEY_USER_NAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[] {name, getHashWithSalt(password)},
                null, null, null, null);
        Log.d(TAG, "number of rows under cursor"+cursor.getCount()+"");

        if (cursor != null & cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;

        User user = new User(Long.parseLong(cursor.getString(0)),
                             cursor.getString(1),
                             cursor.getString(2),
                             cursor.getString(3));
//        Log.d(TAG, user.toString());
        return user;
    }

    // Deleting single contact
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USERS, KEY_ID + " = ?",
                  new String[] { String.valueOf(user.getId()) });
        db.close();
//        Log.d(TAG, rowsDeleted+"");
    }



    private void printList(List<User> list){
        for (User u : list)Log.d(TAG,u.toString());
    }

    public void clearDb(){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "--- Clear " +TABLE_USERS+ " ---");
        // удаляем все записи
        int clearCount = db.delete(TABLE_USERS, null, null);
        Log.d(TAG, "deleted rows count = " + clearCount);
        db.close();
    }

    private void checkDb(){
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, Arrays.asList(cursor.getColumnNames()).toString());
    }

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
