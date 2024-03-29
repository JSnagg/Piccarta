package info.androidhive.slidingmenu;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class Touch implements OnTouchListener {

 // These matrices will be used to move and zoom image
 Matrix matrix = new Matrix();
 Matrix savedMatrix = new Matrix();

 // We can be in one of these 3 states
 static final int NONE = 0;
 static final int DRAG = 1;
 static final int ZOOM = 2;
 int mode = NONE;

 // Remember some things for zooming
 PointF start = new PointF();
 PointF mid = new PointF();
 float oldDist = 1f;

 float[] lastEvent = null;
 float d = 0f;
 float newRot = 0f;
 private static final String TAG = "MyActivity";
 
 @Override
 public boolean onTouch(View v, MotionEvent event) {
	     ImageView view = (ImageView) v;
	
	     // Dump touch event to log
	     dumpEvent(event);
	
	     // Handle touch events here...
	 switch (event.getAction() & MotionEvent.ACTION_MASK) {
		 case MotionEvent.ACTION_DOWN:
			        savedMatrix.set(matrix);
			    start.set(event.getX(), event.getY());
			    //if (Constant.TRACE) Log.d(TAG, "mode=DRAG");
			    mode = DRAG;
			    lastEvent = null;
			    break;
		    
		 case MotionEvent.ACTION_POINTER_DOWN:
			    oldDist = spacing(event);         
			    savedMatrix.set(matrix);
			    midPoint(mid, event);
			    mode = ZOOM;
			   // if (Constant.TRACE) Log.d(TAG, "mode=ZOOM");
			
			    lastEvent = new float[4];
			    lastEvent[0] = event.getX(0);
			    lastEvent[1] = event.getX(1);
			    lastEvent[2] = event.getY(0);
			    lastEvent[3] = event.getY(1);
			    d = rotation(event);
			    break;
			    
		 case MotionEvent.ACTION_UP:
			 
		 case MotionEvent.ACTION_POINTER_UP:
			    mode = NONE;
			    lastEvent = null;
			    //if (Constant.TRACE) Log.d(TAG, "mode=NONE");         
			    break;
		    
		 case MotionEvent.ACTION_MOVE:
			    if (mode == DRAG) {
			       // ...
			       matrix.set(savedMatrix);
			       matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
			    }
			    else if (mode == ZOOM && event.getPointerCount()==2) {
			       float newDist = spacing(event);
			      // if (Constant.TRACE) Log.d(TAG, "Count=" + event.getPointerCount());
			       //if (Constant.TRACE) Log.d(TAG, "newDist=" + newDist);
			       matrix.set(savedMatrix);
			       if (newDist > 10f) {              
			          float scale = newDist / oldDist;
			          matrix.postScale(scale, scale, mid.x, mid.y);
			       } 
			       if (lastEvent!=null){
			           newRot = rotation(event); 
			           //if (Constant.TRACE) Log.d("Degreeeeeeeeeee", "newRot="+(newRot));
			           float r = newRot-d;
			           matrix.postRotate(r, view.getMeasuredWidth()/2, view.getMeasuredHeight()/2);  
			       }
			    }
			    break;
	 }
	
	 view.setImageMatrix(matrix);
	 return true; // indicate event was handled
 }

 /** Show an event in the LogCat view, for debugging */
 private void dumpEvent(MotionEvent event) {
	  String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
	  StringBuilder sb = new StringBuilder();
	  int action = event.getAction();
	  int actionCode = action & MotionEvent.ACTION_MASK;
	  sb.append("event ACTION_").append(names[actionCode]);
	  if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
		   sb.append("(pid ").append(
		     action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
		   sb.append(")");
	  }
	  sb.append("[");
	  for (int i = 0; i < event.getPointerCount(); i++) {
		   sb.append("#").append(i);
		   sb.append("(pid ").append(event.getPointerId(i));
		   sb.append(")=").append((int) event.getX(i));
		   sb.append(",").append((int) event.getY(i));
		   if (i + 1 < event.getPointerCount())
		    sb.append(";");
	  }
	  sb.append("]");
 }

 /** Determine the space between the first two fingers */
 private float spacing(MotionEvent event) {
	  float x = event.getX(0) - event.getX(1);
	  float y = event.getY(0) - event.getY(1);
	  return FloatMath.sqrt(x * x + y * y);
 }

 /** Calculate the mid point of the first two fingers */
 private void midPoint(PointF point, MotionEvent event) {
	  float x = event.getX(0) + event.getX(1);
	  float y = event.getY(0) + event.getY(1);
	  point.set(x / 2, y / 2);
 }
 
 /** Determine the degree between the first two fingers */
 private float rotation(MotionEvent event) {  
	     double delta_x = (event.getX(0) - event.getX(1));
	     double delta_y = (event.getY(0) - event.getY(1));
	     double radians = Math.atan2(delta_y, delta_x);       
	    // if (Constant.TRACE) Log.d("Rotation ~~~~~~~~~~~~~~~~~", delta_x+" ## "+delta_y+" ## "+radians+" ## " +Math.toDegrees(radians));
	     return (float) Math.toDegrees(radians);
 }
}