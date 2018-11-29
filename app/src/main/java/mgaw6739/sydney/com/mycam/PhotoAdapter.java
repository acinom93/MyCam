package mgaw6739.sydney.com.mycam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class PhotoAdapter extends BaseAdapter {

    private static final int Width = 300;
    private static final int Height = 300;
    private static final int Padding = 3;

    private Context context;
    private List<String> listPhotos;

    public PhotoAdapter(Context con, List<String> listImages) {
        context = con;
        this.listPhotos = listImages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView gridImageView = (ImageView) convertView;

        if (gridImageView == null) {
            gridImageView = getImageView();
        }

        Bitmap myBitmap = decodeBitmap(position);
        gridImageView.setImageBitmap(myBitmap);

        return gridImageView;
    }

    private Bitmap decodeBitmap(int position) {
        final BitmapFactory.Options decodeOption = new BitmapFactory.Options();
        decodeOption.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(listPhotos.get(position), decodeOption);
        decodeOption.inSampleSize = 4;
        decodeOption.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(listPhotos.get(position), decodeOption);
    }

    @NonNull
    private ImageView getImageView() {
        ImageView gridImageView;
        gridImageView = new ImageView(context);
        gridImageView.setLayoutParams(new GridView.LayoutParams(Width, Height));
        gridImageView.setPadding(Padding, Padding, Padding, Padding);
        gridImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return gridImageView;
    }

    @Override
    public int getCount() {
        return listPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}