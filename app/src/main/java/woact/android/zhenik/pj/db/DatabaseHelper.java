package woact.android.zhenik.pj.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String TAG = "# DatabaseHelper:> ";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "bank";
    // Table Names
    protected static final String TABLE_USERS = "users";
    private static final String TABLE_GROUPS = "groups";
    private static final String TABLE_USER_GROUP = "user_group";

    // Common column names
    protected static final String KEY_ID = "id";
    // USERS Table - column names
    protected static final String KEY_USER_NAME = "userName";
    protected static final String KEY_PASSWORD = "password";
    protected static final String KEY_FULL_NAME = "fullName";
    protected static final String KEY_MONEY = "money";
    protected static final String KEY_SCORE = "score";
    // GROUPS Table - column names
    private static final String KEY_TOTAL_MONEY = "totalMoney";
    private static final String KEY_AVAILABLE_MONEY = "availableMoney";
    // USER_GROUP Table
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_GROUP_ID = "groupId";
    private static final String KEY_INVESTMENT = "investment";
    private static final String KEY_LOAN = "loan";

    // users table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_NAME + " TEXT NOT NULL UNIQUE,"
            + KEY_PASSWORD + " TEXT NOT NULL,"
            + KEY_FULL_NAME + " TEXT NOT NULL,"
            + KEY_MONEY + " REAL DEFAULT 0,"
            + KEY_SCORE + " INTEGER DEFAULT 0)";

    // group table create statement
    private static final String CREATE_TABLE_GROUPS = "CREATE TABLE "
            + TABLE_GROUPS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TOTAL_MONEY + " REAL DEFAULT 0,"
            + KEY_AVAILABLE_MONEY  + " REAL DEFAULT 0)";

    // user_group table create statement
    private static final String CREATE_TABLE_USER_GROUP = "CREATE TABLE "
            + TABLE_USER_GROUP + "("
            + KEY_USER_ID + " INTEGER PRIMARY KEY,"
            + KEY_GROUP_ID + " INTEGER PRIMARY KEY,"
            + KEY_INVESTMENT + " REAL,"
            + KEY_LOAN + " REAL)";

    /**
     * Synchronized singleton
     * */
    private static DatabaseHelper instance;
    private DatabaseHelper(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
//        db.execSQL(CREATE_TABLE_GROUPS);
//        db.execSQL(CREATE_TABLE_USER_GROUP);
        Log.d(TAG, "db was created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_GROUP);
        Log.d(TAG, "drop tables, change vers from "+oldVersion+" to new "+newVersion);
        onCreate(db);
    }



//
//    // Deleting single contact
//    public void deleteUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int rowsDeleted = db.delete(TABLE_USERS, KEY_ID + " = ?",
//                  new String[] { String.valueOf(user.getId()) });
//        db.close();
////        Log.d(TAG, rowsDeleted+"");
//    }






}
