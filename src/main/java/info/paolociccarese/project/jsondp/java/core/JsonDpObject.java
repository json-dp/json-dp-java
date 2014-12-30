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
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * This class implements the JsonDpObject that is a wrapper to the JSON Object.
 * It extends the JSON Object with optional provenance data.
 * 
 * <p>
 * Eligible value items are: 
 * <ul>
 * <li>java.lang.String
 * <li>JsonDpArray 
 * <li>JsonDpObject
 * <li>org.json.simple.JSONArray
 * <li>org.json.simple.JSONObject.
 * </ul>
 * </p>
 * 
 * <p>
 * A JSONObject such as:
 * </p>
 * 
 * <pre>
 * <code>
 *    {
 *       "Name":"Paolo Ciccarese",
 *       "MiddleInitial":"N"
 *    }
 * </code>
 * </pre>
 * 
 * <p>
 * Becomes, in JsonDpObject terms:
 * </p>
 * 
 * <pre>
 * <code>
 *    [
 *       {
 *           "Name":"Paolo Ciccarese",
 *           "MiddleInitial":"N"
 *           "@provenance": {
 *               "importedFrom":"Public Record"
 *           }
 *       }
 *    ]
 * </code>
 * </pre>
 * 
 * <p>
 * Or, if the items have different provenance:
 * </p>
 * 
 * <pre>
 * <code>
 *    [
 *       {
 *           "Name":"Paolo Ciccarese",
 *           "@provenance": {
 *               "importedFrom":"Public Record"
 *           }
 *       },
 *       {
 *           "MiddleInitial":"N"
 *           "@provenance": {
 *               "importedFrom":"Friend"
 *           }
 *       }
 *    ]
 * </code>
 * </pre>
 * 
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpObject implements JsonDpAware {

	ArrayList<JsonObjectCore> jsonObjects = new ArrayList<JsonObjectCore>();
	
	/**
	 * Put a key/value pair without any provenance data.
	 * @param key   The key
	 * @param value The value
	 */
	public void put(Object key, Object value) {
		if(isValueAcceptable(value)) {
			JsonObjectCore jsonObject = new JsonObjectCore();
			jsonObject.put(key, value);
			jsonObjects.add(jsonObject);
		} else throw new IllegalArgumentException("Only Strings, JSON and JSON-DP values are allowed." +
				" Found " + value.getClass().getName());
	}
	
	/**
	 * Put a key/value pair with provenance data.
	 * @param key			The key
	 * @param value			The value
	 * @param provenance	The provenance data
	 */
	public void put(Object key, Object value, JSONObject provenance) {
		if(isValueAcceptable(value)) {
			for(JsonObjectCore jsonObject: jsonObjects) {
				boolean match = false;
				if(jsonObject.getProvenance()!=null) { 
					for(Object k: provenance.keySet()) {
						if(jsonObject.containsProvenance(k, provenance.get(k)))  {
							match = true;
						} else {
							match = false;
							break;
						}
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
		} else throw new IllegalArgumentException("Only Strings, JSON and JSON-DP values are allowed." +
				" Found " + value.getClass().getName());
	}
	
	/**
	 * Returns true if the value is acceptable.
	 * @param value		The value to be validated
	 * @return True if the value is admissible.
	 */
	private boolean isValueAcceptable(Object value) {
		return (value instanceof String || value instanceof JsonDpAware 
			|| value instanceof JSONAware);
	}
	
	/**
	 * Return all the available values for a particular key. As the object can
	 * contain several value pairs with different provenance data, an array 
	 * might be returned. If no values are present null is returned. If only 
	 * one value is available, a JSON object is returned.
	 * @param key 	The requested key
	 * @return The value(s) for the requested key or null if the key is not present.
	 */
	public Object get(Object key) {
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			if(jsonObject.containsKey(key)) {
				Object d = jsonObject.getValue(key);
				if(d!=null) array.add(d);
			}
		}
		if(array.size()==0) return null;
		else if(array.size()==1) return array.get(0);
		return array;
	}
	
	public Object getWithProvenance(Object key) {
		JsonDpArray array = new JsonDpArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			if(jsonObject.containsKey(key)) {
				Object d = jsonObject.getValue(key);
				if(d!=null) {
					if(jsonObject.getProvenance()!=null) {
						System.out.println(jsonObject.getProvenance());
						array.add(d, jsonObject.getProvenance());
						System.out.println(array.plainJsonWithProvenanceToString());
					} else array.add(d);
				}
			}
		}
		if(array.size()==0) return null;
		// TODO manage one single item and return it as JsonDpObject
		else return array;
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
					return jsonObject.getValue(key);
				}
			}
		}		
		return null;
	}
	
	/**
	 * Returns all the values corresponding to a particular key and where the
	 * provenance data includes the specified key/value pair.
	 * @param key				The key of interest
	 * @param provenanceKey		The provenance key
	 * @param provenanceValues 	The allowed provenance values
	 * @return The values given the key and provenance data.
	 */
	public Object get(Object key, Object provenanceKey, Object... provenanceValues) {
		JsonDpArray object = getValuesWithProvenance(key, provenanceKey, provenanceValues);		
		JSONObject o = new JSONObject();
		o.put(key, object.getAllValuesAsPlainJson());
		return o;
	}

	/**
	 * Returns all the values and provenance corresponding to a particular key and where the
	 * provenance data includes the specified key/value pair.
	 * @param key				The key of interest
	 * @param provenanceKey		The provenance key
	 * @param provenanceValues 	The allowed provenance values
	 * @return The values and provenance given the key and provenance data.
	 */
	public Object getWithProvenance(Object key, Object provenanceKey, Object... provenanceValues) {
		JsonDpArray object = getValuesWithProvenance(key, provenanceKey, provenanceValues);	
		JSONObject o = new JSONObject();
		o.put(key, object.getAllValuesAndProvenanceAsPlainJson());
		return o;
	}
	
	private JsonDpArray getValuesWithProvenance(Object key, Object provenanceKey, Object... provenanceValues) {
		JsonDpArray object = new JsonDpArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			if(jsonObject.containsKey(key)) {
				for(Object provenanceValue: provenanceValues) {
					if(jsonObject.containsProvenance(provenanceKey, provenanceValue)) {
						JSONObject prov = new JSONObject();
						prov.put(provenanceKey, provenanceValue);
						object.add(jsonObject.getValue(key), prov);
					}
				}
			}
		}	
		return object;
	}
	
	public boolean containsKey(Object key) {
		for(JsonObjectCore jsonObject: jsonObjects) {
			if(jsonObject.containsKey(key)) {
				return true;
			}
		}
		return false;
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
	public Object getAsJsonWithProvenance(Object key) {
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
	
	public Object getAsJsonWithProvenance() {
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			array.add(jsonObject.getPairsWithProvenance());
		}
		return array;
	}
	
	/**
	 * Returns the String representation of the data with the provenance.
	 */
	public String plainJsonWithProvenanceToString() {		
		JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			array.add(jsonObject.getPairsWithProvenance());
		}
		return array.toString();
	}
	
	/**
	 * Returns the String representation of the data without the provenance.
	 */
	public String plainJsonToString() {
		JSONObject o = new JSONObject();
		//JSONArray array = new JSONArray();
		for(JsonObjectCore jsonObject: jsonObjects) {
			JSONObject jo = jsonObject.getPairs();
			o.putAll(jo);
		}
		return o.toString();
	}
	
	public String toString() {
		return plainJsonToString();
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
		
		/**
		 * Returns all the pairs as a JSONObject.
		 * @return All the pairs.
		 */
		protected JSONObject getPairs() {
			return pairs;
		}
		
		/**
		 * Adds a new key/value pair.
		 * @param key	The key
		 * @param value The value
		 */
		public void put(Object key, Object value) {
			pairs.put(key, value);
		}
		
		/**
		 * Returns true if the key is present
		 * @param key	The key to look up
		 * @return True if key present
		 */
		public boolean containsKey(Object key) {
			return pairs.containsKey(key);
		}
		
		/**
		 * Returns true if a key/value pair is present
		 * @param key	The key to look up
		 * @param value The value to match
		 * @return True if key/value present.
		 */
		public boolean containsPair(Object key, Object value) {
			return pairs.containsKey(key) && pairs.get(key).equals(value);
		}
		
		/**
		 * Returns the value of the pair identified by the key
		 * @param key	The key to look up
		 * @return The value identified by the requested key.
		 */
		public Object getValue(Object key) {
			return pairs.get(key);
		}
		
		/**
		 * Returns all the keys as a Set.
		 * @return The key set.
		 */
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

		/**
		 * Return the value of a specific key and its provenance.
		 * @param key	The key to look up.
		 * @return The requested value with provenance. 
		 */
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
					obj.put(key, ((JsonDpObject)value).getAsJsonWithProvenance());
				} else if(value instanceof JsonDpArray) {
					obj.put(key, (JSONArray) ((JsonDpArray)value).getAllValuesAndProvenanceAsPlainJson());
				} else {
					 obj.put(key, value);
				}
			}
			if(provenanceObject!=null) obj.put(PROVENANCE, provenanceObject);
			return obj;
		}
	}
}
