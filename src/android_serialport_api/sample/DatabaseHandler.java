package android_serialport_api.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zardosht on 10/26/2015.
 * http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
//    private static final String DATABASE_NAME = "contactsManager";
    private static final String DATABASE_NAME = "Farasn";

    // Contacts table name
    private static final String TABLE_CONTACTS = "Parameters_1";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String TIMEPERIODREAD ="TimePeriodRead";
    private static final String TIMESAVEPROFILE ="TimeSaveProfile";
    private static final String MAXFELLOW = "MaxFellow";
    private static final String MAXVOLLUM="MaxVollume";
    private static final String MAXPERIOD = "MaxPeriod";
    private static final String TAAREFE = "Taarefe";
    private static final String NAMESIGPROF_1 = "NameSigProf_1";
    private static final String NAMESIGPROF_2 = "NameSigProf_2";
    private static final String NAMESIGPROF_3 = "NameSigProf_3";
    private static final String NAMESIGPROF_4 = "NameSigProf_4";






    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Create: ", "Create Table");

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + TIMEPERIODREAD + " INTEGER,"
                + TIMESAVEPROFILE + " INTEGER," + MAXFELLOW + " INTEGER,"
                + MAXVOLLUM + " INTEGER,"+ MAXPERIOD + " INTEGER,"
                + TAAREFE + " INTEGER,"
                + NAMESIGPROF_1 + " TEXT," + NAMESIGPROF_2 + " TEXT,"
                + NAMESIGPROF_3 + " TEXT," + NAMESIGPROF_4 + " TEXT"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    public void NewCreate(SQLiteDatabase db) {
        Log.d("Create: ", "Create Table");

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + TIMEPERIODREAD + " INTEGER,"
                + TIMESAVEPROFILE + " INTEGER," + MAXFELLOW + " INTEGER,"
                + MAXVOLLUM + " INTEGER,"+ MAXPERIOD + " INTEGER,"
                + TAAREFE + " INTEGER,"
                + NAMESIGPROF_1 + " TEXT," + NAMESIGPROF_2 + " TEXT,"
                + NAMESIGPROF_3 + " TEXT," + NAMESIGPROF_4 + " TEXT"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
  //      values.put(KEY_NAME, contact.getName()); // Contact Name
  //      values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
        values.put(TIMEPERIODREAD, contact.getTimePeriodRead());
        values.put(TIMESAVEPROFILE, contact.getTimeSaveProfile());
        values.put(MAXFELLOW, contact.getMaxFellow());
        values.put(MAXVOLLUM, contact.getMaxVollume());
        values.put(MAXPERIOD, contact.getMaxPeriod());
        values.put(TAAREFE, contact.getTaarefe());
        values.put(NAMESIGPROF_1, contact.getNameSigProf_1());
        values.put(NAMESIGPROF_2, contact.getNameSigProf_2());
        values.put(NAMESIGPROF_3, contact.getNameSigProf_3());
        values.put(NAMESIGPROF_4, contact.getNameSigProf_4());

        
        
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_ID , KEY_NAME, KEY_PH_NO,TIMEPERIODREAD,TIMESAVEPROFILE, MAXFELLOW, MAXVOLLUM, MAXPERIOD,TAAREFE,NAMESIGPROF_1, NAMESIGPROF_2, NAMESIGPROF_3, NAMESIGPROF_4
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setTimePeriodRead(Integer.parseInt(cursor.getString(1)));
                contact.setTimeSaveProfile(Integer.parseInt(cursor.getString(2)));
                contact.setNameSigProf_1(cursor.getString(3));
                contact.setNameSigProf_2(cursor.getString(4));
                contact.setNameSigProf_3(cursor.getString(5));
                contact.setNameSigProf_4(cursor.getString(6));
      /*          contact.setMaxFellow(Integer.parseInt(cursor.getString(7)));
                contact.setMaxVollume(Integer.parseInt(cursor.getString(8)));
                contact.setMaxPeriod(Integer.parseInt(cursor.getString(9)));
                contact.setTaarefe(Integer.parseInt(cursor.getString(10)));
*/
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
 //       values.put(KEY_NAME, contact.getName());
 //       values.put(KEY_PH_NO, contact.getPhoneNumber());
        values.put(TIMEPERIODREAD, contact.getTimePeriodRead());
        values.put(TIMESAVEPROFILE, contact.getTimeSaveProfile());
        values.put(MAXFELLOW, contact.getMaxFellow());
        values.put(MAXVOLLUM, contact.getMaxVollume());
        values.put(MAXPERIOD, contact.getMaxPeriod());
        values.put(TAAREFE, contact.getTaarefe());
        values.put(NAMESIGPROF_1, contact.getNameSigProf_1());
        values.put(NAMESIGPROF_2, contact.getNameSigProf_2());
        values.put(NAMESIGPROF_3, contact.getNameSigProf_3());
        values.put(NAMESIGPROF_4, contact.getNameSigProf_4());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


}