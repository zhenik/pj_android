package woact.android.zhenik.pj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.db.DatabaseHelper;
import woact.android.zhenik.pj.db.UserDao;
import woact.android.zhenik.pj.model.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private EditText fullName;
    private Button cBtn;
    private DatabaseHelper db;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        userDao = new UserDao();
        initInputs();
        initButton();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        getSupportActionBar().setTitle("REGISTRATION");
        super.onResume();
    }
    private void initInputs(){
        userName=(EditText)findViewById(R.id.reg_username_input);
        password=(EditText)findViewById(R.id.reg_password_input);
        fullName=(EditText)findViewById(R.id.reg_fullName_input);
    }

    private void initButton(){
        cBtn=(Button)findViewById(R.id.reg_create_btn);
        cBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()){
                    String userN = userName.getText().toString().trim();
                    String passN = password.getText().toString().trim();
                    String fullN = fullName.getText().toString().trim();
                    long id = userDao.registerUser(new User(userN, passN, fullN));

                    if (id == -1)
                        Toasty.error(getApplicationContext(), "user with phone number "+userN+ " already exists", Toast.LENGTH_SHORT).show();
                    else{
                        Toasty.success(getApplicationContext(), "user is registered", Toast.LENGTH_SHORT).show();
                        // score feature + 1000 score for registration
                        userDao.scoreBonus(id, 10000);
                        Intent redirect = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(redirect);
                    }

                }
            }
        });
    }

    private boolean checkFields(){
        if ("".equals(userName.getText().toString()) || "".equals(password.getText().toString()) || "".equals(fullName.getText().toString()))
        {
            Toasty.info(getApplicationContext(), "Field(s) required can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


}
