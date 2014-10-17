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
		jsonObject.put(key, value);
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
			if(jsonObject.containsKey(key)) {
				Object d = jsonObject.getPairValue(key);
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
				jsonObject.put(key, value);
				return;
			}
			
		}
		
		JsonObjectCore jsonObject = new JsonObjectCore();
		jsonObject.put(key, value);
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
			if(jsonObject.containsKey(key)) {
				if(jsonObject.containsProvenance(provenanceKey, provenanceValue)) {
					return jsonObject.getPairValue(key);
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
			if(jsonObject.containsKey(key)) {
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
			array.add(jsonObject.getPairsWithProvenance());
		}
		return array;
	}
	
	/**
	 * Returns the String representation of the data with the provenance.
	 */
	public String toStringWithProvenance() {
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			array.add(jsonObject.getPairsWithProvenance());
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
			JSONObject jo = jsonObject.getPairs();
			o.putAll(jo);
		}
		return o.toString();
	}
	
	/**
	 * Internal JSON-DP object entity. It encodes one or more 
	 * key/value pairs with the same provenance data. The key/value
	 * container is initialized immediately while the provenance
	 * object is initialized lazily.
	 */
	class JsonObjectCore {
		
		private static final String PROVENANCE = "@provenance";
		JSONObject provenanceObject;
		
		JSONObject pairs = new JSONObject();;
		
		protected JSONObject getPairs() {
			return pairs;
		}
		
		public void put(Object key, Object value) {
			pairs.put(key, value);
		}
		
		public boolean containsKey(Object key) {
			return pairs.containsKey(key);
		}
		
		public boolean containsPair(Object key, Object value) {
			return pairs.containsKey(key) && pairs.get(key).equals(value);
		}
		
		public Object getPairValue(Object key) {
			return pairs.get(key);
		}
		
		public Set pairsKeySet() {
			return pairs.keySet();
		}
		
		/**
		 * Add a provanance key/value pair. The provenance data object
		 * is initialized if necessary.
		 * @param key	The key of the provenance pair
		 * @param value The value of the provenance pair
		 */
		public void putProvenance(Object key, Object value) {
			if(provenanceObject==null) {
				provenanceObject = new JSONObject();
			}
			provenanceObject.put(key, value);		
		}
		
		/**
		 * Returns true if the provenance data contain a key/value pair.
		 * @param key		The provenance data key
		 * @param value		The provenance data value
		 * @return True if the pair is present.
		 */
		public boolean containsProvenance(Object key, Object value) {
			return provenanceObject.containsKey(key) && provenanceObject.get(key).equals(value);
		}
		
		/**
		 * Returns the provenance object
		 * @return The provenance data as JSON object
		 */
		protected JSONObject getProvenance() {
			return provenanceObject;
		}
		
		/**
		 * Sets the provenance through a JSON object.
		 * @param provenance	The JSON object with the provenance data
		 */
		public void setProvenance(JSONObject provenance) {
			provenanceObject = provenance;
		}

		public JSONObject getValueAndProvenance(Object key) {
			JSONObject obj = null;
			if(pairs.containsKey(key)) {
				obj = new JSONObject();
				obj.put(key, pairs.get(key));
				if(provenanceObject!=null)
					obj.put(PROVENANCE, provenanceObject);
			}			
			return obj;
		}
		
		/**
		 * Returns all the pairs with their provenance data.
		 * @return The pairs with provenance data.
		 */
		protected JSONObject getPairsWithProvenance() {
			JSONObject obj = new JSONObject();
			for(Object key: pairs.keySet()) {
				Object value = pairs.get(key);
				if(value instanceof JsonDpObject) {
					obj.put(key, ((JsonDpObject)value).getWithProvenance());
				} else if(value instanceof JsonDpArray) {
					obj.put(key, (JSONArray) ((JsonDpArray)value).getWithProvenance());
				} else {
					 obj.put(key, value);
				}
			}
			if(provenanceObject!=null) obj.put(PROVENANCE, provenanceObject);
			return obj;
		}
	}
}
