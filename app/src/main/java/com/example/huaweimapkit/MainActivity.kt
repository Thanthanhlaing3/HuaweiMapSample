package com.example.huaweimapkit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.huaweimapkit.utils.Constants
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "MapViewDemoActivity"

    //Huawei map view setup
    private val REQUEST_CODE = 1996
    private var hMap: HuaweiMap? = null
    private lateinit var mMapView: MapView
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    private var RUNTIME_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMapView = findViewById(R.id.mapView)

        if (HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(this) == com.huawei.hms.api.ConnectionResult.SUCCESS) {
            var mapViewBundle: Bundle? = null
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
            }

            mMapView.onCreate(mapViewBundle)
        }

        mMapView.getMapAsync(this)

        if (!hasPermissions(this, RUNTIME_PERMISSIONS.toList())) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE)
        }
    }

    private fun hasPermissions(context: Context, permissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onMapReady(map: HuaweiMap?) {
        Log.d(TAG, "onMapReady")
        if (map != null) {
            hMap = map
        }

        //Default yangon latlng
        hMap?.animateCamera(com.huawei.hms.maps.CameraUpdateFactory.newLatLngZoom(
            com.huawei.hms.maps.model.LatLng(16.8409, 96.1735), Constants.MIN_MAP_ZOOM
        ))

    }
}
