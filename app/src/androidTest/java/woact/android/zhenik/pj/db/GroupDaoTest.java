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

import woact.android.zhenik.pj.model.Group;

import static org.junit.Assert.*;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_AVAILABLE_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_TOTAL_MONEY;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_GROUPS;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USERS;


public class GroupDaoTest {

    private static final String TAG="GroupDaoTest:> ";
    private GroupDao groupDao;

    @Before
    public void setUp() {
        // In case you need the context in your test
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHelper mDatabaseHelper = DatabaseHelper.getHelper(context);
        DatabaseManager.initializeInstance(mDatabaseHelper);
//        DatabaseManager mDatabaseManager=DatabaseManager.getInstance();
        groupDao = new GroupDao();
    }

    @After
    public void tearDown(){
        groupDao.clearGroupTable();
    }

    @Test
    public void checkDatabaseColumns(){
        // Arrange
        String selectQuery = "SELECT  * FROM " + TABLE_GROUPS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // log
        Log.d(TAG, "TABLE NAME: "+ Arrays.asList(cursor.getColumnNames()).toString());
        // Assert
        assertEquals(3, cursor.getColumnCount());
        assertEquals(KEY_ID, cursor.getColumnName(0));
        assertEquals(KEY_TOTAL_MONEY, cursor.getColumnName(1));
        assertEquals(KEY_AVAILABLE_MONEY, cursor.getColumnName(2));

    }

    @Test
    public void testCreateGroup(){
        // Act
        long id = groupDao.createGroup();

        // Assert
        assertTrue(id!=-1l);
    }

    @Test
    public void checkGetGroup_GroupExist(){
        // Arrange
        long id = groupDao.createGroup();

        // Act
        Group group = groupDao.getGroup(id);

        // Assert
        assertNotNull(group);
        assertEquals(0.0, group.getTotalMoney(), 0.01);
        assertEquals(0.0, group.getAvailableMoney(), 0.01);
    }

    @Test
    public void checkGetGroup_GroupNotExist(){
        // Arrange
        long idNotExist = 12334353l;
        // Act
        Group group = groupDao.getGroup(idNotExist);

        // Assert
        assertNull(group);
    }

    @Test
    public void checkGetTotalMoney_GroupExist(){
        // Arrange
        long id = groupDao.createGroup();

        // Act
        Double totalMoney = groupDao.getTotalMoney(id);

        // Assert
        assertNotNull(totalMoney);
        assertEquals(0.0, totalMoney.doubleValue(), 0.01);
    }

    @Test
    public void checkGetTotalMoney_GroupNotExist(){
        // Arrange
        long idNotExist = 1231234l;

        // Act
        Double totalMoney = groupDao.getTotalMoney(idNotExist);

        // Assert
        assertNull(totalMoney);
    }

    @Test
    public void checkUpdateTotalMoney_GroupExist(){
        // Arrange
        long id = groupDao.createGroup();

        // Act
        Double totalMoneyBeforeUpdate = groupDao.getTotalMoney(id);
        long rowsAffected = groupDao.updateTotalMoney(id, 450.0);
        Double totalMoneyAfterUpdate = groupDao.getTotalMoney(id);

        // Assert
        assertEquals(0.0, totalMoneyBeforeUpdate, 0.01);
        assertEquals(rowsAffected, 1);
        assertEquals(450.0, totalMoneyAfterUpdate, 0.01);
    }

    @Test
    public void checkUpdateTotalMoney_GroupNotExist(){
        // Arrange
        long idNotExist = 1231234l;

        // Act
        long rowsAffected = groupDao.updateTotalMoney(idNotExist, 450.0);

        // Assert
        assertEquals(rowsAffected, 0);
    }

    @Test
    public void checkGetAvailableMoney_GroupExist(){
        // Arrange
        long id = groupDao.createGroup();

        // Act
        Double totalMoney = groupDao.getAvailableMoney(id);

        // Assert
        assertNotNull(totalMoney);
        assertEquals(0.0, totalMoney.doubleValue(), 0.01);
    }

    @Test
    public void checkGetAvailableMoney_GroupNotExist() {
        // Arrange
        long idNotExist = 1231234l;

        // Act
        Double totalMoney = groupDao.getAvailableMoney(idNotExist);

        // Assert
        assertNull(totalMoney);
    }

    @Test
    public void checkUpdateAvailableMoney_GroupExist(){
        // Arrange
        long id = groupDao.createGroup();

        // Act
        Double availableMoneyBeforeUpdate = groupDao.getAvailableMoney(id);
        long rowsAffected = groupDao.updateAvailableMoney(id, 450.0);
        Double availableMoneyAfterUpdate = groupDao.getAvailableMoney(id);

        // Assert
        assertEquals(0.0, availableMoneyBeforeUpdate, 0.01);
        assertEquals(rowsAffected, 1);
        assertEquals(450.0, availableMoneyAfterUpdate, 0.01);
    }

    @Test
    public void checkUpdateAvailableMoney_GroupNotExist(){
        // Arrange
        long idNotExist = 1231234l;

        // Act
        long rowsAffected = groupDao.updateAvailableMoney(idNotExist, 450.0);

        // Assert
        assertEquals(rowsAffected, 0);
    }


}