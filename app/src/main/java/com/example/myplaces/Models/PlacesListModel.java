package com.example.myplaces.Models;

import java.util.List;

public class PlacesListModel {
    private List<PlacesModel> placesModelList;

    public PlacesListModel(List<PlacesModel> placesModelList) {
        this.placesModelList = placesModelList;
    }

    public List<PlacesModel> getPlacesModelList() {
        return placesModelList;
    }

    public void setPlacesModelList(List<PlacesModel> placesModelList) {
        this.placesModelList = placesModelList;
    }
}
