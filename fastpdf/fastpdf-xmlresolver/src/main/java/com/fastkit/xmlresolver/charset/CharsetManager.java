/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fastkit.xmlresolver.charset;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.SortedMap;

/**
 * Whole lotta charset management routines.
 * 
 * @author Philip Helger
 */
public final class CharsetManager {
	private static final SortedMap<String, Charset> s_aAllCharsets;

	static {
		// Returns an immutable object
		s_aAllCharsets = Charset.availableCharsets();
	}

	@SuppressWarnings("unused")
	private static final CharsetManager s_aInstance = new CharsetManager();

	private CharsetManager() {
	}

	/**
	 * Resolve the charset by the specified name. The difference to
	 * {@link Charset#forName(String)} is, that this method has no checked
	 * exceptions but only unchecked exceptions.
	 * 
	 * @param sCharsetName
	 *            The charset to be resolved. May neither be <code>null</code>
	 *            nor empty.
	 * @return The Charset object
	 * @throws IllegalArgumentException
	 *             If the charset could not be resolved.
	 */
	public static Charset getCharsetFromName(final String sCharsetName) {
		try {
			return Charset.forName(sCharsetName);
		} catch (final IllegalCharsetNameException ex) {
			// Not supported in any version
			throw new IllegalArgumentException("Charset '" + sCharsetName
					+ "' unsupported in Java", ex);
		} catch (final UnsupportedCharsetException ex) {
			// Unsupported on this platform
			throw new IllegalArgumentException("Charset '" + sCharsetName
					+ "' unsupported on this platform", ex);
		}
	}

	/**
	 * @return An immutable collection of all available charsets from the
	 *         standard charset provider.
	 */

	public static SortedMap<String, Charset> getAllCharsets() {
		return s_aAllCharsets;
	}

	public static byte[] getAsBytes(final String sText, final Charset aCharset) {
		if (sText == null)
			throw new NullPointerException("text");
		if (aCharset == null)
			throw new NullPointerException("charset");
		if (!aCharset.canEncode())
			throw new IllegalArgumentException("Cannot encode to " + aCharset);

		// IFJDK5
		// return getAsBytes (sText, aCharset.name ());
		// ELSE
		return sText.getBytes(aCharset);
		// ENDIF
	}

	public static String getAsStringInOtherCharset(final String sText,
			final Charset aCurrentCharset, final Charset aNewCharset) {
		if (aCurrentCharset == null)
			throw new NullPointerException("currentCharset");
		if (aNewCharset == null)
			throw new NullPointerException("newCharset");
		if (sText == null || aCurrentCharset.equals(aNewCharset))
			return sText;

		return getAsString(getAsBytes(sText, aCurrentCharset), aNewCharset);
	}

	public static String getAsString(final byte[] aBuffer,
			final Charset aCharset) {
		if (aBuffer == null)
			throw new NullPointerException("buffer");

		return getAsString(aBuffer, 0, aBuffer.length, aCharset);
	}

	public static String getAsString(final byte[] aBuffer, final int nOfs,
			final int nLength, final Charset aCharset) {
		if (aBuffer == null)
			throw new NullPointerException("buffer");
		if (aCharset == null)
			throw new NullPointerException("charset");

		// IFJDK5
		// return getAsString (aBuffer, nOfs, nLength, aCharset.name ());
		// ELSE
		return new String(aBuffer, nOfs, nLength, aCharset);
		// ENDIF
	}

	/**
	 * Get the number of bytes necessary to represent the passed string as an
	 * UTF-8 string.
	 * 
	 * @param s
	 *            The string to count the length. May be <code>null</code> or
	 *            empty.
	 * @return A non-negative value.
	 */

	public static int getUTF8ByteCount(final String s) {
		return s == null ? 0 : getUTF8ByteCount(s.toCharArray());
	}

	/**
	 * Get the number of bytes necessary to represent the passed char array as
	 * an UTF-8 string.
	 * 
	 * @param aChars
	 *            The characters to count the length. May be <code>null</code>
	 *            or empty.
	 * @return A non-negative value.
	 */

	public static int getUTF8ByteCount(final char[] aChars) {
		int nCount = 0;
		if (aChars != null)
			for (final char c : aChars)
				nCount += getUTF8ByteCount(c);
		return nCount;
	}

	public static int getUTF8ByteCount(final char c) {
		return getUTF8ByteCount((int) c);
	}

	/**
	 * Get the number of bytes necessary to represent the passed character.
	 * 
	 * @param c
	 *            The character to be evaluated.
	 * @return A non-negative value.
	 */

	public static int getUTF8ByteCount(final int c) {
		if (c < Character.MIN_VALUE || c > Character.MAX_VALUE)
			throw new IllegalArgumentException("Invalid parameter: " + c);

		// see JVM spec 4.4.7, p 111
		// http://java.sun.com/docs/books/jvms/second_edition/html/ClassFile.doc.html
		// #1297
		if (c == 0)
			return 2;

		// Source: http://icu-project.org/apiref/icu4c/utf8_8h_source.html
		if (c <= 0x7f)
			return 1;
		if (c <= 0x7ff)
			return 2;
		if (c <= 0xd7ff)
			return 3;

		// It's a surrogate...
		return 0;
	}
}
