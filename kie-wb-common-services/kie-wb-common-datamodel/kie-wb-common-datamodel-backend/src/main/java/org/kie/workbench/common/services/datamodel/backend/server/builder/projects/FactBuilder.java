/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.kie.workbench.common.services.datamodel.backend.server.builder.projects;

import java.util.HashMap;
import java.util.Map;

import org.kie.soup.project.datamodel.commons.oracle.ModuleDataModelOracleImpl;

/**
 * Builder for Fact Types
 */
public interface FactBuilder {

    public ModuleDataModelOracleBuilder end();

    public default Map<String, FactBuilder> getInternalBuilders() {
        Map<String, FactBuilder> internalBuilders = new HashMap<>();
        addInternalBuilders(internalBuilders);
        return internalBuilders;
    }

    public void addInternalBuilders(Map<String, FactBuilder> builders);

    public void build(final ModuleDataModelOracleImpl oracle);
}
