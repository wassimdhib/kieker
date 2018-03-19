/*
 * Copyright 2014 NAVER Corp.
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
 */

package kieker.monitoring.common.util;

/**
 * @author emeroad
 */
public class DefaultTimeSlot implements TimeSlot {

    private static final long ONE_MIN_RESOLUTION =  60000; // 1min

    private final long resolution;

    public DefaultTimeSlot() {
        this(ONE_MIN_RESOLUTION);
    }

    public DefaultTimeSlot(long resolution) {
        this.resolution = resolution;
    }

    @Override
    public long getTimeSlot(long time) {
        // not necessary to add ONE_MIN_RESOLUTION as all the timeslots are based on the start value of the given time.
        return (time / resolution) * resolution;
    }
}