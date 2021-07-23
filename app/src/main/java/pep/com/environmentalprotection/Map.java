package pep.com.environmentalprotection;

import android.*;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    String coordinates = "";
    String rname, raddress;
    Double coor1, coor2 = 0.0;
    Double mycoor1,mycoor2=0.0;
    LocationManager locationManager;
    LocationListener locationListener;

    EditText etLocation,etName,etAddress,etDescription;

    Button show,back,your;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            Toast.makeText(this, "Loading Complete", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_map);

            Bundle bn = getIntent().getExtras();
            if (bn != null) {
                rname = bn.getString("name");
                raddress = bn.getString("address");
                coordinates = bn.getString("coordinates");
                String[] arr = coordinates.split(",");
                coor1 = Double.parseDouble(arr[0]);
                coor2 = Double.parseDouble(arr[1]);

            }
            initMap();

            show= (Button) findViewById(R.id.btnShow);
            back= (Button) findViewById(R.id.btnSighting);
            your= (Button) findViewById(R.id.btnYour);
            get_location();
            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DistanceTo();

                }
            });


            your.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rname="Your Location";
                    raddress=coor1 + ","+ coor2;
                    gotolocationZoom(mycoor1,mycoor2,20);
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rname = getIntent().getExtras().getString("name");
                    raddress = getIntent().getExtras().getString("address");
                    gotolocationZoom(coor1,coor2,20);

                }
            });


        }
    }

    private void get_location() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

               //Toast.makeText(Map.this,location.getLatitude() + "," + location.getLongitude(),Toast.LENGTH_LONG).show();
                mycoor1=location.getLatitude();
                mycoor2=location.getLongitude();


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent in=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(in);

            }


        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

    }
    private void initMap() {
        MapFragment mfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mfragment.getMapAsync(this);
    }
    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannot connect to play services", Toast.LENGTH_SHORT).show();

        }
        return false;
    }
    private static final int earthRadius = 6371;
    protected void DistanceTo()
    {



            float dLat = (float) Math.toRadians(coor1 - mycoor1);
            float dLon = (float) Math.toRadians(coor2 - mycoor2);
            float a =
                    (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(coor1))
                            * Math.cos(Math.toRadians(mycoor1)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
            float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
            float d = earthRadius * c;


        Toast.makeText(Map.this,d + " KM away",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    Marker marker;

    private void setMarker( double lat, double lng) {
        if(marker!=null){
            marker.remove();
        }

        MarkerOptions options = new MarkerOptions();
        options.title(rname);
        options.position(new LatLng(lat, lng));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        options.snippet(raddress);
        marker = map.addMarker(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        gotolocationZoom(coor1,coor2,20);
        //gotolocationZoom(4.606302, 114.292027,20);
    }

    private void gotolocationZoom(double lat, double lang, int zoom) {
        LatLng ll = new LatLng(lat, lang);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        map.animateCamera(update);
        map.moveCamera(update);
        setMarker(lat,lang);
        //add a marker

    }
}
