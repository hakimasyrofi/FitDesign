package com.fitdesgin

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationArrayList: ArrayList<LatLng>
    private lateinit var locationNameList: ArrayList<String>
    private lateinit var locationInfoList: ArrayList<String>

    // Get current location
    private lateinit var lastLocation: Location
    private lateinit var fusedLOcationClient: FusedLocationProviderClient

    private lateinit var db: FirebaseFirestore
    val bottomSheetFragment = BottomSheetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        db = FirebaseFirestore.getInstance()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationArrayList = ArrayList()
        locationNameList = ArrayList()
        locationInfoList = ArrayList()

        // Get current location
        fusedLOcationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Get current location
        mMap.uiSettings.isZoomControlsEnabled = true

        setUpMap()

        db.collection("maps").get().addOnCompleteListener{ task->
            if (task.isSuccessful) {
                for (document in task.result!!){
                    val geoPoint: GeoPoint? = document.getGeoPoint("geoPoint")
                    Log.d(TAG, "google maps firebase")
                    locationArrayList!!.add(LatLng(geoPoint!!.getLatitude(), geoPoint!!.getLongitude()))
                    locationNameList.add(document.getString("name").toString())

                    val address = document.getString("address").toString()
                    val phoneNumber = document.getString("phoneNumber").toString()
                    val experience = document.getString("experience").toString()
                    val price = document.getString("price").toString()

                    locationInfoList.add(address + "@" + phoneNumber + "@" + experience + "@" + price)

//                    Log.d(TAG, geoPoint!!.getLatitude().toString())
//                    Log.d(TAG, document.getString("name").toString())
//                    Log.d(TAG, address + "@" + phoneNumber + "@" + experience + "@" + price)
                }
            }

        for (i in locationArrayList!!.indices){
            mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title(locationNameList!![i]).snippet(locationInfoList!![i])
                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_icon_maps)))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10f))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList!!.get(i), 10f))
        }


        mMap.setOnMarkerClickListener{ marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
                Log.d(TAG, "Di klik nih")
            } else {
                //marker.showInfoWindow()
                Log.d(TAG, marker.snippet)
                val listInfoAddress: List<String> = marker.snippet.split("@").toList()
                val bundle = Bundle()
                bundle.putString("title", marker.title)
                bundle.putString("address", listInfoAddress[0])
                bundle.putString("phoneNumber", listInfoAddress[1])
                bundle.putString("experience", listInfoAddress[2])
                bundle.putString("price", listInfoAddress[3])
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
            }
            true
        }
        // Add a marker in Sydney and move the camera
//        val penjahit1 = LatLng(-6.894786837110298, 107.59121561729269)
//        mMap.addMarker(MarkerOptions().position(penjahit1).title("Penjahit 1").snippet("Alamat:, No hanphone: Pengalaman:, Kisaran harga: "))
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(penjahit1, 14f))
    }
}
    // Get current location
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLOcationClient.lastLocation.addOnSuccessListener(this) {
            if (it != null){
                lastLocation = it
                val currentLatLong = LatLng(it.altitude, it.longitude)
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 10f))
//                mMap.addMarker(MarkerOptions().position(currentLatLong).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_circle_maps)))
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 10f))
            }
        }

    }

    fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}