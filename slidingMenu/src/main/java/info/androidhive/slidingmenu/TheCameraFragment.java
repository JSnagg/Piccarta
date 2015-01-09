package info.androidhive.slidingmenu;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.WindowManager;
import com.commonsware.cwac.camera.CameraFragment;
import com.commonsware.cwac.camera.CameraView;
import com.commonsware.cwac.camera.MyCameraHost;
import com.commonsware.cwac.camera.PictureTransaction;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class TheCameraFragment extends CameraFragment
{
	private SeekBar seekBar;
	public float newVal;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		 final View content=inflater.inflate(R.layout.fragment_camera, container, false);
		 CameraView cameraView=(CameraView)content.findViewById(R.id.camera);
		 setCameraView(cameraView);
		 
		 //Test for image overlay
		 final ImageView iv = (ImageView) content.findViewById(R.id.test); 
		 iv.setOnTouchListener(new Touch());
		 		 
		 final MyCameraHost test = (MyCameraHost) getHost();
		 seekBar = (SeekBar) content.findViewById(R.id.seekBar1);
		 seekBar.setMax(100);
		 seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			  int progress = 0;
			  
			  @Override
			  public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				  progress = progresValue;
				  newVal = (float) progress / 100;
				  iv.setAlpha(newVal);
			  }
			
			  @Override
			  public void onStartTrackingTouch(SeekBar seekBar) {
			  }
			
			  @Override
			  public void onStopTrackingTouch(SeekBar seekBar) {
			  }
		 });
		 Button takePicture = (Button) content.findViewById(R.id.button1);
		 takePicture.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // Perform action on click   
            	  Bitmap bitmap;
            	  Bitmap rotated;

                 //For testing
            	  Toast.makeText(getActivity(), "newVal is" + newVal, Toast.LENGTH_LONG).show();
            	  iv.buildDrawingCache();
            	  bitmap = iv.getDrawingCache();
            	  bitmap = makeTransparent(bitmap,newVal);
            	
            	  
            	  
            	  Matrix matrix = new Matrix();
            	  Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            	  int rotation = display.getRotation();
            	  matrix.postRotate(rotation);
            	  rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            	  
            	  //content.setDrawingCacheEnabled(true);
            	  //bitmap = Bitmap.createBitmap(content.getDrawingCache());
            	  content.setDrawingCacheEnabled(false);
            	  
            	  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            	  rotated.compress(Bitmap.CompressFormat.PNG, 40, bytes);
            	  File f = new File(Environment.getExternalStorageDirectory() + File.separator + "photo2.png");
            	  try {
					f.createNewFile();
	            	  FileOutputStream fo = new FileOutputStream(f);
	            	  fo.write(bytes.toByteArray()); 
	            	  fo.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	  

            	  test.overlayImage = rotated;
            	  
            	  
            	  PictureTransaction xact = new PictureTransaction(test);
            	  takePicture(xact);
            	  Toast.makeText(getActivity(), "Picture Taken", Toast.LENGTH_LONG).show();
             }
         });
		 
		 
		 return(content);
    }

	public Bitmap makeTransparent(Bitmap src, float value) {  
		float alphaVal = value * 100;
		int paintAlpha = (int) alphaVal + 75;
	    int width = src.getWidth();
	    int height = src.getHeight();
	    Bitmap transBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    Canvas canvas = new Canvas(transBitmap);
	    canvas.drawARGB(0, 0, 0, 0);
        // config paint
	    final Paint paint = new Paint();
	    paint.setAlpha(paintAlpha);
	    canvas.drawBitmap(src, 0, 0, paint);
	    return transBitmap;
	}
}
