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

	private static final String MP4_FILE1  = "video/video.mp4";
	private static final String VIDEO1_URL =
			System.getenv("EXTERNAL_STORAGE") + "/" + MP4_FILE1;
	private static final String MP4_FILE2  = "video/video.mp4";
	private static final String VIDEO2_URL =
			System.getenv("EXTERNAL_STORAGE") + "/" + MP4_FILE2;
	private TextureView textureView1, textureView2;

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

        textureView1 = (TextureView) findViewById(R.id.textureView1);
        textureView1.setSurfaceTextureListener(this);
        textureView1.setRotation(dig);

		textureView2 = (TextureView) findViewById(R.id.textureView2);
		textureView2.setSurfaceTextureListener(this);
		textureView2.setRotation(dig);
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
		Log.i(TAG, "enter onSurfaceTextureAvailable() " + surfaceTexture);

		MediaPlayer player = new MediaPlayer();
		player.setLooping(true);

		Surface surface = new Surface(surfaceTexture);
		player.setSurface(surface);

		try {
            if(surfaceTexture==textureView1.getSurfaceTexture()) {
                player.setDataSource(VIDEO1_URL);
            } else {
				player.setDataSource(VIDEO2_URL);
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

        //textureView1.setRotation(dig*1);
        //textureView1.setRotationX(dig*1);
        textureView1.setRotationY(dig* 1);

		//textureView2.setRotation(dig*1);
		//textureView2.setRotationX(dig*1);
		textureView2.setRotationY(dig*-1);
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { 
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		return false;
	}
}
