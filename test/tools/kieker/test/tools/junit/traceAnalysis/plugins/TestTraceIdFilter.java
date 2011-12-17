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

package kieker.test.tools.junit.traceAnalysis.plugins;

import java.util.NavigableSet;
import java.util.TreeSet;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;
import kieker.test.tools.junit.traceAnalysis.util.SimpleSinkPlugin;
import kieker.tools.traceAnalysis.plugins.executionFilter.TraceIdFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 */
public class TestTraceIdFilter extends TestCase { // NOCS

	// private static final Log log = LogFactory.getLog(TestTraceIdFilter.class);

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
	private final ExecutionFactory eFactory = new ExecutionFactory(this.systemEntityFactory);

	/**
	 * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that an Execution object <i>exec</i> with traceId not element of
	 * <i>idsToPass</i> is not passed through the filter.
	 */
	@Test
	public void testAssertIgnoreTraceId() {
		final NavigableSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(5l); // NOCS (MagicNumberCheck)
		idsToPass.add(7l); // NOCS (MagicNumberCheck)

		final TraceIdFilter filter = new TraceIdFilter(idsToPass);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();
		final Execution exec = this.eFactory.genExecution(11l, // traceId (must not be element of idsToPass) // NOCS (MagicNumberCheck)
				5, // tin (value not important) // NOCS (MagicNumberCheck)
				10, // tout (value not important) // NOCS (MagicNumberCheck)
				0, 0); // eoi, ess (values not important) // NOCS (MagicNumberCheck)
		Assert.assertTrue("Testcase invalid", !idsToPass.contains(exec.getTraceId()));

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.newExecution(exec);
		Assert.assertTrue("Filter passed execution " + exec + " although traceId not element of " + idsToPass, sinkPlugin.getList()
				.isEmpty());
	}

	/**
	 * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that an Execution object <i>exec</i> with traceId element of
	 * <i>idsToPass</i> is passed through the filter.
	 */
	@Test
	public void testAssertPassTraceId() {
		final NavigableSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(5l); // NOCS (MagicNumberCheck)
		idsToPass.add(7l); // NOCS (MagicNumberCheck)

		final TraceIdFilter filter = new TraceIdFilter(idsToPass);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();
		final Execution exec = this.eFactory.genExecution(7l, // traceId (must be element of idsToPass) // NOCS (MagicNumberCheck)
				5, // tin (value not important) // NOCS (MagicNumberCheck)
				10, // tout (value not important) // NOCS (MagicNumberCheck)
				0, 0); // eoi, ess (values not important) // NOCS (MagicNumberCheck)
		Assert.assertTrue("Testcase invalid", idsToPass.contains(exec.getTraceId()));

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.newExecution(exec);
		Assert.assertFalse("Filter didn't pass execution " + exec + " although traceId element of " + idsToPass, sinkPlugin.getList()
				.isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}
}
