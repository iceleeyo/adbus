package com.pantuo.pojo;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;

import com.pantuo.web.push.PushInter;

/**
 * Created by tliu on 4/13/15.
 */
public class TableRequest {
    private int start = 0;
    private int length = 10;
    private List<Map<String/*column*/, String/*dir*/>> order;
    private int draw;
    private Map<String, String> filter;
    private PushInter pushLet = PushInter.DONOTHING;

    public TableRequest() {
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (length < 1)
            length = 1;
        this.length = length;
    }

	public Sort getSort(String ascOrDesc, String defaultSort) {
		if (order == null || order.isEmpty()) {
			return (defaultSort == null ? null : new Sort(Direction.fromString(ascOrDesc), defaultSort));
		}
		Sort sort = null;
		for (Map<String, String> o : order) {
			if (o.isEmpty())
				continue;
			Map.Entry<String, String> e = o.entrySet().iterator().next();
			String dir = "".equals(e.getValue()) ? "ASC" : e.getValue().toUpperCase();

			Sort s = null;
			if (StringUtils.contains(e.getKey(), "|")) {
				String[] split = StringUtils.split(e.getKey(),"\\|");
				Order r = new Order(Sort.Direction.valueOf(dir), StringUtils.trim(split[0]),
						NullHandling.valueOf(StringUtils.trim(split[1])));//NULLS_LAST  //NULLS_FIRST
				s = new Sort(r);

			} else {

				s = new Sort(Sort.Direction.valueOf(dir), e.getKey());

			}

			if (sort == null)
				sort = s;
			else
				sort = sort.and(s);
		}
		return sort;
	}

	public Sort getSort(String defaultSort) {
		return getSort("desc", defaultSort);
	}

    public List<Map<String, String>> getOrder() {
        return this.order;
    }

    public void setOrder(List<Map<String, String>> order) {
        this.order = order;
    }

    public int getPage() {
        return start/length;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public String getFilter(String key) {
        return getFilter(key, (String)null);
    }

    public String getFilter(String key, String defaultValue) {
        if (filter == null || !filter.containsKey(key))
            return defaultValue;
        String val = filter.get(key);
        if (StringUtils.isBlank(val)) {
            return defaultValue;
        }
        return val;
    }

    public int getFilterInt(String key, int defaultValue) {
        String str = getFilter(key);
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public <T extends Enum<T>> T getFilter(String key, Class<T> tEnum) {
        return getFilter(key, tEnum, (T)null);
    }

    public <T extends Enum<T>> T getFilter(String key, Class<T> tEnum, T defaultValue) {
        if (filter == null)
            return defaultValue;

        String enumStr = filter.get(key);
        if (enumStr == null)
            return defaultValue;
        try {
            return Enum.valueOf(tEnum, enumStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public String toString() {
        return "TableRequest{" +
                "filter=" + filter +
                ", draw=" + draw +
                ", order=" + order +
                ", length=" + length +
                ", start=" + start +
                '}';
    }
    public PushInter getPushLet() {
		return pushLet;
	}

	public void setPushLet(PushInter pushLet) {
		this.pushLet = pushLet;
	}
}
