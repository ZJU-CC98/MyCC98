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

import android.app.Activity;
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

    public static final String EXTRA_BOARD_ID = INTENT_EXTRA_PREFIX
            + "BOARD_ID";
    public static final String EXTRA_BOARD_NAME = INTENT_EXTRA_PREFIX
            + "BOARD_NAME";

    public static final String EXTRA_POST_ID = INTENT_EXTRA_PREFIX
            + "POST_ID";
    public static final String EXTRA_POST_NAME = INTENT_EXTRA_PREFIX
            + "POST_NAME";

    public static final String EXTRA_PAGE_NUMBER = INTENT_EXTRA_PREFIX
            + "PAGE_NUMBER";
    
    public static final String EXTRA_REPLY_USER_NAME = INTENT_EXTRA_PREFIX
            + "REPLY_USER_NAME";
    public static final String EXTRA_REPLY_USER_POST_TIME = INTENT_EXTRA_PREFIX
            + "REPLY_USER_POST_TIME";
    public static final String EXTRA_REPLY_CONTENT = INTENT_EXTRA_PREFIX
            + "REPLY_CONTENT";
    public static final String EXTRA_FLOOR_NUMBER = INTENT_EXTRA_PREFIX
            + "FLOOR_NUMBER";
    public static final String EXTRA_IS_QUOTE_USER = INTENT_EXTRA_PREFIX
            + "IS_QUOTE_USER";
    public static final String EXTRA_PM_TO_USER = INTENT_EXTRA_PREFIX
            + "PM_TO_USER";
    public static final String EXTRA_PM_TITLE = INTENT_EXTRA_PREFIX
            + "PM_TITLE";
    public static final String EXTRA_PM_CONTENT = INTENT_EXTRA_PREFIX
            + "PM_CONTENT";
    public static final String EXTRA_REQUEST_TYPE = INTENT_EXTRA_PREFIX
            + "REQUEST_TYPE";

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
            intent = new Intent();
       }
        
        public Builder(Activity activity, Class<?> class1) {
            intent = new Intent(activity, class1);
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
         public Builder boardName(String name) {
             return add(EXTRA_BOARD_NAME, name); 
         }
         public Builder postName(String name) {
             return add(EXTRA_POST_NAME, name);
         }
         public Builder replyUserName(String name) {
             return add(EXTRA_REPLY_USER_NAME, name);
         }
         public Builder replyUserPostTime(String time) {
             return add(EXTRA_REPLY_USER_POST_TIME, time);
         }
         public Builder replyContent(String content) {
             return add(EXTRA_REPLY_CONTENT, content);
         }
         public Builder floorNumber(int floorNum) {
             return add(EXTRA_FLOOR_NUMBER, floorNum);
         }
         public Builder isQuoteUser(boolean is) {
             return add(EXTRA_IS_QUOTE_USER, is);
         }
         
         public Builder pmToUser(String name) {
             return add(EXTRA_PM_TO_USER, name);
         }
         
         public Builder pmTitle(String name) {
             return add(EXTRA_PM_TITLE, name);
         }
         
         public Builder pmContent(String content) {
             return add(EXTRA_PM_CONTENT, content);
         }
         
         public Builder requestType(int type) {
             return add(EXTRA_REQUEST_TYPE, type);
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
