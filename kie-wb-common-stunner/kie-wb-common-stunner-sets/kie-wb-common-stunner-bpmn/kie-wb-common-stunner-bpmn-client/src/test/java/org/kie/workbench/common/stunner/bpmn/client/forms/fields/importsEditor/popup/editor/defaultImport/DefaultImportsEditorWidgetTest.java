/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.client.forms.fields.importsEditor.popup.editor.defaultImport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.bpmn.client.forms.DataTypeNamesService;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.imports.DefaultImport;
import org.kie.workbench.common.stunner.core.client.api.SessionManager;
import org.kie.workbench.common.stunner.core.client.canvas.CanvasHandler;
import org.kie.workbench.common.stunner.core.client.session.ClientSession;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.backend.vfs.Path;
import org.uberfire.workbench.events.NotificationEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultImportsEditorWidgetTest {

    @Mock
    private SessionManager sessionManager;

    @Mock
    private ClientSession session;

    @Mock
    private CanvasHandler canvasHandler;

    @Mock
    private Diagram diagram;

    @Mock
    private Metadata metadata;

    @Mock
    private Path path;

    @Mock
    private DataTypeNamesService dataTypeNamesService = mock(DataTypeNamesService.class);

    @Mock
    private Event<NotificationEvent> notification = mock(Event.class);

    private DefaultImportsEditorWidget tested;

    @Before
    public void setUp() throws Exception {
        tested = mock(DefaultImportsEditorWidget.class);
        tested.dataTypes = new TreeMap<>();
        tested.sessionManager = sessionManager;
        tested.dataTypeNamesService = dataTypeNamesService;
        tested.notification = notification;

        doCallRealMethod().when(tested).getDataTypes();
        doCallRealMethod().when(tested).getDataType(anyString());
        doCallRealMethod().when(tested).createImport();
        doCallRealMethod().when(tested).loadDefaultDataTypes();
        doCallRealMethod().when(tested).loadServerDataTypes();
        doCallRealMethod().when(tested).addDataTypes(any(List.class), anyBoolean());

        when(dataTypeNamesService.call(any(Path.class))).thenReturn(null);

        when(sessionManager.getCurrentSession()).thenReturn(session);
        when(session.getCanvasHandler()).thenReturn(canvasHandler);
        when(canvasHandler.getDiagram()).thenReturn(diagram);
        when(diagram.getMetadata()).thenReturn(metadata);
        when(metadata.getPath()).thenReturn(path);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor() {
        DefaultImportsEditorWidget widget = spy(new DefaultImportsEditorWidget(sessionManager,
                                                                               dataTypeNamesService,
                                                                               notification));
        verify(widget, times(1)).loadDefaultDataTypes();
        verify(widget, times(1)).loadServerDataTypes();
    }

    @Test
    public void getDataTypes() {
        tested.dataTypes = new TreeMap<>();
        tested.dataTypes.put("test1", "test1");
        tested.dataTypes.put("test2", "test2");
        tested.dataTypes.put("test3", "test3");

        Map<String, String> dataTypes = tested.getDataTypes();
        assertEquals(tested.dataTypes, dataTypes);
    }

    @Test
    public void getDataType() {
        tested.dataTypes = new TreeMap<>();
        final String testValue = "testValue";
        tested.dataTypes.put(testValue, testValue);
        String result = tested.getDataType(testValue);
        assertEquals(testValue, result);
    }

    @Test
    public void createImport() {
        DefaultImport expected = new DefaultImport();
        DefaultImport result = tested.createImport();
        assertEquals(expected, result);
    }

    @Test
    public void loadDefaultDataTypes() {
        tested.dataTypes = new TreeMap<>();
        tested.loadDefaultDataTypes();

        assertEquals(5, tested.dataTypes.size());
        assertTrue(tested.dataTypes.containsKey("Boolean"));
        assertTrue(tested.dataTypes.containsKey("Float"));
        assertTrue(tested.dataTypes.containsKey("Integer"));
        assertTrue(tested.dataTypes.containsKey("Object"));
        assertTrue(tested.dataTypes.containsKey("String"));
    }

    @Test(expected = NullPointerException.class)
    public void loadServerDataTypes() {
        tested.dataTypes = new TreeMap<>();
        tested.loadServerDataTypes();

        verify(sessionManager, times(1)).getCurrentSession();
        verify(session, times(1)).getCanvasHandler();
        verify(canvasHandler, times(1)).getDiagram();
        verify(diagram, times(1)).getMetadata();
        verify(metadata, times(1)).getPath();
        verify(dataTypeNamesService, times(1)).call(path);
    }

    @Test
    public void addDataTypes() {
        tested.dataTypes = new TreeMap<>();

        List<String> dataTypes1 = new ArrayList<>();
        dataTypes1.add("test1");

        List<String> dataTypes2 = new ArrayList<>();
        dataTypes2.add("org.test.test2");

        tested.addDataTypes(dataTypes1, false);
        tested.addDataTypes(dataTypes2, true);

        assertEquals(2, tested.dataTypes.size());
        assertTrue(tested.dataTypes.containsKey("test1"));
        assertTrue(tested.dataTypes.containsKey("org.test.test2"));
        assertTrue(tested.dataTypes.containsValue("test1"));
        assertTrue(tested.dataTypes.containsValue("test2 [org.test]"));
    }
}