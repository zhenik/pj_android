package woact.android.zhenik.pj.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import woact.android.zhenik.pj.model.User;

import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_GROUP_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_USER_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USERS;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USER_GROUP;

public class UserGroupDao {

    private static final String TAG = "# UserGroupDao:> ";
    private UserDao userDao;
    private GroupDao groupDao;

    public UserGroupDao(UserDao userDao, GroupDao groupDao) {
        this.userDao=userDao;
        this.groupDao=groupDao;
    }

    public UserGroupDao(){
        this(new UserDao(), new GroupDao());
    }

    /** TODO: TEST IT
     * @return userGroupId if operation successful, -1 if not
     * */
    public long registerUserInGroup(long userId, long groupId) {
        long userGroupId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        if (isUserIdValid(userId) && isGroupIdValid(groupId)){
            values.put(KEY_USER_ID, userId);
            values.put(KEY_GROUP_ID, groupId);
            // Inserting Row
            userGroupId = db.insert(TABLE_USER_GROUP, null, values);
            DatabaseManager.getInstance().closeDatabase();
            return userGroupId;
        }
        // wrong code
        return -1;
    }

    public int getAmountOfRows(){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_USER_GROUP,null);
        int rows =cursor.getCount();
        DatabaseManager.getInstance().closeDatabase();
        return rows;
    }

    private boolean isUserIdValid(long userId) {return userDao.getUserById(userId)!=null;}
    private boolean isGroupIdValid(long groupId){return groupDao.getGroup(groupId)!=null;}

    /**
     * @return List of users. If found nothing, return empty list (not null)
     * */
    public List<User> getUsersFromGroup(long groupId){

        // Sub-query return only 1 raw
//        String selectQuery = "SELECT * FROM "+ TABLE_USERS+" WHERE "+KEY_ID+"=("
//                + "SELECT "+KEY_USER_ID+" FROM "+TABLE_USER_GROUP+" WHERE "+KEY_GROUP_ID+"=?"+")";

        // INNER JOIN return set of values
//        String selectQuery = "SELECT "+TABLE_USERS+".* FROM "+TABLE_USERS +
//                " INNER JOIN "+TABLE_USER_GROUP+" ON "+TABLE_USERS+"."+KEY_ID+"="+TABLE_USER_GROUP+"."+KEY_USER_ID +
//                " WHERE "+TABLE_USER_GROUP+"."+KEY_GROUP_ID+"=?";

        String selectQuery = String.format(
                "SELECT %s.* FROM %s INNER JOIN %s ON %s.%s=%s.%s WHERE %s.%s=?",
                TABLE_USERS, TABLE_USERS, TABLE_USER_GROUP, TABLE_USERS, KEY_ID,
                TABLE_USER_GROUP, KEY_USER_ID, TABLE_USER_GROUP, KEY_GROUP_ID);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(groupId)});
        return getUsersFromCursor(cursor);
    }

    private List<User> getUsersFromCursor(Cursor cursor){
        List<User> userList = new ArrayList<>();
        if (cursor != null && cursor.getCount()>0)
            cursor.moveToFirst();

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
        return userList;

    }
//    // TODO: test it
//    private List<Long> getUsersIdFromGroup(long groupId){
//        List<Long> usersId = new ArrayList<Long>();
//        // Select All Query
//        String selectQuery = "SELECT " +KEY_USER_ID+ " FROM " + TABLE_USER_GROUP;
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                Long userId = cursor.getLong(0);
//                usersId.add(userId);
//            } while (cursor.moveToNext());
//
//            return usersId;
//        }
//        else
//            return null;
//
//    }


    /**
     * Only for development mode
     * TODO: Move to tests
     * */
    public void clearUserGroupTable() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_USER_GROUP, null, null);
        DatabaseManager.getInstance().closeDatabase();
        Log.d(TAG, "---clean db---");
    }
}
