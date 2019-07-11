package com.MyTodo.todolist

import android.app.Application
import io.realm.Realm

class MyApplicatioin :Application() {
    override fun onCreate() {//이 메서드는 액티비티가 생성되기 전에 호출된다
        super.onCreate()
        Realm.init(this)//Realm.init를 사용해 초기화
    }
}