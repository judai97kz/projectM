package com.judai.projectm;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

@SuppressLint("ParcelCreator")
public class Suggestion implements SearchSuggestion {
    public String getmName() {
        return mName;
    }

    private String mName;
    private boolean mIsHistory = false;

    public Suggestion(String suggestion) {
//        mName= suggestion.toLowerCase();
        mName= suggestion;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

}
