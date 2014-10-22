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
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

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
	
	private void printLabel(String name, String method, String result) {
		System.out.println(" * Checking " + name + " \t " + method + " \t " + result);
	}
	
	@Test
	public void testSimpleJsonObjectWithoutProvenance() {
		System.out.println("-----------------------------------------");
		System.out.println(" testSimpleJsonObjectWithoutProvenance()");
		System.out.println("-----------------------------------------");
		
		System.out.println(" Initializing the object... ");
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo");
		jpo.put("middleName", "Nunzio");
		jpo.put("lastName", "Ciccarese");
		jpo.put("nickname", "Cicca");
		System.out.println(" " + jpo.toString());
		
		printLabel("firstName", "jpo.get(\"firstName\")", jpo.get("firstName").toString());
		assertNotNull(jpo.get("firstName"));
		assertEquals("Paolo", jpo.get("firstName"));
		
		printLabel("firstName", "jpo.get(\"middleName\")", jpo.get("middleName").toString());
		assertNotNull(jpo.get("middleName"));
		assertEquals("Nunzio", jpo.get("middleName"));
		
		printLabel("firstName", "jpo.get(\"lastName\")", jpo.get("lastName").toString());
		assertNotNull(jpo.get("lastName"));
		assertEquals("Ciccarese", jpo.get("lastName"));
		
		printLabel("nickname", "jpo.get(\"nickname\")", jpo.get("nickname").toString());
		assertNotNull(jpo.get("nickname"));
		assertEquals("Cicca", jpo.get("nickname"));
	}
	
	@Test
	public void testSimpleJsonObjectWithProvenance1() {
		System.out.println("---------------------------------------");
		System.out.println(" testSimpleJsonObjectWithProvenance1()");
		System.out.println("---------------------------------------");
		
		System.out.println(" Initializing the object... ");		
		JSONObject provenance = new JSONObject();
		provenance.put("importedFrom", "Friend");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo");
		jpo.put("middleName", "Nunzio");
		jpo.put("lastName", "Ciccarese");
		jpo.put("nickname", "Cicca", provenance);
		System.out.println(" " + jpo.toJsonWithProvenanceString());
		
		printLabel("firstName", "jpo.get(\"firstName\")", jpo.get("firstName").toString());
		assertNotNull(jpo.get("firstName"));
		assertEquals("Paolo", jpo.get("firstName"));
		
		printLabel("firstName", "jpo.get(\"middleName\")", jpo.get("middleName").toString());
		assertNotNull(jpo.get("middleName"));
		assertEquals("Nunzio", jpo.get("middleName"));
		
		printLabel("firstName", "jpo.get(\"lastName\")", jpo.get("lastName").toString());
		assertNotNull(jpo.get("lastName"));
		assertEquals("Ciccarese", jpo.get("lastName"));
		
		printLabel("nickname", "jpo.get(\"nickname\")", jpo.get("nickname").toString());
		assertNotNull(jpo.get("nickname"));
		assertEquals("Cicca", jpo.get("nickname"));
	}
	
	@Test
	public void testSimpleJsonObjectWithProvenance2() {
		System.out.println("---------------------------------------");
		System.out.println(" testSimpleJsonObjectWithProvenance2()");
		System.out.println("---------------------------------------");
		
		System.out.println(" Initializing the object... ");		
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Friends");
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Parents");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo");
		jpo.put("middleName", "Nunzio");
		jpo.put("lastName", "Ciccarese");
		jpo.put("nickname", "Cicca", provenance1);
		jpo.put("nickname", "Tato", provenance2);
		System.out.println(" " + jpo.toJsonWithProvenanceString());
		
		printLabel("firstName", "jpo.get(\"firstName\")", jpo.get("firstName").toString());
		assertNotNull(jpo.get("firstName"));
		assertEquals("Paolo", jpo.get("firstName"));
		
		printLabel("firstName", "jpo.get(\"middleName\")", jpo.get("middleName").toString());
		assertNotNull(jpo.get("middleName"));
		assertEquals("Nunzio", jpo.get("middleName"));
		
		printLabel("firstName", "jpo.get(\"lastName\")", jpo.get("lastName").toString());
		assertNotNull(jpo.get("lastName"));
		assertEquals("Ciccarese", jpo.get("lastName"));
		
		printLabel("nickname", "jpo.get(\"nickname\",\"importedFrom\",\"Parents\")", jpo.get("nickname", "importedFrom", "Parents").toString());
		assertNotNull(jpo.get("nickname", "importedFrom", "Parents"));
		assertEquals("Tato",  jpo.get("nickname", "importedFrom", "Parents").toString());
		
		printLabel("nickname", "jpo.get(\"nickname\",\"importedFrom\",\"Friends\")", jpo.get("nickname", "importedFrom", "Friends").toString());
		assertNotNull(jpo.get("nickname", "importedFrom", "Friends"));
		assertEquals("Cicca",  jpo.get("nickname", "importedFrom", "Friends").toString());
		
		printLabel("Retrieving multiple nicknames", "jpo.get(\"nickname\")", jpo.get("nickname").toString());
		assertNotNull(jpo.get("nickname"));
		assertEquals("[\"Cicca\",\"Tato\"]", jpo.get("nickname").toString());
		
		printLabel("Retrieving multiple nicknames", "jpo.getWithProvenance(\"nickname\")", jpo.getWithProvenance("nickname").toString());
		assertNotNull(jpo.get("nickname"));
		assertEquals("[{\"nickname\":\"Cicca\",\"@provenance\":{\"importedFrom\":\"Friends\"}},{\"nickname\":\"Tato\",\"@provenance\":{\"importedFrom\":\"Parents\"}}]", jpo.getWithProvenance("nickname").toString());
	}
	
	@Test
	public void testSimpleJsonObjectWithProvenance3() {
		System.out.println("---------------------------------------");
		System.out.println(" testSimpleJsonObjectWithProvenance3()");
		System.out.println("---------------------------------------");
		
		System.out.println(" Initializing the object... ");
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friends");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("middleName", "Nunzio", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		jpo.put("firstName", "Paolo Nunzio", provenance2);
		System.out.println(" " + jpo.toJsonWithProvenanceString());
		
		printLabel("Retrieving multiple firstName", "jpo.get(\"firstName\")", jpo.get("firstName").toString());
		assertEquals("[\"Paolo\",\"Paolo Nunzio\"]", jpo.get("firstName").toString());
		
		printLabel("Retrieving multiple firstName", "jpo.getWithProvenance(\"firstName\")", jpo.getWithProvenance("firstName").toString());
		assertEquals("[{\"@provenance\":{\"importedFrom\":\"Public Record\"},\"firstName\":\"Paolo\"},{\"@provenance\":{\"importedFrom\":\"Friends\"},\"firstName\":\"Paolo Nunzio\"}]", jpo.getWithProvenance("firstName"));
	}
	
	@Test
	public void testComplexJsonObjectsWithProvenance1() {
		System.out.println("-----------------------------------------");
		System.out.println(" testComplexJsonObjectsWithProvenance1()");
		System.out.println("-----------------------------------------");
		
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
		System.out.println(" " + jpo.toJsonWithProvenanceString());
		
		printLabel("firstName", "jpo.get(\"firstName\")", jpo.get("firstName").toString());
		assertNotNull(jpo.get("firstName"));
		assertEquals("Paolo", jpo.get("firstName"));
		
		printLabel("lastName", "jpo.get(\"lastName\")", jpo.get("lastName").toString());
		assertNotNull(jpo.get("lastName"));
		assertEquals("Ciccarese", jpo.get("lastName"));
		
		printLabel("address", "jpo.get(\"address\")", jpo.get("address").toString());
		assertNotNull(jpo.get("address"));
		assertEquals("{\"zip\":\"02446\",\"street\":\"Harvard St.\",\"city\":\"Brookline\"}", jpo.get("address").toString());
		
		printLabel("address", "jpo.getWithProvenance(\"address\")", jpo.getWithProvenance("address").toString());
		assertNotNull(jpo.getWithProvenance("address"));
		assertEquals("[{\"address\":{\"zip\":\"02446\",\"street\":\"Harvard St.\",\"city\":\"Brookline\"},\"@provenance\":{\"importedFrom\":\"Public Record\"}}]", jpo.getWithProvenance("address"));
	
		printLabel("all", "jpo", jpo.toString());
		assertNotNull(jpo);
		assertEquals("{\"lastName\":\"Ciccarese\",\"address\":{\"zip\":\"02446\",\"street\":\"Harvard St.\",\"city\":\"Brookline\"},\"firstName\":\"Paolo\"}", jpo.toString());
	}
	
	@Test
	public void testComplexJsonObjectsWithProvenance2() {
		System.out.println("-----------------------------------------");
		System.out.println(" testComplexJsonObjectsWithProvenance2()");
		System.out.println("-----------------------------------------");
		
		System.out.println(" Initializing the object... ");
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Friends");
		
		JsonDpObject jpo = new JsonDpObject();
		jpo.put("firstName", "Paolo", provenance1);
		jpo.put("middleName", "Nunzio", provenance1);
		jpo.put("lastName", "Ciccarese", provenance1);
		jpo.put("firstName", "Paolo Nunzio", provenance2);
		System.out.println(" " + jpo.toJsonWithProvenanceString());
		
		printLabel("firstName", "jpo.get(\"firstName\",\"importedFrom\",\"Friends\")", jpo.get("firstName", "importedFrom", "Friends").toString());
		assertNotNull(jpo.get("firstName", "importedFrom", "Friends"));	
		assertEquals("Paolo Nunzio", jpo.get("firstName", "importedFrom", "Friends"));
		
		printLabel("firstName", "jpo.get(\"firstName\",\"importedFrom\",\"Public Record\")", jpo.get("firstName", "importedFrom", "Public Record").toString());
		assertNotNull(jpo.get("firstName", "importedFrom", "Public Record"));	
		assertEquals("Paolo", jpo.get("firstName", "importedFrom", "Public Record"));
		
		printLabel("firstName", "jpo.get(\"firstName\",\"importedFrom\",\"Friends\",\"Public Record\")", jpo.get("firstName", "importedFrom", "Friends","Public Record").toString());
		assertNotNull(jpo.get("firstName", "importedFrom", "Friends","Public Record"));	
		assertEquals("{\"firstName\":[\"Paolo\",\"Paolo Nunzio\"]}", jpo.get("firstName", "importedFrom", "Friends","Public Record").toString());
	}
}
