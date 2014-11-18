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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpArray implements JsonDpStream {

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
	 * @param value The value to be added to the array
	 */
	public void add(Object value) {
		JsonArrayObject jsonArrayObject = new JsonArrayObject();
		jsonArrayObject.add(value);
		jsonArrayObjects.add(jsonArrayObject);
	}
	
	/**
	 * Adds a value with associated data provenance to the array.
	 * @param value			The value to be added to the array
	 * @param provenance	The provenance data
	 */
	public void add(Object value, JSONObject provenance) {
		JsonArrayObject jsonArrayObject = new JsonArrayObject();
		jsonArrayObject.add(value);
		for(Object k: provenance.keySet()) {
			jsonArrayObject.putProvenance(k, provenance.get(k));
		}
		jsonArrayObjects.add(jsonArrayObject);
	}
	
	/**
	 * Returns an array with all the values (no provenance)
	 * @return Array of values.
	 */
	public Object getValues() {
		JSONArray array = new JSONArray();
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			array.addAll(jsonArrayObject.getItems());
		}
		return array;
	}
	
	/**
	 * Returns the requested item from the array.
	 * @param index The index of the desired item.
	 * @return The item corresponding to the requested index.
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
		return jsonArrayObjects.get(index).getItems();
	}
	
	/**
	 * Returns the requested item and its provenance data from the array.
	 * @param index The index of the desired item.
	 * @return The item (and its provenance data) corresponding to the requested index.
	 */
	public Object getWithProvenance(int index) {
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
		return jsonArrayObjects.get(index).getItems();
	}
	
	public JSONArray getWithProvenance() {
		JSONArray array = new JSONArray();
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			array.add(jsonArrayObject.getItemsWithProvenance());
		}
		return array;
	}
	
	public String toJsonWithProvenanceString() {
		JSONArray array = new JSONArray();
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			array.add(jsonArrayObject.getItemsWithProvenance());
		}
		return array.toString();
	}
	
	/**
	 * Returns the String representation of the data without the provenance.
	 */
	public String toJsonString() {
		JSONArray array = new JSONArray();
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			array.addAll(jsonArrayObject.getItems());
		}
		return array.toString();
	}
	
	public String toString() {
		return toJsonString();
	}
	
	/**
	 * Internal JSON-DP array entity. It encodes one or more 
	 * array elements with the same provenance data. The values
	 * array is initialized immediately while the provenance
	 * object is initialized lazily.
	 */
	class JsonArrayObject {
		
		private static final String PROVENANCE = "@provenance";
		JSONObject provenanceObject;
		
		JSONArray items = new JSONArray();
		
		/**
		 * Returns the number of value items for this array.
		 * @return The number of value items.
		 */
		protected int size() {
			return items.size();
		}
		
		/**
		 * Adds a new value to the array without including any provenance data.
		 * The item will be added as last item of the array.
		 * @param item The value to be added to the array.
		 */
		protected void add(Object item) {
			items.add(item);
		}
		
		/**
		 * Returns all the array items without provenance data.
		 * @return The array of values for this JSON-DP array
		 */
		protected JSONArray getItems() {
			return items;
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
		 * Returns the provenance object
		 * @return The provenance data as JSON object
		 */
		protected JSONObject getProvenance() {
			return provenanceObject;
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
		 * @return The array items with provenance data.
		 */
		protected JSONArray getItemsWithProvenance() {
			JSONArray array = new JSONArray();
			// Values
			for(int i=0; i<items.size();i++) {
				Object arrayItem = items.get(i);
				if(arrayItem instanceof JsonDpObject) {
					array.add(((JsonDpObject)arrayItem).getWithProvenance());
				} else if(arrayItem instanceof JsonDpArray) {
					array.add((JSONArray) ((JsonDpArray)arrayItem).getWithProvenance());
				} else {
					array.add(arrayItem);
				}
			}
			// Provenance
			if(provenanceObject!=null) {
				JSONObject provenance = new JSONObject();
				provenance.put(PROVENANCE, provenanceObject);
				array.add(provenance);
			}
			return array;
		}	
	}
}
