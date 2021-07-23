package pep.com.environmentalprotection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Bulletin extends AppCompatActivity {
String organization;
    List<News> rowList;
    ListView List1;
    String [] ID;
    String [] title;
    String [] contents;
    String [] dateposted;
    String [] postedby;
    String criteria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);

        organization=getIntent().getExtras().getString("organization");
        List1= (ListView) findViewById(R.id.lvBulletin);
criteria="ALL";
        LoadListWorker lw=new LoadListWorker();
        lw.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.bulletin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent in;
        if(item.getItemId()==R.id.addnews){
            in=new Intent(Bulletin.this,addbulletin.class);
            in.putExtra("organization",organization);
            startActivity(in);
            finish();
        }

        if(item.getItemId()==R.id.yournews){
            criteria="mybulletin";
            LoadListWorker lw=new LoadListWorker();
            lw.execute();
        }

        if(item.getItemId()==R.id.allnews){
            criteria="ALL";
            LoadListWorker lw=new LoadListWorker();
            lw.execute();
        }

        if(item.getItemId()==R.id.mycomments){
            in=new Intent(Bulletin.this,myComments.class);
            in.putExtra("organization",organization);
            startActivity(in);
            finish();
        }

        return super.onOptionsItemSelected(item);

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

                String registerurl = "http://10.0.2.2/pep/load_bulletin.php";
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
            p=new ProgressDialog(Bulletin.this);
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
                    title=new String[ja.length()];
                    contents=new String[ja.length()];
                    dateposted=new String[ja.length()];
                    postedby=new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        ID[i]=jo.getString("ID");
                        title[i]=jo.getString("title");
                        contents[i]=jo.getString("contents");
                       dateposted[i]=jo.getString("dateposted");
                        postedby[i]=jo.getString("postedby");

                    }

                    p.dismiss();
                    if(!isempty) {


                       // Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                       load_list();
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

    public void load_list(){

        rowList=new ArrayList<News>();




        if(criteria.equals("ALL")) {

            for (int x = 0; x < ID.length; x++) {


                News n = new News(ID[x], title[x], contents[x], dateposted[x], postedby[x],organization);
                rowList.add(n);
            }

        }
        else {
            for (int x = 0; x < ID.length; x++) {
                    if(postedby[x].equals(organization)) {

                        News n = new News(ID[x], title[x], contents[x], dateposted[x], postedby[x],organization);
                        rowList.add(n);
                    }
            }
        }
        NewsAdapter ra = new NewsAdapter(Bulletin.this, rowList);
        List1.setAdapter(ra);

        ra.notifyDataSetChanged();



    }


}
