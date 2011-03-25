/*
 * Copyright 2010-2011 Hippo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.onehippo.forge.properties.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoBean;

import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.bean.PropertiesBean;
import org.onehippo.forge.properties.bean.PropertiesMap;

public class PropertiesManagerImpl implements PropertiesManager {

    // injected by Spring
    private String defaultDocumentLocation;
    private String defaultDocumentName;

    // javadoc from interface
    @Override
    public String getDefaultDocumentLocation() {
        return this.defaultDocumentLocation;
    }

    // javadoc from interface
    @Override
    public String getDefaultDocumentName() {
        return this.defaultDocumentName;
    }

    // javadoc from interface
    @Override
    public PropertiesBean getPropertiesBean(final HippoBean baseBean) {
        if (this.defaultDocumentName == null) {
            throw new IllegalStateException("defaultDocumentName is null: PropertiesManager not correctly configured");
        }

        return this.getPropertiesBean(this.defaultDocumentName, baseBean);
    }

    // javadoc from interface
    @Override
    public PropertiesBean getPropertiesBean(final String path, final HippoBean baseBean) {
        final HippoBean location = getDefaultLocation(baseBean);
        if (path == null) {
            // get document by default name
            return getPropertiesBean(baseBean);
        }
        else {
            // get document by given path
            return getPropertiesBean(location, path);
        }
    }

    // javadoc from interface
    @Override
    public List<PropertiesBean> getPropertiesBeans(final List<String> paths, final HippoBean baseBean) {

        List<PropertiesBean> propertiesBeans = new ArrayList<PropertiesBean>(paths.size());

        if ((paths == null) || (paths.size() == 0)) {
            // get document by default name
            final PropertiesBean propertiesBean = getPropertiesBean(baseBean);
            if (propertiesBean != null) {
                propertiesBeans.add(propertiesBean);
            }
        }
        else {
            // get multiple documents by given paths
            final HippoBean location = getDefaultLocation(baseBean);
            for (final String path : paths) {

                final PropertiesBean propertiesBean = getPropertiesBean(location, path);
                if (propertiesBean != null) {
                    propertiesBeans.add(propertiesBean);
                }
            }
        }

        return propertiesBeans;
    }

    // javadoc from interface
    @Override
    public Map<String, String> getProperties(final HippoBean baseBean) {

        // use new API and merge beans to one map
        final PropertiesBean propertiesBean = getPropertiesBean(baseBean);
        return (propertiesBean != null) ? new PropertiesMap(propertiesBean) : null;
    }

    // javadoc from interface
    @Override
    public Map<String, String> getProperties(final String[] paths, final HippoBean baseBean) {

        // use new API and merge beans to one map
        final List<PropertiesBean> beans = getPropertiesBeans(Arrays.asList(paths), baseBean);
        return new PropertiesMap(beans);
    }

    // javadoc from interface
    @Override
    public void invalidate(final String canonicalPath) {
        throw new UnsupportedOperationException("Invalidation not supported. Use "
                + CachingPropertiesManagerImpl.class.getName() + " instead");
    }

    // setter for Spring injection
    public void setDefaultDocumentLocation(final String defaultDocumentLocation) {
        this.defaultDocumentLocation = defaultDocumentLocation;
    }

    // setter for Spring injection
    public void setDefaultDocumentName(final String defaultDocumentName) {
        this.defaultDocumentName = defaultDocumentName;
    }

    /**
     * Get the base location where the properties documents are stored.
     */
    protected HippoBean getDefaultLocation(HippoBean baseBean) {

        if (baseBean == null) {
            throw new IllegalArgumentException("argument 'baseBean' is null");
        }
        if (this.defaultDocumentLocation == null) {
            throw new IllegalStateException("defaultDocumentLocation is null: PropertiesManager not correctly configured");
        }

        HippoBean defaultLocation;
        if (this.defaultDocumentLocation.startsWith("/")) {
            defaultLocation = baseBean.getBean(this.defaultDocumentLocation.substring(1));
        }
        else {
            defaultLocation = baseBean.getBean(this.defaultDocumentLocation);
        }

        if (defaultLocation == null) {
            throw new IllegalStateException("Default location '" + this.defaultDocumentLocation + 
                    "' relative to " + baseBean.getPath() + " is not a folder in the repository");
        }

        return defaultLocation;
    }

    /**
     * Get a serializable PropertiesBean by location and path.
     */
    protected PropertiesBean getPropertiesBean(final HippoBean location, final String path) {

        if (location == null) {
            throw new IllegalArgumentException("Location bean is null");
        }
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }

        Properties doc = location.getBean(path, Properties.class);
        if (doc != null) {
            return new PropertiesBean(doc);
        }
        return null;
    }
}
