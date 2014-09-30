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
package info.paolociccarese.project.jsondp.java.tests;

import info.paolociccarese.project.jsondp.java.main.JsonDpArray;

import org.json.simple.JSONObject;
import org.junit.Test;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpArrayTest {

	@Test
	public void testArrayGet() {
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio");
		array.add("Ciccarese");
		
		System.out.println(array.size());
		System.out.println(array.getValues());
		
		System.out.println(array.get(0));
		System.out.println(array.get(1));
		System.out.println(array.get(2));
	}
	
	@Test
	public void testArrayGetWithProvenance() {
		JSONObject provenance = new JSONObject();
		provenance.put("importedFrom", "Public Record");
		
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio", provenance);
		array.add("Ciccarese");
		
		System.out.println(array.size());
		System.out.println(array.getValues());
		
		System.out.println(array.get(0));
		System.out.println(array.get(1));
		System.out.println(array.get(2));
	}
	
	@Test
	public void testArrayGetWithProvenance2() {
		JSONObject provenance = new JSONObject();
		provenance.put("importedFrom", "Public Record");
		
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio", provenance);
		array.add("Ciccarese");
		
		System.out.println(array.size());
		System.out.println(array.getValues());
		
		System.out.println(array.getWithProvenance(0));
		System.out.println(array.getWithProvenance(1));
		System.out.println(array.getWithProvenance(2));
	}
}
