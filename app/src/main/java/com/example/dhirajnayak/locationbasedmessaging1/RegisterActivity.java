package com.example.dhirajnayak.locationbasedmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.LOGIN_URL;
import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.REGISTER_URL;

public class RegisterActivity extends AppCompatActivity implements LoginRegisterAsyncTask.IData {

    EditText editTextUserName;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    Button buttonRegister;
    String userName,firstName,lastName,password,confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextUserName= (EditText) findViewById(R.id.editTextRegisterUserName);
        editTextFirstName= (EditText) findViewById(R.id.editTextRegisterFirstName);
        editTextLastName= (EditText) findViewById(R.id.editTextRegisterLastName);
        editTextPassword= (EditText) findViewById(R.id.editTextRegisterPassword);
        editTextConfirmPassword= (EditText) findViewById(R.id.editTextRegisterConfirmPassword);
        buttonRegister= (Button) findViewById(R.id.buttonRegisterRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName=editTextUserName.getText().toString().trim();
                firstName=editTextFirstName.getText().toString().trim();
                lastName=editTextLastName.getText().toString().trim();
                password=editTextPassword.getText().toString();
                confirmPassword=editTextConfirmPassword.getText().toString();

                if(userName!=null && !userName.isEmpty() && firstName!=null && !firstName.isEmpty() &&
                        lastName!=null && !lastName.isEmpty() && password!=null && !password.isEmpty() &&
                        password.equals(confirmPassword)){
                    new LoginRegisterAsyncTask(RegisterActivity.this).execute(REGISTER_URL,userName,password,firstName,lastName);
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Invalid data",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void setupData(LoginDetails loginDetails) {
        if(loginDetails.getCode()==200){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("token",loginDetails.getToken());
            editor.putString("loggedUser",loginDetails.getUserName());
            editor.apply();
            Intent intent=new Intent(this,InboxActivity.class);
            startActivity(intent);
        }
        Toast.makeText(RegisterActivity.this,loginDetails.getMessage(),Toast.LENGTH_LONG).show();
    }
}
