/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin.filter.forward;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.filter.forward.CountingThroughputFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.util.ImmutableEntry;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests the {@link CountingThroughputFilter}.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
// TODO: Note that currently, we are only testing the {@link CountingThroughputFilter}'s input {@link CountingThroughputFilter#INPUT_PORT_NAME_RECORDS}.
public class TestCountingThroughputFilter extends AbstractKiekerTest {

	private static final long START_TIME_NANOS = 246561L; // just a non-trivial number
	private static final long INTERVAL_SIZE_NANOS = 100; // just a non-trivial number

	private IAnalysisController analysisController;

	/** Provides the list of {@link IMonitoringRecord}s to be processed. */
	private ListReader<IMonitoringRecord> simpleListReader; // initialized in #prepareConfiguration()

	/** Provides the (current) number of {@link IMonitoringRecord}s provided by the {@link #simpleListReader}. */
	private CountingFilter countingFilterReader; // initialized in #prepareConfiguration()

	/** The filter to be tested. */
	private CountingThroughputFilter throughputFilter; // initialized in #prepareConfiguration()

	/** Simply collects all {@link IMonitoringRecord}s processed by the tested filter. */
	private ListCollectionFilter<EmptyRecord> sinkPlugin; // initialized in #prepareConfiguration()

	private volatile boolean intervalsBasedOn1stTstamp; // will be set by the @Test's

	/**
	 * Will be filled by {@link #createInputEvents(SimpleListReader)}.
	 */
	private final List<Entry<Long, Long>> expectedThroughputValues = new ArrayList<Entry<Long, Long>>();

	/**
	 * Creates a new instance of this class.
	 */
	public TestCountingThroughputFilter() {
		// empty default constructor
	}

	// Note that @Before is not working because the configuration depends on which @Test is executed
	public void prepareConfiguration() throws IllegalStateException, AnalysisConfigurationException {
		this.analysisController = new AnalysisController();

		/*
		 * Reader
		 */
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.simpleListReader = new ListReader<IMonitoringRecord>(new Configuration(), this.analysisController);

		/*
		 * Counting filter (before delay)
		 */
		this.countingFilterReader = new CountingFilter(new Configuration(), this.analysisController);
		this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME,
				this.countingFilterReader, CountingFilter.INPUT_PORT_NAME_EVENTS);

		/*
		 * The CountingThroughputFilter to be tested
		 */
		final Configuration throughputFilterConfiguration = new Configuration();
		throughputFilterConfiguration.setProperty(CountingThroughputFilter.CONFIG_PROPERTY_NAME_INTERVAL_SIZE, Long.toString(INTERVAL_SIZE_NANOS));
		throughputFilterConfiguration.setProperty(CountingThroughputFilter.CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP,
				Boolean.toString(this.intervalsBasedOn1stTstamp));
		this.throughputFilter = new CountingThroughputFilter(throughputFilterConfiguration, this.analysisController);
		this.analysisController.connect(this.countingFilterReader, CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				this.throughputFilter, CountingThroughputFilter.INPUT_PORT_NAME_RECORDS); // we use this input port because it's easier to test!

		/*
		 * Sink plugin
		 */
		this.sinkPlugin = new ListCollectionFilter<EmptyRecord>(new Configuration(), this.analysisController);
		this.analysisController.connect(this.throughputFilter, CountingThroughputFilter.OUTPUT_PORT_NAME_RELAYED_OBJECTS,
				this.sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
	}

