package com.example.myflashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

class Torch (context : Context){

    private var cameraId :String? = null//카메라를 키고 끌 때 카메라 id가 필요하다.
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    //getSystemService 메서드는 안드로이드 시스템에서 제공하는 각종 서비스를 관리하는 매니저 클래스를 생성한다.
    //인자로 context 클래스에 정의된 서비스를 정의한 상수를 지정한다. 이 메서드는 object를 반환하기 때문에
    // as 연산자를 사용하여 CameraService형으로 형변환을 한다.

    init {
        cameraId = getCameraID()
    }

    fun flashon(){

        val temp: String? = cameraId
        temp?.let{
            cameraManager.setTorchMode(temp, true)
        }
    }

    fun flashoff(){

        var temp: String? = cameraId
        temp?.let{ cameraManager.setTorchMode(temp, false)}

    }

    private fun getCameraID() : String? {//카메라의 id를 얻는 메서드
        val cameraIds = cameraManager.cameraIdList//카메라가 가진 모든 정보를 제공

        for(id in cameraIds){
            val info = cameraManager.getCameraCharacteristics(id)
            val flashAvailabe = info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            val lensFacing = info.get(CameraCharacteristics.LENS_FACING)
            if(flashAvailabe != null
                && flashAvailabe
                && lensFacing != null
                && lensFacing == CameraCharacteristics.LENS_FACING_BACK){
                return id
            }
        }
        return null
    }
}