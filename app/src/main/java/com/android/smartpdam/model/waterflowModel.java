package com.android.smartpdam.model;

public class waterflowModel {
    private String nama;
    private double rate1,volume1,volume_ml;

    public waterflowModel(String nama, double rate1, double volume1, double volume_ml) {
        this.nama = nama;
        this.rate1 = rate1;
        this.volume1 = volume1;
        this.volume_ml = volume_ml;
    }

    public waterflowModel() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getRate1() {
        return rate1;
    }

    public void setRate1(double rate1) {
        this.rate1 = rate1;
    }

    public double getVolume1() {
        return volume1;
    }

    public void setVolume1(double volume1) {
        this.volume1 = volume1;
    }

    public double getVolume_ml() {
        return volume_ml;
    }

    public void setVolume_ml(double volume_ml) {
        this.volume_ml = volume_ml;
    }
}
