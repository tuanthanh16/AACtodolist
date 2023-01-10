package dk.heimdaldata.aactodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dk.heimdaldata.aactodolist.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText txtUserName;
    EditText txtPassword;
    Button btnLogin;
    TextView btnSignup;
    DatabaseHelper sqlDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        // initialize database to read user table from database
        sqlDb = DatabaseHelper.getInstance(getApplicationContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetViews();
    }

    private void signup() {

        // start Signup Activity
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

    }

    private void login() {
        String userName = txtUserName.getText().toString();
        String password;
        // should read database from here
        if ( !sqlDb.userExist(userName)) {
            Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show();
            resetViews();
//            return;
        } else {
            password = sqlDb.getUserPassword(userName);
            if (txtPassword.getText().toString().equals(password)) {
                int user_id = sqlDb.getUserId(userName);
                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.KEY_USER_ID, user_id);
                // get UserID and send to main activity
                startActivity(intent);
            } else {
                // clear fields
                resetViews();
                Toast.makeText(this, "User/Password is not match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetViews() {
        txtUserName.setText("");
        txtPassword.setText("");
    }
}