/*
* Copyright 2014 Paolo Ciccarese
*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package info.paolociccarese.project.jsondp.java.core;

import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpObject {

	ArrayList<JsonObjectCore> jsonObjects;
		
	public JsonDpObject() {
		jsonObjects = new ArrayList<JsonObjectCore>();
	}
	
	/**
	 * Put a key/value pair without any provenance data.
	 * @param key   The key
	 * @param value The value
	 */
	public void put(Object key, Object value) {
		JsonObjectCore jsonObject = new JsonObjectCore();
		jsonObject.putData(key, value);
		jsonObjects.add(jsonObject);
	}
	
	/**
	 * Return all the available values for a particular key. As the object can
	 * contain several value pairs with different provenance data, an array 
	 * might be returned. If no values are present null is returned. If only 
	 * one value is available, a JSON object is returned.
	 * @param key The requested key
	 * @return The value(s) for the requested key.
	 */
	public Object get(Object key) {
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			if(jsonObject.containsDataKey(key)) {
				Object d = jsonObject.getDataValue(key);
				if(d!=null) array.add(d);
			}
		}
		if(array.size()==0) return null;
		else if(array.size()==1) return array.get(0);
		return array.toString();
	}
	
	/**
	 * Returns the provenance data for this object. As the object can
	 * contain several sets of provenance data, an array  might be returned.
	 * If no values are present null is returned. If only one set of provenance
	 * data is available, a JSON object is returned. 
	 * @return The provenance data. 
	 */
	public Object getProvenance() {
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			JSONObject p = jsonObject.getProvenance();
			if(p!=null) array.add(p);
		}
		if(array.size()==0) return null;
		else if(array.size()==1) return array.get(0);
		return array.toString();
	}
	
	/**
	 * Put a key/value pair with provenance data.
	 * @param key			The key
	 * @param value			The value
	 * @param provenance	The provenance data
	 */
	public void put(Object key, Object value, JSONObject provenance) {
		JsonObjectCore buffer;
		for(JsonObjectCore jsonObject: jsonObjects) {
			boolean match = false;
			for(Object k: provenance.keySet()) {
				if(jsonObject.containsProvenance(k, provenance.get(k)))  {
					match = true;
				} else {
					match = false;
					break;
				}
			}
			
			if(match) {
				jsonObject.putData(key, value);
				return;
			}
			
		}
		
		JsonObjectCore jsonObject = new JsonObjectCore();
		jsonObject.putData(key, value);
		for(Object k: provenance.keySet()) {
			jsonObject.putProvenance(k, provenance.get(k));
		}
		jsonObjects.add(jsonObject);
	}
	
	/**
	 * Returns all the values corresponding to a particular key and where the
	 * provenance data includes the specified key/value pair.
	 * @param key				The key of interest
	 * @param provenanceKey		The provenance key
	 * @param provenanceValue 	The provenance value
	 * @return The values given the key and provenance data.
	 */
	public Object get(Object key, Object provenanceKey, Object provenanceValue) {
		for(JsonObjectCore jsonObject: jsonObjects) {
			if(jsonObject.containsDataKey(key)) {
				if(jsonObject.containsProvenance(provenanceKey, provenanceValue)) {
					return jsonObject.getDataValue(key);
				}
			}
		}		
		return null;
	}
	
	/**
     * Return all the available values (including provenance data) for a 
     * particular key. As the object can contain several value pairs with 
     * different provenance data, an array might be returned. If no values 
     * are present null is returned. If only one value is available, a JSON 
     * object is returned.
	 * @param key The requested key
	 * @return The values with the provenance data
	 */
	public Object getWithProvenance(Object key) {
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			if(jsonObject.containsDataKey(key)) {
				JSONObject obj = jsonObject.getValueAndProvenance(key);
				if(obj!=null) 
					array.add(jsonObject.getValueAndProvenance(key));
			}
		}
		return array.toString();
	}
	
	public Object getWithProvenance() {
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			array.add(jsonObject.getAll());
		}
		return array;
	}
	
	/**
	 * Returns the String representation of the data with the provenance.
	 */
	public String toStringWithProvenance() {
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			array.add(jsonObject.getAll());
		}
		return array.toString();
	}
	
	/**
	 * Returns the String representation of the data without the provenance.
	 */
//	public String toString() {
//		JSONArray array = new JSONArray();
//		for(JsonObjectCore jsonObject: jsonObjects) {
//			JSONObject o = new JSONObject();
//			array.add(jsonObject.getData());
//		}
//		return array.toString();
//	}
	public String toString() {
		JSONObject o = new JSONObject();
		//JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			JSONObject jo = jsonObject.getData();
			o.putAll(jo);
		}
		return o.toString();
	}
	
	class JsonObjectCore {
		
		private static final String PROVENANCE = "@provenance";
		
		JSONObject dataObject;
		JSONObject provenanceObject;
		
		public JsonObjectCore() {
			dataObject = new JSONObject();
		}
		
		protected JSONObject getAll() {
			JSONObject obj = new JSONObject();
			for(Object key: dataObject.keySet()) {
				if(dataObject.get(key) instanceof JsonDpObject) {
					obj.put(key, ((JsonDpObject)dataObject.get(key)).getWithProvenance());
				} else if(dataObject.get(key) instanceof JsonDpArray) {
					JSONArray values = (JSONArray) ((JsonDpArray)dataObject.get(key)).getWithProvenance();
					obj.put(key, values);
				} else {
					 obj.put(key, dataObject.get(key));
				}
			}
			if(provenanceObject!=null) obj.put(PROVENANCE, provenanceObject);
			return obj;
		}
		
		protected JSONObject getData() {
			return dataObject;
		}
		
		public void putData(Object key, Object value) {
			dataObject.put(key, value);
		}
		
		public boolean containsDataKey(Object key) {
			return dataObject.containsKey(key);
		}
		
		public boolean containsData(Object key, Object value) {
			return dataObject.containsKey(key) && dataObject.get(key).equals(value);
		}
		
		public Object getDataValue(Object key) {
			return dataObject.get(key);
		}
		
		public Set dataKeySet() {
			return dataObject.keySet();
		}
		
		protected JSONObject getProvenance() {
			return provenanceObject;
		}
		
		public void setProvenance(JSONObject provenance) {
			provenanceObject = provenance;
		}
		
		public boolean containsProvenance(Object key, Object value) {
			return provenanceObject.containsKey(key) && provenanceObject.get(key).equals(value);
		}
		
		public void putProvenance(Object key, Object value) {
			if(provenanceObject==null) {
				provenanceObject = new JSONObject();
				//dataObject.put(PROVENANCE, provenanceObject);
			}
			provenanceObject.put(key, value);		
		}

		public JSONObject getValueAndProvenance(Object key) {
			JSONObject obj = null;
			if(dataObject.containsKey(key)) {
				obj = new JSONObject();
				obj.put(key, dataObject.get(key));
				if(provenanceObject!=null)
					obj.put(PROVENANCE, provenanceObject);
			}			
			return obj;
		}

//		public Object getProvenance() {
//			if(provenanceObject==null) {
//				provenanceObject = new JSONObject();
//				dataObject.put(PROVENANCE, provenanceObject);
//			}
//			return dataObject.get(PROVENANCE);
//		}
			
		public String toString() {
			return getAll().toString();
		}
	}
}
