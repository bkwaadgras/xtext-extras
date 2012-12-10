package org.eclipse.xtext.xbase.formatting

import org.eclipse.xtext.preferences.StringKey
import org.eclipse.xtext.preferences.IntegerKey

class BasicFormatterPreferenceKeys {
	public static val lineSeparator = new StringKey("line.separator", "\n")
	public static val indentation = new StringKey("indentation", "\t")
	public static val indentationLength = new IntegerKey("indentation.length", 4)
	public static val maxLineWidth = new IntegerKey("line.width.max", 120)
}
