package pep.com.environmentalprotection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class addbulletin extends AppCompatActivity {

    View btnpost;
    EditText etTitle;
    EditText etContent;
    String organization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbulletin);

        btnpost=findViewById(R.id.btnPost);
        etTitle= (EditText) findViewById(R.id.etTitle);
        etContent= (EditText) findViewById(R.id.etContents);

        organization= getIntent().getExtras().getString("organization");

        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etTitle.getText().toString().isEmpty()){
                    etTitle.setError("please enter the title");
                }
                else if(etContent.getText().toString().isEmpty()){
                    etContent.setError("please enter the contents");
                }
                else {
                    BackGroundWorker bw=new BackGroundWorker();
                    bw.execute(etTitle.getText().toString(),etContent.getText().toString(),organization);

                }

            }
        });

    }

    class BackGroundWorker extends AsyncTask<String,Void,Void>{
        ProgressDialog p;
        AlertDialog a;

        String result="";

        @Override
        protected Void doInBackground(String... details) {




            String registerurl="http://10.0.2.2/pep/addbulettin.php";

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
                String postdata= URLEncoder.encode("title","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
                postdata +="&"+ URLEncoder.encode("content","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");
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
            p=new ProgressDialog(addbulletin.this);
            p.setMessage("Processing your request..");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(addbulletin.this,result,Toast.LENGTH_LONG).show();
            if(result.trim().equals("Posted")){
                Intent in=new Intent(addbulletin.this,Bulletin.class);
                in.putExtra("organization",organization);
                startActivity(in);
                finish();
            }
            else {
                Toast.makeText(addbulletin.this,result.toString(),Toast.LENGTH_LONG).show();
            }

            p.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(addbulletin.this,Bulletin.class);
        in.putExtra("organization",organization);
        startActivity(in);
        finish();
    }
}
