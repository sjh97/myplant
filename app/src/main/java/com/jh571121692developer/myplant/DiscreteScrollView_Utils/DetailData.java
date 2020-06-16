package com.jh571121692developer.myplant.DiscreteScrollView_Utils;


import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class DetailData  implements Parcelable {
    private String name;
    private String date;
    private String imageUri;
    private Boolean sunIcon= false;
    private Boolean waterIcon= false;
    private Boolean fertilIcon= false;

    public DetailData(String name, String date, String imageUri, Boolean sunIcon, Boolean waterIcon, Boolean fertilIcon) {
        this.name = name;
        this.date = date;
        this.imageUri = imageUri;
        this.sunIcon = sunIcon;
        this.waterIcon = waterIcon;
        this.fertilIcon = fertilIcon;
    }

    public DetailData(){

    }

    protected DetailData(Parcel in) {
        //https://lx5475.github.io/2016/06/27/android-parcelable/
        name = in.readString();
        date = in.readString();
        imageUri = in.readString();
        byte tmpSunIcon = in.readByte();
        sunIcon = tmpSunIcon == 0 ? null : tmpSunIcon == 1;
        byte tmpWaterIcon = in.readByte();
        waterIcon = tmpWaterIcon == 0 ? null : tmpWaterIcon == 1;
        byte tmpFertilIcon = in.readByte();
        fertilIcon = tmpFertilIcon == 0 ? null : tmpFertilIcon == 1;
    }

    public static final Creator<DetailData> CREATOR = new Creator<DetailData>() {
        @Override
        public DetailData createFromParcel(Parcel in) {
            return new DetailData(in);
        }

        @Override
        public DetailData[] newArray(int size) {
            return new DetailData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(imageUri);
        dest.writeByte((byte) (sunIcon == null ? 0 : sunIcon ? 1 : 2));
        dest.writeByte((byte) (waterIcon == null ? 0 : waterIcon ? 1 : 2));
        dest.writeByte((byte) (fertilIcon == null ? 0 : fertilIcon ? 1 : 2));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getSunIcon() {
        return sunIcon;
    }

    public void setSunIcon(Boolean sunIcon) {
        this.sunIcon = sunIcon;
    }

    public Boolean getWaterIcon() {
        return waterIcon;
    }

    public void setWaterIcon(Boolean waterIcon) {
        this.waterIcon = waterIcon;
    }

    public Boolean getFertilIcon() {
        return fertilIcon;
    }

    public void setFertilIcon(Boolean fertilIcon) {
        this.fertilIcon = fertilIcon;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public DetailData setLastDetail(String initialUri){

        DetailData detailData = new DetailData("name", "date", initialUri, false, false, false);

        return detailData;
    }
}
