package com.coa.tweetcoa.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import twitter4j.MediaEntity;

/**
 * Created by sushant on 5/28/14.
 */
public class GridViewAdapter extends BaseAdapter {
    private ImageLoader imageLoader;
    private Context context;
    private MediaEntity[] mediaEntities;
    private MediaEntity mediaEntity;
    private static LayoutInflater inflater = null;
    public GridViewAdapter(Context context,MediaEntity[] mediaEntitites) {
        this.context = context;
        this.mediaEntities = mediaEntitites;
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
        Log.i("mediaentitylength",String.valueOf(mediaEntitites.length));
    }

    @Override
    public int getCount() {
        return mediaEntities.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public static class ViewHolder{
        ImageView gvImage;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        ViewHolder holder;
        if(vi == null){
            vi = inflater.inflate(R.layout.image_gridview, null);
            holder = new ViewHolder();
            holder.gvImage = (ImageView) vi.findViewById(R.id.ivGridViewImage);
            vi.setTag(holder);
        }else {
            holder = (ViewHolder) vi.getTag();
        }
        if(mediaEntities.length > 0 ){
            mediaEntity = mediaEntities[i];
            String mediaURL = mediaEntity.getMediaURL().toString();
            imageLoader.displayImage(mediaURL, holder.gvImage);
            Log.i("mediaURL",mediaURL);

        }
        return vi;
    }
}
