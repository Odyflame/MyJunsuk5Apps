package com.MyTodo.todolist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Todo (//코틀린에서는 realm사용하는 클래스에게 open 추가
    @PrimaryKey var id : Long = 0,//id는 유일한 값이 되어야 하니PrimaryKey 추가
                var title:String ="",
                var date:Long=0)
    : RealmObject() //RealmObject 클래스를 상속으로 받자 Realm 데이터베이스에서 다룰 수 있습니다
{

}