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

import static org.junit.Assert.*;
import info.paolociccarese.project.jsondp.java.core.JsonDpObject;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpObjectTest {

	@BeforeClass public static void initialize() {
		System.out.println("================================");
		System.out.println(" Testing of JsonDpObject class ");
		System.out.println("================================");
	}
	
	@Test
	public void testSimpleJsonObject() {
		System.out.println("--------------------------------");
		System.out.println(" testSimpleJsonObject()");
		System.out.println("--------------------------------");
		
		System.out.println(" Initializing the object... ");
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo");
		jpo.put("middleName", "Nunzio");
		jpo.put("lastName", "Ciccarese");
		jpo.put("nickname", "Cicca");
		System.out.println(" " + jpo.toString());
		
		//assertNotNull(jpo.toString());
		
		System.out.println(" * Checking firstName");
		assertNotNull(jpo.get("firstName"));
		assertEquals("Paolo", jpo.get("firstName"));
		
		System.out.println(" * Checking middleName");
		assertNotNull(jpo.get("middleName"));
		assertEquals("Nunzio", jpo.get("middleName"));
		
		System.out.println(" * Checking lastName");
		assertNotNull(jpo.get("lastName"));
		assertEquals("Ciccarese", jpo.get("lastName"));
		
		System.out.println(" * Checking nickname");
		assertNotNull(jpo.get("nickname"));
		assertEquals("Cicca", jpo.get("nickname"));
	}
	
	@Test
	public void testCreationOfProvenancedJsonObject() {
		System.out.println("---------------------------------------");
		System.out.println(" testCreationOfProvenancedJsonObject()");
		System.out.println("---------------------------------------");
		
		System.out.println(" Initializing the object... ");
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friend");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("middleName", "Nunzio", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		jpo.put("nickname", "Cicca", provenance2);
		System.out.println(" " + jpo.toStringWithProvenance());

		System.out.println(" * Retrieving firstName with provenance");
		assertEquals("Paolo", jpo.get("firstName", "importedFrom", "Public Record"));
		
		System.out.println(" * Retrieving firstName with wrong provenance");
		assertNotEquals("Paolo", jpo.get("firstName", "importedFrom", "Friend"));
		
		System.out.println(" * Retrieving middleName with provenance");
		assertEquals("Nunzio", jpo.get("middleName", "importedFrom", "Public Record"));
		
		System.out.println(" * Retrieving middleName with wrong provenance");
		assertNotEquals("Nunzio", jpo.get("middleName", "importedFrom", "Friend"));
		
		System.out.println(" * Retrieving lastName with provenance");
		assertEquals("Ciccarese", jpo.get("lastName", "importedFrom", "Public Record"));
		
		System.out.println(" * Retrieving lastName with wrong provenance");
		assertNotEquals("Ciccarese", jpo.get("lastName", "importedFrom", "Friend"));
		
		System.out.println(" * Retrieving nickname with provenance");
		assertEquals("Cicca", jpo.get("nickname", "importedFrom", "Friend"));
		
		System.out.println(" * Retrieving nickname with wrong provenance");
		assertNotEquals("Cicca", jpo.get("nickname", "importedFrom", "Public Record"));
	}
	
	@Test
	public void testGetOfProvenancedJsonObject() {
		System.out.println("----------------------------------");
		System.out.println(" testGetOfProvenancedJsonObject()");
		System.out.println("----------------------------------");
		
		System.out.println(" Initializing the object... ");
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friend");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("middleName", "Nunzio", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		jpo.put("nickname", "Cicca", provenance2);
		System.out.println(" " + jpo.toStringWithProvenance());

		System.out.println(" * Checking provenance");
		assertEquals("[{\"importedFrom\":\"Public Record\"},{\"importedFrom\":\"Friend\"}]", jpo.getProvenance());
	}
	
	@Test
	public void testGetOfProvenancedDuplicateJsonObject() {
		System.out.println("-------------------------------------------");
		System.out.println(" testGetOfProvenancedDuplicateJsonObject()");
		System.out.println("-------------------------------------------");
		
		System.out.println(" Initializing the object... ");
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friend");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("middleName", "Nunzio", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		jpo.put("firstName", "Paolo Nunzio", provenance2);
		System.out.println(" " + jpo.toStringWithProvenance());
		
		System.out.println(" * Retrieving multiple firstName");
		assertEquals("[\"Paolo\",\"Paolo Nunzio\"]", jpo.get("firstName"));
		System.out.println(" * Retrieving multiple firstName with provenance");
		assertEquals("[{\"@provenance\":{\"importedFrom\":\"Public Record\"},\"firstName\":\"Paolo\"},{\"@provenance\":{\"importedFrom\":\"Friend\"},\"firstName\":\"Paolo Nunzio\"}]", jpo.getWithProvenance("firstName"));
	}
}
