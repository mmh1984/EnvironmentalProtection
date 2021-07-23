package pep.com.environmentalprotection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EnviromentRecord extends AppCompatActivity implements View.OnClickListener {
Button btnCamera,btnGallery;
    final static int CAMERA_ACCESS=0;
    final static int GALLERY_ACCESS=1;
    ListView List1;
    EditText etSearch;
    String[] ID;
    String name[];
    String species[];
    String habitat[];
    String description[];
    String address[];
    String location[];
    String addedby[];
    String dateadded[];
    List<Animals> rowList;
    String organization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviroment_record);

        btnCamera= (Button) findViewById(R.id.btnCamera);
        btnGallery= (Button) findViewById(R.id.btnGallery);
        etSearch= (EditText) findViewById(R.id.etsearch);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);

        organization=getIntent().getExtras().getString("organization");

        List1= (ListView) findViewById(R.id.lvList);
        LoadListWorker lw=new LoadListWorker();
        lw.execute();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    load_list(etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.btnCamera){
            Intent in=new Intent(EnviromentRecord.this,addsightings.class);
            /*
            Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(camera_intent.resolveActivity(getPackageManager())!=null) {

                startActivityForResult(camera_intent,CAMERA_ACCESS);
               // startActivityForResult(camera_intent,CAMERA_ACCESS);
            }
*/
            startActivity(in);
            finish();

            //in.putExtra("operation","camera");
        }
        else if(v.getId()==R.id.btnGallery){
            Intent in=new Intent(EnviromentRecord.this,addsightings.class);
            in.putExtra("operation","gallery");
            in.putExtra("organization",organization);
            startActivity(in);
            finish();
        }


    }






    class LoadListWorker extends AsyncTask<Void,Void,Void> {
        InputStream is=null;
        String result="";
        String line="";
        Boolean isempty;
        ProgressDialog p;
        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String registerurl = "http://10.0.2.2/pep/load_animals.php";
                URL url = new URL(registerurl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                is = new BufferedInputStream(urlConnection.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();


                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }






            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isempty=true;
            p=new ProgressDialog(EnviromentRecord.this);
            p.setMessage("Loading....");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {

                if (!result.trim().equals("None")) {

                    JSONArray ja = new JSONArray(result);
                    JSONObject jo = null;
                    isempty=false;
                    ID=new String[ja.length()];
                    name=new String[ja.length()];
                    species=new String[ja.length()];
                    habitat=new String[ja.length()];
                    description=new String[ja.length()];
                    address=new String[ja.length()];
                    location=new String[ja.length()];
                    addedby=new String[ja.length()];
                    dateadded=new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);

                        ID[i]=jo.getString("ID");
                        name[i]=jo.getString("name");
                        species[i]=jo.getString("species");
                        habitat[i]=jo.getString("habitat");
                        description[i]=jo.getString("description");
                        address[i]=jo.getString("address");
                        location[i]=jo.getString("location");
                        addedby[i]=jo.getString("addedby");
                        dateadded[i]=jo.getString("dateadded");

                    }

                    p.dismiss();
                    if(!isempty) {

                        load_list("all");
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No content",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }

            } catch(JSONException e){
                e.printStackTrace();
            }

        }
    }

    public void load_list(String district){

        rowList=new ArrayList<Animals>();

        if(district.equals("all")) {
            for (int x = 0; x < name.length; x++) {
                Animals rst = new Animals(name[x],species[x],ID[x],habitat[x],dateadded[x],addedby[x],address[x],location[x],description[x]);
                rowList.add(rst);
            }
        }
        else {
            for (int x = 0; x < name.length; x++) {
                if(name[x].contains(district)) {

                    Animals rst = new Animals(name[x], species[x], ID[x], habitat[x], dateadded[x], addedby[x],address[x],location[x],description[x]);
                    rowList.add(rst);
                }
            }
        }

        AnimalAdapter ra = new AnimalAdapter(EnviromentRecord.this, rowList);


        List1.setAdapter(ra);

        ra.notifyDataSetChanged();



    }

}
