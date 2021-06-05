package com.example.stockwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StockWidgetConfigActivity extends AppCompatActivity {
    private Context context;
    private EditText stockNo;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //假定組態設定未完成
        setResult(RESULT_CANCELED);
        //載入設定畫面的布局 XML
        setContentView(R.layout.stock_widget_config);

        context = this;
        stockNo = findViewById(R.id.stock_no);
    }

    public void onClick(View view){
        Intent intent = new Intent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        String StockNum = stockNo.getText().toString();
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        new StockWidget(StockNum).updateWidget(context, manager, appWidgetId);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}