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


