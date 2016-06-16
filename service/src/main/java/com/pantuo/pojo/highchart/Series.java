package com.pantuo.pojo.highchart;

import com.pantuo.Reportable;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class Series<X, Y extends Reportable> implements Serializable {

	private static final long serialVersionUID = 1L;
    protected Map<String, String> name;
    protected List<X> xAxis;
	protected SeriesType seriesType;
	protected Map<X, Y> data;
	protected double minY, maxY;
	protected boolean hasOutlier = false;
    protected boolean pointerEnabled = true;
    protected int pointerRadius = 2;

    protected Series(SeriesType seriesType) {
        if(null == seriesType){
            throw new IllegalArgumentException("Illegal Arguments: parameter seriesType is null");
        }
        this.name = new HashMap<String, String>();
        this.seriesType = seriesType;
        this.data = new LinkedHashMap<X, Y>();
    }

	protected Series(SeriesType seriesType, List<X> xAxis) {
        this.name = new HashMap<String, String>();
		this.seriesType = seriesType;
		this.data = new LinkedHashMap<X, Y>();
		
        this.xAxis = xAxis;
	}
	
    public static <X,Y extends Reportable> Series newCategorySeries(SeriesType seriesType, List<X> xAxis) {
    	return new Series<X,Y>(seriesType, xAxis);
    }

    public static DatetimeSeries newDatetimeSeries(SeriesType seriesType, DayList dayList) {
    	return new DatetimeSeries (seriesType, dayList);
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

    public Series addName(String yKey, String name) {
        this.name.put(yKey, name);
        return this;
    }
    
	public String getName(String yKey) {
		return name.get(yKey);
	}

    public String getData(String yKey) {

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

            builder.append(value.toString(null, yKey));
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

    public boolean isPointerEnabled() {
        return pointerEnabled;
    }

    public void setPointerEnabled(boolean pointerEnabled) {
        this.pointerEnabled = pointerEnabled;
    }

    public int getPointerRadius() {
        return pointerRadius;
    }

    public void setPointerRadius(int pointerRadius) {
        this.pointerRadius = pointerRadius;
    }

    public Series setPointer(boolean enabled, int radius) {
        pointerEnabled = enabled;
        pointerRadius = radius;
        return this;
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
