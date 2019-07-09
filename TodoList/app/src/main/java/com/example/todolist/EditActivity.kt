package com.example.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()//인스턴스 얻기
    val calendar :Calendar = Calendar.getInstance() // 날짜를 다룰 캘린더 객체


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //인텐트로부터 데이터를 꺼내 반환한다. name은 아이템을 가리키는 키 defaultValue는 반환되는 값이 없을 때 기본값 설정
        val id = intent.getLongExtra("id", -1L)
        if(id==-1L){
            insertTodo()
        }else{
            updateTodo(id)
        }

        //캘린더 뷰의 날짜를 선택했을 때 calander 객체에 설정
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }


    }

    //추가 모드 초기화 삭제 버튼을 감춘다.
    //뷰를 보이거나 안보이게 할려면 setVisiblilty() 메서드를 사용
    @SuppressLint("RestrictedApi")
    private fun insertMode(){
        //삭제 버튼 감추기
        //GONE = 완전히 보이지 않는다 VISIBLE 보이게 한다. INVISIBLE 영역은 차지하지만 보이지 않는다.
        deleteFab.visibility = View.GONE

        //완료 버튼을 클릭하면 추가
        doneFab.setOnClickListener{ insertTodo() }
    }

    //수정 모드 초기화
    private fun updateMode(id : Long){
        //id에 해당하는 객체를 화면에 표시
        val todo = realm.where<Todo>().equalTo("id", id).findFirst()!!//뭔데 !!붙이는거지지

       todoEditText.setText(todo.title)
        calendarView.date = todo.date

        //완료 버튼을 클릭하면 수정
        doneFab.setOnClickListener{
            updateTodo(id)
        }

        //삭제 버튼을 클릭하면 삭제
        deleteFab.setOnClickListener{ deleteTodo(id)}
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close() // 인스턴스 해제
    }

    private fun insertTodo(){
       realm.beginTransaction() // 트랜잭션 시작
        //realm에서 데이터를 추가, 삭제, 업데이트할 때는 beginTransaction 메서드러 트랜잭션을 실시한다.
        //트랜잭션은 데이터베이스의 작업 단위이다. beginTransaction()메서드와 commitTransaction메서드 사이에 작성한 코드들은
        //전체가 하나의 작업으로 도중에 에러가 나면 모두 취소된다.
        //트랜잭션은 데이터베이스에서 아주 중요한 개념이다. 데이터베이스에서 추가 삭제 업데이트를 할려면 항상 트랜잭션을 시작하고
        //닫아야 합니다.

       val newItem = realm.createObject<Todo>(nextId()) //새 realm 객체 생성
        //createObject<T : RealmModel>(primaryKeyValue : Any?)

        //값 설정
        newItem.title = todoEditText.text.toString()
        newItem.date = calendar.timeInMillis

        realm.commitTransaction() //트랜잭션 종료 반영

        //다이얼로그 표시
        alert("내용이 추가되었습니다"){
            yesButton { finish() }
        }.show()
    }

    //다음 id를 반환
    private fun nextId() : Int{
        //Realm은 기본키 자동 증가기능을 지원하지 않는다. ㄸ라ㅏ서 가장 큰 id값을 얻고 1을 더한 값을 반환하는 메서드를 추가로 작성한다.
        val maxId = realm.where<Todo>().max("id")


        if(maxId != null){
            return maxId.toInt() + 1
        }
        return 0
    }

    private fun updateTodo(id : Long){//id를 인자로 받는다
        realm.beginTransaction() //트랜잭션 시작

        val updateItem = realm.where<Todo>().equalTo("id", id).findFirst()!! //realm 객체의 where<T> 메서드가 반환하는
        //T타입 객체로부터 데이터를 얻습니다. equalTo() 메서드로 조건을 설정, "id" 칼럼에 id 값이 있다면 findFirst()메서드로 첫 번째 데이터를 반환

        updateItem.title = todoEditText.text.toString()
        updateItem.date = calendar.timeInMillis

        realm.commitTransaction() //트랜잭션 종료 반영

        //다이얼로그 표시
        alert ("내용이 변경되었습니다"){ yesButton { finish() } }.show()

    }

    private fun deleteTodo(id:Long ){
        realm.beginTransaction() //트랜잭션 시작

        val deleteItem = realm.where<Todo>().equalTo("id", id).findFirst()!!

        //삭제할 객체
        deleteItem.deleteFromRealm() //삭제

        realm.commitTransaction() //트랜잭션 종료 반영

        //다이얼로그 표시
        alert ("내용이 삭제되었습니다"){ yesButton { finish() } }.show()
    }
}
