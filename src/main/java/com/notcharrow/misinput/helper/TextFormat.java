package com.notcharrow.misinput.helper;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextFormat {
	public static Text styledText(String message) {
		return Text.literal(message).formatted(Formatting.GRAY);
	}
}
