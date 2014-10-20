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

/**
 * @author Dr. Paolo Ciccarese
 */
public interface JsonDpStream {

	/**
	 * Convert a json-dp object to text ignoring provenance.
	 * @return The textual representation of the json-dp object without provenance.
	 */
	public String toJsonString();
	
	/**
	 * Convert a json-dp object to text including provenance.
	 * @return The textual representation of the json-dp object with provenance.
	 */
	public String toJsonWithProvenanceString();
}
