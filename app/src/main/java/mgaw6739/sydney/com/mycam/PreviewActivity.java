package mgaw6739.sydney.com.mycam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PreviewActivity extends AppCompatActivity {

    Boolean bnwFlag = false;
    ImageView imageView;
    String filePhotoPath = null;
    List<Bitmap> listImagesStored = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        filePhotoPath = getIntent().getStringExtra(GridActivity.PATH_KEY);
        if (filePhotoPath != null) {
            setImage();
        }
    }

    private void setImage() {
        imageView = (ImageView) findViewById(R.id.imgPreviewView);
        Bitmap bImage = BitmapFactory.decodeFile(filePhotoPath);
        imageView.setImageBitmap(bImage);
    }

    public void quitActivityPreview(View view) {
        finish();
    }


    public void saveNewImage(View view) {
        if (filePhotoPath != null)
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(filePhotoPath));
                final boolean saved = ((BitmapDrawable) imageView.getDrawable()).
                        getBitmap().compress(Bitmap.CompressFormat.JPEG,
                        100, fileOutputStream);
                Toast.makeText(this, "Photo Stored at " +
                        filePhotoPath, Toast.LENGTH_LONG).show();
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void rotateClick(View view) {
        rotateImage(90);
    }

    private void rotateImage(int i) {
        Bitmap myImg = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        listImagesStored.add(myImg);
        Bitmap   newImage = rotateImage(myImg.copy(myImg.getConfig(),true), i);
        imageView.setImageBitmap(newImage);
    }

    public static Bitmap rotateImage(Bitmap myImg, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                matrix, true);
        return rotated;
    }

    public void bnwClick(View view) {
        if(bnwFlag==false) {
            Bitmap myImg = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            listImagesStored.add(myImg);
            imageView.setImageBitmap(getBWImage(myImg));
            bnwFlag=true;
        }
    }

    private Bitmap getBWImage(Bitmap originalImage)
    {
        Bitmap output = Bitmap.createBitmap(originalImage.getWidth(),originalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(output);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        Paint p = new Paint();
        p.setColorFilter(filter);

        c.drawBitmap(originalImage,0,0,p);
        return output;
    }

    public void undoClick(View view) {
        if (!listImagesStored.isEmpty()) {
            Bitmap lastImage = listImagesStored.get(listImagesStored.size() - 1);
            imageView.setImageBitmap(lastImage);
            listImagesStored.remove(lastImage);
        } else {
            Toast.makeText(this, "This is original Image ", Toast.LENGTH_SHORT).show();
        }
        bnwFlag=false;
    }

}

