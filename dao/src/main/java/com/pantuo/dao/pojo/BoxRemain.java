package com.pantuo.dao.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.*;

/**
 * box content remains
 * Created by tliu on 7/4/15.
 */
public class BoxRemain implements Serializable  {
    public static class Remain implements Serializable {
        long start,size;

        public long getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public Remain() {}

        public Remain(long start, long size) {
            assert(size >= 0);
            this.start = start;
            this.size = size;
        }

        public long fill(long goodsSize, boolean leftOrRight) {
            if (goodsSize >= size)
                return -1;

            if (leftOrRight) {
                //left
                long currStart = start;
                start += goodsSize;
                size -= goodsSize;
                return currStart;
            } else {
                //right
                size -= goodsSize;
                return start + size;
            }
        }

        //fill in the middle, will create another remain object
        public Remain fill(long goodsSize, long offset) {
            if (goodsSize + offset > size)
                return null;

            Remain splitted = new Remain(start, offset);

            long currStart = start + offset;
            start += goodsSize + offset;
            size -= goodsSize + offset;
            return splitted;

        }
    }

    private static ThreadLocal<ObjectMapper> mapper = new ThreadLocal<ObjectMapper>() {
        @Override
        protected ObjectMapper initialValue() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper;
        }
    };

    private long duration;
    private List<Remain> remains;
    @JsonIgnore
    private List<BoxRemain> managed;

    public long getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Remain> getRemains() {
        return remains;
    }

    public void setRemains(List<Remain> remains) {
        this.remains = remains;
    }

    public BoxRemain() {}

    private BoxRemain(long duration) {
        this.duration = duration;
        remains = new ArrayList<Remain>();
    }

    public BoxRemain(long duration, long start, long remain) {
        this(duration);
        remains.add(new Remain(start, remain));
        validate();
    }

    public static BoxRemain fromJson(String json) {
        try {
            BoxRemain b =  mapper.get().readValue(json, BoxRemain.class);
            b.validate();
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    private void sortByBlankSize() {
        Collections.sort(remains, new Comparator<Remain>() {
            @Override
            public int compare(Remain o1, Remain o2) {
                return (int)(o1.size - o2.size);
            }
        });
    }

    private void sortByStartPos() {
        Collections.sort(remains, new Comparator<Remain>() {
            @Override
            public int compare(Remain o1, Remain o2) {
                return (int) (o1.start - o2.start);
            }
        });
    }

    //获取首播位置
    public long putHead(long goodsSize, boolean validate) {
        if (remains.isEmpty())
            return -1;
        sortByStartPos();
        Remain head = remains.get(0);
        long putPos = -1;
        if (head.start == 0) {
            putPos = head.fill(goodsSize, true);
            if (putPos < 0)
                return putPos;

            if (validate) {
                validate();
            }
            if (managed != null) {
                for (BoxRemain m : managed) {
                    m.putDirect(goodsSize, putPos);
                }
            }
        }
        return putPos;
    }

    //获取末播位置
    public long putTail(long goodsSize, boolean validate) {
        if (remains.isEmpty())
            return -1;
        sortByStartPos();
        Remain last = remains.get(remains.size() - 1);
        long putPos = -1;
        if (last.start + last.size == duration) {
            putPos = last.fill(goodsSize, false);
            if (putPos < 0)
                return putPos;

            if (validate) {
                validate();
            }
            if (managed != null) {
                for (BoxRemain m : managed) {
                    m.putDirect(goodsSize, putPos);
                }
            }
        }
        return putPos;
    }

    @JsonIgnore
    public Remain getMax() {
        if (remains.isEmpty())
            return new Remain(0, 0);
        sortByBlankSize();
        return remains.get(remains.size() - 1);
    }

    public BoxRemain(List<Remain> remains) {
        this.remains = remains;
    }

    private void addBlank(long start, long size) {
        this.remains.add(new Remain(start, size));
        validate();
    }

    //check and return put position
    private Remain checkPut (long goodsSize) { 
    	boolean isAll=true;//add by impanxh
    	if(remains.size()>1){
    		isAll=false;
    	}
        for (Remain p : remains) {
            if (p.size >= goodsSize) {
            	if(!isAll){
            		if(p.start==0){
            			continue;//add by impanxh
            		}
            	}
                return p;
            }
        }
        return null;
    }

    //在最小空格里靠左顺序存放
    public long putLeft (long goodsSize, boolean validate) {
        Remain fillPos = checkPut(goodsSize);
        if (fillPos != null) {
            long putPos = -1;
            Remain splitted = null;
            //首播位置预留30秒
            if (fillPos.getStart() == 0) {
                splitted = fillPos.fill(goodsSize, 30);
            }
            if (splitted == null) {
                putPos = fillPos.fill(goodsSize, true);
            } else {
                putPos = splitted.getStart() + 30;
                remains.add(splitted);
            }
            if (putPos < 0)
                return putPos;

            if (validate)
                validate();
            if (managed != null) {
                for (BoxRemain m : managed) {
                    m.putDirect(goodsSize, putPos);
                }
            }
            return putPos;
        } else {
            return -1;
        }
    }
    //在最小空格里靠左顺序存放
    public long putRight (long goodsSize, boolean validate) {
        Remain fillPos = checkPut(goodsSize);
        if (fillPos != null) {
            long putPos = fillPos.fill(goodsSize, false);
            if (putPos < 0)
                return putPos;

            if (validate)
                validate();
            if (managed != null) {
                for (BoxRemain m : managed) {
                    m.putDirect(goodsSize, putPos);
                }
            }
            return putPos;
        } else {
            return -1;
        }
    }

    public void putDirect(long goodsSize, long position) {
        sortByStartPos();
        Remain splitted = null;
        for (Remain r : remains) {
            if (r.getStart() <= position && r.getStart() + r.getSize() >= position + goodsSize) {
                splitted = r.fill(goodsSize, position - r.getStart());
            }
        }
        if (splitted != null && splitted.size > 0) {
            remains.add(splitted);
            validate();
        }
    }

    public long remainSize () {
        if (remains.isEmpty() || duration == 0)
            return 0;
        long remain = 0;
        for (Remain b : remains) {
            remain += b.getSize();
        }
        return remain;
    }

    public double remainPercentage () {
        if (remains.isEmpty() || duration == 0)
            return 0;
        return (remainSize() * 10000/duration)/100.0;
    }

    public void validate() {
        sortByStartPos();

        Iterator<Remain> iter = remains.iterator();
        Remain prev = null;
        while (iter.hasNext()) {
            Remain p = iter.next();
            if (p.size == 0) {
                iter.remove();
            } else {
                assert (p.start >= 0 && p.start + p.size <= duration);
                if (prev != null) {
                    assert (prev.start + prev.size <= p.start);
                    if (prev.start + prev.size == p.start) {
                        //merge
                        prev.size += p.size;
                        iter.remove();
                        continue;
                    }
                }
                prev = p;
            }
        }
    }

    public String toString() {
        try {
            return mapper.get().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static BoxRemain intersect(BoxRemain... list) {
        return intersect(new ArrayList<BoxRemain>(Arrays.asList(list)));
    }

    public static BoxRemain intersect(List<BoxRemain> list) {
        if (list.size() == 0)
            return null;

        int total = list.size();
        List<Long> positions = new ArrayList<Long>();
        long duration = 10000;
        for (BoxRemain a : list) {
            duration = Math.min(duration, a.duration);
            for (Remain remain : a.remains) {
                if (remain.size <= 0)
                    continue;
                positions.add(remain.start);
                //end用负数区分
                positions.add(-(remain.start + remain.size));
            }
        }
        Collections.sort(positions, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                long c = Math.abs(o1) - Math.abs(o2);
                if (c != 0)
                    return (int)c;
                if (o1 + o2 == 0) {
                    //a start and an end
                    return o1 > 0 ? -1 : 1;
                } else {
                    return (int)c;
                }
            }
        });

        BoxRemain bb = new BoxRemain(duration);
        bb.managed = list;
        long start = -1;
        int startCount = 0;
        for (Long pos : positions) {
            if (pos >= 0) {
                //start
                start = pos;
                startCount ++;
            } else {
                //end
                if (start >= 0 && startCount == total) {
                    bb.addBlank(start, -pos - start);
                    start = -1;
                }
                startCount --;
            }
        }
        return bb;
    }

    public static void main(String[] args) {
        BoxRemain b1 = new BoxRemain(100);
        b1.addBlank(0, 10);
        b1.addBlank(20, 20);
        b1.addBlank(80, 20);
        System.out.println("b1:" + b1);
        BoxRemain b2 = new BoxRemain(200);
        b2.addBlank(5, 25);
        b2.addBlank(30, 20);
        b2.addBlank(60, 100);
        b2.addBlank(180, 10);
        System.out.println("b2:" + b2);
        BoxRemain b3 = new BoxRemain(150);
        b3.addBlank(6, 10);
        b3.addBlank(30, 20);
        b3.addBlank(120, 20);
        System.out.println("b3:" + b3);
        System.out.println("intersect:" + BoxRemain.intersect(b1, b2, b3));
    }
}
