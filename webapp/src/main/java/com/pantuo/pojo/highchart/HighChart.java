package com.pantuo.pojo.highchart;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class HighChart<X> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String titleY;
	private String subTitle;
	private List<X> xAxis;
    private Class<X> xType;
	private String linkPrefix;
	Map<String, Series> seriesList;
	protected Map<ScaleType, Double> minY, maxY;
	
	HighChart(String title, Class<X> xType) {
		this.title = title;
		this.seriesList = new LinkedHashMap<String, Series>();
        this.xType = xType;
		minY = new HashMap<ScaleType, Double> ();
		maxY = new HashMap<ScaleType, Double> ();
	}
	public String getTitle() {
		return title;
	}
	void addSeries(String seriesKey, Series<X, ?> series) {
		this.seriesList.put(seriesKey,series);
	}
	
/*	public static void main(String []args) {
		HighChart chart = new HighChart("This chart is for test");
		System.out.println("max: " + chart.getMaxY() + " min: " + chart.getMinY());
		Series series = Series.newCustomSeries();
		series.put("1", Double.POSITIVE_INFINITY);
		series.put("2", Double.NEGATIVE_INFINITY);
		series.put("3", 0.99);
		chart.addSeries("1", series);
		System.out.println("max: " + chart.getMaxY() + " min: " + chart.getMinY());
		series = Series.newCustomSeries();
		series.put("1", 0.88);
		series.put("2", -1);
		series.put("3", -0.88);
		chart.addSeries("2", series);
		System.out.println("max: " + chart.getMaxY() + " min: " + chart.getMinY());
	}
*/	
	void calculateYRange() {
		try {
		for(Series series : seriesList.values()) {
			Double maxy = maxY.get(series.getSeriesType().getScaleType());
			if (maxy == null || maxy < series.getMaxY()) {
				maxY.put(series.getSeriesType().getScaleType(), series.getMaxY());
			}
			Double miny = minY.get(series.getSeriesType().getScaleType());
			if (miny == null || miny > series.getMinY()) {
				minY.put(series.getSeriesType().getScaleType(), series.getMinY());
			}
		}
		
		Iterator<Entry<ScaleType, Double>> maxIter = maxY.entrySet().iterator();
		while (maxIter.hasNext()) {
			Entry<ScaleType, Double> e = maxIter.next();
			e.setValue(e.getValue() + Math.abs(e.getValue()*0.1));
		}
		Iterator<Entry<ScaleType, Double>> minIter = minY.entrySet().iterator();
		while (minIter.hasNext()) {
			Entry<ScaleType, Double> e = minIter.next();
			e.setValue(e.getValue() - Math.abs(e.getValue()*0.1));
		}
		} catch (Exception e) {
			//TODO: log
		}
	}
	
	public static void normalizeYRange(Collection<HighChart> charts) {
		Map<ScaleType, Double> maxY = new HashMap<ScaleType, Double> ();
		Map<ScaleType, Double> minY = new HashMap<ScaleType, Double> ();
		for (HighChart chart : charts) {
			Iterator<Entry<ScaleType, Double>> maxIter = chart.maxY.entrySet().iterator();
			while (maxIter.hasNext()) {
				Entry<ScaleType, Double> e = maxIter.next();
				Double maxy = maxY.get(e.getKey());
				if (maxy == null || maxy < e.getValue()) {
					maxY.put(e.getKey(), e.getValue());
				}
			}
			Iterator<Entry<ScaleType, Double>> minIter = chart.minY.entrySet().iterator();
			while (minIter.hasNext()) {
				Entry<ScaleType, Double> e = minIter.next();
				Double miny = minY.get(e.getKey());
				if (miny == null || miny > e.getValue()) {
					minY.put(e.getKey(), e.getValue());
				}
			}
		}
		
		for (HighChart chart : charts) {
			chart.maxY = maxY;
			chart.minY = minY;
		}
	}
	
	public void merge(HighChart beMerged) {
		HighChartBuilder builder = new HighChartBuilder(this);
		Iterator<Entry<String, Series>> iter = beMerged.getSeriesListAsMap().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Series> e = iter.next();
			builder.addSeries(e.getKey(), e.getValue());
		}
		builder.build();
	}
	
	/**
	 * split series with specified types out of this chart
	 * @return the chart with specified types
	 */
	public HighChart split(SeriesType... types) {
		HighChartBuilder newBuilder = new HighChartBuilder(this.getTitle(), xType)
			.setLinkPrefix(linkPrefix)
			.setSubTitle(subTitle)
			.setTitleY(titleY)
			.setxAxis(xAxis);
		if (types == null || types.length == 0)
			return newBuilder.build();
		
		List<SeriesType> stypes = Arrays.asList(types);
		HighChartBuilder thisBuilder = new HighChartBuilder(this);
		Iterator<Entry<String, Series>> iter = seriesList.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Series> e = iter.next();
			if (stypes.contains(e.getValue().getSeriesType())) {
				newBuilder.addSeries(e.getKey(), e.getValue());
				iter.remove();
			}
		}
		thisBuilder.build();
		return newBuilder.build();
	}
	
	void setTitle(String title) {
		this.title = title;
	}
	public String getTitleY() {
		return titleY;
	}
	void setTitleY(String titleY) {
		this.titleY = titleY;
	}
	public String getSubTitle() {
		return subTitle;
	}
	void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

    public String getxAxisAsString() {
        StringBuffer sbuffer = new StringBuffer(",");
        for(Object item : xAxis){
            sbuffer.append(item.toString());
        }
        return sbuffer.substring(1);
    }

	public List getxAxis() {
		return xAxis;
	}
	void setxAxis(List<X> xAxis) {
		this.xAxis = xAxis;
	}
    public String getxType() {
        return xType.getSimpleName();
    }
	public String getLinkPrefix() {
		return linkPrefix;
	}
	void setLinkPrefix(String linkPrefix) {
		this.linkPrefix = linkPrefix;
	}
	public Collection<Series> getSeriesList() {
		return seriesList.values();
	}

    public Map<String, Series> getSeriesListAsMap() {
        return seriesList;
    }
    
    public Collection<String> getScaleTypes () {
    	List<String> types = new ArrayList<String> ();
    	for (ScaleType type : maxY.keySet()) {
    		types.add(type.name());
    	}
    	return types;
    }
    
    public boolean containsScaleType (String scaleType) {
    	ScaleType type = null;
    	try {
    		type = ScaleType.valueOf(scaleType);
    	} catch (Exception e) {}
    	if (type == null)
    		return false;
    	return maxY.containsKey(type);
    }
    
    public int getScaleTypeCount () {
    	return maxY.size();
    }

    public List<ScaleType> getScaleTypeList() {
        return new ArrayList<ScaleType> (maxY.keySet());
    }
    
    public double getMaxY(String scaleType) {
    	ScaleType type = null;
    	try {
    		type = ScaleType.valueOf(scaleType);
    	} catch (Exception e) {}
    	if (type == null)
    		return 0;
		Double y = maxY.get(type);
		return y == null ? 0 : y; 
	}
	
	public double getMinY(String scaleType) {
    	ScaleType type = null;
    	try {
    		type = ScaleType.valueOf(scaleType);
    	} catch (Exception e) {}
    	if (type == null)
    		return 0;
		Double y = minY.get(type);
		return y == null ? 0 : y; 
	}
	
	public double getMaxYForSeries(String... serieses) {
		double result = 0;
		try {
		for(String s : serieses) {
			Series series = seriesList.get(s);
			if (series == null)
				continue;
			if (result < series.getMaxY()) {
				result = series.getMaxY();
			}
		}
		
		result += Math.abs(result*0.1);
		} catch (Exception e) {
			//TODO: log
		}
		return result;
	}
	
	public double getMinYForSeries(String... serieses) {
		double result = 0;
		try {
		for(String s : serieses) {
			Series series = seriesList.get(s);
			if (series == null)
				continue;
			if (result > series.getMinY()) {
				result = series.getMinY();
			}
		}
		
		result -= Math.abs(result*0.1);
		} catch (Exception e) {
			//TODO: log
		}
		return result;
	}
}
