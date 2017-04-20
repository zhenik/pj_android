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

import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.Invitation;
import woact.android.zhenik.pj.model.User;

import static org.junit.Assert.*;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_GROUP_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_RECEIVED_BY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.KEY_SEND_BY_ID;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_INVITATIONS;
import static woact.android.zhenik.pj.db.DatabaseHelper.TABLE_USERS;

@RunWith(AndroidJUnit4.class)
public class InvitationDaoTest {
    private static final String TAG="InvitationDaoTest:> ";
    private InvitationDao invitationDao;
    private UserDao userDao;
    private GroupDao groupDao;

    // 2 users & 2 groups
    private long userId1;
    private long userId2;
    private long groupId1;
    private long groupId2;

    @Before
    public void setUp() {
        // In case you need the context in your test
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHelper mDatabaseHelper = DatabaseHelper.getHelper(context);
        DatabaseManager.initializeInstance(mDatabaseHelper);

        invitationDao = new InvitationDao();
        userDao=new UserDao();
        groupDao=new GroupDao();
        createDataInDB();

    }

    private void createDataInDB(){
        User user1 = new User("foo1", "bar1", "fullname1");
        User user2 = new User("foo2", "bar2", "fullname2");

        userId1 = userDao.registerUser(user1);
        userId2 = userDao.registerUser(user2);
        assertTrue(userId1!=-1l);
        assertTrue(userId2!=-1l);

        groupId1 = groupDao.createGroup("somename1");
        groupId2 = groupDao.createGroup("somename2");
    }
    @After
    public void tearDown(){
        invitationDao.clearInvitationsTable();
        userDao.clearUsersTable();
        groupDao.clearGroupTable();
    }

    @Test
    public void checkDatabaseColumns(){
        // Arrange
        String selectQuery = "SELECT  * FROM " + TABLE_INVITATIONS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // log
        Log.d(TAG, "TABLE NAME: "+ Arrays.asList(cursor.getColumnNames()).toString());
        // Assert
        assertEquals(4, cursor.getColumnCount());
        assertEquals(KEY_ID, cursor.getColumnName(0));
        assertEquals(KEY_SEND_BY_ID, cursor.getColumnName(1));
        assertEquals(KEY_RECEIVED_BY_ID, cursor.getColumnName(2));
        assertEquals(KEY_GROUP_ID, cursor.getColumnName(3));
    }

    // 1u - 1 user
    // 1r - 1 reciever
    // 1g - 1 group
    @Test
    public void checkCreateInvitation_1u1r1g(){
        // Act
        long inviteId = invitationDao.createInvitation(userId1,userId2,groupId1);

        // Assert
        assertTrue(inviteId!=-1);
    }
    @Test
    public void checkCreateInvitation_1u1r2g(){
        // Act
        long inviteId1 = invitationDao.createInvitation(userId1,userId2,groupId1);
        long inviteId2 = invitationDao.createInvitation(userId1,userId2,groupId1);

        // Assert
        assertTrue(inviteId1!=-1);
        assertTrue(inviteId2!=-1);
    }
    @Test
    public void checkGetInvitationList_1g(){
        // Arrange
        invitationDao.createInvitation(userId1,userId2,groupId1);

        // Assert
        List<Invitation> list = invitationDao.getInvitationsListOfUser(userId2);

        // Assert
        assertTrue(list.size()==1);
        assertEquals("somename1", list.get(0).getGroup().getGroupName());
        assertEquals("foo1", list.get(0).getSendBy().getUserName());
    }

    @Test
    public void checkGetInvitationList_2g(){
        // Arrange
        invitationDao.createInvitation(userId1,userId2,groupId1);
        invitationDao.createInvitation(userId1,userId2,groupId1);

        // Act
        List<Invitation> list = invitationDao.getInvitationsListOfUser(userId2);

        // Assert
        assertTrue(list.size()==2);
    }
    @Test
    public void checkDeleteInvitation(){
        // Arrange
        invitationDao.createInvitation(userId1,userId2,groupId1);
        invitationDao.createInvitation(userId1,userId2,groupId1);

        // Act
        List<Invitation> list = invitationDao.getInvitationsListOfUser(userId2);
        assertTrue(list.size()==2);
        int rawsDeleted=0;
        for (Invitation i : list){
            rawsDeleted+=invitationDao.deleteRaw(i.getId());
        }
        // Assert
        assertEquals(2, rawsDeleted);
    }

}