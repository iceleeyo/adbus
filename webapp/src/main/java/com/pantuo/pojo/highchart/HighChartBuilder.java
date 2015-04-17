package com.pantuo.pojo.highchart;

import java.util.List;

public class HighChartBuilder<X> {
	private HighChart<X> chart;
	
	public HighChartBuilder(String title, XType xType) {
		chart = new HighChart<X>(title, xType);
	}
	public HighChartBuilder(HighChart<X> chart) {
		this.chart = chart; 
	}
	public HighChartBuilder addSeries(String seriesKey, Series<X, ?> series) {
		chart.addSeries(seriesKey, series);
		return this;
	}
	
	public HighChartBuilder setTitle(String title) {
		chart.setTitle(title);
		return this;
	}
	public HighChartBuilder setTitleY(String titleY) {
		chart.setTitleY(titleY);
		return this;
	}
	public HighChartBuilder setSubTitle(String subTitle) {
		chart.setSubTitle(subTitle);
		return this;
	}
	public HighChartBuilder setxAxis(List<X> xAxis) {
		chart.setxAxis(xAxis);
		return this;
	}
	public HighChartBuilder setLinkPrefix(String linkPrefix) {
		chart.setLinkPrefix(linkPrefix);
		return this;
	}
    public HighChartBuilder setStacked(boolean stacked) {
        chart.setStacked(stacked);
        return this;
    }
    public HighChartBuilder setType(ChartType type) {
        chart.setType(type);
        return this;
    }
	public HighChart build() {
		chart.calculateYRange();
		return chart;
	}
}
