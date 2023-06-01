package collision;

import java.util.ArrayList;
import java.util.Arrays;


interface IntervalOper {
	public void oper(Interval i, float k);
}


public class IntervalUnion {
	private ArrayList<Interval> intervals;
	
	public IntervalUnion() {
		intervals = new ArrayList<>();
	}
	
	public IntervalUnion(Interval[] i) {
		intervals = new ArrayList<>(Arrays.asList(i));
	}
	
	public IntervalUnion append(Interval i) {
		intervals.add(i);
		return this;
	}
	
	private IntervalUnion map(IntervalOper oper, float k) { 
		for (Interval i : intervals)
			oper.oper(i, k);
		return this;
	}
	
	private IntervalUnion map(IntervalOper oper, float[] k) {
		for (int i = 0; i < k.length; i++)
			oper.oper(intervals.get(i), k[i]);
		return this;
	}
	
	public IntervalUnion mul(float k) {
		return map((i, w) -> i.mul(w), k);
	}
	
	public IntervalUnion mul(float[] k) {
		return map((i, w) -> i.mul(w), k);
	}
	
	public IntervalUnion add(float k) {
		return map((i, w) -> i.add(w), k);
	}
	
	public IntervalUnion add(float[] k) {
		return map((i, w) -> i.add(w), k);
	}
	
	public IntervalUnion divide(float k) {
		return mul(1 / k);
	}

	public IntervalUnion divide(float[] k) {
		return map((i, w) -> i.divide(w), k);
	}
	
	public Interval solve() {
		if (intervals.size() == 0)
			return new Interval();
		Interval result = intervals.get(0);
		for (int i = 1; i < intervals.size(); i++)
			result = result.union(intervals.get(i));
		return result;
	}
}
