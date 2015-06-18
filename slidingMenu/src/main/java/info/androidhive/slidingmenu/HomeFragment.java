package info.androidhive.slidingmenu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

public class HomeFragment extends Fragment {
	View rootView;
	GoogleMap map;
	private boolean mVisible = true;
	
	public HomeFragment(){
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null)
		{
			mVisible = savedInstanceState.getBoolean("mVisible");
		}
	    if (!mVisible) {
	        getFragmentManager().beginTransaction().hide(this).commit();
	    }
		
		if(rootView == null)
		{
			rootView = inflater.inflate(R.layout.fragment_home, container, false);
			if(map == null)
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		
		map.setMyLocationEnabled(true);
		LatLng pos = new LatLng(43.658039, -79.377643);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 18));
		map.addMarker(new MarkerOptions().title("Put Tag Here").snippet("Put Description Here").position(pos).draggable(true));


        try {
            String urlString = "http://serv-piccarta.rhcloud.com/login.php?username=julian2&password=julian2";
            URL url = new URL(urlString.toString());
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                Toast.makeText(getActivity(), "Logged in", Toast.LENGTH_LONG).show();
            } catch (IOException i){
               //System.out.println(i.getMessage());
                Toast.makeText(getActivity(), "Unable to log in", Toast.LENGTH_LONG).show();
            }
        } catch (MalformedURLException e) {
            //System.out.println("The URL is not valid.");
            //System.out.println(e.getMessage());
            Toast.makeText(getActivity(), "Unable to log in", Toast.LENGTH_LONG).show();
        }


        return rootView;
    }
	
	@Override
	public void onHiddenChanged(boolean _hidden) {
	    super.onHiddenChanged(_hidden);
	    mVisible = !_hidden;
	}
	@Override
	public void onSaveInstanceState(Bundle _outState) {
	    super.onSaveInstanceState(_outState);
	    if (_outState!=null) {
	        _outState.putBoolean("mVisible", mVisible);
	    }
	}
	
}


