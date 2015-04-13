/*
 * Copyright © 1999-2014 maidoumi, Inc. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fan.framework.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.fan.framework.R;

/******************************************
 * 类描述： 自定义对话框 类名称：MyDialog
 * 
 * @version: 1.0
 * @author: shaoningYang
 * @time: 2014-3-11 14:56
 ******************************************/
public class MyProgressDialog extends Dialog {

	private Animation mAnimation;
	private Animation mReverseAnimation;

	public MyProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initDialog(context);
	}

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
		initDialog(context);
	}

	public MyProgressDialog(Context context) {
		super(context, R.style.MyProgressDialogStyle);
		initDialog(context);
	}

	private void initDialog(Context context) {
		setContentView(R.layout.dialog_progress);
		setCanceledOnTouchOutside(false);
		Window window = getWindow();
		window.setWindowAnimations(R.style.pop_menu_animation);
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(params);
	}
	
	@Override
	public void show() {

		if (mAnimation == null) {
			mAnimation = createForeverRotationAnimation();
			mAnimation.setDuration(3000);
		}
		findViewById(R.id.progress_layout_imageView).startAnimation(
				mAnimation);

		if (mReverseAnimation == null) {
			mReverseAnimation = createForeverReverseRotationAnimation();
			mReverseAnimation.setDuration(3000);
		}
		findViewById(R.id.progress_reverse_layout_imageView)
				.startAnimation(mReverseAnimation);
		super.show();
	}
	
	public void setMessage(CharSequence message){
		((TextView)findViewById(R.id.tv_message)).setText(message);
	}
	/**
	 * 创建一个不停旋转的动画
	 * 
	 * @return 动画实例
	 */
	public static Animation createForeverReverseRotationAnimation() {
		Animation mRotateAnimation = new RotateAnimation(720, 0,
				Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
		mRotateAnimation.setInterpolator(new LinearInterpolator());
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
		mRotateAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
		return mRotateAnimation;
	}

	/**
	 * 创建一个不停旋转的动画
	 * 
	 * @return 动画实例
	 */
	public static Animation createForeverRotationAnimation() {
		Animation mRotateAnimation = new RotateAnimation(0, 1080,
				Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
		mRotateAnimation.setInterpolator(new LinearInterpolator());
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
		mRotateAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
		return mRotateAnimation;
	}
}
