package hr.fer.zemris.java.tecaj.hw3;

/**
 * Custom implementation of String type. Similar String can share data array.
 * 
 * Note: CString is immutable
 * 
 * @author Luka Skugor
 *
 */
public final class CString {

	/**
	 * array of characters from which CString is built
	 */
	private final char[] data;
	/**
	 * offset from which actual CString starts in the data array
	 */
	private int offset;
	/**
	 * Length of the CString. Defines how many characters to take from the data
	 * array.
	 */
	private int length;

	/**
	 * This is a helper constructor which evaluates how the CString should be
	 * instanced.
	 * 
	 * @param data
	 *            array of characters from which string is built
	 * @param offset
	 *            offset in the data array
	 * @param length
	 *            length of the string
	 * @param safe
	 *            true if the data field is not available to the user
	 */
	private CString(char[] data, int offset, int length, boolean safe) {
		if (data == null) {
			throw new IllegalArgumentException(
					"Can't construct CString from null array.");
		}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset needs to be positive.");
		}
		if (offset + length > data.length) {
			throw new IllegalArgumentException(
					"Offset + length can't be greater than size of the array");
		}

		this.offset = 0;
		this.length = length;
		if (safe && offset == 0 && length == data.length) {
			this.data = data;
		} else {
			this.data = new char[length];

			for (int i = 0; i < length; i++) {
				this.data[i] = data[offset + i];
			}
		}
	}

	/**
	 * Constructs a CString from a char array for given offset and length.
	 * 
	 * @param data
	 *            char array from which string is built
	 * @param offset
	 *            offset in the data array
	 * @param length
	 *            length of the string
	 */
	public CString(char[] data, int offset, int length) {
		this(data, offset, length, false);
	}

	/**
	 * Construct a CString from a char array.
	 * 
	 * @param data
	 *            char array from which string is built
	 */
	public CString(char[] data) {
		this(data, 0, data.length);
	}

	/**
	 * Constructs a CString from existing CString.
	 * 
	 * @param original
	 *            existing CString
	 */
	public CString(CString original) {
		this(original.data, original.offset, original.length, true);
	}

	/**
	 * Constructs a CString from existing String
	 * 
	 * @param s
	 *            existing String
	 */
	public CString(String s) {
		this.data = s.toCharArray();
		this.offset = 0;
		this.length = data.length;
	}

	/**
	 * Return the length of the CString
	 * 
	 * @return length
	 */
	public int length() {
		return length;
	}

	/**
	 * Returns a char from the index position in the CString.
	 * 
	 * @param index
	 *            position in the CString
	 * @return char at the index position
	 */
	public char charAt(int index) {
		if (index >= length || index < 0) {
			throw new IndexOutOfBoundsException("No data at requested index.");
		}

		return data[offset + index];
	}

	/**
	 * Converts the CString to array of characters.
	 * 
	 * @return array of characters built from the CString
	 */
	public char[] toCharArray() {
		char[] copy = new char[length];

		for (int i = 0; i < copy.length; i++) {
			copy[i] = data[offset + i];
		}

		return copy;
	}

	@Override
	public String toString() {
		return String.copyValueOf(data).substring(offset, offset + length);
	}

	/**
	 * Searches the CString for a character and returns the index of the first
	 * one it finds. If none is found returns -1.
	 * 
	 * @param c
	 *            character to find
	 * @return index of the first found character or -1 if nout found
	 */
	public int indexOf(char c) {
		for (int index = 0; index < length; index++) {
			if (data[index + offset] == c) {
				return index;
			}
		}

		return -1;
	}

	/**
	 * Check if CString's start matches requested string.
	 * 
	 * @param s
	 *            starting to match
	 * @return true if matches, else false
	 */
	public boolean startsWith(CString s) {
		if (this.length < s.length) {
			return false;
		}

		for (int index = 0; index < s.data.length; index++) {
			if (data[index + offset] != s.data[index + s.offset]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if CString's ending matches requested string.
	 * 
	 * @param s
	 *            ending to match
	 * @return true if matches, else false
	 */
	public boolean endsWith(CString s) {
		if (this.length < s.length) {
			return false;
		}

		for (int index = 0; index < s.data.length; index++) {
			if (data[length - 1 - index + offset] != s.data[s.length - 1
					- index + s.offset]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the requested string matches any substring of the CString.
	 * 
	 * @param s
	 *            substring to match
	 * @return true if any substring match is found, else false
	 */
	public boolean contains(CString s) {
		Search: for (int index = offset, end = length - s.length + 1; index < end; index++) {
			for (int subIndex = 0; subIndex < s.length; subIndex++) {
				if (data[index + subIndex] != s.data[s.offset + subIndex]) {
					continue Search;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Creates a substring of the CString. Newly created CString has the same
	 * elements of the CString from startIndex to endIndex - 1 and therefore
	 * length is endIndex-startIndex.
	 * 
	 * For example, substring from 3 to 5 of "sample" would be "le".
	 * 
	 * @param startIndex
	 *            start index of a substring (inclusive). Must be positive and less or
	 *            equals than CString's length.
	 * @param endIndex end index of a substring (exclusive). Must be positive and less or
	 *            equals than CString's length.
	 * @return substring
	 * @throws IndexOutOfBoundsException
	 *             if start or end index is invalid
	 */
	public CString substring(int startIndex, int endIndex) {
		if (startIndex < 0 || startIndex >= length) {
			throw new IndexOutOfBoundsException("Start index doesn't exist.");
		}
		if (endIndex < startIndex || endIndex > length) {
			throw new IndexOutOfBoundsException(
					"End index is invalid. Must be greater or equals than startIndex and less or equals than CString's length.");
		}
		CString sub = new CString(this);
		sub.offset = offset + startIndex;
		sub.length = endIndex - startIndex;

		return sub;
	}
	
	/**
	 * Creates a substring of first n characters.
	 * @param n length of the string
	 * @return substring
	 */
	public CString left(int n) {
		if (n > length || n < 0) {
			throw new IllegalArgumentException(
					"CString cannot be constructed. (0 <= n <= length)");
		}
		return substring(0, n);
	}
	
	/**
	 * Creates a substring of last n characters.
	 * @param n length of the string
	 * @return substring
	 */
	public CString right(int n) {
		if (n > length || n < 0) {
			throw new IllegalArgumentException(
					"CString cannot be constructed. (0 <= n <= length)");
		}
		return substring(length - n, length);
	}
	
	/**
	 * Merges two CStrings.
	 * @param s merging CString
	 * @return merged CString
	 */
	public CString add(CString s) {
		int concatLength = length + s.length;
		char[] concatData = new char[concatLength];

		for (int index = 0; index < this.length; index++) {
			concatData[index] = this.data[offset + index];
		}
		for (int index = 0; index < s.length; index++) {
			concatData[index + this.length] = s.charAt(index);
		}

		return new CString(concatData);
	}
	
	/**
	 * Replaces all matching characters with another character.
	 * @param oldChar character to replace
	 * @param newChar replacement character
	 * @return string with replaced characters
	 */
	public CString replaceAll(char oldChar, char newChar) {
		char[] replaced = new char[length];
		for (int index = 0; index < length; index++) {
			if (data[offset + index] == oldChar) {
				replaced[index] = newChar; 
			} else {
				replaced[index] = data[offset + index];
			}
		}
		return new CString(replaced);
	}
	
	/**
	 * Replaces all matching substring with another string.
	 * @param oldStr substring to replace
	 * @param newStr replacement string
	 * @return string with replaced substrings
	 */
	public CString replaceAll(CString oldStr, CString newStr) {
		CString replaced = new CString("");
		Search: for (int index = offset, end = length - oldStr.length + 1; index < end; index++) {
			for (int subIndex = 0; subIndex < oldStr.length; subIndex++) {
				if (data[index + subIndex] != oldStr.data[oldStr.offset + subIndex]) {
					char[] notMatching = {data[index]};
					replaced = replaced.add(new CString(notMatching));
					continue Search;
				}
			}
			replaced = replaced.add(newStr);
			index += oldStr.length - 1;
			
		}
		return new CString(replaced);
	}

}
