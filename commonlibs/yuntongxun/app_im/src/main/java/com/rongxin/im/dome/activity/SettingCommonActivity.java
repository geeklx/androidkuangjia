package com.rongxin.im.dome.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.rongxin.im.dome.R;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.yuntongxun.plugin.common.ui.YTXECSuperActivity;
import com.yuntongxun.plugin.common.ui.base.RXDialogMgr;
import com.yuntongxun.plugin.common.view.YTXSettingItem;
import com.yuntongxun.plugin.im.manager.YTXIMPluginManager;

/**
 * 设置界面
 */
public class SettingCommonActivity extends YTXECSuperActivity {

	private YTXSettingItem headSet;
	private YTXSettingItem clear;
	private YTXSettingItem showNotify;
	private YTXSettingItem audio;
	private YTXSettingItem vibrate;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initResourceRefs();
	}

	private void initView() {
		headSet = findViewById(R.id.headSet);
		clear =  findViewById(R.id.clear);
		showNotify =findViewById(R.id.show_notify);
		audio = findViewById(R.id.audio);
		vibrate = findViewById(R.id.vibrate);


		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showClearChatRecordDialog();
			}
		});

	}

	private void initResourceRefs() {
		headSet.getCheckedTextView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				headSet.toggle();
				YTXIMPluginManager.getManager().useHandSetToPlayVoice(headSet.isChecked());
				initHeadSetting();
			}
		});

		showNotify.getCheckedTextView().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						showNotify.toggle();
						YTXIMPluginManager.getManager().setReceiveMessagesNotify(showNotify.isChecked());
						initNewsNotifySettings();
					}
				});
		audio.getCheckedTextView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				audio.toggle();
				YTXIMPluginManager.getManager().useSoundToNotify(audio.isChecked());
			}
		});
		vibrate.getCheckedTextView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vibrate.toggle();
				YTXIMPluginManager.getManager().useShakeToNotify(vibrate.isChecked());
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initSettings();
	}

	@Override
	public int getLayoutId() {
		return R.layout.ytx_activity_common_setting;
	}

	private void initSettings() {
		boolean mUseHeadSet = YTXIMPluginManager.getManager().getHandSetSetting();
		headSet.setChecked(mUseHeadSet);
		initNewsNotifySettings();
	}


	private void showClearChatRecordDialog() {
		RXDialogMgr.showDialog(this, R.string.app_tip, R.string.fmt_delcontactmsg_confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showPostingDialog("清空聊天记录");
				ECHandlerHelper handlerHelper = new ECHandlerHelper();
				handlerHelper.postRunnOnThead(new Runnable() {
					@Override
					public void run() {
						YTXIMPluginManager.getManager().clearAllChatRecord();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dismissDialog();
							}
						});
					}
				});
			}
		});
	}
	


	/**
	 * 初始化接收新消息通知设置参数  包括声音与振动
	 */
	private void initNewsNotifySettings() {
		if (showNotify == null) {
			return;
		}
		boolean mShowNotifySetting = YTXIMPluginManager.getManager().getReceiveMessagesNotifySetting();
		showNotify.setChecked(mShowNotifySetting);
		showNotify.showDivider(true);
		
		if(!mShowNotifySetting){
			audio.setVisibility(View.GONE);
			vibrate.setVisibility(View.GONE);
			showNotify.showDivider(false);
			return;
		}
		
		boolean mAudioSetting = YTXIMPluginManager.getManager().getSoundNotifySetting();
		audio.setChecked(mAudioSetting);
		audio.setVisibility(View.VISIBLE);
		
		boolean mVibrateSetting = YTXIMPluginManager.getManager().getShakeNotifySetting();
		vibrate.setChecked(mVibrateSetting);
		vibrate.setVisibility(View.VISIBLE);
	}
	private void initHeadSetting(){
		if(headSet == null){
			return;
		}
		boolean mHeadSetting= YTXIMPluginManager.getManager().getHandSetSetting();
		headSet.setChecked(mHeadSetting);
	}

}
