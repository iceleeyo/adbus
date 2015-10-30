package com.pantuo.web.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.math.NumberUtils;

public class BodyUseView {

	public int line_id;

	public AtomicInteger cars = new AtomicInteger(0);//车辆配车
	public AtomicInteger online = new AtomicInteger(0);//占用车数
	public AtomicInteger moneyFree = new AtomicInteger(0);//本月可使用车数
	public AtomicInteger orders = new AtomicInteger(0);
	public AtomicInteger notDown = new AtomicInteger(0);
	public AtomicInteger nextMonthEndCount = new AtomicInteger(0);

	public int nowCanUseCar = 0; //本月可使用车数  = 总配车数-占用车数-预定数量
	public int nextMonthCanUseCar = 0; //下月可使用车数 =本月可使用车数+次月下刊车数

	public String onlineBl;//广告在刊率 = 在刊广告车数/车辆配车
	public String useBl;//媒体占有率  =(在刊广告车数+预定车数)/车辆配车

	public void reduceTotalInfo() {

		this.nowCanUseCar = cars.get() - online.get() - orders.get();
		this.nextMonthCanUseCar = nowCanUseCar + nextMonthEndCount.get();
		NumberFormat formatter = new DecimalFormat("0.00");
		if (cars.get() != 0) {
			Double x = new Double(online.get() * 1d / cars.get());
			onlineBl = NumberUtils.toDouble(formatter.format(x)) * 100 + "%";
			Double x2 = new Double((online.get() * 1d + orders.get()) / cars.get());
			useBl = NumberUtils.toDouble(formatter.format(x2)) * 100 + "%";
		}

	}

	public int getLine_id() {
		return line_id;
	}

	public void setLine_id(int line_id) {
		this.line_id = line_id;
	}

	public AtomicInteger getCars() {
		return cars;
	}

	public void setCars(AtomicInteger cars) {
		this.cars = cars;
	}

	public AtomicInteger getOnline() {
		return online;
	}

	public void setOnline(AtomicInteger online) {
		this.online = online;
	}

	public AtomicInteger getMoneyFree() {
		return moneyFree;
	}

	public void setMoneyFree(AtomicInteger moneyFree) {
		this.moneyFree = moneyFree;
	}

	public AtomicInteger getOrders() {
		return orders;
	}

	public void setOrders(AtomicInteger orders) {
		this.orders = orders;
	}

	public AtomicInteger getNextMonthEndCount() {
		return nextMonthEndCount;
	}

	public void setNextMonthEndCount(AtomicInteger nextMonthEndCount) {
		this.nextMonthEndCount = nextMonthEndCount;
	}

	public BodyUseView(int line_id) {
		this.line_id = line_id;
	}

	public AtomicInteger getNotDown() {
		return notDown;
	}

	public void setNotDown(AtomicInteger notDown) {
		this.notDown = notDown;
	}

	public int getNowCanUseCar() {
		return nowCanUseCar;
	}

	public void setNowCanUseCar(int nowCanUseCar) {
		this.nowCanUseCar = nowCanUseCar;
	}

	public int getNextMonthCanUseCar() {
		return nextMonthCanUseCar;
	}

	public void setNextMonthCanUseCar(int nextMonthCanUseCar) {
		this.nextMonthCanUseCar = nextMonthCanUseCar;
	}

	public String getOnlineBl() {
		return onlineBl;
	}

	public void setOnlineBl(String onlineBl) {
		this.onlineBl = onlineBl;
	}

	public String getUseBl() {
		return useBl;
	}

	public void setUseBl(String useBl) {
		this.useBl = useBl;
	}

	/**
	 * @see java.lang.Object#toString()
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@Override
	public String toString() {
		return "BodyUseView [line_id=" + line_id + ", cars=" + cars + ", online=" + online + ", moneyFree=" + moneyFree
				+ ", orders=" + orders + ", notDown=" + notDown + ", nextMonthEndCount=" + nextMonthEndCount
				+ ", nowCanUseCar=" + nowCanUseCar + ", nextMonthCanUseCar=" + nextMonthCanUseCar + ", onlineBl="
				+ onlineBl + ", useBl=" + useBl + "]";
	}

}
