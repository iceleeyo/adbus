package com.pantuo.web.view;

import com.pantuo.util.Pair;

public class MapLocationSession {
	public static MapLocationSession EMPTY = new MapLocationSession();

	Pair<Double, Double> locationPair;
	public String city;
	public String address;

	public Pair<Double, Double> getLocationPair() {
		return locationPair;
	}

	public void setLocationPair(Pair<Double, Double> locationPair) {
		this.locationPair = locationPair;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public MapLocationSession(Pair<Double, Double> locationPair, String city, String address) {
		super();
		this.locationPair = locationPair;
		this.city = city;
		this.address = address;
	}

	public MapLocationSession() {
	}

}
