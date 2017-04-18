package woact.android.zhenik.pj.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.User;

import static org.junit.Assert.*;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_GROUP_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_INVESTMENT;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_LOAN;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_USER_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USER_GROUP;

public class UserGroupDaoTest {

    private static final String TAG="UserGroupDaoTest:> ";
    // Testing this
    private UserGroupDao userGroupDao;
    // Help things
    private UserDao userDao;
    private GroupDao groupDao;
    // Existing users
    private long userId;
    private long groupId;

    @Before
    public void setUp() {
        // In case you need the context in your test
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHelper mDatabaseHelper = DatabaseHelper.getHelper(context);
        DatabaseManager.initializeInstance(mDatabaseHelper);
        userGroupDao = new UserGroupDao();

        // utils
        userDao=new UserDao();
        groupDao=new GroupDao();
        createDataInDB();
    }

    @After
    public void tearDown(){
        userGroupDao.clearUserGroupTable();
        userDao.clearUsersTable();
        groupDao.clearGroupTable();
    }

    private void createDataInDB(){
        User user1 = new User("norman", "pass", "fullname");
        userId = userDao.registerUser(user1);
        assertTrue(userId!=-1l);
        groupId = groupDao.createGroup();
    }

    @Test
    public void checkDatabaseColumns(){
        // Arrange
        String selectQuery = "SELECT  * FROM " + TABLE_USER_GROUP;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // log
        Log.d(TAG, "TABLE NAME: "+ Arrays.asList(cursor.getColumnNames()).toString());
        // Assert
        assertEquals(5, cursor.getColumnCount());
        assertEquals(KEY_ID, cursor.getColumnName(0));
        assertEquals(KEY_USER_ID, cursor.getColumnName(1));
        assertEquals(KEY_GROUP_ID, cursor.getColumnName(2));
        assertEquals(KEY_INVESTMENT, cursor.getColumnName(3));
        assertEquals(KEY_LOAN, cursor.getColumnName(4));


    }

    @Test
    public void checkGetAllRowsFromTable_1Raw(){
        // Arrange
        long userGroupId = userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        int raws = userGroupDao.getAmountOfRows();

        // Assert
        assertEquals(1,raws);
    }

    @Test
    public void checkGetAllRowsFromTable_3Raws(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);
        userGroupDao.registerUserInGroup(userId, groupId);
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        int raws = userGroupDao.getAmountOfRows();

        // Assert
        assertEquals(3,raws);
    }


    @Test
    public void checkRegisterUserInGroup_UserAndGroup_Exist(){
        // Act
        long userGroupId = userGroupDao.registerUserInGroup(userId, groupId);

        // Assert
        assertTrue(userGroupId!=-1);
    }

    @Test
    public void checkRegisterUserInGroup_UserAndGroup_NOTExist(){
        // Act
        long userGroupId = userGroupDao.registerUserInGroup(4234l, 242342l);

        // Assert
        Log.d(TAG, userGroupId+"");
        assertTrue(userGroupId==-1);
    }


    @Test
    public void checkGetUsersFromGroup_UserAndGroup_1User1Group(){
        // Arrange
        long userGroupId = userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        List<User> usersInGroup = userGroupDao.getUsersFromGroup(groupId);

        // Assert
        assertEquals(1, usersInGroup.size());
    }

    @Test
    public void checkGetUsersFromGroup_UserAndGroup_0User1Group(){
        // Act
        List<User> usersInGroup = userGroupDao.getUsersFromGroup(groupId);

        // Assert
        assertEquals(0, usersInGroup.size());
    }

    private void printList(List<User> users){
        for (User user : users){
            Log.d(TAG, user.toString());
        }
    }
    @Test
    public void checkGetUsersFromGroup_UserAndGroup_2Users1Group(){
        // Arrange
        long newUserId = userDao.registerUser(new User("foo", "bar", "foos"));
        userGroupDao.registerUserInGroup(userId, groupId);
        userGroupDao.registerUserInGroup(newUserId, groupId);

        // Act
        List<User> usersInGroup = userGroupDao.getUsersFromGroup(groupId);
        Log.d(TAG, "---RAWS---"+userGroupDao.getAmountOfRows()+"");
        printList(usersInGroup);

        // Assert
        assertEquals(2, usersInGroup.size());
    }



    @Test
    public void checkGetGroupsOfUser_1User2Groups(){
        // Arrange
        long newGroupId = groupDao.createGroup();
        assertTrue(newGroupId!=-1);
        userGroupDao.registerUserInGroup(userId, groupId);
        userGroupDao.registerUserInGroup(userId, newGroupId);

        // Act
        List<Group> groupList = userGroupDao.getGroupsOfUser(userId);

        // Assert
        assertEquals(2, groupList.size());

    }

}