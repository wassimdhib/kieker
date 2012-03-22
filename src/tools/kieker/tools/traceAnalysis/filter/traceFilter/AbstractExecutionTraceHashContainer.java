/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.traceAnalysis.filter.traceFilter;

import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;

/**
 * 
 * @author Andre van Hoorn
 */
abstract class AbstractExecutionTraceHashContainer { // NOPMD (abstract class without abstract methods)

	private final ExecutionTrace executionTrace;

	public AbstractExecutionTraceHashContainer(final ExecutionTrace t) {
		this.executionTrace = t;
	}

	public ExecutionTrace getExecutionTrace() {
		return this.executionTrace;
	}
}