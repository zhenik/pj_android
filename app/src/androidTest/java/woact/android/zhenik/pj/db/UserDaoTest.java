package woact.android.zhenik.pj.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import woact.android.zhenik.pj.model.User;

import static org.junit.Assert.*;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USERS;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    private static final String TAG="UserDaoTest:> ";
    private UserDao userDao;

    @Before
    public void setUp() {
        // In case you need the context in your test
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHelper mDatabaseHelper = DatabaseHelper.getHelper(context);
        DatabaseManager.initializeInstance(mDatabaseHelper);
//        DatabaseManager mDatabaseManager=DatabaseManager.getInstance();
        userDao = new UserDao();
    }

    @After
    public void tearDown(){
        userDao.clearUsersTable();
    }

    @Test
    public void checkDatabaseColumns(){
        // Arrange
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // log
        Log.d(TAG, "TABLE NAME: "+Arrays.asList(cursor.getColumnNames()).toString());
        // Assert
        assertEquals(6, cursor.getColumnCount());
        assertEquals("id", cursor.getColumnName(0));
        assertEquals("userName", cursor.getColumnName(1));
        assertEquals("password", cursor.getColumnName(2));
        assertEquals("fullName", cursor.getColumnName(3));
        assertEquals("money", cursor.getColumnName(4));
        assertEquals("score", cursor.getColumnName(5));
    }

    @Test
    public void testUserInsert() {
        long id1 = userDao.registerUser(new User("norman", "pass", "fullname", 2.2, 245));
        assertEquals(1l, id1);
    }



    @Test
    public void testTryRegisterSameUser(){
        long id1 = userDao.registerUser(new User("norman", "pass", "fullname", 2.2, 245));
        long id2 = userDao.registerUser(new User("norman", "pass", "fullname", 2.2, 245));

        // Assert
        assertTrue(id1!=-1l); // created
        assertEquals(-1l, id2); // not create, table constraints
    }

    @Test
    public void testGetUserByNameAndPassword_UserExist(){
        // Arrange
        User user1 = new User("norman", "pass", "fullname");
        long id1 = userDao.registerUser(user1);
        assertTrue(id1!=-1l);
        // Act
        User userFromDb = userDao.getUserByNameAndPassword("norman", "pass");

        // Assert
        assertNotNull(userFromDb);
        assertEquals(user1.getUserName(), userFromDb.getUserName());
    }

    @Test
    public void testGetUserByNameAndPassword_UserNotExist(){
        // Act
        User userFromDb = userDao.getUserByNameAndPassword("notExist", "notExist");

        // Assert
        assertNull(userFromDb);
    }

    @Test
    public void testGetUserById_UserExist(){
        // Arrange
        User user1 = new User("norman", "pass", "fullname");
        long id1 = userDao.registerUser(user1);

        // Act
        User userFromDb = userDao.getUserById(id1);
        Log.d(TAG, userFromDb.toString());

        // Assert
        assertNotNull(userFromDb);
        assertEquals(user1.getUserName(), userFromDb.getUserName());
    }

    @Test
    public void testGetUserById_UserNotExist(){
        // Arrange
        long idNotExist = 123432l;

        // Act
        User userFromDb = userDao.getUserById(idNotExist);

        // Assert
        assertNull(userFromDb);
    }

    @Test
    public void testUpdateScore_UserExist(){
        // Arrange
        User user1 = new User("norman", "pass", "fullname");
        long id1 = userDao.registerUser(user1);

        // Act
        User userBeforeUpdate = userDao.getUserById(id1);
        long rowsAffected = userDao.updateScore(id1, 555l);
        User userAfterUpdate = userDao.getUserById(id1);
        // log print
        Log.d(TAG, userBeforeUpdate.toString());
        Log.d(TAG, userAfterUpdate.toString());

        // Assert
        assertNotNull(userBeforeUpdate);
        assertNotNull(userAfterUpdate);
        assertEquals(1l, rowsAffected);
        assertEquals(555l, userAfterUpdate.getScore());
    }

    @Test
    public void testUpdateScore_UserNotExist(){
        // Arrange
        long idNotExist = 1232353453l;

        // Act
        long rowsAffected = userDao.updateScore(idNotExist, 555l);

        // Assert
        assertEquals(0l, rowsAffected);
    }

    @Test
    public void testGetScore_UserExist(){
        // Arrange
        User user1 = new User("norman", "pass", "fullname");
        long id1 = userDao.registerUser(user1);

        // Act
        long rowsAffected = userDao.updateScore(id1, 555l);
        long score = userDao.getScore(id1);

        // Assert
        assertTrue(rowsAffected==1);
        assertEquals(555l, score);
    }

    @Test
    public void testGetScore_UserNotExist(){
        // Arrange
        long idNotExist = 1232353453l;

        // Act
        Long score = userDao.getScore(idNotExist);

        // Assert
        assertNull(score);
    }

    @Test
    public void testGetAllUsers(){
        // Arrange
        userDao.registerUser(new User("Oda", "p1", "Oda H."));
        userDao.registerUser(new User("Clement", "p23", "Clement M."));
        userDao.registerUser(new User("Elvira", "p3243", "Elvira S."));

        // Act
        List<User> users = userDao.getAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    public void testUpdateMoney_UserExist(){
        // Arrange
        long id = userDao.registerUser(new User("Oda", "p1", "Oda H."));

        // Act
        User userBeforeUpdate = userDao.getUserById(id);
        long rowsAffected = userDao.setMoney(id, 555.5);
        User userAfterUpdate = userDao.getUserById(id);

        // Assert
        assertNotNull(userBeforeUpdate);
        assertNotNull(userAfterUpdate);
        assertEquals(1l, rowsAffected);
        assertEquals(555.5, userAfterUpdate.getMoney(), 0.01);
    }

    @Test
    public void testUpdateMoney_UserNotExist(){
        // Arrange
        long idNotExist = 432423423l;

        // Act
        long rowsAffected = userDao.setMoney(idNotExist, 555.5);

        // Assert
        assertTrue(rowsAffected==0);
    }

    @Test
    public void testGetMoney_UserExist(){
        // Arrange
        long id = userDao.registerUser(new User("Oda", "p1", "Oda H."));

        // Act
        userDao.setMoney(id, 14000.0);
        Double money = userDao.getMoney(id);

        // Assert
        assertEquals(14000.0, money.doubleValue(), 0.01);
    }

    @Test
    public void testGetMoney_UserNotExist(){
        // Arrange
        long idNotExist = 432423423l;

        // Act
        Double money = userDao.getMoney(idNotExist);

        // Assert
        assertNull(money);
    }


}