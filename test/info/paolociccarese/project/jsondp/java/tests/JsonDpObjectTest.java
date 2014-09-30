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

import static org.junit.Assert.*;
import info.paolociccarese.project.jsondp.java.main.JsonDpObject;

import org.json.simple.JSONObject;
import org.junit.Test;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpObjectTest {

	@Test
	public void testCreationOfUnprovenancedJsonObject() {
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo");
		jpo.put("middleName", "Nunzio");
		jpo.put("lastName", "Ciccarese");
		jpo.put("nickname", "Cicca");
		
		//assertNotNull(jpo.toString());
		
		assertNotNull(jpo.get("firstName"));
		System.out.println(jpo.get("firstName"));
		assertEquals("Paolo", jpo.get("firstName"));
		
		assertNotNull(jpo.get("middleName"));
		assertEquals("Nunzio", jpo.get("middleName"));
		
		assertNotNull(jpo.get("lastName"));
		assertEquals("Ciccarese", jpo.get("lastName"));
		
		assertNotNull(jpo.get("nickname"));
		assertEquals("Cicca", jpo.get("nickname"));
	}
	
	@Test
	public void testCreationOfProvenancedJsonObject() {
		
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friend");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("middleName", "Nunzio", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		jpo.put("nickname", "Cicca", provenance2);
		
		assertNotNull(jpo.toString());
		

		System.out.println(jpo);
		System.out.println(jpo.toStringWithProvenance());
	}
	
	@Test
	public void testGetOfProvenancedJsonObject() {
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friend");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("middleName", "Nunzio", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		jpo.put("nickname", "Cicca", provenance2);
		
		assertNotNull(jpo.toString());
		
		System.out.println(jpo.get("firstName"));
		System.out.println(jpo.get("firstName", "importedFrom", "Public Record"));
		System.out.println(jpo.getProvenance());
	}
	
	@Test
	public void testGetOfProvenancedDuplicateJsonObject() {
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friend");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("middleName", "Nunzio", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		jpo.put("firstName", "Paolo Nunzio", provenance2);
		
		assertNotNull(jpo.toString());
		
		//System.out.println(jpo.get("firstName"));
		System.out.println(jpo.getWithProvenance("firstName"));
		//System.out.println(jpo);
	}

}
