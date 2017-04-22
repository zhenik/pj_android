package woact.android.zhenik.pj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.db.DatabaseHelper;
import woact.android.zhenik.pj.db.DatabaseManager;
import woact.android.zhenik.pj.db.DummyDataFactory;
import woact.android.zhenik.pj.db.GroupDao;
import woact.android.zhenik.pj.db.UserDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.User;
import woact.android.zhenik.pj.utils.ApplicationInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG="DB/LoginActivity: ";

    private Button loginBtn;
    private Button registrationBtn;

    private EditText userNameInput;
    private EditText passwordInput;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initInputTextViews();
        initButtons();

        DatabaseManager.initializeInstance(DatabaseHelper.getHelper(getApplicationContext()));
        userDao = new UserDao();
        databaseSetup();
    }

    /**
     * DB setup once only
     * */
    private void databaseSetup(){
//        DummyDataFactory ddF = new DummyDataFactory();
//        ddF.addUserDevMode(new User("nik", "nik", "Nik God", 100000, 100000));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("DUMMY:", " pref contain firstTime key - "+prefs.contains("firstTime"));
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            devModeDummyData();
            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

    }
    private void devModeDummyData(){
        DummyDataFactory ddF = new DummyDataFactory();
        ddF.addUserDevMode(new User("a", "a", "Changiz Hosseini", 5, 150));
        ddF.addUserDevMode(new User("b", "b", "Oda Humlung", 65000, 250));

        ddF.addUserDevMode(new User("c", "c", "Clement Marescaux", 35000, 50));
        ddF.addUserDevMode(new User("e", "e", "Elvira Semenova", 65000, 250));
        ddF.addUserDevMode(new User("i", "i", "Ingrid Kjensli Moe", 65000, 150));
        ddF.addUserDevMode(new User("s", "s", "Silje Lilleeng Johnsen", 65000, 450));
        ddF.addUserDevMode(new User("r", "r", "Robert Eikeland", 15000, 10));
        ddF.addUserDevMode(new User("n", "n", "Hustle Man", 100000, 1000));


        ddF.addGroupDevMode(new Group(1, 13000, 13000, "Friends"));
        ddF.addGroupDevMode(new Group(2,22000,20000, "Family"));
        ddF.addGroupDevMode(new Group(3,13000,7000, "Banda"));
//        GroupDao groupDao = new GroupDao();
//        groupDao.setGroupName(1, "Friends");
//        groupDao.setGroupName(2, "Family");
//        groupDao.setGroupName(3, "Banda");

        ddF.addUserGroupDevMode(1,1,1,5000,0);
        ddF.addUserGroupDevMode(2,2,1,5000,0);
        ddF.addUserGroupDevMode(3,2,2,20000,0);
        ddF.addUserGroupDevMode(4,1,3,5000,1000);
        ddF.addUserGroupDevMode(5,2,3,5000,2000);
        Log.d("DUMMY:", "______________________");
        Log.d("DUMMY:", "dummy data was created");
        Log.d("DUMMY:", "----------------------");
    }

    @Override
    protected void onResume() {
        getSupportActionBar().setTitle("");
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
//        getActionBar().hide();
        super.onResume();
    }

    private void initInputTextViews(){
        userNameInput=(EditText)findViewById(R.id.login_userName_field);
        passwordInput=(EditText)findViewById(R.id.login_password_field);
    }

    private void initButtons(){
        loginBtn=(Button) findViewById(R.id.login_login_btn);
        registrationBtn=(Button)findViewById(R.id.login_registration_btn);
        loginBtn.setOnClickListener(this);
        registrationBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login_btn:
                authenticationProcess();
                break;
            case R.id.login_registration_btn:
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void authenticationProcess(){

        String userName = userNameInput.getText().toString();
        String password = passwordInput.getText().toString();
        userNameInput.setText("");
        passwordInput.setText("");

        if (!"".equals(userName) && !"".equals(password)){
            User user = userDao.getUserByNameAndPassword(userName, password);

            if (user!=null){
                Toasty.success(this, "WELCOME "+user.getFullName(), Toast.LENGTH_SHORT).show();
                Intent redirectToMain = new Intent(getApplicationContext(), MainActivity.class);
                ApplicationInfo.USER_IN_SYSTEM_ID =user.getId();
                Log.d(TAG, " ---- user in system : "+ApplicationInfo.USER_IN_SYSTEM_ID);
                /** set user in system id */
                redirectToMain.putExtra(ApplicationInfo.USER, ApplicationInfo.USER_IN_SYSTEM_ID);
                startActivity(redirectToMain);

            }
            else{
                Toasty.error(this, "FAILURE - check input data", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toasty.info(this, "Check input - fields can not be empty", Toast.LENGTH_SHORT).show();
    }


}
