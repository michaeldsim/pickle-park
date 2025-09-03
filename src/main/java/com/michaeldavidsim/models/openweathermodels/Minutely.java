package com.michaeldavidsim.models.openweathermodels;

public class Minutely {
    private long dt;
    private double precipitation;

    public long getDt() { return dt; }
    public void setDt(long dt) { this.dt = dt; }

    public double getPrecipitation() { return precipitation; }
    public void setPrecipitation(double precipitation) { this.precipitation = precipitation; }
}
