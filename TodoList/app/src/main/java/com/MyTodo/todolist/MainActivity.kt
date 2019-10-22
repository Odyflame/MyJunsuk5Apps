package com.MyTodo.todolist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    //첫 번째 액티비티에 realm객체를 초기화하고 해제하는 코드를 작성한다.
    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //전체 할 일 정보를 가져와서 날짜순으로 내림차순 정렬
        val realmResult = realm.where<Todo>().findAll().sort("date", Sort.DESCENDING)
        val adapter = TodoListAdapter(realmResult)//TodoListAdapter 클래스에 할 일 목록인 realmResult를 전달하여 어뎁터 인스턴스 생성

        listView.adapter = adapter//생성한 어뎁터를 리스트뷰에 설정 이것으로 할 일 목록이 리스트 뷰에 표시된다.

        //데이터가 변경되면 어뎁터에 적용
        //addChangeListener데이터가 변경될 때마다 어댑터에 알려줄 수 있다.
        // 어댑터에 notifyDataSetChanged() 메서드를 호출하면 데이터 변경을 통지하여 리스트를 다시 표시하게 된다.
        realmResult.addChangeListener { _->adapter.notifyDataSetChanged() }

        listView.setOnItemClickListener { parent, view, position, id -> startActivity<EditActivity>("id" to id)  }

        //새 할 일 추가
        fab.setOnClickListener{
            startActivity<EditActivity>()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()//realm 해제
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}