/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license.
 * See LICENSE in the project root for license information.
 */
package com.microsoft.graph.connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.extensions.Event;
import com.microsoft.graph.extensions.IEventCollectionPage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This activity handles the send mail operation of the app.
 * The app must be connected to Office 365 before this activity can send an email.
 * It also uses the GraphServiceController to send the message.
 */
public class SendMailActivity extends AppCompatActivity {

    // arguments for this activity
    public static final String ARG_GIVEN_NAME = "givenName";
    public static final String ARG_DISPLAY_ID = "displayableId";
    public static final String CALENDAR_ID_1 = "AAMkADQwN2Y5ZDRhLTkwMzMtNGNjYy04YjgxLTQ5M2MzZTMxNzhmYQBGAAAAAACbtO3M7fyRS5JKFXkg2k1KBwCjKaSUv49eSaBCQ_waeRiXAAAAAAEGAACjKaSUv49eSaBCQ_waeRiXAABtEzueAAA=";
    public static final String CALENDAR_ID_2 = "AAMkADQwN2Y5ZDRhLTkwMzMtNGNjYy04YjgxLTQ5M2MzZTMxNzhmYQBGAAAAAACbtO3M7fyRS5JKFXkg2k1KBwCjKaSUv49eSaBCQ_waeRiXAAAAAAEGAACjKaSUv49eSaBCQ_waeRiXAABtEzueAAA=";


    private String mPreferredName;
    private JsonObject calandar;
    private JsonObject calendar2;


    final private GraphServiceController mGraphServiceController = new GraphServiceController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

//        // Extract the givenName and displayableId and use it in the UI.
//        mGivenName = getIntent().getStringExtra(ARG_GIVEN_NAME);
//        mTitleTextView.append(mGivenName + "!");
//        mEmailEditText.setText(getIntent().getStringExtra(ARG_DISPLAY_ID));
        mPreferredName = getIntent().getStringExtra(ARG_DISPLAY_ID);
    }

    public void onGetCalendarsButtonClick(View v) {
        mGraphServiceController.getCalendarById(CALENDAR_ID_1, new ICallback<IEventCollectionPage>() {
            @Override
            public void success(IEventCollectionPage iEventCollectionPage) {

                List<MeetingData> meetingDataList = new ArrayList<MeetingData>();
                List<Event> eventList = iEventCollectionPage.getCurrentPage();

                for (Event event : eventList) {

                    MeetingData md = new MeetingData();
                    md.setOrganizer(event.organizer.emailAddress.name);

                    md.setStart(event.start.dateTime);
                    md.setEnd(event.end.dateTime);


                    meetingDataList.add(md);
                }


            }

            @Override
            public void failure(ClientException ex) {
                Log.i("SendMailActivity", "Exception on send mail " + ex.getLocalizedMessage());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disconnectMenuItem:
                AuthenticationManager.getInstance().disconnect();
                Intent connectIntent = new Intent(this, ConnectActivity.class);
                startActivity(connectIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
