package com.pantuo.mybatis.persistence;

import com.pantuo.mybatis.domain.BusExample;

public class BusSubExample extends BusExample {
	public class Criteria2 extends Criteria {
		protected Criteria2() {
			super();
		}
		 public Criteria and_CityEqualTo(Integer value) {
	            addCriterion("bus.city =", value, "city");
	            return (Criteria) this;
	        }
		  public Criteria and_LineIdEqualTo(Integer value) {
	            addCriterion(" bus_line.id =", value, "id");
	            return (Criteria) this;
	      }
		  public Criteria and_LineNameEqualTo(String value) {
	            addCriterion(" bus_line.name =", value, "name");
	            return (Criteria) this;
	      }
		  
		  public Criteria and_contractidEqualTo(Integer value) {
	            addCriterion(" bus_online.contractid =", value, "contractid");
	            return (Criteria) this;
	      }
		  
		  public Criteria and_BusOnlineEnableEqualTo(Integer value) {
	            addCriterion(" bus_online.enable =", value, "enable");
	            return (Criteria) this;
	      }
		  
		public Criteria and_LineLevalEqualTo(Integer value) {
	            addCriterion(" bus_line.level =", value, "level");
	            return (Criteria) this;
	      }
	}

	public Criteria2 createCriteria2() {
		Criteria2 criteria2 = createCriteriaInternal2();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria2);
		}
		return criteria2;
	}

	protected Criteria2 createCriteriaInternal2() {
		Criteria2 criteria = new Criteria2();
		return criteria;
	}
}
