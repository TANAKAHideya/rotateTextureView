package com.example.playbacktexture;

import java.io.IOException;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
//import android.view.SurfaceView;
import android.view.TextureView;
//import android.view.Window;
import android.view.WindowManager;
//import android.widget.LinearLayout;
import android.hardware.display.DisplayManager;

//http://www.mailinglistarchive.com/html/android-group-japan@googlegroups.com/2013-01/msg00229.html

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener {
	private static final String TAG = "MyActivity";

	private static final String UP_MP4_FILE  = "video/up.MP4";
	private static final String UP_VIDEO_URL =
			System.getenv("EXTERNAL_STORAGE") + "/" + UP_MP4_FILE;
	private TextureView textureViewUp;

    private static final String MID_MP4_FILE  = "video/mid.MP4";
    private static final String MID_VIDEO_URL =
            System.getenv("EXTERNAL_STORAGE") + "/" + MID_MP4_FILE;
    private TextureView textureViewMid;

    private static final String DOWN_MP4_FILE  = "video/down.MP4";
    private static final String DOWN_VIDEO_URL =
            System.getenv("EXTERNAL_STORAGE") + "/" + DOWN_MP4_FILE;
    private TextureView textureViewDown;

    private static final String RIGHT_MP4_FILE  = "video/right.MP4";
    private static final String RIGHT_VIDEO_URL =
            System.getenv("EXTERNAL_STORAGE") + "/" + RIGHT_MP4_FILE;
    private TextureView textureViewRight;

	private float dig=0;
    private float scale=1;
    private float translation=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        /* Set Full screen flag */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* Support Transparent color */
		//getWindow().setFormat(PixelFormat.TRANSPARENT);
		getWindow().setFormat(PixelFormat.RGBX_8888);
        /* No window title */
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        // Get the display manager service.
        DisplayManager mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays  = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        for (int i=0;i<displays.length;i++){
            DemoPresentation presentation = new DemoPresentation(this, displays[i]);
            presentation.show();
        }

        textureViewUp = (TextureView) findViewById(R.id.textureViewUp);
        textureViewUp.setSurfaceTextureListener(this);
        textureViewUp.setRotation(dig);

        textureViewMid = (TextureView) findViewById(R.id.textureViewMid);
        textureViewMid.setSurfaceTextureListener(this);
        textureViewMid.setRotation(dig);

        textureViewDown = (TextureView) findViewById(R.id.textureViewDown);
        textureViewDown.setSurfaceTextureListener(this);
        textureViewDown.setRotation(dig);

        textureViewRight = (TextureView) findViewById(R.id.textureViewRight);
        textureViewRight.setSurfaceTextureListener(this);
        textureViewRight.setRotation(dig);
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
		Log.i(TAG, "enter onSurfaceTextureAvailable() " + surfaceTexture);

		MediaPlayer player = new MediaPlayer();
		player.setLooping(true);

		Surface surface = new Surface(surfaceTexture);
		player.setSurface(surface);

		try {
            if(surfaceTexture==textureViewDown.getSurfaceTexture()) {
                player.setDataSource(DOWN_VIDEO_URL);
            } else
            if(surfaceTexture==textureViewMid.getSurfaceTexture())  {
                player.setDataSource(MID_VIDEO_URL);
            } else
            if(surfaceTexture==textureViewUp.getSurfaceTexture()) {
                player.setDataSource(UP_VIDEO_URL);
            } else {
                player.setDataSource(RIGHT_VIDEO_URL);
            }
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			player.prepare();
			Log.i(TAG,"Prepared");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.start();
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		dig+=0.1;

        scale -= 0.002;
        if (scale <=0 ) scale=1;

        translation += 0.1;
        if (translation >=320) translation=0;

		textureViewUp.setRotation(dig*0);
		textureViewUp.setRotationX(dig*0);
		textureViewUp.setRotationY(dig*3);
        textureViewUp.setTranslationY(translation);

        textureViewMid.setRotation(dig*3);
        textureViewMid.setRotationX(dig*0);
        textureViewMid.setRotationY(dig*0);
        textureViewMid.setScaleX(scale);
        textureViewMid.setScaleY(scale);

        textureViewDown.setRotation(dig*0);
        textureViewDown.setRotationX(dig*0);
        textureViewDown.setRotationY(dig*3);
        //textureViewDown.setTranslationX(translation);

        textureViewRight.setRotation(dig*1);
        textureViewRight.setRotationX(dig*1);
        textureViewRight.setRotationY(dig*1);
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { 
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		return false;
	}
    /**
     * The presentation to show on the secondary display.
     *
     * Note that the presentation display may have different metrics from the display on which
     * the main activity is showing so we must be careful to use the presentation's
     * own {@link Context} whenever we load resources.
     */
    private final class DemoPresentation extends Presentation {

        public DemoPresentation(Context context, Display display) {
            super(context, display);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // Be sure to call the super class.
            super.onCreate(savedInstanceState);

            // Set the background to a random gradient.
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);

            Point p = new Point();
            getDisplay().getSize(p);
            drawable.setGradientRadius(Math.max(p.x, p.y) / 2);

            final int[] colors = new int[] {
                    ((int) (Math.random() * Integer.MAX_VALUE)) | 0xFF000000,
                    ((int) (Math.random() * Integer.MAX_VALUE)) | 0xFF000000 };
            drawable.setColors(colors);

            findViewById(android.R.id.content).setBackground(drawable);
        }
    }

}
