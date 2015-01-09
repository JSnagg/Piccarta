package info.androidhive.slidingmenu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
 
public class Splash extends Activity {
 
	/*loads the splash art sequence for set amount of time every time app opens*/
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final ImageView iv = (ImageView) findViewById(R.id.imageView1);
        iv.setBackgroundResource(R.animator.animation);
		AnimationDrawable animator = (AnimationDrawable) iv.getBackground ();
		animator.setCallback(iv);
		animator.setVisible(true, true);
		animator.start();

        Thread background = new Thread() 
        {
            public void run() 
            {  
                try 
                {
                    sleep(5000);
                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),MainActivity.class);
                    
                    startActivity(i);
                     
                    //Remove activity
                    finish();   
                } 
                
                catch (Exception e) 
                {
                }
            }
        };
         
        // start thread
        background.start();   
    }
     
    
    
    @Override
    protected void onDestroy() {
         
        super.onDestroy();
         
    }
}