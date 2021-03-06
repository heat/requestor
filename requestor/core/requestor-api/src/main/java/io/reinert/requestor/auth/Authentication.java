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
package io.reinert.requestor.auth;

import io.reinert.requestor.RequestOrder;

/**
 * Abstraction for HTTP Authentication methods.
 *
 * @author Danilo Reinert
 */
public interface Authentication {

    /**
     * Performs the logic for making the request authenticated, then dispatch the request.
     * <p/>
     *
     * IMPORTANT: You must call requestOrder#send() after the authentication has finished in order to dispatch the
     * request, otherwise the request will never be sent. <br>
     * The request can be sent only once.
     *
     * @param requestOrder  The request about to be sent
     */
    void authenticate(RequestOrder requestOrder);
}
