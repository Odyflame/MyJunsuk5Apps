package com.example.myflashlight

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       /* val torch = Torch(this)

        flashSwitch.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                //torch.flashon()
            }else{
                // torch.flashoff()
            }
        }*/

        flashSwitch.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                startService(intentFor<TorchService>().setAction("on"))//anko 버전을 사용한 코드
            }else{
                startService(intentFor<TorchService>().setAction("off"))
            }
        }
    }
}
