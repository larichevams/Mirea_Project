package ru.mirea.laricheva.mireaproject;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.laricheva.mireaproject.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity {

    private MapView mapView = null;
    private ActivityMapBinding binding;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isRightGranted = false;
    MyLocationNewOverlay locationNewOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = binding.mapView;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        int accessCoarseLocationStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFineLocationStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int accessBackgroundLocationStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        if (accessCoarseLocationStatus == PackageManager.PERMISSION_GRANTED
                && accessFineLocationStatus == PackageManager.PERMISSION_GRANTED
                && accessBackgroundLocationStatus == PackageManager.PERMISSION_GRANTED) {
            isRightGranted = true;
            Log.d("Permissions", "true");
        }
        else {
            Log.d("Permissions", "request");
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
            Log.d("Permissions", "true+");
        }

        if (isRightGranted) {

            locationNewOverlay = new MyLocationNewOverlay(new
                    GpsMyLocationProvider(getApplicationContext()), mapView);
            locationNewOverlay.enableMyLocation();
            mapView.getOverlays().add(this.locationNewOverlay);

            locationNewOverlay.runOnFirstFix(new Runnable() {
                @Override
                public void run() {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                IMapController mapController = mapView.getController();
                                mapController.setZoom(12.0);
                                GeoPoint point = new GeoPoint(55.751662,37.619661);
                                mapController.setCenter(point);
                            }
                        });
                    }
                    catch (Exception e){}
                }
            });
        }

            CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(), new
                    InternalCompassOrientationProvider(getApplicationContext()), mapView);
            compassOverlay.enableCompass();
            mapView.getOverlays().add(compassOverlay);

            final Context context = this.getApplicationContext();
            final DisplayMetrics dm = context.getResources().getDisplayMetrics();
            ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
            scaleBarOverlay.setCentred(true);
            scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
            mapView.getOverlays().add(scaleBarOverlay);

            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(55.757343, 37.633494));
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    Toast.makeText(getApplicationContext(), "Рынок на Маросейке", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            mapView.getOverlays().add(marker);
            marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
            marker.setTitle("м. Китай-Город");

            Marker marker2 = new Marker(mapView);
            marker2.setPosition(new GeoPoint(55.742405,37.610207));
            marker2.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker2, MapView mapView) {
                    Toast.makeText(getApplicationContext(), "Serf Coffee", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            mapView.getOverlays().add(marker2);
            marker2.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
            marker2.setTitle("м. Кропоткинская");

            Marker marker3 = new Marker(mapView);
            marker3.setPosition(new GeoPoint(55.768003,37.614892));
            marker3.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker3, MapView mapView) {
                    Toast.makeText(getApplicationContext(), "Sub Zero", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            mapView.getOverlays().add(marker3);
            marker3.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
            marker3.setTitle("м. Трубная");

    }
    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // permission granted
            isRightGranted = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}
