package com.example.stockwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.example.stockwidget.utils.Stock;
import com.example.stockwidget.utils.StockUtils;

public class StockWidget extends AppWidgetProvider {
    private String stockNo;
    private String stockUrl;
    private String price;
    private String diff;
    private String time;

    public StockWidget() {
    }

    public StockWidget(String stockNo) {
        this.stockNo = stockNo;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        if(stockNo == null || stockNo.trim().length() == 0 || !stockNo.matches("\\d\\d\\d\\d")){
            stockNo = context.getResources().getString(R.string.stock_no);
        }

        stockUrl = context.getResources().getString(R.string.stock_url);
        price = context.getResources().getString(R.string.price);
        diff = context.getResources().getString(R.string.diff);
        time = context.getResources().getString(R.string.time);

        //設定 RemoteViews
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget);
        AsyncTask<String, Integer, Stock> task = new AsyncTask<String, Integer, Stock>() {
            @Override
            protected Stock doInBackground(String... strings) {
                StockUtils utils = new StockUtils();
                Stock stock = utils.getStock(strings[0]);

                return stock;
            }

            @Override
            protected void onPostExecute(Stock stock) {
                views.setTextViewText(R.id.stockName, stock.get名稱());
                views.setTextViewText(R.id.stockNo, stockNo);
                views.setTextViewText(R.id.price, price+stock.get成交());
                views.setTextViewText(R.id.diff, diff+stock.get漲跌());
                views.setTextViewText(R.id.time, time+stock.get時間());
                super.onPostExecute(stock);

                //按下 App Widget 圖片後的動作
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(stockUrl+stockNo));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                views.setOnClickPendingIntent(R.id.stockName, pendingIntent);

                //更新 Widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        };
        task.execute(stockNo);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}