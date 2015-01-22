package com.example.playbacktexture;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;

//http://www.mailinglistarchive.com/html/android-group-japan@googlegroups.com/2013-01/msg00229.html

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener {
	private static final String TAG = "MyActivity";

	private static final String RIGHT_MP4_FILE  = "Movies/r.MP4";
	private static final String VIDEO_URL = 
			System.getenv("EXTERNAL_STORAGE") + "/" + RIGHT_MP4_FILE;
	private TextureView textureView;
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

        textureView = new TextureView(this);
		textureView.setSurfaceTextureListener(this);
		
		textureView.setRotation(0);
/*		dig = (float) 5;
		textureView.setRotation(dig*0);
		textureView.setRotationX(dig*6);
		textureView.setRotationY(dig*9);
*/
		setContentView(textureView);
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
		Log.i(TAG, "enter onSurfaceTextureAvailable()");

		MediaPlayer player = new MediaPlayer();
		player.setLooping(true);

		Surface surface = new Surface(surfaceTexture);
		player.setSurface(surface);

		try {
			player.setDataSource(VIDEO_URL);
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
		textureView.setRotation(dig*0);
		textureView.setRotationX(dig*6);
		textureView.setRotationY(dig*9);
       /* */
    }

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { 
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		return false;
	}
}