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
import info.paolociccarese.project.jsondp.java.core.JsonDpArray;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpArrayTest {

	@BeforeClass public static void initialize() {
		System.out.println("================================");
		System.out.println("  Testing of JsonDpArray class ");
		System.out.println("================================");
	}
	
	@Test public void testSimpleArraySizeAndValues() {
		System.out.println("--------------------------------");
		System.out.println(" testSimpleArraySizeAndValues()");
		System.out.println("--------------------------------");

		System.out.println(" Initializing the array... ");
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio");
		array.add("Ciccarese");
		System.out.println(" " + array.toString());
		
		System.out.println(" * Checking size");
		assertEquals(3, array.size());
		
		System.out.println(" * Checking item 0");
		assertEquals("Paolo", array.get(0));
		System.out.println(" * Checking item 1");
		assertEquals("Nunzio", array.get(1));
		System.out.println(" * Checking item 2");
		assertEquals("Ciccarese", array.get(2));
		
		System.out.println(" * Checking toString");
		assertEquals("[\"Paolo\",\"Nunzio\",\"Ciccarese\"]", array.toString());
		System.out.println(" > " + array.toString());
		
		System.out.println(" * Checking toStringWithProvenance");
		assertEquals("[[\"Paolo\"],[\"Nunzio\"],[\"Ciccarese\"]]", array.toJsonWithProvenanceString());
		System.out.println(" > " + array.toJsonWithProvenanceString());
	}
	
	@Test
	public void testArrayGetWithProvenance() {
		System.out.println("--------------------------------");
		System.out.println(" testArrayGetWithProvenance()");
		System.out.println("--------------------------------");
		
		JSONObject provenance = new JSONObject();
		provenance.put("importedFrom", "Public Record");
		
		System.out.println(" Initializing the array... ");
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio", provenance);
		array.add("Ciccarese");
		System.out.println(" " + array.toJsonWithProvenanceString());
		
		System.out.println(" * Checking size");
		assertEquals(3, array.size());
		
		System.out.println(" * Checking item 0");
		assertEquals("Paolo", array.get(0));
		System.out.println(" * Checking item 1");
		assertEquals("Nunzio", array.get(1));
		System.out.println(" * Checking item 2");
		assertEquals("Ciccarese", array.get(2));
		
		System.out.println(" * Checking toString");
		assertEquals("[\"Paolo\",\"Nunzio\",\"Ciccarese\"]", array.toString());
		System.out.println(" > " + array.toString());
		
		System.out.println(" * Checking toStringWithProvenance");
		assertEquals("[[\"Paolo\"],[\"Nunzio\",{\"@provenance\":{\"importedFrom\":\"Public Record\"}}],[\"Ciccarese\"]]", array.toJsonWithProvenanceString());
		System.out.println(" > " + array.toJsonWithProvenanceString());
	}
	
	 	
}
