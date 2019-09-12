package eu.xenit.custodian.domain.model.buildsystem;
/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Objects;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
 * A build system that can be used by a generated project.
 *
 * @author Andy Wilkinson
 */
public interface BuildSystem {

    /**
     * The id of the build system.
     * @return the id
     */
    String id();

    /**
     * The dialect of the build system, or {@code null} if the build system does not
     * support multiple dialects.
     * @return the dialect or {@code null}
     */
    default String dialect() {
        return null;
    }

    static BuildSystem forId(String id) {
        return forIdAndDialect(id, null);
    }

    static BuildSystem forIdAndDialect(String id, String dialect) {
        return SpringFactoriesLoader.loadFactories(BuildSystemFactory.class, BuildSystem.class.getClassLoader())
                .stream().map((factory) -> factory.createBuildSystem(id, dialect)).filter(Objects::nonNull).findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Unrecognized build system id '" + id + "' and dialect '" + dialect + "'"));
    }

}
