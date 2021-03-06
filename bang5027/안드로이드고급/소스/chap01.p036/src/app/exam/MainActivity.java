package app.exam;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MainActivity extends Activity {	
	private TextView txtInfo;
	private LocationManager locationManager;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		txtInfo = (TextView) findViewById(R.id.txtInfo);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}
	
	private LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			locationManager.removeUpdates(locationListener);
			double latitude =  location.getLatitude();
			double longitude = location.getLongitude();
			getAddress(latitude, longitude);
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};
	
	private void getAddress(final double latitude, final double longitude) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Geocoder geocoder = new Geocoder(MainActivity.this);
				List<Address> addressList = null;
				try {
					addressList = geocoder.getFromLocation(latitude, longitude, 1);
				} catch(IOException e) {
					e.printStackTrace();
				}
				Message msg = Message.obtain();
				msg.obj = addressList;
				handler.sendMessage(msg);
			}
		};
		thread.start();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			List<Address> addressList = (List<Address>)msg.obj;
			if(addressList != null && addressList.size() != 0) {
				Address address = addressList.get(0);
				String fullAddress = "";
				int maxAddressLineIndex = address.getMaxAddressLineIndex();
				for(int i=0; i<=maxAddressLineIndex; i++) {
					fullAddress += address.getAddressLine(i);
				}
				txtInfo.append("전체주소: " + fullAddress + "\n");				
				txtInfo.append("국가이름: " + address.getCountryName() + "\n");	
				txtInfo.append("국가코드: " + address.getCountryCode() + "\n");	
				txtInfo.append("도: " + address.getAdminArea() + "\n");			
				txtInfo.append("시: " + address.getLocality() + "\n");
				txtInfo.append("구: " + getGoo(fullAddress) + "\n");
				txtInfo.append("동,면,읍,로: " + address.getThoroughfare() + "\n");
				txtInfo.append("번지: " + address.getFeatureName() + "\n");		
				txtInfo.append("우편번호: " + address.getPostalCode() + "\n");
			} else {
				txtInfo.append("주소를 찾지 못했습니다.");
			}
		}
	};
	
	private String getGoo(String fullAddress) {
		StringTokenizer st = new StringTokenizer(fullAddress, " ");
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if(token.substring(token.length()-1).equals("구")) {
				return token;
			}
		}
		return "";
	}

}