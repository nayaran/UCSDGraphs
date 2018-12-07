package util;

import java.util.Calendar;

public class SimpleCacheable implements Cacheable {
	private Object object = null;
	private Object objectIdentifier = null;
	private java.util.Date expiryDate = null;
	
	public SimpleCacheable(Object object, Object identifier, int timeToLiveSeconds) {
		this.object = object;
		this.objectIdentifier = identifier;
		
		// calculate and populate expiryDate based on timeToLive
		if (timeToLiveSeconds != 0) {
			expiryDate = new java.util.Date();
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(expiryDate);
	        calendar.add(calendar.SECOND, timeToLiveSeconds);
	        expiryDate = calendar.getTime();
		}
		
	}
	@Override
	public Object getEnclosingObject() {
		return this.object;
	}
	@Override
	public boolean isExpired() {
		// Object lives forever if expiryDate is not set
		if (expiryDate == null) {
			return false;
		}
		
		// Object has expired
		if (expiryDate.after(new java.util.Date())) {
			return false;
		}
		
		// Object has life
		return true;		
	}

	@Override
	public Object getIdentifier() {
		// TODO Auto-generated method stub
		return this.objectIdentifier;
	}
	
	@Override
	public String toString() {
		return this.object.toString();
	}
}
