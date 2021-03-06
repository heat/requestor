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
package io.reinert.requestor.serialization.json;

import java.util.Collection;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;

import io.reinert.requestor.serialization.DeserializationContext;
import io.reinert.requestor.serialization.Serdes;
import io.reinert.requestor.serialization.SerializationContext;

/**
 * Serializer/Deserializer of Overlay types.
 *
 * @author Danilo Reinert
 */
public class OverlaySerdes implements Serdes<JavaScriptObject> {

    public static boolean USE_SAFE_EVAL = true;

    private static OverlaySerdes INSTANCE = new OverlaySerdes();

    public static OverlaySerdes getInstance() {
        return INSTANCE;
    }

    @Override
    public Class<JavaScriptObject> handledType() {
        return JavaScriptObject.class;
    }

    @Override
    public String[] mediaType() {
        return JsonSerdes.MEDIA_TYPE_PATTERNS;
    }

    @Override
    public JavaScriptObject deserialize(String response, DeserializationContext context) {
        return eval(response);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Collection<JavaScriptObject>> C deserialize(Class<C> collectionType, String response,
                                                                  DeserializationContext context) {
        JsArray<JavaScriptObject> jsArray = eval(response);
        C col = context.getInstance(collectionType);
        for (int i = 0; i < jsArray.length(); i++) {
            JavaScriptObject t = jsArray.get(i);
            col.add(t);
        }
        return col;
    }

    @Override
    public String serialize(JavaScriptObject t, SerializationContext context) {
        return stringify(t);
    }

    @Override
    public String serialize(Collection<JavaScriptObject> c, SerializationContext context) {
        StringBuilder sb = new StringBuilder("[");
        for (JavaScriptObject t : c) {
            sb.append(stringify(t)).append(',');
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    protected <T extends JavaScriptObject> T eval(String response) {
        return USE_SAFE_EVAL ? JsonUtils.<T>safeEval(response) : JsonUtils.<T>unsafeEval(response);
    }

    protected native String stringify(JavaScriptObject jso) /*-{
        return JSON.stringify(jso);
    }-*/;
}
