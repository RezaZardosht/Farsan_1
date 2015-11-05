package android_serialport_api.sample;

/**
 * Created by Zardosht on 07/04/2015.
 */
public class alaki {
}
/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
package android_serialport_api.sample;

        import android.app.Activity;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.TextView.OnEditorActionListener;
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

    public static String SERVERIP = "http://192.69.204.34:8001/Sanchoory/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.console);
        jsonRootObject = new JsonChiller();


        //		setTitle("Loopback test");
        mReception = (EditText) findViewById(R.id.EditTextReception);
        EditText Emission = (EditText) findViewById(R.id.EditTextEmission);
        Emission.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int i;
                CharSequence t = v.getText();
                char[] text = new char[t.length()];
                for (i = 0; i < t.length(); i++) {
                    text[i] = t.charAt(i);
                }
//            	try {
//                    mOutputStream.write(new String(text).getBytes());
//					mOutputStream.write('\n');
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
                return false;
            }
        });
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
                        mReception.append(Integer.toString(counter));

                    }
                });
            }
        }, 5000, 2000);


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
                    mReception.append(MyStr);
                    ParsDataValue(buffer, size);
                    connected = true;
                    //                   mReception.append(new String(buffer, 0, size));
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
            int timeoutConnection = 3000;
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
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

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
            Log.d("ConsoleActivity", "timeoooooooooooooooooooooooooooooooooooooooooot");// e.getLocalizedMessage()
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
                if (connected && PostStatus != POSTStatus.Waiting) {
                    PostStatus = POSTStatus.Waiting;
                    PostRespStr = POST(urls[0], jsonObj);
                    PostStatus = POSTStatus.NoPOst;
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

    private void ParsDataValue(byte[] bytes, int bytesLength) {
        int i, j;
        String MyStr, MyStr2;
        for (i = 0; i < bytesLength; i++) {
            RecivedData[ConstReqPos++] = bytes[i];
            if (ConstReqPos < 7) continue;
            if (ConstReqPos > 60) {
                ConstReqPos = 0;
                continue;
            }
            //Todo check for all Request Type
            if (RecivedData[ConstReqPos - 7] == 0x02 && RecivedData[ConstReqPos - 6] == 0x7D && RecivedData[ConstReqPos - 5] == 0x65 &&
                    RecivedData[ConstReqPos - 4] == -1 && RecivedData[ConstReqPos - 3] == -1 &&
                    (RecivedData[ConstReqPos - 2] == -111 || RecivedData[ConstReqPos - 2] == -112) && RecivedData[ConstReqPos - 1] == 0x77) {
                for (j = 0; j < 7; j++)
                    RequestHeader[j] = (char) RecivedData[ConstReqPos - (7 - j)];
                ResponseBodyLength = 0;//(RecivedData[6]+ 8);
                if ((RecivedData[ConstReqPos - 2] & 0XFF) == 0X90) {
                    ResponseBodyLengthForSum = 6;
                    ResponseBodyLength = 7;
                }
                if ((RecivedData[ConstReqPos - 2] & 0XFF) == 0X91) {
                    ResponseBodyLengthForSum = 8 + RecivedData[6];
                    ResponseBodyLength = 9 + RecivedData[6];
                }
                for (j = 0; j < ResponseBodyLength; j++)
                    ResponseBody[j] = (char) RecivedData[j];
                MyStr = "1 -->";
                for (j = 0; j < ResponseBodyLength; j++)
                    MyStr = MyStr + "," + String.format("%02X", (ResponseBody[j] & 0xFF));
                boolean CheckPSum = false;
                CheckPSum = CheckPackageSum(ResponseHeader, ResponseBody, ResponseBodyLengthForSum);
                MyStr = MyStr + " ,Shck = " + CheckPSum;
                if (CheckPSum) {
                    try {
                        CheckResponsePackage();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("DataEncript ", MyStr);
                ConstReqPos = 0;

            } else if (RecivedData[ConstReqPos - 7] == 0x02 && RecivedData[ConstReqPos - 6] == -1 && RecivedData[ConstReqPos - 5] == -1 &&
                    RecivedData[ConstReqPos - 4] == 0x7D && RecivedData[ConstReqPos - 3] == 0x65 && RecivedData[ConstReqPos - 2] == 0x06 &&
                    (RecivedData[ConstReqPos - 1] == -111 || RecivedData[ConstReqPos - 1] == -112)) {
                for (j = 0; j < 7; j++)
                    ResponseHeader[j] = (char) RecivedData[ConstReqPos - (7 - j)];
                RequestBodyLength = 0;
                if ((RecivedData[ConstReqPos - 1] & 0XFF) == 0X91) {
                    RequestBodyLengthForSum = 7;
                    RequestBodyLength = 8;
                }
                if ((RecivedData[ConstReqPos - 1] & 0XFF) == 0X90) {
                    RequestBodyLengthForSum = 8 + RecivedData[6];
                    RequestBodyLength = 9 + RecivedData[6];
                }
                for (j = 0; j < RequestBodyLength; j++) {
                    RequestBody[j] = (char) RecivedData[j];
                }

                MyStr2 = "2 -->";
                for (j = 0; j < ConstReqPos - 7; j++)
                    MyStr2 = MyStr2 + "," + String.format("%02X", (RequestBody[j] & 0xFF));
                MyStr2 = MyStr2 + " ,Shck= " + CheckPackageSum(RequestHeader, RequestBody, RequestBodyLengthForSum);
                Log.d("DataEncript ", MyStr2);
                ConstReqPos = 0;

            }

        }
    }

    public void CheckResponsePackage() throws JSONException {
        //Todo check check sum
        counter++;
        //       mReception.append(Integer.toString( counter++)+'\n');

        String MStr = "";
        MStr = MStr + ResponseBody[0];
        MStr = MStr + ResponseBody[1];
        MStr = MStr + ResponseBody[3];
        Log.d("DataEncript ", MStr);
        if (ResponseBody[0] == 2 && ResponseBody[1] == 5 && ResponseBody[3] == 0) {
            float value = (ResponseBody[8] & 0Xff) * 256 + (ResponseBody[9] & 0XFF);
            Log.d("DataEncript ", String.format("%2.2f", value / 100));
            switch (ResponseBody[2]) {
                case 0:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("CHWOutLetTemp", String.format("%2.2f", value / 256));
                    break;
                case 1:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("HTWOutLetTemp", String.format("%2.2f", value / 256));
                    break;
                case 2:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("RefTemp", String.format("%2.2f", value / 256));
                    break;
                case 3:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("ValvePosition", String.format("%2.2f", value));
                    break;
                case 8:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("CHWInLetTemp", String.format("%2.2f", value));
                    break;
                case 9:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("HTWInLetTemp", String.format("%2.2f", value));
                    break;
                case 10:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("COWOutLetTemp", String.format("%2.2f", value));
                    break;
                case 11:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("CowInLetTemp", String.format("%2.2f", value));
                    break;
                case 13:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("DilutionTemp", String.format("%2.2f", value));
                    break;
                case 14:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("SolutionTemp", String.format("%2.2f", value));
                    break;
                case 15:
                    jsonRootObject.jsonRootObject.getJSONObject("Status").put("ExhaustTemp", String.format("%2.2f", value));
                    break;
            }
        }
        String Mstr = "";
        Mstr = jsonRootObject.jsonRootObject.getJSONObject("Status").get("CHWOutLetTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("HTWOutLetTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("RefTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("ValvePosition").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("CHWInLetTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("HTWInLetTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("COWOutLetTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("CowInLetTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("DilutionTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("SolutionTemp").toString();
        Mstr = Mstr + "," + jsonRootObject.jsonRootObject.getJSONObject("Status").get("ExhaustTemp").toString();
        jsonObj.put("RowData", Mstr);
        mReception.append("\n-->" + Mstr + "\n");
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
}*/
