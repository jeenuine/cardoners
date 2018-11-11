package org.androidtown.probono;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Request extends AppCompatActivity {
    private MqttAndroidClient client;
    private PahoMqttClient pahoMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);




        pahoMqttClient = MainActivity.pahoMqttClient;
        pahoMqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) {
                String tmp = new String(mqttMessage.getPayload());
                Log.e("MQTT","MSG2:"+tmp+"  s : "+s  );
                String[] splits =tmp.split(",");
                if(splits[0].equals(MainActivity.myNumber)) {

                    if (splits[1].equals("2")) {
                        Intent intent = new Intent(getApplicationContext(), Driverinfo.class);
                        intent.putExtra("str", tmp);
                        startActivity(intent);
                    }
                    if (splits[1].equals("3")) {

                        Intent intent = new Intent(getApplicationContext(), Finish.class);
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

}


