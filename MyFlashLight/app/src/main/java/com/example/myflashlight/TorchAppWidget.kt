package com.example.myflashlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class TorchAppWidget : AppWidgetProvider() {//브로드캐스트 리시버 클래스를 상속받는다.

    //onupdate 메서드는 위젯이 업데이트되어야 할 때 호출된다.
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {//위젯이 여러개 배치되었다면 모든 위젯 업데이트
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    //위젯이 처음 생성될 때 호출
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    //위젯이 여러개일 경우 마지막 위젯이 종료될 때 호출
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        //위젯 업데이트시 수행하는 코드
        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object RemoteViews객체 구성
            val views = RemoteViews(context.packageName, R.layout.torch_app_widget)
            //위젯을 배치하는 뷰는 따로 존재하니 그것들은 RemoteViews객체로 가져올 수 있다.

            views.setTextViewText(R.id.appwidget_text, widgetText)//위젯 클릭시 처리 추가

            //추가로 작성할 부분
            //위젯 클릭시 처리 추가해야한다.

            val intent = Intent(context, TorchService::class.java)
            val pendingIntent = PendingIntent.getService(context, 0, intent, 0)

            //위젯 클릭시 위에서 정의한 intent 실행
            //클릭 이벤트 연결 위해 setOnClickPendingIntent 메서드 사용
            //클릭이 발생할 뷰의 id와 pendingIntent객체가 필요하다.
            views.setOnClickPendingIntent(R.id.appwidget_layout, pendingIntent)


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)//레이아웃 모두 수정시 appWidgetManager를 사용해서 위젯 업데이트
        }
    }
}

