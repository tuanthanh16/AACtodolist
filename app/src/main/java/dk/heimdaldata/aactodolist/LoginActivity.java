package dk.heimdaldata.aactodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.heimdaldata.aactodolist.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText txtUserName;
    EditText txtPassword;
    Button btnLogin, btnSignup;
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

    private void signup() {

        // start Signup Activity
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

    }

    private void login() {
        String phoneNumber;
        String password;
        // should read database from here
        if (txtUserName.getText().toString().equals("123456")) {
            if (txtPassword.getText().toString().equals("123456")) {
                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}