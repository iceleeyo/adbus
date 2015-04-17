/**
 * 
 */
package com.pantuo.pojo.highchart;

import com.pantuo.util.DateUtil;
import org.apache.commons.lang.time.FastDateFormat;

import java.util.*;

/**
 * list of days
 * 
 * @author tliu
 *
 */
public class DayList implements Iterable<Date> {
	protected static FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
	protected static FastDateFormat shortFormat = FastDateFormat.getInstance("yyMMdd");
	
	private List<Date> list = null;
	private boolean isRange = false;

	private DayList(List<Date> list, boolean isRange) {
		this.isRange = isRange;
		if (isRange) {
			if ((list == null || list.size() != 2))
				throw new IllegalArgumentException("Range DateList with list: " + list);
		}
		
		if (list != null)
			this.list = new ArrayList<Date> (list);
		else
			this.list = new ArrayList<Date> ();

		normalize();
	}
	
	private DayList(DayList dayList) {
		this.isRange = dayList.isRange;
		if (dayList.list != null) {
			this.list = new ArrayList<Date> (dayList.list);
		}
	}
	
	public static DayList range(Date start, Date end) {
		List<Date> list = new ArrayList<Date> ();
		list.add(start);
		list.add(end);
		return new DayList(list, true);
	}
	
	public static DayList range(Date end, int daysBack) {
		List<Date> list = new ArrayList<Date> ();
		
		Calendar c = DateUtil.newCalendar();
		c.setTime(end);
		c.add(Calendar.DATE, -daysBack);
		list.add(c.getTime());
		list.add(end);
		return new DayList(list, true);
	}
	
	public static DayList oneMonth(Date end) {
		return DayList.range(end, 29);
	}
	
	public static DayList list(Date... days) {
		List<Date> list = new ArrayList<Date> ();
		for (Date d : days) {
			list.add(d);
		}
		return new DayList(list, false);
	}
	
	public static DayList single(Date day) {
		return DayList.list(day);
	}

	public static DayList list(List<Date> days) {
		return new DayList(days, false);
	}
	
	public boolean isEmpty () {
		return (isRange && list.get(0).after(list.get(1))) || list == null || list.isEmpty();
	}

    public boolean contains (Date day) {
        if (day == null)
            return false;
        if (isRange)
            return (day.equals(list.get(0)) || day.equals(list.get(1))
                    || (day.before(list.get(1)) && day.after(list.get(0))));

        for (Date d : list) {
            if (day.equals(d))
                return true;
        }
        return false;
    }

	private void normalize() {
		Calendar c = DateUtil.newCalendar();
		Set<Date> set = new TreeSet<Date>();
		for (Date d : list) {
			c.setTime(d);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			set.add(c.getTime());
		}
		
		list.clear();
		list.addAll(set);
		
		Collections.sort(list);
		
		if (isRange && list.size() == 1) {
			list.add(list.get(0));
		}
	}
	
	public List<Date> toDayList() {
		if (!isRange)
			return list;
		return toList().list;
	}
	
	public DayList toList() {
		if (!isRange)
			return new DayList(this);
		
		List<Date> newList = new ArrayList<Date> ();
		Calendar c = DateUtil.newCalendar();
		c.setTime(list.get(1));
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		Date end = c.getTime();
		
		c.setTime(list.get(0));
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		while (!c.getTime().after(end)) {
			newList.add(c.getTime());
			c.add(Calendar.DATE, 1);
		}
		
		return new DayList(newList, false);
	}
	
	/**
	 * Not backed with internal data, so changes to the iterator will not take effect to the original object 
	 */
	@Override
	public Iterator<Date> iterator() {
		DayList newList = toList();
		return newList.list.iterator();
	}
	
	public DayList remove(DayList dayList) {
		if (this.isEmpty() || dayList.isEmpty()) {
			return new DayList(this);
		} else {
			List<Date> thisList = this.toList().list;
			List<Date> thatList = dayList.toList().list;
			boolean changed = thisList.removeAll(thatList);
			if (changed) {
				return new DayList(thisList, false);
			} else {
				return new DayList(this);
			}
		}
	}
	
