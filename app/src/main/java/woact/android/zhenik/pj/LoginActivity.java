package woact.android.zhenik.pj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.handler.DatabaseUserHandler;
import woact.android.zhenik.pj.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG="DB/LoginActivity: ";

    private Button loginBtn;
    private Button registrationBtn;

    private EditText userNameInput;
    private EditText passwordInput;

    private DatabaseUserHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initInputTextViews();
        initButtons();



        db = new DatabaseUserHandler(getApplicationContext());

        /**
         * DEV MODE
         * */
        devMode();
    }

    private void devMode(){
        //        db.clearDb();
        // fake user
        db.addUser(new User("nik", "password", "Nikita Zhevnitskiy"));
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

                String userName = userNameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (userName!=null & !"".equals(userName) & password!=null & !"".equals(password)){
                    User user = db.getUserByNameAndPassword(userName, password);
                    if (user!=null)
//                        Toast.makeText(this, "WELCOME "+user.getFullName(), Toast.LENGTH_SHORT).show();
                        Toasty.success(this, "WELCOME "+user.getFullName(), Toast.LENGTH_SHORT).show();
                    else
                        Toasty.error(this, "FAILURE - check input data", Toast.LENGTH_SHORT).show();
                }
                else
                    Toasty.info(this, "Check input - fields can not be empty", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_registration_btn:
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }









    /**
     * CRUD manual tests Operations
     * */
    private void checkCRUD(){

//        long userId1 = db.addUser(new User("userName", "password", "ololoNAME"));
//        long userId3 = db.addUser(new User("userName", "password", "ololoNAME"));
//
//        User user = db.getUser(userId1);
//        db.deleteUser(user);
//        long userId2 = db.addUser(new User("nik39", "cool-password", "Nikita Zhevnitskiy"));
//        User user1 = db.getUserByNameAndPassword("nik39", "cool-password");
//        Log.d(TAG, " found by userName & password:" + user1.toString());
//
//
//        db.getAllUsers();
//        db.clearDb();
    }
}
