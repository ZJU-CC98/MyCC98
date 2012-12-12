/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tk.djcrazy.MyCC98.util;

import java.io.Serializable;

import android.content.Intent;

/**
 * Helper for creating intents
 */
public class Intents {

    /**
     * Prefix for all intents created
     */
    public static final String INTENT_PREFIX = "tk.djcrazy.mycc98.";

    /**
     * Prefix for all extra data added to intents
     */
    public static final String INTENT_EXTRA_PREFIX = INTENT_PREFIX + "extra.";

    /**
     * Repository handle
     */
    public static final String EXTRA_BOARD_ID = INTENT_EXTRA_PREFIX
            + "BOARD_ID";

    /**
     * Repository ids collection handle
     */
    public static final String EXTRA_POST_ID = INTENT_EXTRA_PREFIX
            + "POST_ID";

    /**
     * Repository name
     */
    public static final String EXTRA_PAGE_NUMBER = INTENT_EXTRA_PREFIX
            + "PAGE_NUMBER";

     /**
     * Builder for generating an intent configured with extra data such as an
     * issue, repository, or gist
     */
    public static class Builder {

        private final Intent intent;

        /**
         * Create builder with suffix
         *
         * @param actionSuffix
         */
        public Builder(String actionSuffix) {
            // actionSuffix = e.g. "repos.VIEW"
            intent = new Intent(INTENT_PREFIX + actionSuffix);
        }
        
         public Builder() {
            // actionSuffix = e.g. "repos.VIEW"
            intent = new Intent();
        }

         public Builder boardId(String boardId) {
             return add(EXTRA_BOARD_ID, boardId);
         }
           
         public Builder postId(String postId) {
             return add(EXTRA_POST_ID, postId);
         }
           
         public Builder pageNumber(int postNumber) {
             return add(EXTRA_PAGE_NUMBER, postNumber);
         }
           
         /**
         * Add extra field data value to intent being built up
         *
         * @param fieldName
         * @param value
         * @return this builder
         */
        private Builder add(String fieldName, String value) {
            intent.putExtra(fieldName, value);
            return this;
        }

 
        /**
         * Add extra field data value to intent being built up
         *
         * @param fieldName
         * @param value
         * @return this builder
         */  
        private Builder add(String fieldName, int value) {
            intent.putExtra(fieldName, value);
            return this;
        }

        /**
         * Add extra field data value to intent being built up
         *
         * @param fieldName
         * @param values
         * @return this builder
         */
        private Builder add(String fieldName, int[] values) {
            intent.putExtra(fieldName, values);
            return this;
        }

        /**
         * Add extra field data value to intent being built up
         *
         * @param fieldName
         * @param values
         * @return this builder
         */
        private Builder add(String fieldName, boolean[] values) {
            intent.putExtra(fieldName, values);
            return this;
        }

        /**
         * Add extra field data value to intent being built up
         *
         * @param fieldName
         * @param value
         * @return this builder
         */
        private Builder add(String fieldName, Serializable value) {
            intent.putExtra(fieldName, value);
            return this;
        }

        /**
         * Get built intent
         *
         * @return intent
         */
        public Intent toIntent() {
            return intent;
        }
    }
}
