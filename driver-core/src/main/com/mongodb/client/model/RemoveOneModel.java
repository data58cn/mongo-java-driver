/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
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

package com.mongodb.client.model;

/**
 * A model describing the removal of at most one document matching the query filter.
 *
 * @param <T> the type of document to update.  In practice this doesn't actually apply to updates but is here for consistency with the
 *           other write models
 * @param <D> the document type. This can be of any type for which a {@code Codec} is registered
 * @since 3.0
 * @mongodb.driver.manual manual/tutorial/remove-documents/ Remove
 */
public class RemoveOneModel<T, D> extends WriteModel<T, D> {
    private final D filter;

    /**
     * Construct a new instance.
     *
     * @param filter a document describing the query filter, which may not be null. The filter can be of any type for which a
     * {@code Codec} is registered
     */
    public RemoveOneModel(final D filter) {
        this.filter = filter;
    }

    /**
     * Gets the query filter.
     *
     * @return the query filter
     */
    public D getFilter() {
        return filter;
    }
}