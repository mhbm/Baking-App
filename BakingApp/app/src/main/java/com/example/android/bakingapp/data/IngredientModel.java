package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lsitec101.macedo on 09/11/17.
 */

public class IngredientModel implements Parcelable {
    private String quantity;
    private String measure;
    private String ingredient;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    public IngredientModel() {
    }

    protected IngredientModel(Parcel in) {
        this.quantity = in.readString();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Parcelable.Creator<IngredientModel> CREATOR = new Parcelable.Creator<IngredientModel>() {
        @Override
        public IngredientModel createFromParcel(Parcel source) {
            return new IngredientModel(source);
        }

        @Override
        public IngredientModel[] newArray(int size) {
            return new IngredientModel[size];
        }
    };
}
