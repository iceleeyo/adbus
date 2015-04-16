package com.pantuo.pojo.highchart;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Series<X, Y> implements Serializable {

	private static final long serialVersionUID = 1L;
    protected String name;
    protected List<X> xAxis;
	protected SeriesType seriesType;
	protected Map<X, Y> data;
	protected double minY, maxY;
	protected boolean hasOutlier = false;

    protected Series(String name, SeriesType seriesType) {
        if(null == seriesType){
            throw new IllegalArgumentException("Illegal Arguments: parameter seriesType is null");
        }
        this.name = name;
        this.seriesType = seriesType;
        this.data = new LinkedHashMap<X, Y>();
    }

	protected Series(String name, SeriesType seriesType, List<X> xAxis) {
        this.name = name;
		this.seriesType = seriesType;
		this.data = new LinkedHashMap<X, Y>();
		
        this.xAxis = xAxis;
	}
	
    public static <X,Y> Series newCategorySeries(String name, SeriesType seriesType, List<X> xAxis) {
    	return new Series<X,Y>(name, seriesType, xAxis);
    }

    public static DatetimeSeries newDatetimeSeries(SeriesType seriesType, DayList dayList) {
        return new DatetimeSeries ("", seriesType, dayList);
    }

    public static DatetimeSeries newDatetimeSeries(String name, SeriesType seriesType, DayList dayList) {
    	return new DatetimeSeries (name, seriesType, dayList);
    }
    
	public SeriesType getSeriesType() {
		return seriesType;
	}

	public Object put(X x, Y y) {
		updateYRange (y);
		return data.put(x, y);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean put(Map<X, ? extends Y> o) {
    	Iterator<?> iter = o.entrySet().iterator();
    	while (iter.hasNext()) {
			Entry entry = (Entry)iter.next();
    		put((X)entry.getKey(), (Y)entry.getValue());
    	}
        return true;
    }

    protected void updateYRange(Y y) {
		if(y instanceof Number) {
			double curr = ((Number)y).doubleValue();
			if (curr == Double.POSITIVE_INFINITY || 
					curr == Double.NEGATIVE_INFINITY) {
				hasOutlier = true;
			} else if (curr == Double.NaN) {
				//do nothing
			} else {
				if(curr > maxY) {
					maxY = curr;
				}
				
				if(curr < minY) {
					minY = curr;
				}
			}
		}
    }
    
	public String getName() {
		return name;
	}

    public String getData() {

        if(null == xAxis || xAxis.isEmpty()){
            return "";
        }

        StringBuilder builder = new StringBuilder();

        Iterator<X> iter = xAxis.iterator();
        while (iter.hasNext()) {
            X  item = iter.next();
            Y value = data.get(item);

            if(builder.length() > 0){
                builder.append(",");
            }

            if(null == value){
                builder.append("null");
                continue;
            }

            builder.append(value.toString());
        }
        return builder.toString();
    }

    public Map<X, Y> getDataMap(){
        return this.data;
    }

	public double getMinY() {
		return minY;
	}

	public double getMaxY() {
		return maxY;
	}
	
	public static void main(String []args) {
//		Series<Object, Object> series = Series.newCustomSeries();
//		series.put("1", 5.0);
//		series.put("2", 1.0);
//		series.put("3", -200);
//		series.put("4", Double.NEGATIVE_INFINITY);
//		series.put("5", Double.POSITIVE_INFINITY);
//		series.put("6", -1.0);
	}
}
