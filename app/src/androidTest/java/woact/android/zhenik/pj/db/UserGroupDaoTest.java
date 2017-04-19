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
        groupId = groupDao.createGroup("somename");
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
        long newGroupId = groupDao.createGroup("somename");
        assertTrue(newGroupId!=-1);
        userGroupDao.registerUserInGroup(userId, groupId);
        userGroupDao.registerUserInGroup(userId, newGroupId);

        // Act
        List<Group> groupList = userGroupDao.getGroupsOfUser(userId);

        // Assert
        assertEquals(2, groupList.size());

    }

    @Test
    public void checkGetUserInvestment_UserAndGroupExist(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        Double investment = userGroupDao.getUserInvestment(userId, groupId);

        // Assert
        assertEquals(0.0, investment, 0.01);
    }

    @Test
    public void checkGetUserInvestment_UserOrGroupNOTExist(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        Double investment1 = userGroupDao.getUserInvestment(231231, groupId);
        Double investment2 = userGroupDao.getUserInvestment(231231, 231231);
        Double investment3 = userGroupDao.getUserInvestment(userId, 231231);

        // Assert
        assertTrue(investment1==null);
        assertTrue(investment2==null);
        assertTrue(investment3==null);
    }


    @Test
    public void checkSetUserInvestment_UserAndGroupExist(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        Double investmentBefore = userGroupDao.getUserInvestment(userId, groupId);
        userGroupDao.setUserInvestment(userId,groupId,investmentBefore+10.0);
        Double investmentAfter = userGroupDao.getUserInvestment(userId, groupId);
        int rows = userGroupDao.getAmountOfRows();

        // Assert
        assertEquals(0.0, investmentBefore, 0.01);
        assertEquals(10.0, investmentAfter, 0.01);
        assertEquals(1, rows);
    }
    @Test
    public void checkSetUserInvestment_UserAOrGroupNOTExist(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act

        long rowsAffected1 = userGroupDao.setUserInvestment(231231,groupId,10.0);
        long rowsAffected2 = userGroupDao.setUserInvestment(userId,231231,10.0);
        long rowsAffected3 = userGroupDao.setUserInvestment(231231,231231,10.0);

        // Assert
        assertEquals(0, rowsAffected1);
        assertEquals(0, rowsAffected2);
        assertEquals(0, rowsAffected3);

    }


    @Test
    public void checkGetUserLoan_UserAndGroupExist(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        Double loan = userGroupDao.getUserLoan(userId, groupId);

        // Assert
        assertEquals(0.0, loan, 0.01);
    }

    @Test
    public void checkGetUserLoan_UserOrGroupNOTExist(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        Double loan1 = userGroupDao.getUserLoan(231231, groupId);
        Double loan2 = userGroupDao.getUserLoan(231231, 231231);
        Double loan3 = userGroupDao.getUserLoan(userId, 231231);

        // Assert
        assertTrue(loan1==null);
        assertTrue(loan2==null);
        assertTrue(loan3==null);
    }


    @Test
    public void checkSetUserLoan_UserAndGroupExist(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act
        Double loanBefore = userGroupDao.getUserLoan(userId, groupId);
        userGroupDao.setUserLoan(userId,groupId,10.0);
        Double loantAfter = userGroupDao.getUserLoan(userId, groupId);
        int rows = userGroupDao.getAmountOfRows();

        // Assert
        assertEquals(0.0, loanBefore, 0.01);
        assertEquals(10.0, loantAfter, 0.01);
        assertEquals(1, rows);
    }
    @Test
    public void checkSetUserLoan_UserAOrGroupNOTExist(){
        // Arrange
        userGroupDao.registerUserInGroup(userId, groupId);

        // Act

        long rowsAffected1 = userGroupDao.setUserLoan(231231,groupId,10.0);
        long rowsAffected2 = userGroupDao.setUserLoan(userId,231231,10.0);
        long rowsAffected3 = userGroupDao.setUserLoan(231231,231231,10.0);

        // Assert
        assertEquals(0, rowsAffected1);
        assertEquals(0, rowsAffected2);
        assertEquals(0, rowsAffected3);
    }

    @Test
    public void checkGetAllInvestmentsInGroup(){
        // Arrange
        long newUserId = userDao.registerUser(new User("foo", "bar", "foos"));
        userGroupDao.registerUserInGroup(userId, groupId);
        userGroupDao.registerUserInGroup(newUserId, groupId);

        // Act
        userGroupDao.setUserInvestment(userId, groupId, 5000);
        userGroupDao.setUserInvestment(newUserId, groupId, 6000);
        Double investments = userGroupDao.getAllInvestmentsInGroup(groupId);

        // Assert
        assertTrue(investments!=null);
        assertEquals(11000, investments, 0.01);

    }



}