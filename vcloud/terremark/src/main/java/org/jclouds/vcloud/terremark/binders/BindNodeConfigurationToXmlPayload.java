/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.vcloud.terremark.binders;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.vcloud.terremark.reference.TerremarkConstants.PROPERTY_TERREMARK_EXTENSION_NS;

import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.MapBinder;
import org.jclouds.rest.binders.BindToStringPayload;

import com.google.common.base.Throwables;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 
 * @author Adrian Cole
 * 
 */
@Singleton
public class BindNodeConfigurationToXmlPayload implements MapBinder {

   private final String ns;
   private final BindToStringPayload stringBinder;

   @Inject
   BindNodeConfigurationToXmlPayload(@Named(PROPERTY_TERREMARK_EXTENSION_NS) String ns, BindToStringPayload stringBinder) {
      this.ns = ns;
      this.stringBinder = stringBinder;
   }

   protected String generateXml(Map<String, String> postParams) throws ParserConfigurationException,
         FactoryConfigurationError, TransformerException {
      XMLBuilder rootBuilder = XMLBuilder.create("NodeService").a("xmlns", ns).a("xmlns:xsi",
            "http://www.w3.org/2001/XMLSchema-instance").a("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
      rootBuilder.e("Name").t(checkNotNull(postParams.get("name"), "name"));
      rootBuilder.e("Enabled").t(checkNotNull(postParams.get("enabled"), "enabled"));
      if (postParams.containsKey("description") && postParams.get("description") != null)
         rootBuilder.e("Description").t(postParams.get("description"));
      Properties outputProperties = new Properties();
      outputProperties.put(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
      return rootBuilder.asString(outputProperties);
   }

   @Override
   public void bindToRequest(HttpRequest request, Map<String, String> postParams) {
      try {
         stringBinder.bindToRequest(request, generateXml(postParams));
      } catch (Exception e) {
         Throwables.propagate(e);
      }
   }

   @Override
   public void bindToRequest(HttpRequest request, Object input) {
      throw new IllegalArgumentException("this is a map binder");
   }
}
