package com.MyTodo.todolist

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class TodoListAdapter(realmResult: OrderedRealmCollection<Todo>) :RealmBaseAdapter<Todo>(realmResult) {

    //아이템에 표시하는 뷰를 구성한다. getView 메서드는 매 아이템이 화면에 보일 때마다 호출됨
    //두번째 인자인 convertView는 아이템 작성전에는 null이고 한번작성했으면 이전에 작성했던 뷰 호출
    //convertView가 null이면 레이아웃을 작성한다
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val vh : ViewHolder
        val view : View

        if(convertView == null) {//convertView가 null이면 레이아웃 작성

            //LayoutInflater는 xmㅣ레이아웃 파일을 코드로 불러오는 기능을 제공
            // LayoutInflater.from(parent?.context) 메서드로 객체를 얻고 inflate 메서드로 xml 레이아웃 파일을 읽어서
            //뷰로 반환하여 view 변수에 할당한다.
            //inflate(Resource : Int, root :ViewGroup, attatchToroot : Boolean) Resource는 불러올 레이아웃 xml 리소스 id를 저장
            //root는 불러온 레이아웃 파일이 붙을 뷰그룹인 parent를 지정, attatchToRoot는 xml파일 불러올 때 false 지정
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_todo, parent, false)


            //뷰 홀더 객체 초기화
            vh = ViewHolder(view)
            view.tag = vh//tag프로퍼티에는 Any형으로 어떠한 객체도 저장이 가능하다

        }else{//이전에 작성한 convertView 재사용
            view = convertView
            vh = view.tag as ViewHolder//반환되는 데이터형이 Any이므로 ViewHolder형으로 형변환
        }

        //realmBaseAdapter는 adapterData 프로퍼티를 제공한다. 여기서 데이터에 접근 가능
        //값이 있다면 해당 위치의 데이터를 item 변수에 담는다.
        if(adapterData != null){
            val item = adapterData!![position]
            vh.textTextview.text = item.title

            //DateFormat.format( 메서드는 지정한 형식으로 long형 시간 데이터를 반환한다.
            //import android.text.format.DateFormat 임포트해야함
            vh.dateTextView.text = DateFormat.format("yyyy/MM/dd", item.date)
        }

        return view
    }

    override fun getItemId(position: Int): Long {

        if(adapterData != null){
            return adapterData!![position].id
        }
        return super.getItemId(position)
    }

    class ViewHolder(view : View){
        val dateTextView : TextView = view.findViewById(R.id.text1)
        val textTextview : TextView = view.findViewById(R.id.text2)
    }
}