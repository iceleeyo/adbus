package com.pantuo.web.view.report;

public   class FreeBox {

	public long begin;
	public long end;

	public FreeBox(long begin, long end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	/**
	 * @see java.lang.Object#toString()
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@Override
	public String toString() {
		return "FreeBox [begin=" + begin + ", end=" + end + "]";
	}

}
