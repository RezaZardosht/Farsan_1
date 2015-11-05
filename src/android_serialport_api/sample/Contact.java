package android_serialport_api.sample;

/**
 * Created by Zardosht on 10/26/2015.
 */
public class Contact {

    //private variables
    int _id;
    String _name;
    String _phone_number;

    // Empty constructor
    public Contact() {

    }

    // constructor
    public Contact(int id, String _name, String _phone_number) {
        this._id = id;
        this._name = _name;
        this._phone_number = _phone_number;

    }

    // constructor
    public Contact(String _name, String _phone_number) {
        this.TimePeriodRead = 111111;
        this.TimeSaveProfile = 22222;

    }

    // constructor
    public Contact(String TimePeriodRead, String TimeSaveProfile, String NameSigProf_1, String NameSigProf_2
            , String NameSigProf_3, String NameSigProf_4, String MaxFellow, String MaxVollume, String MaxPeriod, String Taarefe) {

        try {
            this.TimePeriodRead = Integer.parseInt(TimePeriodRead);
            this.TimeSaveProfile = Integer.parseInt(TimeSaveProfile);
            this.MaxFellow = Integer.parseInt(MaxFellow);
            this.MaxVollume = Integer.parseInt(MaxVollume);
            this.MaxPeriod = Integer.parseInt(MaxPeriod);
            this.Taarefe = Integer.parseInt(Taarefe);
            this.NameSigProf_1 = NameSigProf_1;
            this.NameSigProf_2 = NameSigProf_2;
            this.NameSigProf_3 = NameSigProf_3;
            this.NameSigProf_4 = NameSigProf_4;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // constructor
    public Contact(int TimePeriodRead, int TimeSaveProfile, int MaxFellow, int MaxVollume, int MaxPeriod, int Taarefe,
                   String NameSigProf_1, String NameSigProf_2, String NameSigProf_3, String NameSigProf_4) {
        this.TimePeriodRead = TimePeriodRead;
        this.TimeSaveProfile = TimeSaveProfile;
        this.MaxFellow = MaxFellow;
        this.MaxVollume = MaxVollume;
        this.MaxPeriod = MaxPeriod;
        this.Taarefe = Taarefe;
        this.NameSigProf_1 = NameSigProf_1;
        this.NameSigProf_2 = NameSigProf_2;
        this.NameSigProf_3 = NameSigProf_3;
        this.NameSigProf_4 = NameSigProf_4;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }


    private int TimePeriodRead = 0, TimeSaveProfile = 0, MaxFellow = 0, MaxVollume = 0, MaxPeriod = 0, Taarefe = 0;
    private String NameSigProf_1 = "", NameSigProf_2 = "", NameSigProf_3 = "", NameSigProf_4 = "";

    public int getTimePeriodRead() {
        return this.TimePeriodRead;
    }

    public void setTimePeriodRead(int timePeriodRead) {
        this.TimePeriodRead = timePeriodRead;
    }

    public int getTimeSaveProfile() {
        return this.TimeSaveProfile;
    }

    public void setTimeSaveProfile(int timeSaveProfile) {
        this.TimeSaveProfile = timeSaveProfile;
    }

    public int getMaxFellow() {
        return this.MaxFellow;
    }

    public void setMaxFellow(int maxFellow) {
        this.MaxFellow = maxFellow;
    }

    public int getMaxVollume() {
        return this.MaxVollume;
    }

    public void setMaxVollume(int maxVollume) {
        this.MaxVollume = maxVollume;
    }

    public int getMaxPeriod() {
        return this.MaxPeriod;
    }

    public void setMaxPeriod(int maxPeriod) {
        this.MaxPeriod = maxPeriod;
    }

    public int getTaarefe() {
        return this.Taarefe;
    }

    public void setTaarefe(int taarefe) {
        this.Taarefe = taarefe;
    }

    public String getNameSigProf_1() {
        return this.NameSigProf_1;
    }

    public void setNameSigProf_1(String nameSigProf_1) {
        this.NameSigProf_1 = nameSigProf_1;
    }

    public String getNameSigProf_2() {
        return this.NameSigProf_2;
    }

    public void setNameSigProf_2(String nameSigProf_2) {
        this.NameSigProf_2 = nameSigProf_2;
    }

    public String getNameSigProf_3() {
        return this.NameSigProf_3;
    }

    public void setNameSigProf_3(String nameSigProf_3) {
        this.NameSigProf_3 = nameSigProf_3;
    }

    public String getNameSigProf_4() {
        return this.NameSigProf_4;
    }

    public void setNameSigProf_4(String NameSigProf_4) {
        this.NameSigProf_4 = NameSigProf_4;
    }

}


