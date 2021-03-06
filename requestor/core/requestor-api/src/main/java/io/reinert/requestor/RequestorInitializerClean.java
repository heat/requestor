/*
 * Copyright 2014 Danilo Reinert
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
package io.reinert.requestor;

import io.reinert.requestor.serialization.misc.TextSerdes;
import io.reinert.requestor.serialization.misc.VoidSerdes;

/**
 * Initializer that configures the Requestor for handling only text plain.
 *
 * @author Danilo Reinert
 */
public class RequestorInitializerClean implements RequestorInitializer {

    @Override
    public void configure(Requestor requestor) {
        requestor.addSerdes(VoidSerdes.getInstance());
        requestor.addSerdes(TextSerdes.getInstance());
    }
}
