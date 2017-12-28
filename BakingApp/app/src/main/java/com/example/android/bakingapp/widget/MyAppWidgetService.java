package com.example.android.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by lsitec101.macedo on 28/12/17.
 */

public class MyAppWidgetService extends RemoteViewsService {

    FactoryListViewWidget factoryListViewWidget;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        factoryListViewWidget = new FactoryListViewWidget(intent, this.getApplicationContext());
        return factoryListViewWidget;
    }
}
