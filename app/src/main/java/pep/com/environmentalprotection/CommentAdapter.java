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

public class CommentAdapter extends BaseAdapter{

    Context context;
    List<NewsComments> rowItems;

    CommentAdapter(Context context, List<NewsComments> rowItems) {
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
       TextView comments;
        TextView dateposted;
        TextView postedby;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_comments, null);
            holder = new ViewHolder();

            holder.comments= (TextView) convertView.findViewById(R.id.tvCComments);
            holder.dateposted= (TextView) convertView.findViewById(R.id.tvCDateposted);
            holder.postedby= (TextView) convertView.findViewById(R.id.tvcCFrom);


            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();


        }
        NewsComments row_pos = rowItems.get(position);

        holder.comments.setText(row_pos.getNewsComments());
        holder.dateposted.setText("Posted on: " +row_pos.getNewsDate());
        holder.postedby.setText("By: " +row_pos.getNewsPosted());







        return convertView;
    }


}
