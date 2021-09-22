package com.ibunda.mitrailifeapps.ui.maps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.ibunda.mitrailifeapps.BuildConfig.MAPS_API_KEY
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.ActivityMapsBinding
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var shops: Shops

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private var lastKnownAddress: String? = null
    private var mapMarker: Marker? = null
    private var shopLocation: LatLng? = null

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val place = result.data?.let { Autocomplete.getPlaceFromIntent(it) }

                    lastKnownLocation!!.latitude =
                        place?.latLng?.latitude ?: defaultLocation.latitude
                    lastKnownLocation!!.longitude =
                        place?.latLng?.longitude ?: defaultLocation.longitude
                    lastKnownAddress = place?.address
                    moveCamera()
                    Log.i(TAG, "Place: ${place?.name}, ${place?.id}, ${place?.latLng}")
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    val status = result.data?.let { Autocomplete.getStatusFromIntent(it) }
                    Log.i(TAG, status?.statusMessage.toString())
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getBooleanExtra(EXTRA_SHOPS_DATA, false)) {
            shops = intent.getParcelableExtra<Shops>(EXTRA_USER_MAPS) as Shops
            lastKnownAddress = shops.address
            binding.btnKonfirmasiLokasi.visibility = View.GONE
            binding.searchLocation.visibility = View.GONE
            getShopLocation()
        }

        // Initialize the SDK
        Places.initialize(applicationContext, MAPS_API_KEY)
        placesClient = Places.createClient(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.searchLocation.setOnClickListener {
            openPlacesApi()
        }

        binding.btnKonfirmasiLokasi.setOnClickListener {
            postNewLocation()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.uiSettings?.isZoomControlsEnabled = true
        map?.uiSettings?.isMapToolbarEnabled = true
        getLocationPermission()

        map?.setOnCameraIdleListener(this@MapsActivity)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCameraIdle() {
        val service = getSystemService(LOCATION_SERVICE) as LocationManager
        val enabled = service
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!enabled) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        if (enabled) {
            if (intent.getBooleanExtra(EXTRA_SHOPS_DATA, false)) {
                if (shopLocation == null) {
                    getShopLocation()
                } else {
                    mapMarker?.remove()
                    mapMarker = map?.addMarker(
                        MarkerOptions()
                            .position(shopLocation)
                            .title(shops.shopName)
                            .snippet(shops.address)
                    )
                    mapMarker?.showInfoWindow()
                }
            } else
                if (locationPermissionGranted) {
                    val cameraPosition = map?.cameraPosition?.target
                    val localeID = Locale("in", "ID")
                    val geocoder = Geocoder(this@MapsActivity, localeID)
                    try {
                        lastKnownAddress =
                            cameraPosition?.let {
                                geocoder.getFromLocation(
                                    it.latitude,
                                    cameraPosition.longitude,
                                    1
                            )[0].getAddressLine(0)
                        }
                } catch (e: IOException) {
                    e.printStackTrace();
                }

                mapMarker?.remove()
                mapMarker = map?.addMarker(
                    MarkerOptions()
                        .position(cameraPosition)
                        .title("Posisi Anda Saat Ini")
                        .snippet(lastKnownAddress)
                )
                mapMarker?.showInfoWindow()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    private fun postNewLocation() {

        val address = lastKnownAddress
        val latitude = lastKnownLocation?.latitude
        val longitude = lastKnownLocation?.longitude

        val intent = Intent()
        intent.putExtra("address", address)
        intent.putExtra("latitude", latitude)
        intent.putExtra("longitude", longitude)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        moveCamera()
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getShopLocation() {
        shopLocation = LatLng(shops.latitude!!, shops.longitude!!)
        map?.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(shopLocation, DEFAULT_ZOOM.toFloat())
        )
        map?.uiSettings?.isMyLocationButtonEnabled = false
        binding.btnKonfirmasiLokasi.visibility = View.GONE
        binding.searchLocation.visibility = View.GONE

    }

    private fun moveCamera() {
        if (lastKnownLocation != null) {
            val latLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    DEFAULT_ZOOM.toFloat()
                )
            )
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            updateLocationUI()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }

    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
                getDeviceLocation()
            } else {
                Toast.makeText(
                    this,
                    "Mohon Setujui Permintaan Akses Lokasi Untuk Menggunakan Fitur Ini",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    private fun openPlacesApi() {
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setCountry("ID")
            .build(this)
        getResult.launch(intent)

    }

    companion object {
        private val TAG = MapsActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 17
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        const val EXTRA_USER_MAPS = "extra_user_maps"
        const val EXTRA_SHOPS_DATA = "extra_shops_data"

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

    }



}