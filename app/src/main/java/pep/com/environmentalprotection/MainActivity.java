package pep.com.environmentalprotection;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    Button btnepp;
    Button bulletin;
    Button contactus;
    String organization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager= (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);

        organization=getIntent().getExtras().getString("organization");
        btnepp= (Button) findViewById(R.id.btnEPP);
        bulletin= (Button) findViewById(R.id.btnBNB);
        contactus= (Button) findViewById(R.id.btnCU);
        contactus.setOnClickListener(this);
        btnepp.setOnClickListener(this);
        bulletin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnEPP){
            Intent in=new Intent(MainActivity.this,EnviromentRecord.class);
            in.putExtra("organization",organization);
            startActivity(in);

        }

        if(v.getId()==R.id.btnBNB){
            Intent in=new Intent(MainActivity.this,Bulletin.class);
            in.putExtra("organization",organization);
            startActivity(in);

        }
        if(v.getId()==R.id.btnCU){
            Intent in=new Intent(MainActivity.this,Contacts.class);
            in.putExtra("organization",organization);
            startActivity(in);
        }
    }

    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(1);
                    }
                    else if(viewPager.getCurrentItem()==1) {
                        viewPager.setCurrentItem(2);
                    }
                    else if(viewPager.getCurrentItem()==2) {
                        viewPager.setCurrentItem(3);
                    }
                    else if(viewPager.getCurrentItem()==3) {
                        viewPager.setCurrentItem(4);
                    }
                    else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.mysightings){
            Intent in=new Intent(MainActivity.this,MySightings.class);
            in.putExtra("organization",organization);
            startActivity(in);
        }
        if(item.getItemId()==R.id.myprofile){
            Intent in=new Intent(MainActivity.this,Profile.class);
            in.putExtra("organization",organization);
            startActivity(in);
        }


        return super.onOptionsItemSelected(item);
    }
}
