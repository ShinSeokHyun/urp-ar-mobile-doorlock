package ar.app.exam06;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import ar.app.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LocationOverlayActivity extends MapActivity {
	private GeoPoint geoPoint;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	};	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		
		//MapView ����
		MapView mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		
		//���� ��ǥ
		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra("latitude", 0);
		double longitude = intent.getDoubleExtra("longitude", 0);
		geoPoint = new GeoPoint((int)(latitude*1E6), (int)(longitude*1E6));
		
		//MapView�� �߾� ��ǥ
		MapController mapController = mapView.getController();
		mapController.setZoom(16);			
		mapController.setCenter(geoPoint);	
		
		//�������� �߰�
		List<Overlay> overlays = mapView.getOverlays();	
		overlays.add(new DrawOverlay());	
	}
	
	private class DrawOverlay extends Overlay {
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			//���� ��ǥ�� �ȼ� ��ǥ�� ��ȯ
			Projection projection = mapView.getProjection();
			Point point = new Point();
			projection.toPixels(geoPoint, point);			
			if(shadow == false) {
				//���������� ���� ������ �����
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location);	
				canvas.drawBitmap(bitmap, point.x, point.y, null);
			}
		}
	}
}

