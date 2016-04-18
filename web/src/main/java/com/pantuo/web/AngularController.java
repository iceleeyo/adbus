package com.pantuo.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.NgTable;

import com.pantuo.web.table.AngularRequest;

/**
 *   Test 测试
 * <b><code>AngularController</code></b>
 * <p>
 * angular mvc 
 * </p>
 * <b>Creation Time:</b> 2016年1月12日 上午9:35:47
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Controller
public class AngularController {

	private static Logger logger = LoggerFactory.getLogger(AngularController.class);
	List<Issue> MEMORY = new ArrayList<AngularController.Issue>();

	@RequestMapping(value = "/ngtable/public_demo")
	@ResponseBody
	public Page<Issue> index(@NgTable AngularRequest table, HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		if (MEMORY.size() == 0) {
			for (int i = 0; i < 300; i++) {
				MEMORY.add(new Issue());
			}
		}
		List<Issue> _MEMORY = null;
		String keyFilter = table.getFilter("key");
		if (StringUtils.isNoneBlank(keyFilter)) {
			_MEMORY = new ArrayList<AngularController.Issue>();
			for (Issue issue : MEMORY) {
				if (StringUtils.contains(issue.getTitle(), keyFilter.trim())) {
					_MEMORY.add(issue);
				}
			}
		} else {
			_MEMORY = MEMORY;
		}

		int start = (table.getPage() - 1) * table.getLength(), end = start + table.getLength();
		Sort sort = table.getSort("desc", "id");
		if (null != sort.getOrderFor("Theme")) {
			if (sort.getOrderFor("Theme").getDirection() == Direction.ASC)
				Collections.sort(_MEMORY, themeCompare_asc);
			else {
				Collections.sort(_MEMORY, themeCompare_desc);
			}
		}
		if (null != sort.getOrderFor("_date")) {
			if (sort.getOrderFor("_date").getDirection() == Direction.ASC)
				Collections.sort(_MEMORY, SORT_COMPARATOR2_ASC);
			else {
				Collections.sort(_MEMORY, SORT_COMPARATOR2_DESC);
			}
		}
		int listSize = _MEMORY.size();
		List<Issue> r = _MEMORY;
		if (_MEMORY.size() != 0) {
			r = _MEMORY.subList(start, (end > listSize - 1) ? listSize - 1 : end);
		}

		PageImpl<Issue> list = new PageImpl<Issue>(r, new PageRequest(table.getPage(), table.getLength(), new Sort(
				Direction.fromString("desc"), "id")), listSize);
		return list;
	}

	//比较器 asc
	Comparator<Issue> themeCompare_asc = new Comparator<Issue>() {
		public int compare(Issue vo1, Issue vo2) {
			return vo1.number - vo2.number;
		}
	};
	//比较器 desc
	Comparator<Issue> themeCompare_desc = new Comparator<Issue>() {
		public int compare(Issue vo1, Issue vo2) {
			return vo2.number - vo1.number;
		}
	};

	//比较器
	Comparator<Issue> SORT_COMPARATOR2_ASC = new Comparator<Issue>() {
		public int compare(Issue vo1, Issue vo2) {
			return vo1.get_date().getTime() - vo2.get_date().getTime() > 0 ? 1 : -1;
		}
	};

	//比较器
	Comparator<Issue> SORT_COMPARATOR2_DESC = new Comparator<Issue>() {
		public int compare(Issue vo1, Issue vo2) {
			return vo2.get_date().getTime() - vo1.get_date().getTime() > 0 ? 1 : -1;
		}
	};
	static final java.util.concurrent.atomic.AtomicInteger r = new AtomicInteger();
	static final java.util.concurrent.atomic.AtomicLong dateScr = new AtomicLong(System.currentTimeMillis());

	public class Issue {
		String title = "Save ->" + r.incrementAndGet();
		String html_url = "https://github.com/esvit/ng-table/issues/782";
		int number = 10 + r.incrementAndGet();
		String updated_at = "2016-01-11T12:41:18Z";
		Date _date = new Date(dateScr.getAndAdd(100000));
		User user = new User();

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getHtml_url() {
			return html_url;
		}

		public void setHtml_url(String html_url) {
			this.html_url = html_url;
		}

		public int getNumber() {
			return number;
		}

		public Date get_date() {
			return _date;
		}

		public void set_date(Date _date) {
			this._date = _date;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String getUpdated_at() {
			return updated_at;
		}

		public void setUpdated_at(String updated_at) {
			this.updated_at = updated_at;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

	}

	public class User {
		String login = (r.incrementAndGet() % 2 == 0 ? "cve " : "abc") + r.incrementAndGet();
		String avatar_url = "https://avatars.githubusercontent.com/u/" + (594621 + r.incrementAndGet()) + "?v=3";
		String ur = "https://api.github.com/users/cve";

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getAvatar_url() {
			return avatar_url;
		}

		public void setAvatar_url(String avatar_url) {
			this.avatar_url = avatar_url;
		}

		public String getUr() {
			return ur;
		}

		public void setUr(String ur) {
			this.ur = ur;
		}

	}

}
