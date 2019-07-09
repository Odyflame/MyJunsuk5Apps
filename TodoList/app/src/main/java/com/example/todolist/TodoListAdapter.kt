package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import android.text.format.DateFormat

class TodoListAdapter(realmResult: OrderedRealmCollection<Todo>) :RealmBaseAdapter<Todo>(realmResult) {

    //아이템에 표시하는 뷰를 구성한다.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        val vh : ViewHolder
        val view : View

        if(convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_todo, parent, false)

            vh = ViewHolder(view)
            view.tag = vh

        }else{
            view = convertView
            vh = view.tag as ViewHolder
        }
    }

}