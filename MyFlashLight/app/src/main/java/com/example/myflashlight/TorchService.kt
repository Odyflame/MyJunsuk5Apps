package com.example.myflashlight

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TorchService : Service() {//서비스 클래스 상속받는다

    //TorchService가 Torch클래스를 사용해야 한다. torch 클래스의 인스턴스를 얻는 방법에는 onCreate()
    // 콜백 메서드를 사용하는 방법과 by lazy를 사용하는 방법이 있다. onCreate는 코드가 더 길어지기 때문에
    // by lazy를 사용해서 초기화 지연 방법을 사용했다. 이 방법을 사용하면 Torch 객체를 처음 사용할 때 초기화한다.
    //
    private val torch : Torch by lazy{
        Torch(this)
    }

    private var isRunning = false

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //외부에서 startSErvice() 메서드로 TrochService를 호출하면 onStartCommand()콜백 서비스가 호출된다.
        //보통 인텐트에 action값을 설정하여 호출하는데 "on", "off", 문자열을 액션으로 받았을 때 각각 플래시를
        //끄고 키는 동작을 하도록 코드 작성
        when(intent?.action) {
            //앱에서 실행할 경우
            "on" ->{torch.flashon(); isRunning = true}
            "off" -> { torch.flashoff(); isRunning = false }

            //서비스에서 실행할 경우
            else -> {
                isRunning=!isRunning
                if(isRunning) torch.flashon()
                else torch.flashoff()
            }
        }
        return super.onStartCommand(intent, flags, startId)
        // 서비스는 메모리 부족 이유로 강제종료될수 있다.
        //onStartCommand메서드는 다음 중 하나를 반환한다,
        //start_sticky : null 인텐트로 다시 시작 명령을 실행하지는 않지만 무기한으로 실행 중이며
        //작업을 기다리고 있는 미디어 플레이어와이ㅡ 비슷한 경우로 적합하다.
        //start_not_sticky : 다시 시작하지 않음
        //start_redeliver_intent 마지막 인텐트로 다시 시작 능동적으로 수행 중인 파일 다운로드와 같은 서비스에 적합
        //일반적으로 start_sticky를 반환한다.
    }
}
