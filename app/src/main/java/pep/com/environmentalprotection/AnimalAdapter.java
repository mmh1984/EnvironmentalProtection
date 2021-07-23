package pep.com.environmentalprotection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static pep.com.environmentalprotection.R.id.tvDAdd;

/**
 * Created by Maynard on 4/8/2017.
 */

public class AnimalAdapter extends BaseAdapter{

    Context context;
    List<Animals> rowItems;

    AnimalAdapter(Context context, List<Animals> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    private class ViewHolder {
        ImageView profile_pic;
        TextView name;
        TextView species;
        TextView habitat;
        TextView date;
        TextView addedby;
        Button map;
        Button open;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ViewHolder holderimage=new ViewHolder();
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_anima_list, null);
            holder = new ViewHolder();

           holder.name= (TextView) convertView.findViewById(R.id.etListName);
            holder.species= (TextView) convertView.findViewById(R.id.etlistSpecies);
            holder.habitat= (TextView) convertView.findViewById(R.id.etListHabitat);
            holder.addedby= (TextView) convertView.findViewById(R.id.etListAddedBY);
            holder.date= (TextView) convertView.findViewById(R.id.etlistdate);
            holder.open= (Button) convertView.findViewById(R.id.btnPreview);
            holderimage.profile_pic= (ImageView) convertView.findViewById(R.id.imageView3);

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();


        }
        final Animals row_pos = rowItems.get(position);

        holder.name.setText(row_pos.getName());
        holder.species.setText("Species: " +row_pos.getSpecies());
        holder.habitat.setText("Habitat: " +row_pos.getHabitat());
        holder.date.setText("Date added: " +row_pos.getDate());
        holder.addedby.setText("Added by: " +row_pos.getAddedby());

        final String url="http://10.0.2.2/pep/animals/" + row_pos.getID() + ".jpg";

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Bitmap bmap=getBitmapfromURL(url);
                    holderimage.profile_pic.setImageBitmap(bmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();





        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog profile=new Dialog(context);
                profile.setContentView(R.layout.activity_dialoganimals);
                profile.show();

                final ImageView dImage= (ImageView) profile.findViewById(R.id.dialogIV);
                TextView dDescription= (TextView) profile.findViewById(R.id.tvDDesc);
                TextView dAddress= (TextView) profile.findViewById(R.id.tvDAdd);

                dDescription.setText("Description: \n" + row_pos.getDescription());
                dAddress.setText("Address: \n" + row_pos.getDescription());

                Button dMap= (Button) profile.findViewById(R.id.btnDMap);
                final String url="http://10.0.2.2/pep/animals/" + row_pos.getID() + ".jpg";

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Bitmap bmap=getBitmapfromURL(url);
                            dImage.setImageBitmap(bmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

                dMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name=row_pos.getName();
                        String coordinates=row_pos.getCoor();
                        String address=row_pos.getAddress();
                        Intent i=new Intent(context,Map.class);
                        i.putExtra("name",name.toString());
                        i.putExtra("address",address.toString());
                        i.putExtra("coordinates",coordinates.toString());
                        context.startActivity(i);
                    }
                });




            }
        });

        return convertView;
    }

    public Bitmap getBitmapfromURL(String src){

        try {
            URL url=new URL(src);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input=connection.getInputStream();

            Bitmap mybit= BitmapFactory.decodeStream(input);

            return mybit;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
