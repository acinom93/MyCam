package mgaw6739.sydney.com.mycam;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

public class GridActivity extends AppCompatActivity {

    int PHOTO_ACTIVITY_CONSTANT = 1234;
    int GRID_ACTIVITY_CONSTANT = 1234;
    public static String PATH_KEY = "MY_CAMP_PATH";

    ArrayList<String> gridImagesList;
    PhotoAdapter photoAdapter;
    Intent photoPreviewIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        photoPreviewIntent = new Intent(this, PreviewActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        gridImagesList = new ArrayList<String>();
        getImagesFromFiles();
        GridView gridview = (GridView) findViewById(R.id.photoGrid);
        photoAdapter = new PhotoAdapter(this, gridImagesList);
        gridview.setAdapter(photoAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                photoPreviewIntent.putExtra(GridActivity.PATH_KEY, gridImagesList.get(position));
                startActivityForResult(photoPreviewIntent, GRID_ACTIVITY_CONSTANT);
            }
        });
    }

    public void getImagesFromFiles() {
        Cursor cursor;
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        cursor = this.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null,
                        MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        populatePhotoList(cursor);
    }

    private void addClickedPhotoPath() {

        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "/Images/");
        if(!directory.exists())
        {
            return;
        }
        File[] allPhotos = directory.listFiles();
        if (allPhotos != null) {
            for (File photo : allPhotos) {
                String filePath = photo.getAbsolutePath();
                if (!gridImagesList.contains(filePath)) {
                    gridImagesList.add(0, filePath);
                }
            }
        }
    }

    private void populatePhotoList(Cursor cursor) {
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            String imageAbsPath = cursor.getString(column_index);
            gridImagesList.add(imageAbsPath);
        }
    }

    public void OnClickNew(View v) {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivityForResult(intent, PHOTO_ACTIVITY_CONSTANT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        addClickedPhotoPath();
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addClickedPhotoPath();
        photoAdapter.notifyDataSetChanged();
    }
}
