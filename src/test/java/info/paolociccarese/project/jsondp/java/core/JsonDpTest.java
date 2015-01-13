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

import static org.junit.Assert.assertEquals;

import org.json.simple.JSONObject;
import org.junit.Test;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpTest {

	@Test
	public void testJsonDpWithProvenance1() {
		System.out.println("--------------------------------");
		System.out.println(" testJsonDpWithProvenance1()");
		System.out.println("--------------------------------");
		
		JSONObject provenance = new JSONObject();
		provenance.put("importedFrom", "Public Record");
		
		System.out.println(" Initializing the array... ");
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio", provenance);
		array.add("Ciccarese");
		System.out.println(" " + array.plainJsonWithProvenanceToString());
		
		System.out.println(" * Checking size");
		assertEquals(3, array.size());
		
		System.out.println(" Initializing the object... ");
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Harvard Catalyst");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
	
		JsonDpObject address = new JsonDpObject();
		address.put("city", "Brookline", provenance1);
		address.put("zip", "02446", provenance1);
		address.put("street", "Harvard St.", provenance2);
		
		jpo.put("address", address, provenance1);
		array.add(jpo);
		
		System.out.println(array.toString());
		System.out.println(array.plainJsonWithProvenanceToString());
	}
	
	@Test
	public void testJsonDpWithProvenance2() {
		System.out.println("--------------------------------");
		System.out.println(" testJsonDpWithProvenance2()");
		System.out.println("--------------------------------");
		
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friends");
		
		System.out.println(" Initializing the object... ");
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		
		JsonDpArray array = new JsonDpArray();
		array.add("Cicca", provenance2);
		array.add("Nunzio", provenance1);
		jpo.put("middleName", array, provenance1);
		
		System.out.println(jpo.toString());
		System.out.println(jpo.plainJsonWithProvenanceToString());	
	}
	
	@Test
	public void testJsonDpWithProvenance3() {
		System.out.println("--------------------------------");
		System.out.println(" testJsonDpWithProvenance2()");
		System.out.println("--------------------------------");
		
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friends");
		
		System.out.println(" Initializing the object... ");
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		
		JsonDpArray array = new JsonDpArray();
		array.add("Cicca", provenance2);
		array.add("Nunzio", provenance1);
		jpo.put("middleName", array);
		
		System.out.println(jpo.toString());
		System.out.println(jpo.plainJsonWithProvenanceToString());	
	}
	
	@Test
	public void testJsonDpWithProvenance4() {
		System.out.println("--------------------------------");
		System.out.println(" testJsonDpWithProvenance4()");
		System.out.println("--------------------------------");
		
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friends");
		
		System.out.println(" Initializing the object... ");
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		
		JsonDpArray array1 = new JsonDpArray();
		array1.add("Nunzio1", provenance2);
		array1.add("Nunzio2", provenance1);
		
		JsonDpArray array = new JsonDpArray();
		array.add("Cicca", provenance2);
		array.add(array1, provenance1);
		jpo.put("middleName", array);
		
		System.out.println(jpo.toString());
		System.out.println(jpo.plainJsonWithProvenanceToString());	
	}
}