	public DayList remove(List<Date> days) {
		return this.remove(DayList.list(days));
	}
	
	public DayList add(DayList dayList) {
		if (this.isEmpty()) {
			return new DayList(dayList);
		} else if (dayList.isEmpty()) {
			return new DayList(this);
		} else {
			List<Date> thisList = this.toList().list;
			List<Date> thatList = dayList.toList().list;
			thatList.removeAll(thisList);
			boolean changed = thisList.addAll(thatList);
			if (changed) {
				return new DayList(thisList, false);
			} else {
				return new DayList(this);
			}
		}
	}
	
	public DayList add(List<Date> days) {
		return this.add(DayList.list(days));
	}
	
/*	public Query toLuceneQuery (String key) {
		if (isEmpty()) {
			return null;
		}

		if (isRange) {
			String start = format.format(list.get(0));
			String end = format.format(list.get(1));
			return new TermQuery(new Term(key, "[" + start + " TO " + end + "]"));
		} else {
			if (list.size() == 1) {
				return new TermQuery(new Term(key, format.format(list.get(0))));
			} else {
				BooleanQuery dayQuery = new BooleanQuery();
				for (Date d : list) {
					dayQuery.add(new TermQuery(new Term(key, format.format(d))), BooleanClause.Occur.SHOULD);
				}
				return dayQuery;
			}
		}
	}*/

	public String toString() {
		if (isEmpty()) {
			return "NoDay";
		}
		if (isRange) {
			String start = format.format(list.get(0));
			String end = format.format(list.get(1));

			return "[" + start + " TO " + end + "]";
		} else {
			String first = format.format(list.get(0));
			String last = null;
			if (list.size() > 1) {
				last = format.format(list.get(list.size() - 1));
			}
			
			return "[" + first + (last == null ? "" : ", ...(total: "+list.size()+")..., " + last) + "]";
		}
	}
	
	public String toVerbalString() {
		if (isEmpty()) {
			return "NoDay";
		}
		if (isRange) {
			String start = shortFormat.format(list.get(0));
			String end = shortFormat.format(list.get(1));

			return "[" + start + "~" + end + "]";
		} else {
			StringBuilder sb = new StringBuilder("[");
			for (Date d : list) {
				sb.append(shortFormat.format(d)).append("_");
			}
			sb.setLength(sb.length() - 1);
			sb.append("]");
			
			return sb.toString();
		}
	}

    public Date getEarlyestDay(){
        Date earlyestDate = null;
        for (Date d : list) {
            earlyestDate = (null == earlyestDate || earlyestDate.after(d))? d: earlyestDate;
        }

        return earlyestDate;
    }

    public Date getLastDay(){
        Date lastDate = null;
        for (Date d : list) {
            lastDate = (null == lastDate || lastDate.before(d))? d: lastDate;
        }

        return lastDate;
    }

	public static void main(String[] args) {
		DayList range = DayList.range(new Date(System.currentTimeMillis() - 30*24*3600*1000L),new Date());
		
		Calendar c = DateUtil.newCalendar();
		c.setTime(new Date(System.currentTimeMillis() - 20*24*3600*1000L));
		Date end = new Date(System.currentTimeMillis() - 10*24*3600*1000L);
		List<Date> ds = new ArrayList<Date> ();
		while (end.after(c.getTime())) {
			ds.add(c.getTime());
			ds.add(c.getTime());
			c.add(Calendar.DATE, 1);
		}
		DayList list = DayList.list(ds);
		
//		System.out.println(range.toLuceneQuery("reportDateString"));
//		System.out.println(list.toLuceneQuery("reportDateString"));
//
//		System.out.println(range.remove(list).toLuceneQuery("reportDateString"));
//		System.out.println(list.remove(range).toLuceneQuery("reportDateString"));
//
//		System.out.println(range.add(list).toLuceneQuery("reportDateString"));
//		System.out.println(list.add(range).toLuceneQuery("reportDateString"));

	}

}
