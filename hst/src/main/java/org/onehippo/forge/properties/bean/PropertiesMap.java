/*
 * Copyright 2009-2010 Hippo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.properties.bean;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Bean representing a properties document that holds a map of name/value pairs.
 * 
 * In an HST Component, a bean like this can be created with a PropertiesBean object  
 * as argument and subsequently set on the request for access in jsp.  
 * 
 * Example 1:
 *    // get a document named "properties" at same level as current bean
 *    HippoBean propertiesDoc = this.getContentBean(request).getParentBean().getBean("properties"); 
 *    
 *    if (propertiesDoc instanceof Properties) {
 *        request.setAttribute("properties", new PropertiesMap(new PropertiesBean(propertiesDoc)));
 *    }   
 * 
 * There is also a Spring instantiated PropertiesManager present with which 
 * beans can be gotten from content:   
 *    
 * Example 2:
 *    ComponentManager componentManager = (ComponentManager) this.getDefaultClientComponentManager();
 *    this.propertiesManager = componentManager.getComponent(PropertiesManager.class.getName());
 *    
 *    Map properties = this.propertiesManager.getProperties(
 *                                      this.getContentBean(request)
 *                                      this.getSiteContentBaseBean(request));
 *        
 *
 * Because it is a map, access in jsp can be done using direct expression 
 * language notation like "properties['label.header']"
 */
public class PropertiesMap implements Map<String, String> {

    private final Map<String,String> properties = new HashMap<String,String>();

    /**
     * Create a new map with a properties bean.
     */
    public PropertiesMap(final PropertiesBean properties) {

        Iterator<PropertyBean> props = properties.getPropertyBeans().iterator();
        while (props.hasNext()) {
            PropertyBean prop = props.next();
            this.properties.put(prop.getName(), prop.getValue());
        }
    }

    /**
     * Create a new map with multiple properties beans. 
     * 
     * Note that properties are not overwritten, so in case of duplicates the 
     * first entered remains!
     */
    public PropertiesMap(final Collection<PropertiesBean> propertiesCol) {

        Iterator<PropertiesBean> it = propertiesCol.iterator();
        while (it.hasNext()) {
            Iterator<PropertyBean> props = it.next().getPropertyBeans().iterator();
            while (props.hasNext()) {
                PropertyBean prop = props.next();
                if (!this.properties.containsKey(prop.getName())) {
                   this.properties.put(prop.getName(), prop.getValue());
                }
            }
        }
    }

    public void clear() {
        this.properties.clear();
    }

    public boolean containsKey(Object key) {
        return this.properties.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.properties.containsValue(value);
    }

    public Set<Entry<String, String>> entrySet() {
        return this.properties.entrySet();
    }

    public String get(Object key) {
        
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Argument key is not a String but "
                    + ((key == null) ? "null" : key.getClass().getName()));
        }

        return this.properties.get((String) key);
    }

    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    public Set<String> keySet() {
        return this.properties.keySet();
    }

    public String put(String arg0, String arg1) {
        throw new UnsupportedOperationException("PropertiesBean is a readonly object");
    }

    public void putAll(Map<? extends String, ? extends String> arg0) {
        throw new UnsupportedOperationException("PropertiesBean is a readonly object");
    }

    public String remove(Object arg0) {
        throw new UnsupportedOperationException("PropertiesBean is a readonly object");
    }

    public int size() {
        return this.properties.size();
    }

    public Collection<String> values() {
        return this.properties.values();
    }
}