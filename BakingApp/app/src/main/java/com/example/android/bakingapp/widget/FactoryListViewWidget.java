package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by lsitec101.macedo on 28/12/17.
 */

public class FactoryListViewWidget implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<String> mIngredientList;
    private Context mContext;

    @Override
    public void onCreate() {
        //Not implemented yed
    }

    @Override
    public void onDestroy() {
        //Not implemented yed
    }

    public FactoryListViewWidget(Intent intent, Context mContext) {
        this.mIngredientList = intent.getStringArrayListExtra(UpdateServiceWidget.GET_LIST_INGREDIENTS);
        this.mContext = mContext;
    }

    @Override
    public void onDataSetChanged() {
        this.mIngredientList = MyAppWidgetProvider.mIngredientList;
    }

    @Override
    public int getCount() {
        if (mIngredientList != null) {
            return mIngredientList.size();
        } else {
            return 1;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);

        if (mIngredientList != null) {
            remoteViews.setTextViewText(R.id.tv_widget_ingredient_title, mIngredientList.get(i));
        } else {
            remoteViews.setTextViewText(R.id.tv_widget_ingredient_title, "None data");
        }

        Intent intent = new Intent();
        remoteViews.setOnClickFillInIntent(R.id.tv_widget_ingredient_title, intent);
        return remoteViews;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
