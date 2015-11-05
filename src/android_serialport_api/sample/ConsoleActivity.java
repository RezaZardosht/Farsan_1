package android_serialport_api.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class ConsoleActivity extends SerialPortActivity {

    EditText mReception;
    TextView CHWOutLetTemp;
    TextView HTWOutLetTemp;
    TextView RefTemp;
    TextView ValvePosition;
    TextView CHWInLetTemp;
    TextView HTWInLetTemp;
    TextView COWOutLetTemp;
    TextView CowInLetTemp;
    TextView DilutionTemp;
    TextView SolutionTemp;
    TextView ExhaustTemp;
    TextView alaki;
    TextView alaki2;
    CheckBox chckboxShowData;

    boolean PostToWebPermit = false;
    public static String SERVERIP = "http://192.69.204.34:8002/Sanchoory/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.console);
        jsonRootObject = new JsonChiller();




        try {
            SetPreferncesStatusValueOnStartUp();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    public void run() {

                        if (!Systemrunningafter5sec) {
                            new HttpAsyncTask().execute(SERVERIP);//hmkcode.appspot.com/jsonservlet
                            Systemrunningafter5sec = true;

                        }
//                        mReception.append(Integer.toString(counter));



/*

                            alaki = (TextView) (findViewById(R.id.Heat));
                            alaki.setText("Heat: " + jsonRootObject.jsonRootObject.getJSONObject("Other").get("Heat").toString());
                            alaki = (TextView) (findViewById(R.id.Temp));
                            alaki.setText("Temp: " + jsonRootObject.jsonRootObject.getJSONObject("Other").get("Temp").toString());
                            alaki = (TextView) (findViewById(R.id.Model));
                            alaki.setText("Model: " + jsonRootObject.jsonRootObject.getJSONObject("Other").get("Model").toString());
                            alaki = (TextView) (findViewById(R.id.Sensor));
                            alaki.setText("Sensor: " + jsonRootObject.jsonRootObject.getJSONObject("Other").get("Sensor").toString());
                            alaki = (TextView) (findViewById(R.id.RefPump));
                            alaki.setText("RefPump: " + jsonRootObject.jsonRootObject.getJSONObject("Other").get("RefPump").toString());
*/
                        // send data to public varrabla in main activity the server read and replay it to local network
                        jsonObjForLocalServer = jsonObj;

/*
                        try {
                            ToMicroPulsSerialPort_mOutputStream.write(new String("P11:23:221394/12/01E").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
*/
                    }
                });
                PostToWebPermit = true;
            }
        }, 5000, 5000);


    }

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                String MyStr = "";
                if (mReception != null) {
                    for (int i = 0; i < size; i++)
                        MyStr = MyStr + String.format(",%02X", (buffer[i] & 0xFF));
                    MyStr = MyStr + "\n";
                    Log.d("ConsoleActivity ", MyStr);

                    //                   mReception.append(MyStr);
                    try {
                        ParsDataValue(buffer, size);
                    } catch (Exception e) {
                        Log.e("Foo failed", e.toString());
                    }
                    connected = true;
//                                      mReception.append(new String(buffer, 0, size));
//                    mReception.append(MyStr);

                }
            }
        });
    }

    private int counter = 0;
    private boolean connected = false;
    private Timer t;
    private boolean Systemrunningafter5sec = false;

    public enum POSTStatus {NoPOst, Waiting, Execute}

    private POSTStatus PostStatus = POSTStatus.NoPOst;
    private JSONObject jsonMainObject;


    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static String POST(String url, JSONObject jsonRootObject) {

        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            int timeoutConnection = 4000;
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 2000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);

            // 2. make POST request to the given URL

            HttpPost httpPost = new HttpPost(url);

            String json = "";

            json = jsonRootObject.toString();
            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
