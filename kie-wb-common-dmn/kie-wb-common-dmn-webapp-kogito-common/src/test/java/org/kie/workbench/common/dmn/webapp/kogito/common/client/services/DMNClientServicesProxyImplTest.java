/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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
package org.kie.workbench.common.dmn.webapp.kogito.common.client.services;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.dmn.api.definition.model.ItemDefinition;
import org.kie.workbench.common.dmn.api.editors.included.DMNIncludedModel;
import org.kie.workbench.common.dmn.api.editors.included.DMNIncludedNode;
import org.kie.workbench.common.dmn.api.editors.included.IncludedModel;
import org.kie.workbench.common.dmn.api.editors.included.PMMLDocumentMetadata;
import org.kie.workbench.common.dmn.api.editors.included.PMMLIncludedModel;
import org.kie.workbench.common.dmn.api.editors.types.DMNSimpleTimeZone;
import org.kie.workbench.common.dmn.api.editors.types.RangeValue;
import org.kie.workbench.common.stunner.core.client.service.ClientRuntimeError;
import org.kie.workbench.common.stunner.core.client.service.ServiceCallback;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.backend.vfs.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class DMNClientServicesProxyImplTest {

    private static final String MODEL_NAME = "model_name";

    private static final String NAMESPACE = "namespace";

    @Mock
    private Path path;

    private DMNClientServicesProxyImpl service;

    @Before
    public void setup() {
        final TimeZonesProvider timeZonesProvider = spy(new TimeZonesProvider());
        this.service = new DMNClientServicesProxyImpl(timeZonesProvider);

        doCallRealMethod().when(timeZonesProvider).getTimeZones();
        doReturn(new String[]{"A"}).when(timeZonesProvider).getNames();
        doReturn(10.0).when(timeZonesProvider).getOffset("A");
        doReturn("Aa").when(timeZonesProvider).getOffsetString("A");
    }

    @Test
    public void testLoadModels() {
        final ServiceCallback<List<IncludedModel>> callback = newServiceCallback(actual -> assertThat(actual).isEmpty());

        service.loadModels(path, callback);
    }

    @Test
    public void testLoadNodesFromImports() {
        final List<DMNIncludedModel> includedModels = Collections.emptyList();

        final ServiceCallback<List<DMNIncludedNode>> callback = newServiceCallback(actual -> assertThat(actual).isEmpty());

        service.loadNodesFromImports(includedModels, callback);
    }

    @Test
    public void testLoadPMMLDocumentsFromImports() {
        final List<PMMLIncludedModel> includedModels = Collections.emptyList();

        final ServiceCallback<List<PMMLDocumentMetadata>> callback = newServiceCallback(actual -> assertThat(actual).isEmpty());

        service.loadPMMLDocumentsFromImports(path, includedModels, callback);
    }

    @Test
    public void testLoadItemDefinitionsByNamespace() {
        final ServiceCallback<List<ItemDefinition>> callback = newServiceCallback(actual -> assertThat(actual).isEmpty());

        service.loadItemDefinitionsByNamespace(MODEL_NAME, NAMESPACE, callback);
    }

    @Test
    public void testParseFEELList() {
        final ServiceCallback<List<String>> callback = newServiceCallback(actual -> assertThat(actual).containsOnly("\"a\"", "\"b\""));

        service.parseFEELList("\"a\", \"b\"", callback);
    }

    @Test
    public void testParseRangeValue() {
        final ServiceCallback<RangeValue> callback = newServiceCallback(actual -> {
            assertThat(actual.getIncludeStartValue()).isTrue();
            assertThat(actual.getIncludeEndValue()).isTrue();
            assertThat(actual.getStartValue()).isEqualTo("1");
            assertThat(actual.getEndValue()).isEqualTo("2");
        });

        service.parseRangeValue("[1..2]", callback);
    }

    @Test
    public void testIsValidVariableName() {
        final ServiceCallback<Boolean> callback = newServiceCallback(actual -> assertThat(actual).isTrue());

        service.isValidVariableName("", callback);
        service.isValidVariableName("anything", callback);
        service.isValidVariableName("   bad value  ", callback);
    }

    @Test
    public void testGetTimeZones() {
        final ServiceCallback<List<DMNSimpleTimeZone>> callback = newServiceCallback(actual -> {
            assertThat(actual).isNotNull();
            assertThat(actual).isNotEmpty();
        });

        service.getTimeZones(callback);
    }

    private <T> ServiceCallback<T> newServiceCallback(final Consumer<T> assertions) {
        return new ServiceCallback<T>() {
            @Override
            public void onSuccess(final T actual) {
                assertThat(actual).isNotNull();
                assertions.accept(actual);
            }

            @Override
            public void onError(final ClientRuntimeError error) {
                fail(error.getMessage());
            }
        };
    }
}