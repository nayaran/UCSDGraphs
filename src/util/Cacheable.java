package util;

public interface Cacheable {
	public boolean isExpired();
	public Object getIdentifier();
	public Object getEnclosingObject();
}
