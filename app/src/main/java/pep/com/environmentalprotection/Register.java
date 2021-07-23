package pep.com.environmentalprotection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.hardware.camera2.TotalCaptureResult;
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

public class Register extends AppCompatActivity implements View.OnClickListener {
Button btnregister,btncancel;
    EditText txtfname,txtlname,txtcontact,txtorgid,txtpassword,txtconfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnregister= (Button) findViewById(R.id.btnRegister);
        btncancel= (Button) findViewById(R.id.btnCancel);

        txtfname= (EditText) findViewById(R.id.etFname);
        txtlname= (EditText) findViewById(R.id.etLname);
        txtcontact= (EditText) findViewById(R.id.etLContactNo);
        txtorgid= (EditText) findViewById(R.id.etOrgname);
        txtpassword= (EditText) findViewById(R.id.etPassword);
        txtconfirm= (EditText) findViewById(R.id.etConfirm);

        btnregister.setOnClickListener(this);
        btncancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnRegister){
            if(login_valid()){
                if(txtconfirm.getText().toString().equals(txtpassword.getText().toString())){

                    String fname,lname,contact,organization,password;
                    fname=txtfname.getText().toString();
                    lname=txtlname.getText().toString();
                    contact=txtcontact.getText().toString();
                    organization=txtorgid.getText().toString();
                    password=txtpassword.getText().toString();
                BackGroundWorker bw=new BackGroundWorker();
                    bw.execute(fname,lname,contact,organization,password);
                }
                else {
                    Toast.makeText(Register.this,"Passwords didnt match", Toast.LENGTH_SHORT).show();
                    txtpassword.setError("didnt match");
                    txtconfirm.setError("didnt match");
                }

            }
        }
        if(v.getId()==R.id.btnCancel){ finish();}

    }

    class BackGroundWorker extends AsyncTask<String,Void,Void>{
        ProgressDialog p;
        AlertDialog a;

        String result="";

        @Override
        protected Void doInBackground(String... details) {




                String link="http://10.0.2.2/pep/register.php";

                try {
                    URL url=new URL(link);
                    HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);


                    //output stream
                    OutputStream out=urlConnection.getOutputStream();
                    BufferedWriter br=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                    String photoname=details[0].replace(" ","_");
                    //create data url
                    String postdata= URLEncoder.encode("fname","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
                    postdata +="&"+ URLEncoder.encode("lname","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");
                    postdata +="&"+ URLEncoder.encode("contact","UTF-8")+"=" + URLEncoder.encode(details[2],"UTF-8");
                    postdata +="&"+ URLEncoder.encode("organization","UTF-8")+"=" + URLEncoder.encode(details[3],"UTF-8");
                    postdata +="&"+ URLEncoder.encode("password","UTF-8")+"=" + URLEncoder.encode(details[4],"UTF-8");

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
            p=new ProgressDialog(Register.this);
            p.setMessage("Processing your request..");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(Register.this, result.toString(), Toast.LENGTH_SHORT).show();
            if(result.trim().toString().equals("Registration Complete")) {
                Toast.makeText(Register.this, result.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(result.trim().toString().equals("ID already taken")){
                Toast.makeText(Register.this,result.toString(),Toast.LENGTH_SHORT).show();
                txtorgid.setError("This ID is already taken");
            }
            p.dismiss();
        }
    }

    public Boolean login_valid() {
        Boolean Flag=true;
        if(txtfname.getText().toString().equals("")){
            Flag=false;
            txtfname.setError("cannot be empty");
        }
        else if (txtlname.getText().toString().equals("")){
            Flag=false;
            txtlname.setError("cannot be empty");
        }

        else if (txtcontact.getText().toString().equals("")){
            Flag=false;
            txtcontact.setError("cannot be empty");
        }

        else if (txtorgid.getText().toString().equals("")){
            Flag=false;
            txtorgid.setError("cannot be empty");
        }

        else if (txtpassword.getText().toString().equals("") || txtconfirm.getText().equals("")){
            Flag=false;
            txtpassword.setError("cannot be empty");
            txtconfirm.setError("cannot be empty");
        }
        return Flag;
    }
}
