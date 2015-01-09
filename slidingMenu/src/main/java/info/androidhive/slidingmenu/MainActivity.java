package info.androidhive.slidingmenu;


import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.commonsware.cwac.camera.MyCameraHost;

import info.androidhive.slidingmenu.adapter.NavDrawerListAdapter;
import info.androidhive.slidingmenu.model.NavDrawerItem;

import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MainActivity extends Activity  implements CameraHostProvider{

	public String cameraDir = android.os.Environment.getExternalStorageDirectory() + "/DCIM/Camera";
	public String capturedFile = "";
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private boolean initializeMap = false;

    private Fragment map = null;
    private Fragment photos = null;
    private Fragment camera = null;
    private Fragment profile = null;
	
	private boolean mapS = false;
    private boolean photosS = false;
    private boolean cameraS = false;
    private boolean profileS = false;

    private FragmentManager fragmentManager = getFragmentManager();

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        createDrawer();
		if (savedInstanceState == null) {
			// on first time display view for first nav item
            initializeMap = true;

			map = new HomeFragment();
			photos = new PhotosFragment();
			profile = new CommunityFragment();
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener 
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	@SuppressLint("SimpleDateFormat")
	private void displayView(int position) {
		// update the main content by replacing fragments
		switch (position) {
		case 0:
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case 1:
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			break;
		case 2:
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			break;
		case 3:
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			break;
		default:
			break;
		}

		if (position == 0) //google map
		{
			if(photosS == true)
				fragmentManager.beginTransaction().remove(photos).commit();
			if(cameraS == true)
				fragmentManager.beginTransaction().remove(camera).commit();
			if(profileS == true)
				fragmentManager.beginTransaction().remove(profile).commit();
			if(initializeMap == true)
			{
				fragmentManager.beginTransaction().add(R.id.frame_container,map).commit();
                initializeMap = false;
			}
			else if(initializeMap == false)
			{
				fragmentManager.beginTransaction().show(map).commit();
			}
				
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			mapS = true;
			photosS = false;
			cameraS = false;
			profileS = false;
		} 
		
		else if (position == 1) // gallery
		{
			if(mapS == true)
				fragmentManager.beginTransaction().hide(map).commit();
			if(cameraS == true)
				fragmentManager.beginTransaction().remove(camera).commit();
			if(profileS == true)
				fragmentManager.beginTransaction().remove(profile).commit();

			fragmentManager.beginTransaction().add(R.id.frame_container,photos).commit();

            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);	
            
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			photosS =true;
			cameraS = false;
			mapS = false;
			profileS = false;
		} 
		
		else if (position == 2) // camera
		{
			if(mapS == true)
				fragmentManager.beginTransaction().hide(map).commit();
			if(photosS == true)
				fragmentManager.beginTransaction().remove(photos).commit();
			if(profileS == true)
				fragmentManager.beginTransaction().remove(profile).commit();

            camera = new TheCameraFragment();
			fragmentManager.beginTransaction().add(R.id.frame_container,camera).commit();
			
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			cameraS = true;
			mapS = false;
			profileS = false;
			photosS = false;
		} 
		else if( position == 3) //community
		{
			if(mapS == true)
				fragmentManager.beginTransaction().hide(map).commit();
			if(photosS == true)
				fragmentManager.beginTransaction().remove(photos).commit();
			if(cameraS == true)
				fragmentManager.beginTransaction().remove(camera).commit();

			fragmentManager.beginTransaction().add(R.id.frame_container,profile).commit();
				
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			cameraS = false;
			mapS = false;
			profileS = true;
			photosS = false;
		} 

		else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

    void startUpload(final String filename)
    {
    	Thread thread = new Thread(new Runnable(){
    	    @Override
    	    public void run() {
    	        try {
    	        	UploadFiles u = new UploadFiles();
    	        	u.upload(filename);

    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	    }
    	});
    	thread.start(); 
    }
    
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

        	if (requestCode == 1) {
        		            		
        		// Update file in gallery
        		MediaScannerConnection.scanFile(this,
        		          new String[] { cameraDir + "/" + capturedFile }, null,
        		          new MediaScannerConnection.OnScanCompletedListener() {
        		      public void onScanCompleted(String path, Uri uri) {}
        		 });
        		
        		startUpload(cameraDir + "/" + capturedFile);
        	}
            else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);
                c.close();

                startUpload(picturePath);
            }
        }
	}
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

    private void createDrawer(){
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home/Map
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Gallery
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Camera
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

	@Override
	public CameraHost getCameraHost() {
		return(new MyCameraHost(this));
	}

}
