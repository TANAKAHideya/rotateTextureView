package com.example.playbacktexture;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

//http://www.mailinglistarchive.com/html/android-group-japan@googlegroups.com/2013-01/msg00229.html

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener {
	private static final String TAG = "MyActivity";

	private static final String UP_MP4_FILE  = "video/up.MP4";
	private static final String UP_VIDEO_URL =
			System.getenv("EXTERNAL_STORAGE") + "/" + UP_MP4_FILE;
	private TextureView textureViewUp;

    private static final String DOWN_MP4_FILE  = "video/down.MP4";
    private static final String DOWN_VIDEO_URL =
            System.getenv("EXTERNAL_STORAGE") + "/" + DOWN_MP4_FILE;
    private TextureView textureViewDown;

	private float dig=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        /* Set Full screen flag */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* Support Transparent color */
		//getWindow().setFormat(PixelFormat.TRANSPARENT);
		getWindow().setFormat(PixelFormat.RGBX_8888);
        /* No window title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        textureViewUp = (TextureView) findViewById(R.id.textureViewUp);
        textureViewUp.setSurfaceTextureListener(this);
        textureViewUp.setRotation(dig);

        textureViewDown = (TextureView) findViewById(R.id.textureViewDown);
        textureViewDown.setSurfaceTextureListener(this);
        textureViewDown.setRotation(dig);
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
            } else {
                player.setDataSource(UP_VIDEO_URL);
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
		textureViewUp.setRotation(dig*0);
		textureViewUp.setRotationX(dig*0);
		textureViewUp.setRotationY(dig*3);

        textureViewDown.setRotation(dig*0);
        textureViewDown.setRotationX(dig*3);
        textureViewDown.setRotationY(dig*0);
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { 
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		return false;
	}
}
