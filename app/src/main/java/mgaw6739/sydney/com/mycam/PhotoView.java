package mgaw6739.sydney.com.mycam;

        import android.content.Context;
        import android.content.res.Configuration;
        import android.hardware.Camera;
        import android.util.Log;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;

        import java.io.IOException;

public class PhotoView extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder surfaceHolder;

    public PhotoView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        int orient = this.getResources().getConfiguration().orientation;
        if (orient == Configuration.ORIENTATION_LANDSCAPE) {
            setOrientation("landscape", 0, 0);
        } else {
            setOrientation("portrait", 90, 90);
        }
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.e("Camera", String.valueOf(e));
        }
    }

    public void setOrientation(String name, int rotation, int displayOrientation) {
        Camera.Parameters camaraParam = camera.getParameters();
        camaraParam.set("orientation", name);
        camaraParam.setRotation(rotation);
        camera.setDisplayOrientation(displayOrientation);
    }
}