//            httpPost.setHeader("Accept", "application/json");
            //           httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            Log.d("ConsoleActivity", "Sending Pos=================================================> ");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            Log.d("ConsoleActivity", "response<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<= " + result);

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            String ddd = e.getLocalizedMessage();
            Log.d("ConsoleActivity", ddd);
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private String PostRespStr = "";

        @Override
        protected String doInBackground(String... urls) {
            while (Systemrunningafter5sec) {
                connected= true;
                if (connected && PostStatus != POSTStatus.Waiting && PostToWebPermit) {
                    Log.d("ConsoleActivity", ">>>post");
                    PostStatus = POSTStatus.Waiting;
                    try {
                        PostRespStr = POST(urls[0], jsonObj);
                    } catch (Exception e) {
                        String ddd = e.getLocalizedMessage();
                        Log.d("ConsoleActivity", ddd);
                    }
                    PostStatus = POSTStatus.NoPOst;
                    PostToWebPermit = false;
//                    Log.d("ConsoleActivity", "*****************************: " );
                }
            }
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }

    }

    char[] RequestHeader = new char[10];
    char[] RequestBody = new char[100];
    char[] ResponseHeader = new char[10];
    char[] ResponseBody = new char[100];
    int RequestBodyLength = 0;
    int ResponseBodyLength = 0;
    int RequestBodyLengthForSum = 0;
    int ResponseBodyLengthForSum = 0;
    int[] RecivedData = new int[400];
    int ConstReqPos = 0;
    JsonChiller jsonRootObject;
    JSONObject jsonObj = new JSONObject();
    boolean WaitForRequestBody = false;
    boolean WaitForResponseBody = false;
    public static final String SETTINGPREFS = "MainSettingPrefs";

    private void ParsDataValue(byte[] bytes, int bytesLength) {
        int i, j;
        String MyStr, MyStr2;
        MyStr = "1 -->";
//        for (j = 0; j < ConstReqPos; j++)
//            MyStr = MyStr + "," + String.format("%02X", RecivedData[j] & 0xFF);
//        Log.d("BBBBBB==>", MyStr);
        for (i = 0; i < bytesLength; i++) {
            RecivedData[ConstReqPos++] = bytes[i];
            if (ConstReqPos < 7) continue;
            if (ConstReqPos > 60) {
                ConstReqPos = 0;
                continue;
            }
            //Todo check for all Request Type
            if (ConstReqPos < 7) return;

            if (!WaitForRequestBody && RecivedData[ConstReqPos - 7] == 0x02 && RecivedData[ConstReqPos - 6] == 0x7D && RecivedData[ConstReqPos - 5] == 0x65 &&
                    RecivedData[ConstReqPos - 4] == -1 && RecivedData[ConstReqPos - 3] == -1 &&
                    (RecivedData[ConstReqPos - 2] == -111 || RecivedData[ConstReqPos - 2] == -112) && RecivedData[ConstReqPos - 1] == 0x77) {
                for (j = 0; j < 7; j++)
                    RequestHeader[j] = (char) RecivedData[ConstReqPos - (7 - j)];
                RequestBodyLength = 0;
                WaitForRequestBody = true;
                ConstReqPos = 0;
                continue;
            }
            if (WaitForRequestBody && ConstReqPos >= 7) {
                if ((RequestHeader[5] & 0XFF) == 0X91) {
                    RequestBodyLengthForSum = 7;
                    RequestBodyLength = 8;
                }
                if ((RequestHeader[5] & 0XFF) == 0X90) {
                    RequestBodyLengthForSum = 8 + RecivedData[6];
                    RequestBodyLength = 9 + RecivedData[6];
                }

                if (ConstReqPos == RequestBodyLength) {
                    for (j = 0; j < RequestBodyLength; j++) {
                        RequestBody[j] = (char) RecivedData[j];
                    }

                    MyStr2 = "2 -->";
                    for (j = 0; j < RequestBodyLength; j++)
                        MyStr2 = MyStr2 + "," + String.format("%02X", (RequestBody[j] & 0xFF));
                    MyStr2 = MyStr2 + " ,Shck= " + CheckPackageSum(RequestHeader, RequestBody, RequestBodyLengthForSum);
                    Log.d("ConsoleActivity ", MyStr2);
                    Log.d("ConsoleActivity ", MyStr);
                    WaitForRequestBody = false;
                    ConstReqPos = 0;
                    continue;
                }
            }
            if (!WaitForRequestBody && RecivedData[ConstReqPos - 7] == 0x02 && RecivedData[ConstReqPos - 6] == -1 && RecivedData[ConstReqPos - 5] == -1 &&
                    RecivedData[ConstReqPos - 4] == 0x7D && RecivedData[ConstReqPos - 3] == 0x65 && RecivedData[ConstReqPos - 2] == 0x06 &&
                    (RecivedData[ConstReqPos - 1] == -111 || RecivedData[ConstReqPos - 1] == -112)) {
                for (j = 0; j < 7; j++)
                    ResponseHeader[j] = (char) RecivedData[ConstReqPos - (7 - j)];
                ResponseBodyLength = 0;//(RecivedData[6]+ 8);
                WaitForResponseBody = true;
                ConstReqPos = 0;
                continue;
            }
            if (WaitForResponseBody && ConstReqPos >= 7) {
                if ((ResponseHeader[6] & 0XFF) == 0X90) {
                    ResponseBodyLengthForSum = 6;
                    ResponseBodyLength = 7;
                }
                if ((ResponseHeader[6] & 0XFF) == 0X91) {
                    ResponseBodyLengthForSum = 8 + RecivedData[6];
                    ResponseBodyLength = 9 + RecivedData[6];
                }
                if (ConstReqPos == ResponseBodyLength) {
                    for (j = 0; j < ResponseBodyLength; j++)
                        ResponseBody[j] = (char) RecivedData[j];
                    MyStr = "1 -->";
                    for (j = 0; j < ResponseBodyLength; j++)
                        MyStr = MyStr + "," + String.format("%02X", (ResponseBody[j] & 0xFF));
                    boolean CheckPSum = false;
                    CheckPSum = CheckPackageSum(ResponseHeader, ResponseBody, ResponseBodyLengthForSum);
                    MyStr = MyStr + " ,Shck = " + CheckPSum;
                    Log.d("ConsoleActivity", MyStr);
                    if (CheckPSum) {
                        try {
                            CheckResponsePackage();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WaitForResponseBody = false;
                    ConstReqPos = 0;
                    continue;
                }
            }
            if ((WaitForResponseBody && ResponseBodyLength < ConstReqPos) || (WaitForRequestBody && RequestBodyLength < ConstReqPos)) {
                WaitForResponseBody = false;
                WaitForRequestBody = false;
                ConstReqPos = 0;
                continue;

            }

        }
    }

    public void CheckResponsePackage() throws JSONException {
        CheckIsStatusTemp();
        CheckIsSetting();
        CheckIsAlarmD();
        CheckIsOther();
        SetAppInfo();
    }

    public void SetAppInfo() throws JSONException {
        jsonObj.put("Chiller_no", "01");
        jsonObj.put("InOut_Input", "0,0,0,0,0,0,0,0,0,0,0,0");
        jsonObj.put("InOut_Out", "0,0,0,0,0,0,0,0,0,0,0,0");
    }

    public void CheckIsStatusTemp() throws JSONException {

        String MStr = "";
        MStr = MStr + ResponseBody[0];
        MStr = MStr + ResponseBody[1];
        MStr = MStr + ResponseBody[3];
        Log.d("ConsoleActivity", MStr);
        try {
            if (ResponseBody[0] == 2 && ResponseBody[1] == 5 && ResponseBody[3] == 0) {
                float value = (ResponseBody[9] & 0Xff) * 256 + (ResponseBody[8] & 0XFF);
                switch (ResponseBody[2]) {
                    case 0:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("CHWOutLetTemp", String.format("%2.2f", value / 100));
                        break;
                    case 1:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("HTWOutLetTemp", String.format("%2.2f", value / 100));
                         break;
                    case 2:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("RefTemp", String.format("%2.2f", value / 100));
                         break;
                    case 3:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("ValvePosition", String.format("%2.2f", value));
                        break;
                    case 8:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("CHWInLetTemp", String.format("%2.2f", value / 100));
                       break;
                    case 9:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("HTWInLetTemp", String.format("%2.2f", value / 100));
                        break;
                    case 10:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("COWOutLetTemp", String.format("%2.2f", value / 100));
                        break;
                    case 11:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("CowInLetTemp", String.format("%2.2f", value / 100));
                        break;
                    case 13:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("DilutionTemp", String.format("%2.2f", value / 100));
                        break;
                    case 14:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("SolutionTemp", String.format("%2.2f", value / 10));
                        break;
                    case 15:
                        jsonRootObject.jsonRootObject.getJSONObject("Status").put("ExhaustTemp", String.format("%2.2f", value / 10));
                       break;
                }
            }
            String Mstr = "";
            Mstr = jsonRootObject.jsonRootObject.getJSONObject("Status").get("CHWOutLetTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("DilutionTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("RefTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("HTWOutLetTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("ValvePosition").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("CHWInLetTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("HTWInLetTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("CowInLetTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("COWOutLetTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("SolutionTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("ExhaustTemp").toString();
//        Mstr = "1,2,3,4,5,6,7,8,9,10";
            jsonObj.put("Temp", Mstr);
            if (chckboxShowData.isChecked())
                mReception.append("\n-->" + Mstr + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        mReception.append(Integer.toString( counter++));
//        mReception.append(jsonRootObject.jsonRootObject.toString());
//        main.setViewNoStatic(jsonRootObject);
    }

    public void CheckIsSetting() throws JSONException {
        SharedPreferences shared = getSharedPreferences(SETTINGPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor;

        String MStr = "";
        float value = 0;
        value = ((ResponseBody[9] & 0Xff) * 256 + (ResponseBody[8] & 0XFF));
        try {
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X12 && ResponseBody[2] == 0 && ResponseBody[3] == 0X16) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("CoolingTemp", String.format("%2.2f", value / 100));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "CoolingTemp", String.format("%2.2f", value / 100));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X12 && ResponseBody[2] == 1 && ResponseBody[3] == 0X16) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("HeatingTemp", String.format("%2.2f", value / 100));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "HeatingTemp", String.format("%2.2f", value / 100));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 2 && ResponseBody[3] == 0X9) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("RefLowTemp", String.format("%2.2f", value / 100));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "RefLowTemp", String.format("%2.2f", value / 100));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 2 && ResponseBody[3] == 0X11) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", String.format("%2.2f", value / 100));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "FreezingTemp", String.format("%2.2f", value / 100));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 2 && ResponseBody[3] == 0X1A) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("DilutionTemp", String.format("%2.2f", value / 100));
                editor = shared.edit();
                editor.putString(SETTINGPREFS+ "DilutionTemp", String.format("%2.2f", value / 100));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 2 && ResponseBody[3] == 0X1F) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("StrtGuardTime", String.format("%2.2f", value / 100));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "StrtGuardTime", String.format("%2.2f", value / 100));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 0 && ResponseBody[3] == 0X20) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("Spry2PumpTime", String.format("%2.2f", value));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "Spry2PumpTime", String.format("%2.2f", value ));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 0 && ResponseBody[3] == 0X1D) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("Spry3PumpTime", String.format("%2.2f", value));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "Spry3PumpTime", String.format("%2.2f", value ));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 0 && ResponseBody[3] == 0X1E) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("RefPumpGuardTime", String.format("%2.2f", value));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "RefPumpGuardTime", String.format("%2.2f", value ));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 7 && ResponseBody[3] == 0X16) {
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("RefPumpStopTemp", String.format("%2.2f", value / 100));
                editor = shared.edit();
                editor.putString(SETTINGPREFS+ "RefPumpStopTemp", String.format("%2.2f", value / 100));
                editor.commit();
            }
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10 && ResponseBody[2] == 7 && ResponseBody[3] == 0X15) {
                value = ((ResponseBody[9] & 0Xff) * 256 + (ResponseBody[8] & 0XFF)) / 100;
                jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("RefPumpStartTemp", String.format("%2.2f", value / 100));
                editor = shared.edit();
                editor.putString(SETTINGPREFS + "RefPumpStartTemp", String.format("%2.2f", value / 100));
                editor.commit();
            }
            String Mstr = "";
            Mstr = jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("CoolingTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("HeatingTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("RefLowTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("FreezingTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("DilutionTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("StrtGuardTime").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("Spry2PumpTime").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("Spry3PumpTime").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("RefPumpGuardTime").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("RefPumpStopTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("MainSetting").get("RefPumpStartTemp").toString();
            jsonObj.put("MainSetting", Mstr);
            if (chckboxShowData.isChecked())
                mReception.append("\n-->" + Mstr + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CheckIsAlarmD() throws JSONException {

        String MStr = "";
        MStr = MStr + ResponseBody[0];
        MStr = MStr + ResponseBody[1];
        MStr = MStr + ResponseBody[3];
        Log.d("ConsoleActivity", MStr);
        try {
            if (ResponseBody[0] == 1 && ResponseBody[1] == 0XE && ResponseBody[3] == 0) {

                int value = (ResponseBody[8] & 0XF) > 0 ? 1 : 0;
                Log.d("ConsoleActivity ", String.format("%d", value));

                switch (ResponseBody[2]) {
                    case 1:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("RefLowgTemp", String.format("%d", value));
                        break;
                    case 2:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("SolHighTemp", String.format("%d", value));
                        break;
                    case 3:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("ExGasHighTemp", String.format("%d", value));
                        break;
                    case 4:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("HGHighPressure", String.format("%d", value));
                        break;
                    case 5:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("GasPressure", String.format("%d", value));
                        break;
                    case 6:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("HGLowLevel", String.format("%d", value));
                        break;
                    case 7:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("RefPumpOverload", String.format("%d", value));
                        break;
                    case 8:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("FlameFailure", String.format("%d", value));
                        break;
                    case 9:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("CHWFreezing", String.format("%d", value));
                        break;
                    case 10:
                        jsonRootObject.jsonRootObject.getJSONObject("Alarm").put("WaterFlow", String.format("%d", value));
                        break;
                }
            }
            String Mstr = "";
            Mstr = jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("RefLowgTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("SolHighTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("ExGasHighTemp").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("HGHighPressure").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("GasPressure").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("HGLowLevel").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("RefPumpOverload").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("FlameFailure").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("CHWFreezing").toString();
            Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Alarm").get("WaterFlow").toString();
            //        Mstr = "1,2,3,4,5,6,7,8,9,10";
            jsonObj.put("Alarm", Mstr);
            if (chckboxShowData.isChecked())
                mReception.append("\n-->" + Mstr + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CheckIsOther() throws JSONException {

        String MStr = "";
        MStr = MStr + ResponseBody[0];
        MStr = MStr + ResponseBody[1];
        MStr = MStr + ResponseBody[3];
        Log.d("ConsoleActivity", MStr);
        try {
            if (ResponseBody[0] == 2 && ResponseBody[1] == 0X10) {
/*
                if (ResponseBody[2] == 2 && ResponseBody[3] == 0X1B)
                    jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("Fuel", String.format("%d", (ResponseBody[8] == 1) ? 1 : 0));
*/
                if (ResponseBody[2] == 0X2 && ResponseBody[3] == 0X0B)
                    jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("Heat", String.format("%d", (ResponseBody[8] == 1) ? 1 : 0));
                if (ResponseBody[2] == 2 && ResponseBody[3] == 0X14)
                    jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("Temp", String.format("%d", (ResponseBody[8] == 1) ? 1 : 0));
                if (ResponseBody[2] == 2 && ResponseBody[3] == 0X1B)
                    jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("Model", String.format("%d", (ResponseBody[8] == 1) ? 1 : 0));
                if (ResponseBody[2] == 7 && ResponseBody[3] == 0X14)
                    jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("Sensor", String.format("%d", (ResponseBody[8] == 1) ? 1 : 0));
                if (ResponseBody[2] == 2 && ResponseBody[3] == 0X20)
                    jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("RefPump", String.format("%d", (ResponseBody[8] == 1) ? 1 : 0));
                String Mstr = "";
                Mstr = jsonRootObject.jsonRootObject.getJSONObject("Other").get("Fuel").toString();
                Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Other").get("Heat").toString();
                Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Other").get("Temp").toString();
                Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Other").get("Model").toString();
                Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Other").get("Sensor").toString();
                Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Other").get("RefPump").toString();
                jsonObj.put("Other", Mstr);
                if (chckboxShowData.isChecked())
                    mReception.append("\n-->" + Mstr + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        mReception.append(Integer.toString( counter++));
//        mReception.append(jsonRootObject.jsonRootObject.toString());
//        main.setViewNoStatic(jsonRootObject);
    }

    boolean CheckPackageSum(char[] header, char[] body, int Length) {
        if (Length < 7) return false;
        int Sum = 0;
        for (int j = 0; j < 7; j++)
            Sum = (header[j] & 0xFF) + Sum;
        for (int j = 0; j < Length; j++)
            Sum = Sum + (body[j] & 0XFF);
        return (Sum & 0XFF) == (body[Length] & 0XFF);
    }
    private void         SetPreferncesStatusValueOnStartUp() throws JSONException {
        SharedPreferences shared = getSharedPreferences(SETTINGPREFS, MODE_PRIVATE);
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "CoolingTemp", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "HeatingTemp", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "RefLowTemp", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "FreezingTemp", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "DilutionTemp", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "StrtGuardTime", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "Spry2PumpTime", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "Spry3PumpTime", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "RefPumpGuardTime", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "RefPumpStopTemp", "0.0"));
        jsonRootObject.jsonRootObject.getJSONObject("MainSetting").put("FreezingTemp", shared.getString(SETTINGPREFS + "RefPumpStartTemp", "0.0"));



    }

}
