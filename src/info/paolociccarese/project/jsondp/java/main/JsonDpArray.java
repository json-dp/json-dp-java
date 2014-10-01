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
package info.paolociccarese.project.jsondp.java.main;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpArray {

	ArrayList<JsonArrayObject> jsonArrayObjects;

	public JsonDpArray() {
		jsonArrayObjects = new ArrayList<JsonArrayObject>();
	}
	
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
			array.addAll(jsonArrayObject.getValues());
		}
		return array;
	}
	
	public Object get(int index) {
		int cursor = 0;
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			if(jsonArrayObject.size()>0 && index>=(cursor+jsonArrayObject.size())) {
				cursor = cursor + jsonArrayObject.size();
			} else {
				return jsonArrayObject.getValues().get(index-cursor);
			}
		}
		return jsonArrayObjects.get(index).getValues();
	}
	
	public Object getWithProvenance(int index) {
		int cursor = 0;
		for(JsonArrayObject jsonArrayObject:jsonArrayObjects) {
			if(jsonArrayObject.size()>0 && index>=(cursor+jsonArrayObject.size())) {
				cursor = cursor + jsonArrayObject.size();
			} else {
				JSONArray a = new JSONArray();
				a.add(jsonArrayObject.getValues().get(index-cursor));
				if(jsonArrayObject.getProvenance()!=null) 
					a.add(jsonArrayObject.getProvenanceObject());
				return a;
			}
		}
		return jsonArrayObjects.get(index).getValues();
	}
	
	class JsonArrayObject {
		
		private static final String PROVENANCE = "@provenance";
		
		JSONArray values;
		JSONObject provenanceObject;
		
		public JsonArrayObject() {
			values = new JSONArray();
		}
		
		/**
		 * Returns the number of value items.
		 * @return The number of value items.
		 */
		protected int size() {
			return values.size();
		}
		
		/**
		 * Adds a new value to the array without including any provenance data.
		 * @param value The value to be added to the array.
		 */
		protected void add(Object value) {
			values.add(value);
		}
		
		/**
		 * Returns all the array items with their provenance data.
		 * @return The array items with provenance data.
		 */
		protected JSONArray getAll() {
			JSONArray array = new JSONArray();
			for(int i=0; i<array.size();i++) {
				array.add(values.get(i));
			}
			JSONObject provenanceObject = new JSONObject();
			provenanceObject.put(PROVENANCE, provenanceObject);
			array.add(provenanceObject);
			return array;
		}
		
		/**
		 * Add a key/value pair.
		 * @param key	The key of the pair
		 * @param value The value of the pair
		 */
		public void putProvenance(Object key, Object value) {
			if(provenanceObject==null) {
				provenanceObject = new JSONObject();
				//dataObject.put(PROVENANCE, provenanceObject);
			}
			provenanceObject.put(key, value);		
		}
		
		protected JSONObject getProvenance() {
			return provenanceObject;
		}
		
		protected JSONObject getProvenanceObject() {
			JSONObject provenanceObject = new JSONObject();
			provenanceObject.put(PROVENANCE, getProvenance());
			return provenanceObject;
		}
		
		/**
		 * Returns all the array values without the provenance.
		 * @return
		 */
		protected JSONArray getValues() {
			return values;
		}
	}
}
