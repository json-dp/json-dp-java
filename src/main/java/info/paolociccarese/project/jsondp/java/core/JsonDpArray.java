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

import info.paolociccarese.project.jsondp.java.core.JsonDpObject.JsonObjectCore;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * This class implements the JsonDpArray that optionally includes provenance
 * for each array item. 
 * 
 * <p>
 * Eligible array items are: 
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
 * A JSONArray such as:
 * </p>
 * 
 * <pre>
 * <code>
 *    [
 *       "Paolo Ciccarese",
 *       "Paolo N Ciccarese"
 *    ]
 * </code>
 * </pre>
 * 
 * <p>
 * Becomes, in JsonDpArray terms:
 * </p>
 * 
 * <pre>
 * <code>
 *    [
 *       [
 *           "Paolo Ciccarese",
 *           "Paolo N Ciccarese",
 *           {
 *               "@provenance": {
 *                   "importedFrom":"Public Record"
 *               }
 *           }
 *       ]
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
 *       [
 *           "Paolo Ciccarese",
 *           {
 *               "@provenance": {
 *                   "importedFrom":"Public Record"
 *               }
 *           }
 *       ],
 *       [
 *           "Paolo N Ciccarese",
 *           {
 *               "@provenance": {
 *                   "contributedBy":"Friend"
 *               }
 *           }
 *       ]
 *    ]
 * </code>
 * </pre>
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpArray implements JsonDpAware {

	private static final boolean COMPACT = true;
	
	ArrayList<JsonArrayObject> jsonArrayObjects = new ArrayList<JsonArrayObject>();
	
	/**
	 * Returns the total size of the array.
	 * @return The size of the array
	 */
	public int size() {
		int size=0;
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			size += jsonArrayObject.size();
		}
		return size;
	}
	
	/**
	 * Adds a value without associated data provenance to the array.
	 * @param value 		The value to be added to the array
	 * @throws IllegalArgumentException if the item value is not acceptable.
	 */
	public void add(Object value) {
		if(isValueAcceptable(value)) {
			JsonArrayObject jsonArrayObject = new JsonArrayObject();
			jsonArrayObject.add(value);
			jsonArrayObjects.add(jsonArrayObject);
		} else throw new IllegalArgumentException("Only Strings, JSON and JSON-DP values are allowed." +
			" Found " + value.getClass().getName());
	}
	
	/**
	 * Adds a value with associated data provenance to the array.
	 * @param value			The value to be added to the array
	 * @param provenance	The provenance data
	 * @throws IllegalArgumentException if the item value is not acceptable.
	 */
	public void add(Object value, JSONObject provenance) {
		if(isValueAcceptable(value)) {
			JsonArrayObject jsonArrayObject = new JsonArrayObject();
			jsonArrayObject.add(value);
			for(Object k: provenance.keySet()) {
				jsonArrayObject.putProvenance(k, provenance.get(k));
			}
			jsonArrayObjects.add(jsonArrayObject);
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
	 * Returns the requested item from the array.
	 * @param index The index of the desired item.
	 * @return The item corresponding to the requested index or null.
	 * @throws IndexOutOfBoundsException if the requested index exceeds the array size.
	 */
	public Object get(int index) {
		int cursor = 0;
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			if(jsonArrayObject.size()>0 && index>=(cursor+jsonArrayObject.size())) {
				cursor = cursor + jsonArrayObject.size();
			} else {
				return jsonArrayObject.getItems().get(index-cursor);
			}
		}
		return new IndexOutOfBoundsException("The requested index " + index + 
				" does not exist as the total size of the array is " + size());
	}
	
	/**
	 * Replaces the array item with the specified index with 
	 * the specified replacement.
	 * @param index			The index of the item to replace
	 * @param replacement	The replacement item
	 */
	public void replace(int index, Object replacement) {
		// TODO validate index
		if(isValueAcceptable(replacement)) {
			JsonArrayObject jsonArrayObject = new JsonArrayObject();
			jsonArrayObject.add(replacement);
			jsonArrayObjects.set(index, jsonArrayObject);
		} else throw new IllegalArgumentException("Only Strings, JSON and JSON-DP values are allowed." +
				" Found " + replacement.getClass().getName());
	}
	
	/**
	 * Returns the requested item and its provenance data in a JSON array.
	 * @param index The index of the desired item.
	 * @return The item (and its provenance data) corresponding to the requested index.
	 * @throws IndexOutOfBoundsException if the requested index exceeds the array size.
	 */
	public JSONArray getWithProvenanceAsPlainJson(int index) {
		int cursor = 0;
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			if(jsonArrayObject.size()>0 && index>=(cursor+jsonArrayObject.size())) {
				cursor = cursor + jsonArrayObject.size();
			} else {
				JSONArray a = new JSONArray();
				a.add(jsonArrayObject.getItems().get(index-cursor));
				if(jsonArrayObject.getProvenance()!=null) 
					a.add(jsonArrayObject.getProvenanceObject());
				return a;
			}
		}
		throw new IndexOutOfBoundsException("The requested index " + index + 
				" does not exist as the total size of the array is " + size());
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
		for(JsonArrayObject jsonObject: jsonArrayObjects) {
			JSONObject p = jsonObject.getProvenance();
			if(p!=null) array.add(p);
		}
		if(array.size()==0) return null;
		else if(array.size()==1) return array.get(0);
		return array.toString();
	}
	
	/**
	 * Returns a JSON array with all the values (no provenance)
	 * @return JSON array of values.
	 */
	public JSONArray getAllValuesAsPlainJson() {
		JSONArray array = new JSONArray();
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			array.addAll(jsonArrayObject.getItems());
		}
		return array;
	}
	
	/**
	 * Returns a JSON array with all the values and their provenance.
	 * @return JSON array of values with the provenance
	 */
	public JSONArray getAllValuesAndProvenanceAsPlainJson() {
		JSONArray array = new JSONArray();
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			array.add(jsonArrayObject.getItemsWithProvenance());
		}
		return array;
	}
	
	/**
	 * Returns the String representation of the data with the provenance.
	 * @return The JSON array with all the values and provenance as a String.
	 */
	public String plainJsonWithProvenanceToString() {
		return getAllValuesAndProvenanceAsPlainJson().toString();
	}
	
	/**
	 * Returns the String representation of the data without the provenance.
	 * @return The JSON array with all the values as a String.
	 */
	public String plainJsonToString() {
		return getAllValuesAsPlainJson().toString();
	}
	
	@Override
	public String toString() {
		return plainJsonToString();
	}
	
	/**
	 * Internal JSON-DP array entity. It encodes one or more 
	 * array elements with the same provenance data. The values
	 * array is initialized immediately while the provenance
	 * object is initialized lazily.
	 */
	class JsonArrayObject {
		
		private static final String PROVENANCE = "@provenance";
		
		/**
		 * Collects all the items with no provenance or with a given provenance
		 */
		JSONArray items = new JSONArray();
		
		/**
		 * Provenance object.
		 */
		JSONObject provenanceObject;
		
		/**
		 * Returns the number of value items for this array.
		 * @return The number of value items.
		 */
		protected int size() {
			return items.size();
		}
		
		/**
		 * Returns all the array items without provenance data.
		 * @return The array of values for this JSON-DP array
		 */
		protected JSONArray getItems() {
			return items;
		}
		
		/**
		 * Adds a new value to the array without including any provenance data.
		 * The item will be added as last item of the array.
		 * @param item The value to be added to the array.
		 */
		protected void add(Object item) {
			items.add(item);
		}
		
		// PROVENANCE
		// ---------- 
		
		/**
		 * Add a provanance key/value pair. The provenance data object
		 * is initialized if necessary.
		 * @param key	The key of the provenance pair
		 * @param value The value of the provenance pair
		 */
		protected void putProvenance(Object key, Object value) {
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
		 * Returns the provenance as JSON object
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
		 * Returns the provenance object with the appropriate provenance
		 * relationship. This is used for serialization with provenance.
		 * @return The provenance JSON object with the provenance relationship
		 */
		protected JSONObject getProvenanceObject() {
			JSONObject provenanceObject = new JSONObject();
			provenanceObject.put(PROVENANCE, getProvenance());
			return provenanceObject;
		}
		
		/**
		 * Returns all the array items with their provenance data.
		 * @return The array items with provenance data as a JSON array.
		 */
		protected JSONArray getItemsWithProvenance() {
			JSONArray array = new JSONArray();
			// Values
			for(int i=0; i<items.size();i++) {
				Object arrayItem = items.get(i);
				if(arrayItem instanceof JsonDpObject) {
					array.add(((JsonDpObject)arrayItem).getAllValuesAndProvenanceAsPlainJson());
				} else if(arrayItem instanceof JsonDpArray) {
					array.add((JSONArray) ((JsonDpArray)arrayItem).getAllValuesAndProvenanceAsPlainJson());
				} else {
					array.add(arrayItem);
				}
			}
			// Provenance
			if(provenanceObject!=null) {
				array.add(getProvenanceObject());
			}
			return array;
		}	
	}
}
