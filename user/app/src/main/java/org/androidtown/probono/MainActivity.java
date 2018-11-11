package org.androidtown.probono;

import android.Manifest;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapLabelInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TMapGpsManager.onLocationChangedCallback {
    private static final String TAG = "MainActivity";
    private final String TMAP_API_KEY = "2061bf08-f7fe-4b20-97fc-807bb7eaaad7";
    TMapView tmap;
    boolean trackingmode = true;
    TMapPoint point;
    TMapGpsManager gps;
    private TMapData tmapdata = new TMapData();
    TextView txt1;

    boolean ea = false;
    private String now;

    public static PahoMqttClient pahoMqttClient;
    public static String myNumber;

    private String host="211.254.212.221";
    private String topic="asd";

    double distance = 83;
    String dest = "tmp";


    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yy/MM/dd \n a hh:mm");


    private  DBHelper dbHelper;

    private historyResource global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(
                this,
                "login",
                null,1
        );
        final Person UserData =  dbHelper.getPersonData();

        myNumber = UserData.getPhone();



        global=(historyResource)getApplicationContext();

        Intent refintent = this.getIntent();
        final String kind = refintent.getStringExtra("kind");

        pahoMqttClient = new PahoMqttClient(getApplicationContext(),host, topic,1,"gggpps",true,null);



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            return;
        }

        Button btn1 = (Button) findViewById(R.id.button1);
        Button  btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);
        txt1 = (TextView) findViewById(R.id.Text1);



        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tmap = new TMapView(this);
        tmap.setSKTMapApiKey(TMAP_API_KEY);
        linearLayoutTmap.addView(tmap);

        tmap.setIconVisibility(true);

        gps = new TMapGpsManager(this);
        gps.setMinTime(1);
        gps.setMinDistance(1);
        gps.setProvider(gps.NETWORK_PROVIDER);
        gps.OpenGps();
        tmap.setTrackingMode(true);

        final CustomAnimationDialog customAnimationDialog;

                customAnimationDialog = new CustomAnimationDialog(MainActivity.this);
                customAnimationDialog.show();

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
//                                Toast.makeText(MainActivity.this, "lat :"+ gps.getLocation().getLatitude()+"long : "+ gps.getLocation().getLongitude(), Toast.LENGTH_SHORT).show();

                customAnimationDialog.dismiss();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (gps.getLocation().getLatitude() != 0.0) break;
                }
                handler.sendEmptyMessage(0);
            }
        }).start();

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("통합검색");

                final EditText input = new EditText(MainActivity.this);
                builder.setView(input);

                builder.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strData = input.getText().toString();
                        tmap.removeAllMarkerItem();
                        tmap.removeAllTMapPolyLine();
                        tmapdata.findAroundKeywordPOI(gps.getLocation(), strData, 1000, 5, new TMapData.FindAroundKeywordPOIListenerCallback() {
                            @Override
                            public void onFindAroundKeywordPOI(ArrayList<TMapPOIItem> poiItem) {
                                if (poiItem.size() > 0) {
                                    ea = true;
                                    for (int i = 0; i < poiItem.size(); i++) {

                                        TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                                        double lat = Double.parseDouble(String.valueOf(item.noorLat));
                                        double lon = Double.parseDouble(String.valueOf(item.noorLon));

                                        tmap.setCenterPoint(lon,lat);

                                        TMapPoint tpoint = new TMapPoint(lat, lon);

                                        TMapMarkerItem tItem = new TMapMarkerItem();
                                        tItem.setCalloutRightButtonImage(bitmap);
                                        tItem.setTMapPoint(tpoint);
                                        tItem.setName(item.getPOIName());
                                        tItem.setVisible(TMapMarkerItem.VISIBLE);
                                        Bitmap src = null;
                                        try {
                                            String URL = "http://www.example.com/image.jpg";
                                            InputStream in = new URL(URL).openStream();
                                            src = BitmapFactory.decodeStream(in);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        tItem.setIcon(src);

                                        tItem.setCanShowCallout(true);
                                        tItem.setCalloutTitle(item.getPOIName().toString());
                                        tmap.addMarkerItem("test" + i, tItem);

                                        Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                                "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                                "Point: " + item.getPOIPoint().toString() + " gps " + String.valueOf(item.frontLat) + " gps2 " + String.valueOf(item.noorLat));
                                    }
                                } else
                                    ea = false;
                            }


                        });

                    }
                }).setNegativeButton("취소", null);
                builder.show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmap.removeAllMarkerItem();
                tmap.removeAllTMapPolyLine();
                ea=false;
                txt1.setText(null);
                dest = "tmp";
                tmap.setCenterPoint(gps.getLocation().getLongitude(),gps.getLocation().getLatitude(),true);
                tmap.setZoomLevel(15);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmapdata.convertGpsToAddress(gps.getLocation().getLatitude(), gps.getLocation().getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String s) {
                        String time = getTime();
                        now = s;
                        Log.d("HHelp", "now==onConvertToGPSToAddress: success //  s="+s+"now ="+now);

                        String msg = myNumber+",1,"+now+","+dest+","+UserData.getName()+","+kind+","+distance+"km"+","+time;
                        if (dest != "tmp") {
                            try {
                                pahoMqttClient.publishMessage(msg, 1, topic);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            global.setDistance(String.valueOf(distance));
                            global.setNow(now);
                            global.setTime(time);
                            global.setDest(dest);
                            Intent intent = new Intent(MainActivity.this,Request.class);
                            intent.setFlags( Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }
                        else {
                            new Thread() {
                                public void run(){

                                    Log.d("HHelp", "목적지가 없습니다");
                                }
                            }.start();

                        }
                    }
                });

            }
        });


        tmap.setOnClickReverseLabelListener(new TMapView.OnClickReverseLabelListenerCallback() {
            @Override
            public void onClickReverseLabelEvent(TMapLabelInfo tMapLabelInfo) {
                if (!ea) {
                    // TODO Auto-generated method stub
                    if (tMapLabelInfo != null) {
                        double lat = Double.valueOf(tMapLabelInfo.labelLat);
                        double lon = Double.valueOf(tMapLabelInfo.labelLon);
                        TMapMarkerItem marker1 = new TMapMarkerItem();
                        marker1.setName(tMapLabelInfo.labelName);
                        marker1.setVisible(TMapMarkerItem.VISIBLE);
                        marker1.setCalloutRightButtonImage(bitmap);
                        marker1.setCanShowCallout(true);
                        marker1.setCalloutTitle(tMapLabelInfo.labelName.toString());
                        marker1.setID("reverseLabelID");
                        marker1.setTMapPoint(new TMapPoint(lat, lon));
                        tmap.addMarkerItem("reverseLabelID", marker1);
                    }
                }
            }
        });

        tmap.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {

            @Override
            public void onCalloutRightButton(final TMapMarkerItem tMapMarkerItem) {
                txt1.setText(tMapMarkerItem.getCalloutTitle());
                dest=(tMapMarkerItem.getCalloutTitle());
                    tmapdata.convertGpsToAddress(tMapMarkerItem.latitude, tMapMarkerItem.longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
                        @Override
                        public void onConvertToGPSToAddress(String s) {
                            Log.d("HHelp", "dest==onConvertToGPSToAddress: success///s = "+s+"  dest = "+ dest);
                            dest = s;
                            txt1.setText(dest);
                        }
                    });


                new Thread() {
                    public void run(){

                        TMapPoint tMapPointStart = new TMapPoint(gps.getLocation().getLatitude(), gps.getLocation().getLongitude());
                        TMapPoint tMapPointEnd = new TMapPoint(tMapMarkerItem.latitude, tMapMarkerItem.longitude);

                        try {
                            TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineWidth(2);
                            tmap.addTMapPolyLine("Line1", tMapPolyLine);
                            distance = tMapPolyLine.getDistance()/1000.0;
                            distance = Math.round(distance*100)/100.0 ;
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userName);
        navUsername.setText(UserData.getName()+" 님");


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager manager = getFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_request) {

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this,History.class);
            startActivity(intent);
        } else if (id == R.id.nav_information) {
            Intent intent = new Intent(MainActivity.this,Information.class);
            startActivity(intent);
        } else if (id == R.id.nav_inquire) {
            Intent intent = new Intent(MainActivity.this,MainInqireActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );



        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChange(Location location) {
        if (trackingmode) {
            tmap.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }


    @Override
    protected void onDestroy() {
        try {
            pahoMqttClient.disconnect();
            Log.d(TAG, "onDestroysuccess: ");
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}

