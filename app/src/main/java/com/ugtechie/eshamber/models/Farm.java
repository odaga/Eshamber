package com.ugtechie.eshamber.models;

public class Farm {
    private String _id;
    private String farmName;
    private String farmDescription;
    private String farmLocation;
    private String crop;
    private String duration;
    private String farmerExperience;
    private String farmYearsOfExperience;
    private String farmImageUrl;
    private String farmAmount;
    private String farmROI;

    //public no-argument constructor needed by firebase
    public Farm() {
    }

    public Farm(String _id, String farmName, String farmDescription, String farmLocation, String crop, String duration, String farmerExperience, String farmYearsOfExperience, String farmImageUrl, String farmAmount, String farmROI) {
        this._id = _id;
        this.farmName = farmName;
        this.farmDescription = farmDescription;
        this.farmLocation = farmLocation;
        this.crop = crop;
        this.duration = duration;
        this.farmerExperience = farmerExperience;
        this.farmYearsOfExperience = farmYearsOfExperience;
        this.farmImageUrl = farmImageUrl;
        this.farmAmount = farmAmount;
        this.farmROI = farmROI;
    }

    public String get_id() {
        return _id;
    }

    public String getFarmName() {
        return farmName;
    }

    public String getFarmDescription() {
        return farmDescription;
    }

    public String getFarmLocation() {
        return farmLocation;
    }

    public String getCrop() {
        return crop;
    }

    public String getDuration() {
        return duration;
    }

    public String getFarmerExperience() {
        return farmerExperience;
    }

    public String getFarmYearsOfExperience() {
        return farmYearsOfExperience;
    }

    public String getFarmImageUrl() {
        return farmImageUrl;
    }

    public String getFarmAmount() {
        return farmAmount;
    }

    public String getFarmROI() {
        return farmROI;
    }
}

