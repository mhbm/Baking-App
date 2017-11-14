package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.bakingapp.data.RecipeModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int RECIPE_LOADER_ID = 0;
    private static final String TAG = MainActivity.class.getSimpleName();


    private ArrayList<RecipeModel> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar menuToolbar = (Toolbar) findViewById(R.id.menu_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_title));

    }



}
