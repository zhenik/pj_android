package woact.android.zhenik.pj.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
    private static final String TAG = "# DatabaseManager:> ";
    private Integer mOpenCounter = 0;

    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
            Log.d(TAG, " init instance");
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                                                    " is not initialized, call initializeInstance(..) method first.");
        }
        Log.d(TAG, " get instance");
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mDatabase = mDatabaseHelper.getWritableDatabase();
        Log.d(TAG, " open writable db");
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mDatabase.close();
        Log.d(TAG, " close db");
    }
}
