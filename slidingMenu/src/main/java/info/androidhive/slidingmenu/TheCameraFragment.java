package info.androidhive.slidingmenu;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    private Bitmap bitmap;
    private Bitmap newImage;
    public static final int GALLERY_REQUEST_CODE = 0;
	public float alpha;
    public ImageView iv;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
        /*
         * Initialize camera view
         */
		 final View content=inflater.inflate(R.layout.fragment_camera, container, false);
		 CameraView cameraView=(CameraView)content.findViewById(R.id.camera);
		 setCameraView(cameraView);

        /*
         * Image overlay view, add the touch listener to it
         */
		 iv = (ImageView) content.findViewById(R.id.test);
		 iv.setOnTouchListener(new Touch());

        /*
         * Call for gallery, to change the overlay
         */
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);

         /*
          * Set up the camera
          */
		 final MyCameraHost test = (MyCameraHost) getHost();


        /*
         * Set up the seekbar for transparency
         */
		 seekBar = (SeekBar) content.findViewById(R.id.seekBar1);
		 seekBar.setMax(100);
		 seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		 int progress = 0;
			  @Override
			  public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				  progress = progresValue;
                  alpha = (float) progress / 100;
				  iv.setAlpha(alpha);
			  }
			
			  @Override
			  public void onStartTrackingTouch(SeekBar seekBar) {
			  }
			
			  @Override
			  public void onStopTrackingTouch(SeekBar seekBar) {
			  }
		 });

         /*
          * Take photo button
          * One of the problems is rotation orientation
          * (1) With orientation turned on - Phone crashes, because of fragment restart
          * (2) With orientation turned off - Phone does not crash, but get orientation does not work properly. The camera library picks up orientation properly, you'll get inaccurate orientation
          */
		 Button takePicture = (Button) content.findViewById(R.id.button1);
		 takePicture.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	  //Toast.makeText(getActivity(), "alpha is" + alpha, Toast.LENGTH_LONG).show();
                  iv.setDrawingCacheEnabled(true);
            	  iv.buildDrawingCache();
            	  bitmap = iv.getDrawingCache(); // Take drawingCache of iv image
            	  bitmap = makeTransparent(bitmap,alpha); // Add transparency to the image

            	  Matrix matrix = new Matrix(); // Create a new matrix for iv/bitmap image so it will fit the camera image, a temporary fix
                  matrix.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, 1944, 2592), Matrix.ScaleToFit.FILL);
                  newImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            	  //content.setDrawingCacheEnabled(false);

            	  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                  newImage.compress(Bitmap.CompressFormat.PNG, 40, bytes);
            	  File f = new File(Environment.getExternalStorageDirectory() + File.separator + "photo2.png");
            	  try {
					f.createNewFile();
	            	  FileOutputStream fo = new FileOutputStream(f);
	            	  fo.write(bytes.toByteArray()); 
	            	  fo.close();
					} catch (IOException e) {
						e.printStackTrace();
				    }

            	  test.overlayImage = newImage;
            	  PictureTransaction pictureToken = new PictureTransaction(test);
            	  takePicture(pictureToken);
            	  Toast.makeText(getActivity(), "Picture Taken", Toast.LENGTH_LONG).show();
             }
         });
		 
		 
		 return(content);
    }

    /*
     * Transparency method
     */
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

    /*
     *  Ask for access to gallery
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == GALLERY_REQUEST_CODE) {

            Uri selectedImageUri = data.getData();
            String[] fileColumn = { MediaStore.Images.Media.DATA };

            Cursor imageCursor = getActivity().getContentResolver().query(selectedImageUri,
                    fileColumn, null, null, null);
            imageCursor.moveToFirst();

            int fileColumnIndex = imageCursor.getColumnIndex(fileColumn[0]);
            String picturePath = imageCursor.getString(fileColumnIndex);

            Bitmap pictureObject = BitmapFactory.decodeFile(picturePath);

            iv.setImageBitmap(pictureObject);
        }
    }
}
