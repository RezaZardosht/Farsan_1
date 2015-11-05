package android_serialport_api.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Zardosht on 10/26/2015.
 */
public class Parameters extends Activity {

    // todo active show display
    // todo load values from database
    // todo show values
    // todo save values
    // todo send parameters to local variables
    EditText ETxt_getTimePeriod;
    EditText ETxt_getTimeSave;
    EditText ETxt_NameSigProf_1;
    EditText ETxt_NameSigProf_2;
    EditText ETxt_NameSigProf_3;
    EditText ETxt_NameSigProf_4;
    EditText ETxt_MaxFellow;
    EditText ETxt_MaxVollume;
    EditText ETxt_MaxPeriod;
    EditText ETxt_Taarefe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameters);
        Button Btn_Save = (Button) findViewById(R.id.Btn_Save);
        Btn_Save.setOnClickListener(myhandler1);

        ETxt_getTimePeriod = (EditText) findViewById(R.id.ETxt_getTimePeriod);
        ETxt_getTimeSave = (EditText) findViewById(R.id.ETxt_getTimeSave);
        ETxt_NameSigProf_1 = (EditText) findViewById(R.id.ETxt_NameSigProf_1);
        ETxt_NameSigProf_2 = (EditText) findViewById(R.id.ETxt_NameSigProf_2);
        ETxt_NameSigProf_3 = (EditText) findViewById(R.id.ETxt_NameSigProf_3);
        ETxt_NameSigProf_4 = (EditText) findViewById(R.id.ETxt_NameSigProf_4);
        ETxt_MaxFellow = (EditText) findViewById(R.id.ETxt_MaxFellow);
        ETxt_MaxVollume = (EditText) findViewById(R.id.ETxt_MaxVollume);
        ETxt_MaxPeriod = (EditText) findViewById(R.id.ETxt_MaxPeriod);
        ETxt_Taarefe = (EditText) findViewById(R.id.ETxt_Taarefe);


        DatabaseHandler db = new DatabaseHandler(this);
        db.onCreate(db.getWritableDatabase());

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            ETxt_getTimePeriod.setText(String.valueOf(cn.getTimePeriodRead()));
            ETxt_getTimeSave.setText(String.valueOf(cn.getTimeSaveProfile()));
            ETxt_NameSigProf_1.setText(cn.getNameSigProf_1());
            ETxt_NameSigProf_2.setText(cn.getNameSigProf_2());
            ETxt_NameSigProf_3.setText(cn.getNameSigProf_3());
            ETxt_NameSigProf_4.setText(cn.getNameSigProf_4());
            ETxt_MaxFellow.setText(String.valueOf(cn.getMaxFellow()));
            ETxt_MaxVollume.setText(String.valueOf(cn.getMaxVollume()));
            ETxt_MaxPeriod.setText(String.valueOf(cn.getMaxPeriod()));
            ETxt_Taarefe.setText(String.valueOf(cn.getTaarefe()));
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getTimePeriodRead() + " ,Phone: " + cn.getTimeSaveProfile() +
                    " ," + cn.getTimePeriodRead() +
                    " ," + cn.getTimeSaveProfile() +
                    " ," + cn.getNameSigProf_1() +
                    " ," + cn.getNameSigProf_2() +
                    " ," + cn.getNameSigProf_3() +
                    " ," + cn.getNameSigProf_4() +
                    " ," + String.valueOf(cn.getMaxFellow()) +
                    " ," + String.valueOf(cn.getMaxVollume()) +
                    " ," + String.valueOf(cn.getMaxPeriod()) +
                    " ," + String.valueOf(cn.getTaarefe()) +
                    " ," + ETxt_getTimePeriod.getText().toString() +
                    " ," + ETxt_getTimeSave.getText().toString() +
                    " ," + ETxt_NameSigProf_1.getText().toString() +
                    " ," + ETxt_NameSigProf_2.getText().toString() +
                    " ," + ETxt_NameSigProf_3.getText().toString() +
                    " ," + ETxt_NameSigProf_4.getText().toString() +
                    " ," + ETxt_MaxFellow.getText().toString() +
                    " ," + ETxt_MaxVollume.getText().toString() +
                    " ," + ETxt_MaxPeriod.getText().toString() +
                    " ," + ETxt_Taarefe.getText().toString();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        db.close();
    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(Parameters.this, "Button Clicked", Toast.LENGTH_SHORT).show();

            String temp1 = ETxt_getTimePeriod.getText().toString() ;
            String temp2 = ETxt_getTimeSave.getText().toString() ;
                    String temp3 = ETxt_NameSigProf_1.getText().toString() ;
                    String temp4 = ETxt_NameSigProf_2.getText().toString() ;
                    String temp5 = ETxt_NameSigProf_3.getText().toString() ;
                    String temp6 = ETxt_NameSigProf_4.getText().toString() ;
                    String temp7 = ETxt_MaxFellow.getText().toString() ;
                    String temp8 = ETxt_MaxVollume.getText().toString() ;
                    String temp9 = ETxt_MaxPeriod.getText().toString() ;
                    String temp10 = ETxt_Taarefe.getText().toString();
          //  Log.d("CheckSave: ", log);
            DatabaseHandler db = new DatabaseHandler(Parameters.this);
            db.onCreate(db.getWritableDatabase());


            // it was the 1st button
            /**
             * CRUD Operations
             * */
            // Inserting Contacts
            Log.d("Insert: ", "Inserting ..");
            db.addContact(new Contact(temp1, temp2,temp3,temp4,temp5,temp6,temp7,temp8,temp9,temp10));
        }
    };
}
