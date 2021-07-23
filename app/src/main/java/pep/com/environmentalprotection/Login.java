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

public class Login extends AppCompatActivity implements View.OnClickListener {
Button btnlogin,btnRegister;
    String orgid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin= (Button) findViewById(R.id.btnLogin);
        btnRegister= (Button) findViewById(R.id.btnRegister);

btnlogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnLogin){
            final Dialog login=new Dialog(Login.this);
            login.setContentView(R.layout.activity_login_dialog);
            login.setTitle("Member Login");
            login.setCancelable(false);
            login.show();

            Button btnOK= (Button) login.findViewById(R.id.btnLogin);
            Button btnCancel= (Button) login.findViewById(R.id.btnCancel);
            final EditText userid= (EditText) login.findViewById(R.id.etUserID);
            final EditText userpass= (EditText) login.findViewById(R.id.etPassword);

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(userid.getText().toString().equals("") || userpass.getText().equals("")){
                        Toast.makeText(Login.this,"Fill in the username and password",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        orgid=userid.getText().toString();
                        BackgroundWorker bw=new BackgroundWorker();
                        bw.execute(userid.getText().toString(),userpass.getText().toString());
                    }

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login.dismiss();
                }
            });


        }
        else if(view.getId()==R.id.btnRegister) {
            startActivity(new Intent(Login.this,Register.class));
        }
    }

    class BackgroundWorker extends AsyncTask<String,Void,Void>{

        ProgressDialog p;
        AlertDialog a;

        String result="";

        @Override
        protected Void doInBackground(String... details) {




            String registerurl="http://10.0.2.2/pep/login.php";

            try {
                URL url=new URL(registerurl);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);


                //output stream
                OutputStream out=urlConnection.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String photoname=details[0].replace(" ","_");
                //create data url
                String postdata= URLEncoder.encode("userid","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
                postdata +="&"+ URLEncoder.encode("userpass","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");

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
            p=new ProgressDialog(Login.this);
            p.setMessage("Processing your request..");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result.trim().toString().equals("Login Accepted")) {
                Toast.makeText(Login.this, result.toString(), Toast.LENGTH_SHORT).show();
                Intent in=new Intent(Login.this,MainActivity.class);
                in.putExtra("organization",orgid);
                startActivity(in);
                finish();

            }
            else {
                Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_SHORT).show();

            }
            p.dismiss();
        }
    }
}
