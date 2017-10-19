package com.zhy.zhy_gesturelockview;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zhy.zhy_gesturelockview.view.GestureLockViewGroup;
import com.zhy.zhy_gesturelockview.view.GestureLockViewGroup.OnGestureLockViewListener;

public class MainActivity extends Activity
{

	private GestureLockViewGroup mGestureLockViewGroup;
	private int number_set_password = 1;
	/**从sp获取当前保存的密码*/
	private int attain_password;
	/**
	 * 第一次设置的密码
	 */
	private String one_password="";
	/**
	 * 第二次设置的密码
	 */
	private String two_password="";

	/**
	 * 存入到sp的密码
	 */
	private int[] current_password;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences shared_get = getApplicationContext().getSharedPreferences("sp_password", MODE_PRIVATE);
		attain_password = shared_get.getInt("password", 0);
		Log.e("当前sp存的值为: ",attain_password+"" );
		if(attain_password==0){
			Log.e("当前密码为", attain_password+"");
			mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
			mGestureLockViewGroup.setOnGestureLockViewListener(no_set_password);
		}else{
			String str = String.valueOf(attain_password);
			current_password = new int[str.length()];
			for (int i = 0; i <str.length() ; i++) {
				current_password[i] = Integer.parseInt(str.substring(i, i + 1));
				Log.e("打印密码的集合", current_password[i]+"");
			}
			mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
			mGestureLockViewGroup.setAnswer(current_password);
			mGestureLockViewGroup.setOnGestureLockViewListener(has_set_password);
		}
	}

	/**
	 * 未设置密码
	 */
	OnGestureLockViewListener no_set_password = new OnGestureLockViewListener() {
		@Override
		public void onUnmatchedExceedBoundary()
		{

		}
		@Override
		public void onGestureEvent(boolean matched)
		{
			if(number_set_password==1&&!one_password.isEmpty()){
				number_set_password += 1;
				Log.e("第一次设置的密码完成，且为: ",one_password );
				/**清空轨迹*/
				mGestureLockViewGroup.reset();
			}
			else if(number_set_password==2&&!two_password.isEmpty()){
				Log.e("第二次设置的密码完成，且为: ",two_password );
				if(one_password.equals(two_password)){
					SharedPreferences shared_put = getApplicationContext().getSharedPreferences("sp_password", MODE_PRIVATE);
					SharedPreferences.Editor put_value_editor = shared_put.edit();
					put_value_editor.putInt("password",Integer.parseInt(two_password));
					put_value_editor.commit();
					Log.e("密码存入sp,且为: ",two_password );
					current_password = new int[two_password.length()];
					for (int i = 0; i <two_password.length() ; i++) {
						current_password[i] = Integer.parseInt(two_password.substring(i, i + 1));
						Log.e("打印设置密码的集合", current_password[i]+"");
					}
					mGestureLockViewGroup.setAnswer(current_password);
					/**密码存到集合*/
					Log.e("密码存到集合为: ", current_password.toString()+"");
					/**清空轨迹*/
					mGestureLockViewGroup.reset();
					/**置空*/
					one_password ="";
					two_password ="";
					number_set_password = 1;
					// TODO: 2017/10/19 调到下个界面
					Toast.makeText(MainActivity.this, "调到下个界面", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MainActivity.this, "2次密码不相同，请重新设置密码", Toast.LENGTH_SHORT).show();
					/**清空轨迹*/
					one_password ="";
					two_password ="";
					number_set_password = 1;
					mGestureLockViewGroup.reset();
				}
			}
		}
		@Override
		public void onBlockSelected(int cId)
		{
			if(number_set_password==1){
				one_password += "" + cId;
				Log.e("第一次设置的密码为: ",one_password );
			}else if(number_set_password==2){
				two_password += "" + cId;
				Log.e("第二次设置的密码为: ",two_password );
			}
		}
	};

	/**
	 * 已设置密码
	 */
	OnGestureLockViewListener has_set_password = new OnGestureLockViewListener() {
		@Override
		public void onUnmatchedExceedBoundary()
		{
			Toast.makeText(MainActivity.this, "错误5次...",
					Toast.LENGTH_SHORT).show();
			mGestureLockViewGroup.setUnMatchExceedBoundary(5);
			one_password ="";
			/**清空轨迹*/
			mGestureLockViewGroup.reset();
		}
		@Override
		public void onGestureEvent(boolean matched)
		{
			one_password ="";
			/**清空轨迹*/
			mGestureLockViewGroup.reset();
			Toast.makeText(MainActivity.this, "是否相同："+matched,
					Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onBlockSelected(int cId)
		{
			if(number_set_password==1){
				one_password += "" + cId;
				Log.e("当前输入的密码为: ",one_password );
			}
		}
	};


}
