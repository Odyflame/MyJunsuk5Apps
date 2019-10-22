package com.example.mygallery

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            //권한이 허용되지 않음

            //shouldShowRequestPermissionRationale 메서드는 사용자가 전에권한 요청을 거부했는지를 반환한다. true를 반환하면 거부한 적이 있는것
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                //이전에 이미 권한이 거부되었을 때 설명
                alert ("사진 정보를 얻으려면 외부 저장소 권한이 필요합니다.", "권한이 필요한 이유"){
                    yesButton {
                        //권한 요청
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton {

                    }
                }.show()

            }else{

                //권한 요청
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)

            }
        }else{

            //권한이 이미 허용됨
           getAllPhotos()

        }
    }

    private fun getAllPhotos(){

        //모든 정보 불러오기
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")//찍은 날짜 내림차순

        //사진 정보를 가지고 있는 Cursor 객체는 내부적으로 데이터를 이동하는 포인터를 가지고 있어서 moveToNext()메서드로 다음 정보로 이동하고 그 결과를 true로 반환한다.
        //While 문을 사용하면 모든 데잍를 순회할 수 있다. 만약 사진이 없다면 Cursor 객체는 null이다.

        //프래글먼트를 아이템으로 하는 ArrayList를 생성
        val fragments = ArrayList<PhotoFragment>()

        if(cursor!=null){
            while(cursor.moveToNext()){
                //사진 정보 url가져오기
                //사진의 경로가 저장된 데이터베이스의 컬럼명은 DATA상수에 정의되어 있다. getColumImdexOrThrow 메서드를 사용하면 해당 컬럼이 몇 번째 인덱스인지 알수있다.
                //getString 메서드에 그 인덱스를 전달하면 해당하는 데이터를 String타입으로 반환한다.
                //이것이 uri 즉 사진이 저장된 위치의 경로가 된다.
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                Log.d( "MainActivity", uri)

                //사진을 Cursor객체로부터 가져올 때마다 PhotoFragment.newInstance(uri)로 Freagment를 생성하면서
                //fragments 리스트에 추가한다.
                fragments.add(PhotoFragment.newInstance(uri))
            }

            //커서 객체를 더 이상 사용하지 않을 때에는 close메서드로 닫아야 한다. 닫지 않으면 메모리 누수 발생
            cursor.close()
        }

        //어댑터
        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.updateFragment(fragments)
        ViewPager.adapter = adapter

        //3초마다 자동슬라이드
        timer(period = 3000){
            runOnUiThread {//timer가 백그라운드 스레드로 동작해 ui를 변경하도록 runOnUiThread로 코드를 감쌉니다.
                if(ViewPager.currentItem < adapter.count - 1){
                    ViewPager.currentItem = ViewPager.currentItem + 1
                }else{
                    ViewPager.currentItem = 0
                }
            }
        }
    }

    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){

                    //권한 허용됨
                    getAllPhotos()
                }else{
                    //권한 거부됨
                    toast("권한 거부됨")

                }
                return
            }
        }
    }

}
