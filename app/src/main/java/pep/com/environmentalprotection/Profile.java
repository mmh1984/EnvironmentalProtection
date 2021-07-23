package pep.com.environmentalprotection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Profile extends AppCompatActivity {
String organization;
    TextView tvOrganization,tvName,tvPhone;
    String name[];
    String password[];
    String phone[];
    String org[];
    Button delete,changepass;
    String primary[];
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        organization=getIntent().getExtras().getString("organization");

        LoadUser lw=new LoadUser();
        lw.execute();
        tvOrganization= (TextView) findViewById(R.id.tvOrganization);
        tvName= (TextView) findViewById(R.id.tvName);
        tvPhone= (TextView) findViewById(R.id.tvPhone);
        delete= (Button) findViewById(R.id.btnDelete);
        changepass= (Button) findViewById(R.id.btnChangePass);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);
                alertDialogBuilder.setMessage("Deactivate Your Account");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                DeleteAccount dw=new DeleteAccount();
                                dw.execute(key);

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d=new Dialog(Profile.this);
                d.setContentView(R.layout.change_password);
                d.setTitle("Change Password");
                d.show();
                d.setCancelable(false);

                Button change= (Button) d.findViewById(R.id.btnChange);
                Button cancel= (Button) d.findViewById(R.id.btnCancel);
                final EditText oldpass= (EditText) d.findViewById(R.id.etOldPass);
                final EditText newpass= (EditText) d.findViewById(R.id.etNewPass);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(oldpass.getText().toString().isEmpty() || newpass.getText().toString().isEmpty()){
                            oldpass.setError("enter your old pass");
                            newpass.setError("enter your new pass");

                        }
                        else {
                            if(oldpass.getText().toString().equals(password[0])){
                                UpdatePassword up=new UpdatePassword();
                                up.execute(organization,newpass.getText().toString());
                            }
                            else{
                                oldpass.setError("old password is incorrect");
                            }
                        }

                    }
                });
            }
        });



        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_DIAL);
                in.setData(Uri.parse("tel:" + tvPhone.getText()));
                startActivity(in);
            }
        });


    }

    class LoadUser extends AsyncTask<Void,Void,Void> {
        InputStream is=null;
        String result="";
        String line="";
        Boolean isempty;
        ProgressDialog p;
        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String registerurl = "http://10.0.2.2/pep/load_users.php";
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
            p=new ProgressDialog(Profile.this);
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
                     name=new String[ja.length()];
                    phone=new String[ja.length()];
                    password=new String[ja.length()];
                    org=new String[ja.length()];
                    primary=new String[ja.length()];

                    if(ja.length()==1){

                        jo = ja.getJSONObject(0);
                        name[0]=jo.getString("firstname") + " " + jo.getString("lastname");
                        phone[0]=jo.getString("contactno");
                        password[0]=jo.getString("password");
                        org[0]=jo.getString("organizationid");
                        primary[0]=jo.getString("id");

                        tvOrganization.setText(organization);
                        tvName.setText("Full Name: " + name[0]);
                        tvPhone.setText("Phone: " + phone[0]);
                        key=primary[0];

                    }
                    else {


                        for (int i = 0; i < ja.length(); i++) {
                            jo = ja.getJSONObject(i);

                            name[i] = jo.getString("firstname") + " " + jo.getString("lastname");
                            phone[i] = jo.getString("contactno");
                            password[i] = jo.getString("password");
                            org[i] = jo.getString("organizationid");
                            primary[i]=jo.getString("id");

                        }


                       for(int x=0;x<name.length;x++){
                           if (org[x].equals(organization)) {
                               Toast.makeText(getApplicationContext(), org[x], Toast.LENGTH_SHORT).show();
                               tvOrganization.setText(organization);
                               tvName.setText("Full Name: " + name[x]);
                               tvPhone.setText("Phone: " + phone[x]);
                               key=primary[x];
                               break;

                           }
                       }
                    }
                    p.dismiss();
                    if(!isempty) {


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


    class UpdatePassword extends AsyncTask<String,Void,Void>{
        ProgressDialog p;
        AlertDialog a;

        String result="";

        @Override
        protected Void doInBackground(String... details) {




            String registerurl="http://10.0.2.2/pep/update_password.php";

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
                String postdata= URLEncoder.encode("organization","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
                postdata +="&"+ URLEncoder.encode("password","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");



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
            p=new ProgressDialog(Profile.this);
            p.setMessage("Processing your request..");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result.trim().equals("OK")){
                Toast.makeText(Profile.this,"Password Updated",Toast.LENGTH_LONG).show();
                Intent in=new Intent(Profile.this,Login.class);

                startActivity(in);
                finish();
            }


            p.dismiss();
        }
    }


    class DeleteAccount extends AsyncTask<String,Void,Void>{
        ProgressDialog p;
        AlertDialog a;

        String result="";

        @Override
        protected Void doInBackground(String... details) {




            String registerurl="http://10.0.2.2/pep/delete_account.php";

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
            p=new ProgressDialog(Profile.this);
            p.setMessage("Processing your request..");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(Profile.this,result,Toast.LENGTH_LONG).show();
            if(result.trim().equals("OK")){
                Toast.makeText(Profile.this,"Account Deleted",Toast.LENGTH_LONG).show();
                Intent in=new Intent(Profile.this,Login.class);
                startActivity(in);
                finish();
            }


            p.dismiss();
        }
    }
}
