package pep.com.environmentalprotection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Comments extends AppCompatActivity {
String ID,organization;
    Button btncomment;
    List<NewsComments> rowList;
    String [] newsid;
    String [] newscomments;
    String [] newsposted;
    String [] newsdate;
    ListView list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ID=getIntent().getExtras().getString("ID");
        organization=getIntent().getExtras().getString("organization");
        btncomment= (Button) findViewById(R.id.btnPost);
        list1= (ListView) findViewById(R.id.lvComments);

        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d=new Dialog(Comments.this);
                d.setContentView(R.layout.activity_dialogcomments);
                d.setTitle("Enter your Comment");
                d.show();

                final EditText comments= (EditText) d.findViewById(R.id.etComments);
                Button btnpost= (Button) d.findViewById(R.id.btnPost);

                btnpost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BackGroundWorker bw=new BackGroundWorker();
                        bw.execute(ID,comments.getText().toString(),organization);
                        d.cancel();
                        LoadListWorker lw=new LoadListWorker();
                        lw.execute();
                    }
                });

            }
        });

       LoadListWorker lw=new LoadListWorker();
        lw.execute();

    }

    class BackGroundWorker extends AsyncTask<String,Void,Void> {
        ProgressDialog p;
        AlertDialog a;

        String result="";

        @Override
        protected Void doInBackground(String... details) {




            String registerurl="http://10.0.2.2/pep/add_comments.php";

            try {
                URL url=new URL(registerurl);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);


                //output stream
                OutputStream out=urlConnection.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));

                //create data url
                String postdata= URLEncoder.encode("ID","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
                postdata +="&"+ URLEncoder.encode("comments","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");
                postdata +="&"+ URLEncoder.encode("organization","UTF-8")+"=" + URLEncoder.encode(details[2],"UTF-8");


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
            p=new ProgressDialog(Comments.this);
            p.setMessage("Processing your request..");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result.trim().equals("Posted")){
                Toast.makeText(Comments.this,"Comment Posted",Toast.LENGTH_LONG).show();



            }
            else {
                Toast.makeText(Comments.this,result,Toast.LENGTH_LONG).show();
            }

            p.dismiss();

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

                String registerurl = "http://10.0.2.2/pep/load_comments.php";
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
            p=new ProgressDialog(Comments.this);
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
                    newsid=new String[ja.length()];
                    newscomments=new String[ja.length()];
                    newsdate=new String[ja.length()];
                    newsposted=new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                         newsid[i]=jo.getString("bulletinID");
                        newscomments[i]=jo.getString("comments");
                        newsdate[i]=jo.getString("dateposted");
                        newsposted[i]=jo.getString("organization");



                    }

                    p.dismiss();
                    if(!isempty) {

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

        rowList=new ArrayList<NewsComments>();


            for (int x = 0; x < newsid.length; x++) {
                if(newsid[x].equals(ID)) {
                    NewsComments rst = new NewsComments(newsid[x], newscomments[x], newsdate[x], newsposted[x]);
                    rowList.add(rst);
                }

            }



        CommentAdapter ra = new CommentAdapter(Comments.this, rowList);


         list1.setAdapter(ra);

        ra.notifyDataSetChanged();



    }

}
