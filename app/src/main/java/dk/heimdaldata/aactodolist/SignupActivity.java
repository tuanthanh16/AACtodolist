package dk.heimdaldata.aactodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.heimdaldata.aactodolist.database.DatabaseHelper;

public class SignupActivity extends AppCompatActivity {

    EditText txtUserName, txtPassword;
    Button btnAdd;
    DatabaseHelper sqlDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        txtUserName = (EditText) findViewById(R.id.txtNewUser);
        txtPassword = (EditText) findViewById(R.id.txtNewPassword);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        sqlDb = DatabaseHelper.getInstance(getApplicationContext());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                boolean result = sqlDb.insertUser(user, password);
                if (result) {
                    Toast.makeText(v.getContext(), "User created successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "Signup failed", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }
}