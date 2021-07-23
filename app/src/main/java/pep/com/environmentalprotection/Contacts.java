package pep.com.environmentalprotection;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class Contacts extends AppCompatActivity {
TextView phone,website,facebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        phone= (TextView) findViewById(R.id.tvPhone);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_DIAL);
                in.setData(Uri.parse("tel:6738170288"));
                startActivity(in);
            }
        });

        website= (TextView) findViewById(R.id.etWebsite);
        facebook= (TextView) findViewById(R.id.etFacebook);

        String websitesrc="http://www.bruneibirds.com";
        String facebooksrc="http://www.facebook.com/rrajah";
        website.setText(Html.fromHtml("<a href='" +websitesrc+ "'>Website: http://www.bruneibirds.com</a>"));
        website.setMovementMethod(LinkMovementMethod.getInstance());
        facebook.setText(Html.fromHtml("<a href='" +facebooksrc+ "'>Facebook: http://www.facebook.com/rrajah</a>"));
        facebook.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
