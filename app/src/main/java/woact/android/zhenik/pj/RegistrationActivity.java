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

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private EditText fullName;
    private Button cBtn;
    private DatabaseUserHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        db = new DatabaseUserHandler(getApplicationContext());
        initInputs();
        initButton();
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
                    db.addUser(new User(userN, passN, fullN));

                    Toasty.success(getApplicationContext(), "user was created", Toast.LENGTH_SHORT).show();

                    Intent redirect = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(redirect);
                }
            }
        });
    }

    private boolean checkFields(){
        if ("".equals(userName.getText().toString()) || "".equals(password.getText().toString()) || "".equals(fullName.getText().toString()))
        {
            Toasty.info(getApplicationContext(), "All fields are required. Can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
