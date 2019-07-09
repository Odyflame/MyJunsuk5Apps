package com.example.mymapsactivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //locationinit에서 초기화한다.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val REQUEST_ACCESS_FINE_LOCATION = 1000

    //PolyLine 옵션 객체 생성
    //선의 굵기, 색상 설정가능
    private val polyLineOptions = PolylineOptions().width(5f).color(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //화면이 꺼지지 않게 하기
        window.addFlags((WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON))
        //세로 모드로 화면 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment를 가져와서 지도가 준비되면 알림을 받는다
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationInit()
    }

    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationCallback = MyLocatoinCallBack()

        locationRequest = LocationRequest()

        //GPS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY//가장 정확한 위치를 요청한다,
        //업데이트 인터벌
        //위치 정보가 없을 때는 업데이트 안 함
        //상황에 따라 짧아질 수 있음, 정확하지 않음
        //다른 앱에서 짧은 인터벌로 위치 정보를 요청하면 짧아질 수 있음
        locationRequest.interval = 10000
        //정확함. 이것보다 짧은 업데이트는 하지 않음
        locationRequest.fastestInterval = 5000
    }

    override fun onResume() {
        super.onResume()

        //권한 요청
        permissionCheck(cancel = {//사용자가 이전에 권한 요청을 거부했을 때
            //위치 정보가 필요한 이유 다이얼로그 표시
            showPermissinInfoDialog()
        }, ok = { //사용자가 권한을 수락했을 때 호출
            //현재 위치를 주기적으로 요청(권한이 필요한 부분)
            addLocationListener()
            //액티비티가 활성화하는 onResume 메서드에서 수행하며 addLocationListener와 같이 별도의 메서드로 작성한다.
        })

    }

    @SuppressLint("MissingPermission")
    //권한 요청 코드를 제대로 작성했지만 별도의 메서드로 해당 코드 블록을 분리하면 안드로이드 스튜디오가 에러로 판단한다.
    private fun addLocationListener(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    //requestLocationUpdates에서 전달되는 인자 중 LocationCallback을 구현한 내부 클래스는 locationResult 객체를 반환하고
    //lastLocation 프로퍼티로 Location객체를 얻는다
    inner class MyLocatoinCallBack : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)

           val location = p0?.lastLocation

            //기기의 GPS설정이 꺼져 있거나 현재 위치 정보를 얻을 수 없는 경우에 LOCATIOM객체가 null일 수 있다.
            //location 객체가 null이 아닐 때 해당 위도와 경도 위치로 카메라를 이동한다.
            location?.run{
                //14 level로 확대하고 현재 위치로 카메라 이동
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                Log.d("MapsActivity", "위도: $latitude, 경도 : $longitude")

                //PolyLine에 좌표 추가
                polyLineOptions.add(latLng)

                //PolyLine 그리기
                mMap.addPolyline(polyLineOptions)
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /**
     * 사용 가능한 맵을 조작한다
     * 지도를 사용할 준비가 되면 이 콜백이 호출된다.
     * 여기서 카메라를 이동하거나 청취자를 추가, 마커나 선을 추가살 수 있다.
     * 우리는 단지 오스트레일리아의 시드니를 추가했다.
     * 만약 구글 플레이서비스가 장치를 설치하지 않았다면 유저는 SupportMapFragment안에 시도해야 할 것이다.
     * 이 메서드는 사용자가 google play service를 설치하고 앱으로 돌아온 후에만 호출된다.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap //지도가 준비되면 googleMap 객체를 얻습니다.

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val seoul = LatLng(37.541, 126.986)
        mMap.addMarker(MarkerOptions().position(seoul).title("Marker in Seoul"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul))
    }

    private fun permissionCheck(cancel: () ->Unit, ok : ()->Unit){//함수 인자 두개를 받는다
        //위치 권한이 있는지 검사
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //권한이 허용되지 않음
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                //이전에 한 번 거부했을 경우 실행할 함수
                cancel()
            }else{
                //권한 요청
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
            }
        }else{
            //권한을 수락했을 때 실행
            ok()
        }
    }

    private fun showPermissinInfoDialog() {
        alert ("현재 위치 정보를 얻으려면 위치 권한이 필요하다.", "위치 권한이 필요한 이유"){
            yesButton {
                ActivityCompat.requestPermissions(
                    this@MapsActivity,//requestPermissions의 첫번째 인자는 conText또는 Activity를 전달, this는 DialogInterface를 나타낸다
                    //액티비티를 명시적으로 나타낼려면 this@MapsActivity로 작성해야 한다.
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION)

            }
            noButton {  }//do nothing
        }.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_ACCESS_FINE_LOCATION -> {
                if(grantResults.isNotEmpty() && PackageManager.PERMISSION_GRANTED == grantResults[0]){
                    //권한 허용됨
                    addLocationListener()
                }else{
                    //권한 거부
                    toast("권한 거부")
                }

                return
            }
        }
    }

    //액티비티 생명주기에 따라 앱이 동작하지 않을 때 위치 정보 요청을 삭제해야 한다면 onPause메서드에서 한다.
    override fun onPause() {
        super.onPause()

        //위치 요청 취소
        removeLocationListener()
    }

    private fun removeLocationListener() {
        //현재 위치 요청을 삭제
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}
