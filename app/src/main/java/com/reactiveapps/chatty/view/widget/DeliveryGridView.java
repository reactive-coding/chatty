package com.reactiveapps.chatty.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class DeliveryGridView extends GridView {
	public DeliveryGridView(Context context, AttributeSet attrs) {
        super(context, attrs);     
    }     
    
    public DeliveryGridView(Context context) {
        super(context);     
    }     
    
    public DeliveryGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);     
    }     
    
    /* (非 Javadoc)
    * Title: onMeasure
    * Description:
    * 不重写该方法的话, GridView显示不全<只能显示一行>
    * 具体原因有待调查，暂时记录在此!!!
    * @param widthMeasureSpec
    * @param heightMeasureSpec
    * @see android.widget.GridView#onMeasure(int, int)
    */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {     
    
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);     
    }     
}
