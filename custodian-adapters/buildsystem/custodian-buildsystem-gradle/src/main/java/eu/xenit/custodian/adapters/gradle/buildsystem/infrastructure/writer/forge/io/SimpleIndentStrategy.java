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

package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge.io;

import java.util.Objects;
import java.util.function.Function;

/**
 * A simple indenting strategy that uses a configurable {@code indent} value.
 *
 * @author Stephane Nicoll
 */
public class SimpleIndentStrategy implements Function<Integer, String> {

	private final String indent;

	/**
	 * Create a new instance with the indent style to apply.
	 * @param indent the indent to apply for each indent level
	 */
	public SimpleIndentStrategy(String indent) {
		Objects.requireNonNull(indent, "Indent must be provided");
		this.indent = indent;
	}

	@Override
	public String apply(Integer level) {
		if (level < 0) {
			throw new IllegalArgumentException("Indent level must not be negative, got" + level);
		}
		StringBuilder indentBuilder = new StringBuilder();
		for (int i = 0; i < level; i++) {
			indentBuilder.append(this.indent);
		}
		return indentBuilder.toString();
	}

}