package com.khatta.rockets.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.Contacts;
import com.digits.sdk.android.ContactsCallback;
import com.digits.sdk.android.ContactsUploadResult;
import com.digits.sdk.android.ContactsUploadService;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

/**
 * Created by veripark on 5/25/2016.
 */
public class ContactsReceiver extends BroadcastReceiver {

    private Context receiverContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        receiverContext = context;
        if (ContactsUploadService.UPLOAD_COMPLETE.equals(intent.getAction())) {
            ContactsUploadResult result = intent
                    .getParcelableExtra(ContactsUploadService.UPLOAD_COMPLETE_EXTRA);
            Toast.makeText(context, "Contacts Uploaded Successfully", Toast.LENGTH_SHORT).show();

            Log.d("Test>>>", "Contacts Uploaded Successfully");

        }else {
            // Post failure notification
        }
    }
}