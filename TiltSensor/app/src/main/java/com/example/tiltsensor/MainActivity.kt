package com.example.tiltsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        
        //센서값이 변경되면 호출됨
        //valuse[0] : x축 값이 위로 기울이면 -10~0, 아래롤 기울이면 0~10
        //values[1] : y축 값이 왼쪽으로 기울이면 -10~0 오른쪽으로 기울이면 0~10
        //values[2] : z축은 미사용 화면을 바깥쪽을 가리킨다.
        
        event?.let {
            //Log.d는 디버깅용 로그를 표시할 때 사용한다. 이밖에도 Log.e, Log,w, Log.i, Log.v가 있다.
            Log.d("MainActivity", "onSensorChanged : x :" + "${event.values[0]}, y : ${event.values[1]}, z : ${event.values[2]}")
            //Log 사용방법 : Log(태그, 메시지) 태그는 필터링 시 사용 , 메세지는 출력할 메세지 작성

            tiltView.onSensorEvent(event)
        }
    }

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private lateinit var tiltView: TiltView // 늦은 초기화 선언을 한다.

    override fun onCreate(savedInstanceState: Bundle?) {

        //화면이 꺼지지 않게 하기
        //window.addFlags에는 액티비티 상태를 지정하는 여러 플래그를 설정할 수 있다.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //화면이 가로 모드로 고정되게 하기
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE

        super.onCreate(savedInstanceState)

        tiltView = TiltView(this)//생성자에 this를 넘겨주면서 tiltView를 초기화

        setContentView(tiltView)
    }

    override fun onResume() {
        super.onResume()

        //registerListener로 사용할 센서를 등록한다. 첫 번째 인자는 센서값을 받을 SensorEventlistener이다.
        //여기서는 this를 지정하여 액티비티에서 센서값을 받도록 한다.
        sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),//사용할 센서 종류 지정
            SensorManager.SENSOR_DELAY_FASTEST)//센서값을 얼마나 자주 받을지 시정
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}