	/**
	 * Returns a list with the given number of {@link IMonitoringRecord}s whose {@link IMonitoringRecord#getLoggingTimestamp()}s are contained in the given interval
	 * bounds.
	 * Not that the first to {@link IMonitoringRecord#getLoggingTimestamp()}s will be assigned to the boundaries of the interval.
	 * 
	 * @param startTimeOfCurInterval
	 *            last timestamp in this interval
	 * @param stopTimeOfCurInterval
	 *            first timestamp in this interval
	 * @param count
	 *            number of events to generate
	 * @return
	 */
	private List<IMonitoringRecord> createRecordsForInterval(final long startTimeOfCurInterval, final long stopTimeOfCurInterval, final int count) {
		final List<IMonitoringRecord> retList = new ArrayList<IMonitoringRecord>(count);

		for (int i = 1; i <= count; i++) {
			final long timestamp;
			switch (i) {
			case 1:
				timestamp = startTimeOfCurInterval;
				break;
			case 2:
				timestamp = stopTimeOfCurInterval;
				break;
			default:
				timestamp = startTimeOfCurInterval + ((stopTimeOfCurInterval - startTimeOfCurInterval) / 2); // just to have a value (most likely) not on the
																												// boundary
				break;
			}
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(timestamp);
			retList.add(r);
		}
		return retList;
	}

	private void createInputEvents(final ListReader<IMonitoringRecord> reader) {
		final long startTimeOfFirstInterval;
		if (this.intervalsBasedOn1stTstamp) {
			startTimeOfFirstInterval = START_TIME_NANOS;
		} else {
			startTimeOfFirstInterval = (START_TIME_NANOS / INTERVAL_SIZE_NANOS) * INTERVAL_SIZE_NANOS;
		}

		final long stopTimeOfFirstInterval = (startTimeOfFirstInterval + INTERVAL_SIZE_NANOS) - 1;

		final int[] expectedCountsForIntervals = { 24, 4, 0, 11, 55, 0, 1 };

		for (int i = 0; i < expectedCountsForIntervals.length; i++) {
			final int countForCurInterval = expectedCountsForIntervals[i];

			final long startTimeOfCurInterval = startTimeOfFirstInterval + (i * INTERVAL_SIZE_NANOS);
			final long stopTimeOfCurInterval = stopTimeOfFirstInterval + (i * INTERVAL_SIZE_NANOS);

			if (countForCurInterval > 0) {
				final List<IMonitoringRecord> recordsForInterval = this.createRecordsForInterval(startTimeOfCurInterval, stopTimeOfCurInterval, countForCurInterval);
				for (final IMonitoringRecord r : recordsForInterval) {
					reader.addObject(r);
				}
			}

			this.expectedThroughputValues.add(new ImmutableEntry<Long, Long>(stopTimeOfCurInterval + 1, (long) countForCurInterval));
		}
	}

	@Test
	public void testIntervalsBasedOn1stTstamp() throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		this.intervalsBasedOn1stTstamp = true;
		this.prepareConfiguration();
		this.doTheTest();

		// Make PMD happy
		Assert.assertTrue(true);
	}

	@Test
	public void testIntervalsBasedOn1970() throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		this.intervalsBasedOn1stTstamp = false;
		this.prepareConfiguration();
		this.doTheTest();

		// Make PMD happy
		Assert.assertTrue(true);
	}

	private void doTheTest() throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		this.createInputEvents(this.simpleListReader);
		Assert.assertEquals(0, this.sinkPlugin.size());

		this.analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.analysisController.getState());

		final Collection<Entry<Long, Long>> throughputListFromFilter = this.throughputFilter.getCountsPerInterval();
		final List<Entry<Long, Long>> throughputListFromFilterAndCurrentInterval = new ArrayList<Map.Entry<Long, Long>>();
		{ // We'll need to append the value for the current (pending) interval // NOCS (nested block)
			throughputListFromFilterAndCurrentInterval.addAll(throughputListFromFilter);
			throughputListFromFilterAndCurrentInterval.add(new ImmutableEntry<Long, Long>(
					this.throughputFilter.getLastTimestampInCurrentInterval() + 1, this.throughputFilter.getCurrentCountForCurrentInterval()));
		}

		Assert.assertEquals(this.expectedThroughputValues, throughputListFromFilterAndCurrentInterval);

		/*
		 * Make sure that all events have been passed through the delay filter
		 */
		Assert.assertEquals("Unexpected number of relayed events", this.countingFilterReader.getMessageCount(), this.sinkPlugin.size());
	}
}
