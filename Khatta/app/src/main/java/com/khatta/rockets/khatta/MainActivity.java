package com.khatta.rockets.khatta;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Contacts;
import com.digits.sdk.android.ContactsCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.khatta.rockets.models.User;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "2mtArD8E2tLyhxDojoMz25hBF";
    private static final String TWITTER_SECRET = "jb7E3xwOLd17UuaiEmC0yG6bDRyXMWGgfoAk0n0FITDnfYYlWd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        setContentView(R.layout.activity_main);

        Button findFriends = (Button) findViewById(R.id.buttonFindContacts);
        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Digits.getInstance().getContactsClient().lookupContactMatches(null, null,
                        new ContactsCallback<Contacts>() {

                            @Override
                            public void success(Result<Contacts> result) {
                                if (result.data.users != null) {
                                    Log.d("Test>>>","Contacts Received "+ result.data.users.size()+"");
                                    Log.d("Test>>>","Contacts Received Count: "+ result.data.users.size()+"");
                                    Toast.makeText(MainActivity.this,"Contacts Received "+ result.data.users.size()+"", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Toast.makeText(MainActivity.this,"Contacts Exception", Toast.LENGTH_SHORT).show();
                                Log.d("Test>>>","Contacts Exception" + exception.getMessage() + "");
                            }
                        });
            }
        }) ;

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, LENGTH_LONG).show();

                DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
                digitsButton.setCallback(new AuthCallback() {
                    @Override
                    public void success(DigitsSession session, String phoneNumber) {
                        // TODO: associate the session userID with your user model
                        makeText(getApplicationContext(), "Authentication successful for "
                                + phoneNumber, LENGTH_LONG).show();

                        Firebase ref = new Firebase("https://khata-app.firebaseio.com");
                        User currentUser = new User();

                        currentUser.setCellNo(session.getPhoneNumber());
                        currentUser.setEmailId("test " + session.getPhoneNumber() + "@test.com");
                        currentUser.setFirstName("FirstName " + session.getPhoneNumber());
                        currentUser.setLastName("LastName " + session.getPhoneNumber());
                        currentUser.setSessionId(session.getAuthToken().toString());
                        currentUser.setUserId(session.getId() + "");


                        ref.setValue(currentUser, new Firebase.ValueResultHandler<Object>() {

                            @Override
                            public void onSuccess(Object o) {
                                Log.d("Firebase>>> Success: ", o.toString());
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Log.d("Firebase>>> Failure: ", firebaseError.getMessage());
                            }
                        });

                        Digits.getInstance().getContactsClient().startContactsUpload();
                        //Digits.getInstance().getContactsClient().startContactsUpload();
                    }

                    @Override
                    public void failure(DigitsException exception) {
                        Log.d("Digits", "Sign in with Digits failure", exception);
                    }
                });


            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
