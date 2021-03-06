/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.namedRecordPipe;

import kieker.common.record.IMonitoringRecord;

/**
 * This is a simple interface for a writer that works on a pipe.
 *
 * @author Andre van Hoorn, Christian Wulf
 *
 * @since 1.5
 *
 * @deprecated since 1.13 (to be removed in 1.14) Use {@link PipeWriter} instead.
 */
@Deprecated
public interface IPipeWriter {

	/**
	 * Called for each new record.
	 *
	 * @param record
	 *            the record.
	 *
	 * @since 1.5
	 */
	public void writeMonitoringRecord(final IMonitoringRecord record);
}
