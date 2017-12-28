package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by lsitec101.macedo on 28/12/17.
 */

public class UpdateServiceWidget extends IntentService {

    public final static String GET_LIST_INGREDIENTS = "GET_LIST_INGREDIENTS";

    public UpdateServiceWidget() {
        super("UpdateServiceWidget");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<String> listOfIngredient = intent.getStringArrayListExtra(GET_LIST_INGREDIENTS);

        Intent intentUpdateWidget = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        intentUpdateWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentUpdateWidget.putExtra(GET_LIST_INGREDIENTS, listOfIngredient);

        sendBroadcast(intentUpdateWidget);
    }

    public static void startWdigetService(ArrayList<String> ingredientList, Context context) {
        Intent intent = new Intent(context, UpdateServiceWidget.class);
        intent.putExtra(GET_LIST_INGREDIENTS, ingredientList);
        context.startService(intent);
    }
}
