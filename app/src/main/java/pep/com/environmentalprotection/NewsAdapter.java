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

public class NewsAdapter extends BaseAdapter {

    Context context;
    List<News> rowItems;

    NewsAdapter(Context context, List<News> rowItems) {
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
        TextView title;

        TextView dateposted;
        TextView postedby;
        Button btnview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_news_list, null);
            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.tvNewsTitle);
            holder.dateposted = (TextView) convertView.findViewById(R.id.tvNewsDate);
            holder.postedby = (TextView) convertView.findViewById(R.id.tvNewsPosted);

            holder.btnview = (Button) convertView.findViewById(R.id.btnNewsRead);


            convertView.setTag(holder);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final News row_pos = rowItems.get(position);

        holder.title.setText(row_pos.getTitle());
        holder.dateposted.setText("Date Posted: " + row_pos.getDateposted());
        holder.postedby.setText("Posted by: " + row_pos.getAddedby());

        holder.btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(context,NewsContents.class);
                in.putExtra("title",row_pos.getTitle());
                in.putExtra("ID",row_pos.getID());
                in.putExtra("contents",row_pos.getContents());
                in.putExtra("organization",row_pos.getAddedby());
                in.putExtra("loggedas",row_pos.getOrganization());
                context.startActivity(in);
                ((Activity)context).finish();

            }
        });


        return convertView;
    }

}

