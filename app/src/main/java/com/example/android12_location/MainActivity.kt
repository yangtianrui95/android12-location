package com.example.android12_location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("MissingPermission", "SetTextI18n")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requireLocationPermissions()

        android_loc_req.setOnClickListener {
            loc_result.text = "定位中"
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0f
            ) { l ->
                runOnUiThread {
                    loc_result.text = "lon: ${l.longitude}, lat: ${l.latitude}"
                }
            }
        }
        gaode_loc_req.setOnClickListener {
            loc_result.text = "定位中"
            val client = AMapLocationClient(this)
            val options = AMapLocationClientOption()
            options.interval = 1000
            client.setLocationOption(options)
            client.setLocationListener { l ->
                runOnUiThread {
                    loc_result.text = "lon: ${l?.longitude}, lat: ${l?.latitude}, err: ${l.errorCode}, erri: ${l.errorInfo}"
                }
            }
            client.startLocation()
        }
    }

    private fun requireLocationPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        requestPermissions(permissions, 100)
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
            loc_result.text = "没有授权"
            Toast.makeText(this, "没有授权", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
        }
    }
}