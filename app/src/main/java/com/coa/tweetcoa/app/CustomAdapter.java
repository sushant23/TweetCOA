package com.coa.tweetcoa.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import twitter4j.Status;

/**
 * Created by sushant on 5/27/14.
 */


public class CustomAdapter extends BaseAdapter{
    private ImageLoader imageLoader;
    Activity context;
    private List<Status> receivedStatusList;
    private static LayoutInflater inflater = null;
    public Resources resources;
    Status status = null;

    public CustomAdapter(Activity context, List<Status> receivedStatusList, Resources resources) {
        this.context = context;
        this.receivedStatusList = receivedStatusList;
        this.resources = resources;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

        imageLoader = imageLoader.getInstance();
    }

    public static class ViewHolder {
        public TextView name;
        public TextView tweet;
        public TextView tweetedDate;
        public ImageView imageViewProfile;
        public GridView gridView;

    }




    @Override
    public int getCount() {
        if (receivedStatusList.size() <= 0)
            return 1;
        return receivedStatusList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vi = view;
        ViewHolder holder;

        if(vi == null){

            vi = inflater.inflate(R.layout.row_timeline,null);
            holder = new ViewHolder();
            holder.name = (TextView) vi.findViewById(R.id.tvTimelineName);
            holder.tweet = (TextView) vi.findViewById(R.id.tvTimeLineText);
            holder.tweetedDate = (TextView) vi.findViewById(R.id.tvTweetedDate);
            holder.imageViewProfile = (ImageView) vi.findViewById(R.id.ivTimelineImage);
            holder.gridView = (GridView) vi.findViewById(R.id.gvImages);
            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if(receivedStatusList.size() <= 0){
            holder.tweet.setText("No data available");
        } else {
            status = null;

            status = (Status) receivedStatusList.get(position);
            holder.name.setText(status.getUser().getName());
            holder.tweet.setText(status.getText());
            holder.tweetedDate.setText(status.getCreatedAt().toString());
            Log.i("customadapter",String .valueOf(status.getMediaEntities().length));
            holder.gridView.setAdapter(new GridViewAdapter(context,status.getMediaEntities()));
            try {
                URL imageUrl = new URL(status.getUser().getProfileImageURL());
                imageLoader.displayImage(imageUrl.toString(),holder.imageViewProfile);
                /*Bitmap profilePic = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());*/
                /*holder.imageViewProfile.setImageBitmap(profilePic);*/
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        return vi;
    }
}
