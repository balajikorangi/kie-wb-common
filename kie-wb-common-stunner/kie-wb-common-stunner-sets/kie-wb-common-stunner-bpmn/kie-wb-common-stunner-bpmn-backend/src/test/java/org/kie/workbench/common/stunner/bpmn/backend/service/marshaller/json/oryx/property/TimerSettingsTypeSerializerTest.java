/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.backend.service.marshaller.json.oryx.property;

import org.junit.Before;
import org.junit.Test;
import org.kie.workbench.common.stunner.bpmn.backend.marshall.json.oryx.property.TimerSettingsTypeSerializer;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.timer.TimerSettingsValue;

import static org.junit.Assert.assertEquals;

public class TimerSettingsTypeSerializerTest {

    private static final char DELIMITER = '|';

    private static final String TIME_DATE = "TIME_DATE";

    private static final String TIME_DURATION = "TIME_DURATION";

    private static final String TIME_CYCLE = "TIME_CYCLE";

    private static final String TIME_CYCLE_LANGUAGE = "TIME_CYCLE_LANGUAGE";

    private TimerSettingsTypeSerializer serializer;

    private static final String timerSettingsSerialized = TIME_DATE + DELIMITER +
            TIME_DURATION + DELIMITER +
            TIME_CYCLE + DELIMITER +
            TIME_CYCLE_LANGUAGE;

    private TimerSettingsValue timerSettings;

    @Before
    public void setUp() {
        timerSettings = new TimerSettingsValue(TIME_DATE,
                                               TIME_DURATION,
                                               TIME_CYCLE,
                                               TIME_CYCLE_LANGUAGE);
        serializer = new TimerSettingsTypeSerializer();
    }

    @Test
    public void testParse() {

        TimerSettingsValue result = serializer.parse(timerSettingsSerialized);
        assertEquals(TIME_DATE,
                     result.getTimeDate());
        assertEquals(TIME_DURATION,
                     result.getTimeDuration());
        assertEquals(TIME_CYCLE,
                     result.getTimeCycle());
        assertEquals(TIME_CYCLE_LANGUAGE,
                     result.getTimeCycleLanguage());
    }

    @Test
    public void testSerialize() {
        String result = serializer.serialize(timerSettings);
        assertEquals(timerSettingsSerialized,
                     result);
    }

    @Test
    public void testSerializeWithProperty() {
        String result = serializer.serialize(new Object(),
                                             timerSettings);
        assertEquals(timerSettingsSerialized,
                     result);
    }
}