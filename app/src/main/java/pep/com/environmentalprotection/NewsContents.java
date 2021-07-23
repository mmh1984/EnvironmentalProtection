package pep.com.environmentalprotection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

public class NewsContents extends AppCompatActivity {
TextView tvTitle;
    TextView tvContents;
    String title,contents,organization,ID,loggedas;
    Button btnComments,btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_contents);

        tvTitle= (TextView) findViewById(R.id.tvTitle);
        tvContents= (TextView) findViewById(R.id.tvContents);
        btnComments= (Button) findViewById(R.id.btnViewComments);
        btnDelete= (Button) findViewById(R.id.btnDeletePost);
        ID=getIntent().getExtras().getString("ID");
        title=getIntent().getExtras().getString("title");
        contents=getIntent().getExtras().getString("contents");
        organization=getIntent().getExtras().getString("organization");
        loggedas=getIntent().getExtras().getString("loggedas");


        tvTitle.setText(title);
        tvContents.setText(contents);

        if(loggedas.equals(organization)){
            btnDelete.setVisibility(View.VISIBLE);
        }
        else {
            btnDelete.setVisibility(View.INVISIBLE);
        }

        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(NewsContents.this,Comments.class);
                in.putExtra("ID",ID);
                in.putExtra("organization",organization);
                startActivity(in);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackGroundWorker bw=new BackGroundWorker();
                bw.execute(ID);
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(NewsContents.this,Bulletin.class);
        in.putExtra("organization",organization);
        startActivity(in);
        finish();
    }

    class BackGroundWorker extends AsyncTask<String,Void,Void> {
        ProgressDialog p;
        AlertDialog a;

        String result="";

        @Override
        protected Void doInBackground(String... details) {




            String registerurl="http://10.0.2.2/pep/delete_news.php";

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
            p=new ProgressDialog(NewsContents.this);
            p.setMessage("Processing your request..");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(NewsContents.this,result,Toast.LENGTH_LONG).show();
            if(result.trim().equals("Deleted")){
                Toast.makeText(NewsContents.this,"Deleted",Toast.LENGTH_LONG).show();
                Intent in=new Intent(NewsContents.this,Bulletin.class);
                in.putExtra("organization",organization);
                startActivity(in);
                finish();


            }
            else {
                Toast.makeText(NewsContents.this,result,Toast.LENGTH_LONG).show();
            }

            p.dismiss();

        }
    }


}
