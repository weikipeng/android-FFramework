package com.fan.framework.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class FFScrollAlwaysTextView extends TextView {

	public FFScrollAlwaysTextView(Context context) {
		this(context, null);
	}

	public FFScrollAlwaysTextView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.textViewStyle);
	}

	public FFScrollAlwaysTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused)
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	public void onWindowFocusChanged(boolean focused) {
		if (focused)
			super.onWindowFocusChanged(focused);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}