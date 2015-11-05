package android_serialport_api.sample;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zardosht on 06/17/2015.
 */

public class JsonChiller {
    String strJson = "{\"Status\": {" +
            "\"CHWOutLetTemp\": \"0.0\"," +
            "\"HTWOutLetTemp\": \"0.0\"," +
            "\"RefTemp\": \"0.0\"," +
            "\"ValvePosition\": \"0.0\"," +
            "\"CHWInLetTemp\": \"0.0\"," +
            "\"HTWInLetTemp\": \"0.0\"," +
            "\"COWOutLetTemp\": \"0.0\"," +
            "\"CowInLetTemp\": \"0.0\"," +
            "\"DilutionTemp\": \"0.0\"," +
            "\"SolutionTemp\": \"0.0\"," +
            "\"ExhaustTemp\": \"0.0\"" +
            "  }," +
            "\"MainSetting\":{" +
            "\"CoolingTemp\": \"0.0\"," +
            "\"HeatingTemp\": \"0.0\"," +
            "\"RefLowTemp\": \"0.0\"," +
            "\"FreezingTemp\": \"0.0\"," +
            "\"DilutionTemp\": \"0.0\"," +
            "\"DilutionTemp\": \"0.0\"," +
            "\"StrtGuardTime\": \"0.0\"," +
            "\"Spry2PumpTime\": \"0.0\"," +
            "\"Spry3PumpTime\": \"0.0\"," +
            "\"RefPumpGuardTime\": \"0.0\"," +
            "\"RefPumpStopTemp\": \"0.0\"," +
            "\"RefPumpStartTemp\": \"0.0\"," +
            "\"RefPumpStopTemp1\": \"0.0\"," +
            "\"RefPumpStartTemp1\": \"0.0\"" +
            "  }," +
            "\"InpuOutput_in\":{" +
            "\"RefLowgTemp\": \"0\"," +
            "\"SolHighTemp\": \"0\"," +
            "\"ExGasHighTemp\": \"0\"," +
            "\"HGHighPressure\": \"0\"," +
            "\"GasPressure\": \"0\"," +
            "\"HGLowLevel\": \"0\"," +
            "\"RefPumpOverload\": \"0\"," +
            "\"FlameFailure\": \"0\"," +
            "\"CHWFreezing\": \"0\"," +
            "\"WaterFlow\": \"0\"" +
            "  }," +
            "\"Alarm\":{" +
            "\"RefLowgTemp\": \"0\"," +
            "\"SolHighTemp\": \"0\"," +
            "\"ExGasHighTemp\": \"0\"," +
            "\"HGHighPressure\": \"0\"," +
            "\"GasPressure\": \"0\"," +
            "\"HGLowLevel\": \"0\"," +
            "\"RefPumpOverload\": \"0\"," +
            "\"FlameFailure\": \"0\"," +
            "\"CHWFreezing\": \"0\"," +
            "\"WaterFlow\": \"0\"" +
            "  }," +
            "\"Other\":{" +
            "\"Fuel\": \"0\"," +
            "\"Heat\": \"0\"," +
            "\"Temp\": \"0\"," +
            "\"Model\": \"0\"," +
            "\"Sensor\": \"0\"," +
            "\"RefPump\": \"0\"," +
            "\"RefPump1\": \"0\"" +

             "}}";//RefPumpGuardTime
    public JSONObject jsonRootObject;
    public JsonChiller(){
        try {
            jsonRootObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
