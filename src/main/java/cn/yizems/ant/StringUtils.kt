package cn.yizems.ant

import java.util.*

/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ /**
 * Miscellaneous [String] utility methods.
 *
 *
 * Mainly for internal use within the framework; consider
 * [Apache's Commons Lang](https://commons.apache.org/proper/commons-lang/)
 * for a more comprehensive suite of `String` utilities.
 *
 *
 * This class delivers some simple functionality that should really be
 * provided by the core Java [String] and [StringBuilder]
 * classes. It also provides easy-to-use methods to convert between
 * delimited strings, such as CSV strings, and collections and arrays.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Rick Evans
 * @author Arjen Poutsma
 * @author Sam Brannen
 * @author Brian Clozel
 * @since 16 April 2001
 */
internal object StringUtils {
    private val EMPTY_STRING_ARRAY = arrayOf<String>()

    /**
     * Check that the given `String` is neither `null` nor of length 0.
     *
     * Note: this method returns `true` for a `String` that
     * purely consists of whitespace.
     *
     * @param str the `String` to check (may be `null`)
     * @return `true` if the `String` is not `null` and has length
     * @see .hasText
     */
    fun hasLength(str: String?): Boolean {
        return str != null && !str.isEmpty()
    }

    /**
     * Check whether the given `String` contains actual *text*.
     *
     * More specifically, this method returns `true` if the
     * `String` is not `null`, its length is greater than 0,
     * and it contains at least one non-whitespace character.
     *
     * @param str the `String` to check (may be `null`)
     * @return `true` if the `String` is not `null`, its
     * length is greater than 0, and it does not contain whitespace only
     * @see .hasLength
     * @see Character.isWhitespace
     */
    @JvmStatic
    fun hasText(str: String?): Boolean {
        return str != null && !str.isEmpty() && containsText(str)
    }

    private fun containsText(str: CharSequence): Boolean {
        val strLen = str.length
        for (i in 0 until strLen) {
            if (!Character.isWhitespace(str[i])) {
                return true
            }
        }
        return false
    }

    /**
     * Replace all occurrences of a substring within a string with another string.
     *
     * @param inString   `String` to examine
     * @param oldPattern `String` to replace
     * @param newPattern `String` to insert
     * @return a `String` with the replacements
     */
    fun replace(inString: String, oldPattern: String, newPattern: String?): String {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString
        }
        var index = inString.indexOf(oldPattern)
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString
        }
        var capacity = inString.length
        if (newPattern.length > oldPattern.length) {
            capacity += 16
        }
        val sb = StringBuilder(capacity)
        var pos = 0 // our position in the old string
        val patLen = oldPattern.length
        while (index >= 0) {
            sb.append(inString, pos, index)
            sb.append(newPattern)
            pos = index + patLen
            index = inString.indexOf(oldPattern, pos)
        }

        // append any characters to the right of a match
        sb.append(inString, pos, inString.length)
        return sb.toString()
    }

    /**
     * Tokenize the given `String` into a `String` array via a
     * [StringTokenizer].
     *
     * The given `delimiters` string can consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using @link #delimitedListToStringArray.
     *
     * @param str               the `String` to tokenize (potentially `null` or empty)
     * @param delimiters        the delimiter characters, assembled as a `String`
     * (each of the characters is individually considered as a delimiter)
     * @param trimTokens        trim the tokens via [String.trim]
     * @param ignoreEmptyTokens omit empty tokens from the result array
     * (only applies to tokens that are empty after trimming; StringTokenizer
     * will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens
     * @see StringTokenizer
     *
     * @see String.trim
     */
    @JvmStatic
    @JvmOverloads
    fun tokenizeToStringArray(
        str: String?,
        delimiters: String?,
        trimTokens: Boolean = true,
        ignoreEmptyTokens: Boolean = true
    ): Array<String> {
        if (str == null) {
            return EMPTY_STRING_ARRAY
        }
        val st = StringTokenizer(str, delimiters)
        val tokens: MutableList<String> = ArrayList()
        while (st.hasMoreTokens()) {
            var token = st.nextToken()
            if (trimTokens) {
                token = token.trim { it <= ' ' }
            }
            if (!ignoreEmptyTokens || token.isNotEmpty()) {
                tokens.add(token)
            }
        }
        return tokens.toTypedArray()
    }
}
