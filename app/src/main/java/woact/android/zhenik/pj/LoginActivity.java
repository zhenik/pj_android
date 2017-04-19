package woact.android.zhenik.pj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        /**
         * dev mode ONLY
         * TODO: Delete application from mobile-devise before jUnit tests, because dummydata conflicts.
         * */
        devModeDummyData();
    }

    @Override
    protected void onResume() {
        getSupportActionBar().setTitle("DNB BONUS");
        super.onResume();
    }

    private void devModeDummyData(){
        DummyDataFactory ddF = new DummyDataFactory();
        ddF.addUserDevMode(new User("a", "a", "Changiz Hosseini", 50000, 150));
        ddF.addUserDevMode(new User("b", "b", "Oda Humlung", 65000, 250));

        ddF.addGroupDevMode(new Group(1,30000,20000, "Friends"));
        ddF.addGroupDevMode(new Group(2,20000,20000, "Family"));
        ddF.addGroupDevMode(new Group(3,10000,7000, "Banda"));
//        GroupDao groupDao = new GroupDao();
//        groupDao.setGroupName(1, "Friends");
//        groupDao.setGroupName(2, "Family");
//        groupDao.setGroupName(3, "Banda");

        ddF.addUserGroupDevMode(1,1,1,20000,10000);
        ddF.addUserGroupDevMode(2,2,1,10000,0);
        ddF.addUserGroupDevMode(3,2,2,20000,0);
        ddF.addUserGroupDevMode(4,1,3,5000,1000);
        ddF.addUserGroupDevMode(5,2,3,5000,2000);

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
