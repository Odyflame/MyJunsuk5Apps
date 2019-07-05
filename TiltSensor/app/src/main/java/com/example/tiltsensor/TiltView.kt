package com.example.tiltsensor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.view.View

class TiltView(context: Context?) : View(context) {

    private val greenPaint : Paint = Paint()
    private val blackPaint : Paint = Paint()

    private var Cx : Float = 0f
    private var Cy : Float= 0f

    private var xCoord : Float = 0f
    private var yCoord : Float = 0f

    init{
        //green Paint
        greenPaint.color = Color.GREEN
        //black
        blackPaint.style = Paint.Style.STROKE
        blackPaint.color = Color.RED

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Cx = w / 2f
        Cy = h / 2f
    }

    override fun onDraw(canvas: Canvas?) {//인자로 넘어오는 canvas 객체에 view의 모습을 그린다.
        //바깥 원
        canvas?.drawCircle(Cx,Cy, 100f, blackPaint)

        //녹색 안 원
        canvas?.drawCircle(Cx + xCoord,Cy + yCoord, 100f, greenPaint)

        //가운데 십자가
        canvas?.drawLine(Cx-20, Cy, Cx+20, Cy,blackPaint)
        canvas?.drawLine(Cx,Cy-20,Cx, Cy+20, blackPaint)
    }

    fun onSensorEvent(event : SensorEvent){
        yCoord = event.values[0]*20;
        xCoord = event.values[1]*20;

        //뷰의 onDraw를 다시 호출하는 메서드 즉 뷰를 다시 그리게 됨
        invalidate()
    }
}