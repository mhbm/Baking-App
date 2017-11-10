package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lsitec101.macedo on 09/11/17.
 */

public class RecipeModel implements Parcelable {
    private String id;
    private String name;
    private ArrayList<IngredientModel> ingredients;
    private ArrayList<StepModel> steps;
    private String servings;
    private String image;

    public ArrayList<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(IngredientModel ingredient) {
        this.ingredients.add(ingredient);
    }

    public ArrayList<StepModel> getSteps() {
        return steps;
    }

    public void setSteps(StepModel step) {
        this.steps.add(step);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeString(this.servings);
        dest.writeString(this.image);
    }

    public RecipeModel() {
    }

    protected RecipeModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(IngredientModel.CREATOR);
        this.steps = new ArrayList<StepModel>();
        in.readList(this.steps, StepModel.class.getClassLoader());
        this.servings = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<RecipeModel> CREATOR = new Parcelable.Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel source) {
            return new RecipeModel(source);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };
}
