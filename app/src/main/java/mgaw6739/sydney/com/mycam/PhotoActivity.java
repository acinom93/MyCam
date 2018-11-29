package mgaw6739.sydney.com.mycam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {

    Camera camera;
    Context context;
    ImageView imageView;
    PhotoView photoView;
    FrameLayout frameLayout;
    Intent previewActivityIntent;

    String strLastFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = findViewById(R.id.latestImgView);
        previewActivityIntent = new Intent(this, PreviewActivity.class);
        frameLayout = (FrameLayout) findViewById(R.id.viewCamera);
        fetchLatestImg();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open();
        photoView = new PhotoView(this, camera);
        frameLayout.addView(photoView);
    }

    public void clickPhoto(View view) {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File newFile = createNewImageFile();
                if (newFile != null) {
                    try {
                        setBitmapImage(data, newFile);
                        strLastFilePath =newFile.getAbsolutePath();
                        previewActivityIntent.putExtra(GridActivity.PATH_KEY,
                                newFile.getAbsolutePath());
                        stopCameraView();
                        startActivity(previewActivityIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setBitmapImage(byte[] data, File newFile) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        Bitmap bitmap = PreviewActivity.rotateImage(BitmapFactory.
                decodeByteArray(data, 0, data.length), 90);
        final boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG,
                100, fileOutputStream);
        Toast.makeText(context, "Image is Saved Successfully " +
                newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        fileOutputStream.close();
        imageView.setImageBitmap(bitmap);
    }


    private void fetchLatestImg() {

        boolean firstImg;
        File latestImg;
        File[] allPhotos;

        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "/Images/");

        if (directory.exists()) {

            allPhotos = directory.listFiles();
            latestImg = null;

            if (allPhotos != null) {

                firstImg = true;
                for (File photo : allPhotos) {

                    if (firstImg) {
                        firstImg = false;
                        latestImg = photo;
                    }

                    if (photo.lastModified() > latestImg.lastModified()) {
                        latestImg = photo;
                    }
                }
            }
            if (latestImg != null) {
                Bitmap bImage = BitmapFactory.decodeFile(latestImg.getAbsolutePath());
                imageView.setImageBitmap(bImage);
                strLastFilePath = latestImg.getAbsolutePath();
            }
        }
    }


    public void lastImgClicked(View view) {
        if(strLastFilePath!=null)
        {
            previewActivityIntent.putExtra(GridActivity.PATH_KEY, strLastFilePath);
            startActivity(previewActivityIntent);
        }
        else
            Toast.makeText(context,"Please Click an Picture...",Toast.LENGTH_SHORT).show();

        stopCameraView();
    }

    private void stopCameraView() {
        frameLayout.removeView(photoView);
        camera.stopPreview();
    }

    public void quitPhotoActivity(View view) {
        setResult(Activity.RESULT_CANCELED);
        stopCameraView();
        finish();

    }

    private File createNewImageFile() {
        String fileName = getPhotoFileName();
        File file = null;
        try {
            String folderName = "/Images/";
            String envFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File directory = new File(envFilePath, folderName);
            if (!directory.exists() && !directory.mkdirs()) {
                directory.createNewFile();
            }
            file = new File(directory.getPath() + File.separator + fileName);
            file.createNewFile();

        } catch (Exception ex) {
            Log.d("Exception File Creation", ex.getStackTrace().toString());
        }
        return file;
    }

    @NonNull
    private String getPhotoFileName() {
        Date newDate = new Date();
        SimpleDateFormat spf = new SimpleDateFormat("ddmmyyyyhhmmss");
        String date = spf.format(newDate);
        return "Img_" + date + ".jpg";
    }

}

