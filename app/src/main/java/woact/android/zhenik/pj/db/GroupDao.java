package woact.android.zhenik.pj.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.User;

import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_AVAILABLE_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_GROUP_NAME;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_SCORE;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_TOTAL_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_GROUPS;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USERS;


public class GroupDao {

    private static final String TAG = "# GroupDao:> ";

    public GroupDao() {}

    public long createGroup() {
        long groupId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL_MONEY, 0l);
        values.put(KEY_AVAILABLE_MONEY, 0l);
        // Inserting Row
        groupId = db.insert(TABLE_GROUPS, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return groupId;
    }

    /**
     * Only for development mode
     * TODO: Move to tests
     * */
    public void clearGroupTable() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_GROUPS, null, null);
        DatabaseManager.getInstance().closeDatabase();
        Log.d(TAG, "---clean db---");
    }

    public Group getGroup(long groupId){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(
                TABLE_GROUPS,
                new String[] {KEY_ID, KEY_TOTAL_MONEY, KEY_AVAILABLE_MONEY, KEY_GROUP_NAME},
                KEY_ID + "=?",
                new String[] {String.valueOf(groupId)},
                null, null, null, null);

        if (cursor != null && cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;
        Group group = new Group(cursor.getLong(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getString(3));
        return group;
    }


    public Double getTotalMoney(long groupId){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(
                TABLE_GROUPS,
                new String[] {KEY_TOTAL_MONEY},
                KEY_ID + "=?",
                new String[] {String.valueOf(groupId)},
                null, null, null, null);

        if (cursor != null && cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;
        Double totalMoney = cursor.getDouble(0);
        return totalMoney;
    }

    public long updateTotalMoney(long groupId, double newMoney){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL_MONEY, newMoney);
        // updating row
        return db.update(TABLE_GROUPS, values, KEY_ID + " = ?",
                         new String[] { String.valueOf(groupId) });
    }



    public Double getAvailableMoney(long groupId){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(
                TABLE_GROUPS,
                new String[] {KEY_AVAILABLE_MONEY},
                KEY_ID + "=?",
                new String[] {String.valueOf(groupId)},
                null, null, null, null);

        if (cursor != null && cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;
        Double availableMoney = cursor.getDouble(0);
        DatabaseManager.getInstance().closeDatabase();
        return availableMoney;
    }

    public long updateAvailableMoney(long groupId, double newMoney){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AVAILABLE_MONEY, newMoney);
        // updating row
        long rowAffected= db.update(TABLE_GROUPS, values, KEY_ID + " = ?",
                         new String[] { String.valueOf(groupId) });
        DatabaseManager.getInstance().closeDatabase();
        return rowAffected;

    }

    // TODO: test it
    public long setGroupName(long groupId, String groupName){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME, groupName);
        // updating row
        long rowsAffected = db.update(TABLE_GROUPS, values, KEY_ID + " = ?",
                         new String[] { String.valueOf(groupId) });
        DatabaseManager.getInstance().closeDatabase();
        return rowsAffected;
    }

//    public String getGroupName(long groupId){
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        Cursor cursor = db.query(
//                TABLE_GROUPS,
//                new String[] {KEY_AVAILABLE_MONEY},
//                KEY_ID + "=?",
//                new String[] {String.valueOf(groupId)},
//                null, null, null, null);
//
//        if (cursor != null && cursor.getCount()>0)
//            cursor.moveToFirst();
//        else
//            return null;
//        Double availableMoney = cursor.getDouble(0);
//        DatabaseManager.getInstance().closeDatabase();
//        return availableMoney;
//    }


}
