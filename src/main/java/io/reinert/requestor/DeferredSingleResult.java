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

import java.util.logging.Level;
import java.util.logging.Logger;

import io.reinert.gdeferred.impl.DeferredObject;
import io.reinert.requestor.serialization.DeserializationContext;
import io.reinert.requestor.serialization.Deserializer;
import io.reinert.requestor.serialization.SerdesManager;

class DeferredSingleResult<T> extends DeferredObject<T, Throwable, RequestProgress> implements DeferredRequest<T> {

    private static Logger logger = Logger.getLogger(DeferredSingleResult.class.getName());

    private final Class<T> responseType;
    private final SerdesManager serdesManager;
    private final ProviderManager providerManager;

    public DeferredSingleResult(Class<T> responseType, SerdesManager serdesManager, ProviderManager providerManager) {
        this.responseType = responseType;
        this.serdesManager = serdesManager;
        this.providerManager = providerManager;
    }

    @Override
    public DeferredRequest<T> resolve(Request request, Response response) {
        // Check if access to Response was requested
        if (responseType == io.reinert.requestor.Response.class) {
            @SuppressWarnings("unchecked")
            final T result = (T) response;
            super.resolve(result);
            return this;
        }

        final Headers headers = response.getHeaders();
        String responseContentType = headers.getValue("Content-Type");
        if (responseContentType == null) {
            responseContentType = "*/*";
            logger.log(Level.INFO, "Response with no 'Content-Type' header received from '" + request.getUrl()
                    + "'. The content-type value has been automatically set to '*/*' to match deserializers.");
        }

        final Deserializer<T> deserializer = serdesManager.getDeserializer(responseType, responseContentType);
        final DeserializationContext context = new HttpDeserializationContext(request.getUrl(), headers,
                responseType, providerManager);
        T result = deserializer.deserialize(response.getText(), context);

        super.resolve(result);
        return this;
    }
}
