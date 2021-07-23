package pep.com.environmentalprotection;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class addsightings extends AppCompatActivity implements View.OnClickListener {
    final static int CAMERA_ACCESS = 0;
    final static int GALLERY_ACCESS = 1;
    Button btnRotate, btnRotate2,btnSave;
    ImageView ivImage;
    Spinner sp1, sp2;

    LocationManager locationManager;
    LocationListener locationListener;
    EditText etLocation,etName,etAddress,etDescription;
    String organization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsightings);

        ivImage = (ImageView) findViewById(R.id.ivAnimal);
        btnRotate = (Button) findViewById(R.id.btnRotate);
        btnRotate2 = (Button) findViewById(R.id.btnRotate1);
        etLocation= (EditText) findViewById(R.id.etLocation);
        etName= (EditText) findViewById(R.id.etName);
        etAddress= (EditText) findViewById(R.id.etAddress);
        etDescription= (EditText) findViewById(R.id.etDescription);
        btnSave= (Button) findViewById(R.id.btnSave);
        get_location();
        load_spinners();
        String operation = getIntent().getExtras().getString("operation");
       organization=getIntent().getExtras().getString("organization");
        if (operation.equals("camera")) {


            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (camera_intent.resolveActivity(getPackageManager()) != null) {

                startActivityForResult(camera_intent, CAMERA_ACCESS);
                // startActivityForResult(camera_intent,CAMERA_ACCESS);
            }
        } else {

            Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery_intent, GALLERY_ACCESS);

        }


        btnRotate.setOnClickListener(this);
        btnRotate2.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void get_location() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                etLocation.setText(location.getLatitude() + "," + location.getLongitude());
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void load_spinners() {
        try{

           List<String> species=new ArrayList<String>();

            species.add("Mammal");
            species.add("Fish");
            species.add("Reptiles");
            species.add("Amphibians");
            species.add("Insects");



            List<String> habitat=new ArrayList<String >();

            habitat.add("Grasslands");
            habitat.add("Rainforest");
            habitat.add("Dessert");
            habitat.add("Fresh Water");
            habitat.add("Salt Water");

            sp1= (Spinner) findViewById(R.id.spinner1);
            sp2= (Spinner) findViewById(R.id.spinner2);

            ArrayAdapter<String> sp1A=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,species);
            sp1.setAdapter(sp1A);

            ArrayAdapter<String> sp2A=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,habitat);
            sp2.setAdapter(sp2A);


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_ACCESS && resultCode==RESULT_OK){
            Uri captured=data.getData();
            ivImage.setImageURI(captured);

        }

        if(requestCode==GALLERY_ACCESS && resultCode==RESULT_OK){
            Uri captured=data.getData();
            ivImage.setImageURI(captured);

        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnRotate){
            ivImage.setRotation(ivImage.getRotation() +90);
        }
        if(view.getId()==R.id.btnRotate1){
            ivImage.setRotation(ivImage.getRotation() -90);
        }

        if(view.getId()==R.id.btnSave){

            save_animal();

        }
    }

    private void save_animal() {
        if(etLocation.getText().toString().isEmpty()){
            etLocation.setError("please enter your location");

        }
        else if(etName.getText().toString().isEmpty()){
            etName.setError("please enter your location");

        }

        else if(etDescription.getText().toString().isEmpty()){
            etDescription.setError("please enter your location");

        }
        else if(etAddress.getText().toString().isEmpty()){
            etAddress.setError("please enter your location");

        }
        else {
            Bitmap decodeimg=((BitmapDrawable)ivImage.getDrawable()).getBitmap();
            ByteArrayOutputStream btimage=new ByteArrayOutputStream();
            decodeimg.compress(Bitmap.CompressFormat.JPEG,100,btimage);
            String strphoto;

            strphoto= Base64.encodeToString(btimage.toByteArray(),Base64.DEFAULT);

            String [] params=new String[8];
            params[0]=etName.getText().toString();
            params[1]=sp1.getSelectedItem().toString();
            params[2]=sp2.getSelectedItem().toString();
            params[3]=etDescription.getText().toString();
            params[4]=etAddress.getText().toString();
            params[5]=etLocation.getText().toString();
            params[6]=organization;
            params[7]=strphoto;

            BackgroundWorker bw=new BackgroundWorker();
            bw.execute(params);

        }
    }


    public class BackgroundWorker extends AsyncTask<String,Void,Void> {

        ProgressDialog p;
        AlertDialog a;

        String result="";


        @Override
        protected Void doInBackground(String... details) {

            String openurl="http://10.0.2.2/pep/addanimals.php";
            try {
                URL url=new URL(openurl);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);


                //output stream
                OutputStream out=urlConnection.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));


                String postdata= URLEncoder.encode("r1","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
                postdata +="&"+ URLEncoder.encode("r2","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");
                postdata +="&"+ URLEncoder.encode("r3","UTF-8")+"=" + URLEncoder.encode(details[2],"UTF-8");
                postdata +="&"+ URLEncoder.encode("r4","UTF-8")+"=" + URLEncoder.encode(details[3],"UTF-8");
                postdata +="&"+ URLEncoder.encode("r5","UTF-8")+"=" + URLEncoder.encode(details[4],"UTF-8");
                postdata +="&"+ URLEncoder.encode("r6","UTF-8")+"=" + URLEncoder.encode(details[5],"UTF-8");
                postdata +="&"+ URLEncoder.encode("r7","UTF-8")+"=" + URLEncoder.encode(details[6],"UTF-8");
                postdata +="&"+ URLEncoder.encode("photo","UTF-8")+"=" + URLEncoder.encode(details[7],"UTF-8");

                br.write(postdata);
                br.flush();
                br.close();
                out.close();

                InputStream in=urlConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in,"ISO-8859-1"));

                String line;

                while ((line=reader.readLine())!=null){
                    result+=line;
                }
                reader.close();
                in.close();
                urlConnection.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;


        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            p=new ProgressDialog(addsightings.this);
            p.setMessage("Performing your request");
            p.show();

        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);



            Toast.makeText(addsightings.this,result,Toast.LENGTH_LONG).show();

            Intent in=new Intent(addsightings.this,EnviromentRecord.class);
            in.putExtra("organization",organization);
            startActivity(in);
            finish();
            p.hide();
            p.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(addsightings.this,EnviromentRecord.class);
        in.putExtra("organization",organization);
        startActivity(in);
        finish();
    }
}
