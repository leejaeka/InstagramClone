/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{
  Boolean signUpModeActive = true;
  EditText passwordEditText;
  public void showUserList() {
    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intent);
  }

  // CHANGE SIGNUP MODE
  @Override
  public void onClick(View view) {
    if (view.getId()==R.id.changeSignupModeTextView){
      TextView changeSignUpModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);
      Button signupButton = (Button) findViewById(R.id.SignupButton);
      if (signUpModeActive && changeSignUpModeTextView!= null){
        signupButton.setText("LOGIN");
        changeSignUpModeTextView.setText("Or, SIGNUP here");
        signUpModeActive = false;
      } else {
        if (changeSignUpModeTextView!= null){
          signupButton.setText("SIGNUP");
          changeSignUpModeTextView.setText("Or, LOGIN here");
          signUpModeActive = true;
        }

      }
      //Log.i("AppInfo","Change Signup Mode");
      // PLEASE MAKE KEYBOARD GO AWAY IF I CLICK SOMEWHERE ELSE
    } else if (view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoImageView) {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }

  public void signUp(View view){
    EditText usernameEditText = (EditText) findViewById(R.id.editText);
    EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
    if (usernameEditText.getText().toString().matches("")  || passwordEditText.getText().toString()==""){
      Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();
    } else{
      if (signUpModeActive){
        ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null){
              Log.i("Signup", "Succesful");
              showUserList();
            } else{
              Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      } else {
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (e == null){
              Log.i("LOGIN", "Login Succesful!");
              showUserList();
            } else{
              Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();

            }
          }
        });
      }

    }
  }

  @Override
  public boolean onKey(View view, int i , KeyEvent keyEvent){
    if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
      signUp(view);
    }
    return false;
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TextView changeSignUpModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);
    changeSignUpModeTextView.setOnClickListener(this);
    passwordEditText = (EditText) findViewById(R.id.editTextPassword);
    passwordEditText.setOnKeyListener(this);
    RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
    ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);
    backgroundRelativeLayout.setOnClickListener(this);
    logoImageView.setOnClickListener(this);
    if (ParseUser.getCurrentUser()!= null){
      showUserList();
    }
    // GET USER
    //ParseUser.getCurrentUser();
    // USER LOGOUT
//    ParseUser.logOut();
//    if (ParseUser.getCurrentUser() != null){
//      Log.i("CurrentUser", "User logged in " +ParseUser.getCurrentUser().getUsername());
//    } else {
//      Log.i("CurrentUser", "User not logged in");
//    }
//    // USER LOGIN
//    ParseUser.logInInBackground("user", "password", new LogInCallback() {
//      @Override
//      public void done(ParseUser user, ParseException e) {
//        if (user != null){
//          Log.i("Login", "Succesful");
//        } else {
//          Log.i("Login", "Failed");
//        }
//      }
//    });

// CREATE USER
//    ParseUser user = new ParseUser();
//    user.setUsername("user");
//    user.setPassword("password");
//    user.signUpInBackground(new SignUpCallback() {
//      @Override
//      public void done(ParseException e) {
//        if (e==null){
//          Log.i("Sign Up", "Succesful");
//        } else{
//          Log.i("Sign Up", "Failed");
//        }
//      }
//    });
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }
}