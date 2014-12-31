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

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dr. Paolo Ciccarese
 */
public class JsonDpArrayTest {

	private static Logger logger = LoggerFactory.getLogger(JsonDpArrayTest.class.getName());
	
	private static void info(String message) { logger.info(message); }
	
	@BeforeClass public static void initialize() {
		
		ConsoleAppender console = new ConsoleAppender(); // create appender
		// configure the appender
		String PATTERN = "%d [%p|%c] %m%n";
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(Level.DEBUG);
		console.activateOptions();
		// add appender to any Logger (here is root)
		org.apache.log4j.Logger.getRootLogger().addAppender(console);
		
		info("================================");
		info("  Testing of JsonDpArray class ");
		info("================================");
	}
	
	@Test public void testSimpleArraySizeAndValues() {
		info("--------------------------------");
		info(" testSimpleArraySizeAndValues()");
		info("--------------------------------");

		info(" Initializing the array... ");
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio");
		array.add("Ciccarese");
		info(" " + array.toString());
		
		info(" * Checking size (=3)");
		assertEquals(3, array.size());
		
		info(" * Checking item 0 (=Paolo)");
		assertEquals("Paolo", array.get(0));
		info(" * Checking item 1 (=Nunzio)");
		assertEquals("Nunzio", array.get(1));
		info(" * Checking item 2 (=Ciccarese)");
		assertEquals("Ciccarese", array.get(2));
		
		info(" * Checking toString");
		assertEquals("[\"Paolo\",\"Nunzio\",\"Ciccarese\"]", array.toString());
		info(" > " + array.toString());
		
		info(" * Checking plainJsonWithProvenanceToString");
		assertEquals("[[\"Paolo\"],[\"Nunzio\"],[\"Ciccarese\"]]", array.plainJsonWithProvenanceToString());
		info(" > " + array.plainJsonWithProvenanceToString());
	}
	
	@Test
	public void testArrayGetWithProvenance() {
		info("--------------------------------");
		info(" testArrayGetWithProvenance()");
		info("--------------------------------");
		
		JSONObject provenance = new JSONObject();
		provenance.put("importedFrom", "Public Record");
		
		info(" Initializing the array... ");
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio", provenance);
		array.add("Ciccarese");
		info(" " + array.plainJsonWithProvenanceToString());
		
		info(" * Checking size (=3)");
		assertEquals(3, array.size());
		
		info(" * Checking item 0 (=Paolo)");
		assertEquals("Paolo", array.get(0));
		info(" * Checking item 1 (=Nunzio)");
		assertEquals("Nunzio", array.get(1));
		info(" * Checking item 2 (=Ciccarese)");
		assertEquals("Ciccarese", array.get(2));
		
		info(" * Checking toString");
		assertEquals("[\"Paolo\",\"Nunzio\",\"Ciccarese\"]", array.toString());
		info(" > " + array.toString());
		
		info(" * Checking plainJsonWithProvenanceToString");
		assertEquals("[[\"Paolo\"],[\"Nunzio\",{\"@provenance\":{\"importedFrom\":\"Public Record\"}}],[\"Ciccarese\"]]", array.plainJsonWithProvenanceToString());
		info(" > " + array.plainJsonWithProvenanceToString());
	}
	
	@Test
	public void testArrayGetWithMixedProvenance() {
		info("-----------------------------------");
		info(" testArrayGetWithMixedProvenance()");
		info("-----------------------------------");
		
		JSONObject provenance1 = new JSONObject();
		provenance1.put("importedFrom", "Public Record");
		
		JSONObject provenance2 = new JSONObject();
		provenance2.put("importedFrom", "Personal Record");
		
		info(" Initializing the array... ");
		JsonDpArray array = new JsonDpArray();
		array.add("Paolo");
		array.add("Nunzio", provenance1);
		array.add("Nunzio", provenance2);
		array.add("Ciccarese");
		info(" " + array.plainJsonWithProvenanceToString());
		
		info(" * Checking size (=4)");
		assertEquals(4, array.size());
		
		info(" * Checking item 0 (=Paolo)");
		assertEquals("Paolo", array.get(0));
		info(" * Checking item 1 (=Nunzio)");
		assertEquals("Nunzio", array.get(1));
		info(" * Checking item 2 (=Nunzio)");
		assertEquals("Nunzio", array.get(2));
		info(" * Checking item 3 (=Ciccarese)");
		assertEquals("Ciccarese", array.get(3));
		
		info(" * Checking toString");
		assertEquals("[\"Paolo\",\"Nunzio\",\"Nunzio\",\"Ciccarese\"]", array.toString());
		info(" > " + array.toString());
		
		info(" * Checking plainJsonWithProvenanceToString");
		assertEquals("[[\"Paolo\"],[\"Nunzio\",{\"@provenance\":{\"importedFrom\":\"Public Record\"}}],[\"Nunzio\",{\"@provenance\":{\"importedFrom\":\"Personal Record\"}}],[\"Ciccarese\"]]", array.plainJsonWithProvenanceToString());
		info(" > " + array.plainJsonWithProvenanceToString());
		
		info(" * Checking getAllValuesAsPlainJson");
		assertEquals("[\"Paolo\",\"Nunzio\",\"Nunzio\",\"Ciccarese\"]", array.getAllValuesAsPlainJson().toJSONString());
		info(" > " + array.getAllValuesAsPlainJson().toJSONString());
		
		info(" * Checking getProvenance");
		assertEquals("[{\"importedFrom\":\"Public Record\"},{\"importedFrom\":\"Personal Record\"}]",array.getProvenance().toString());
		info(" > " + array.getProvenance().toString());
	}
}
