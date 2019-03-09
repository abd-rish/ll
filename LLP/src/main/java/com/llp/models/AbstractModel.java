package com.llp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AbstractModel extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void set(String key, Object value) {
		super.put(key, value);
	}
	
	public String getString(String key) {
		Object value = super.get(key);
		if(value == null)
			return null;
		if(value instanceof String)
			return (String) value;
		value = value.toString();
		super.put(key, value);
		return (String) value;
	}
	
	public Character getCharacter(String key) {
		Object value = super.get(key);
		if(value == null)
			return null;
		if(value instanceof Character)
			return (Character) value;
		if(!(value instanceof String))
			value = value.toString();
		if(((String) value).length() != 1)
			return null;
		value = new Character(((String) value).charAt(0));
		super.put(key, value);
		return (Character) value;
	}
	
	public Long getLong(String key) {
		Object value = super.get(key);
		if(value == null)
			return null;
		if(value instanceof Long)
			return (Long) value;
		value = Long.parseLong(value.toString());
		super.put(key, value);
		return (Long) value;
	}
	
	public Integer getInteger(String key) {
		Object value = super.get(key);
		if(value == null)
			return null;
		if(value instanceof Integer)
			return (Integer) value;
		value = Integer.parseInt(value.toString());
		super.put(key, value);
		return (Integer) value;
	}
	
	public Short getShort(String key) {
		Object value = super.get(key);
		if(value == null)
			return null;
		if(value instanceof Short)
			return (Short) value;
		value = Short.parseShort(value.toString());
		super.put(key, value);
		return (Short) value;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Short> getShortList(String key) {
		Object shortListObject = super.get(key);
		if(shortListObject == null || !(shortListObject instanceof List))
			return null;
		List shortListOfObject = (List) shortListObject;
		List<Short> shortList = new ArrayList<Short>();
		for(Object shortObject : shortListOfObject) {
			if(shortObject == null)
				return null;
			if(shortObject instanceof Short) {
				shortList.add((Short) shortObject);
				continue;
			}
			try {
				shortList.add(Short.parseShort(shortObject.toString()));
			} catch(Exception e) {
				return null;
			}
		}
		return shortList;
	}
	
	public Byte getByte(String key) {
		Object value = super.get(key);
		if(value == null)
			return null;
		if(value instanceof Byte)
			return (Byte) value;
		value = Byte.parseByte(value.toString());
		super.put(key, value);
		return (Byte) value;
	}
	
	public Date getDate(String key) {
		Object value = super.get(key);
		if(value == null)
			return null;
		if(value instanceof Date)
			return (Date) value;
		if(value instanceof Long)
			value = new Date((Long) value);
		else
			value = new Date(Long.parseLong(value.toString()));
		super.put(key, value);
		return (Date) value;
	}
	
	public void setStart(Integer start) {
		set("start", start);
	}
	
	public Integer getStart() {
		return getInteger("start");
	}
	
	public void setLength(Integer length) {
		set("length", length);
	}
	
	public Integer getLength() {
		return getInteger("length");
	}
	
}
