package woact.android.zhenik.pj.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import woact.android.zhenik.pj.model.User;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {
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


}