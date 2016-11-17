package com.chenandroid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.chenandroid.R;


/**
 *自定义Dialog，提示用户 
 *
 */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private OnClickListener okListener ;
		private OnClickListener resetPassword;

		public Builder(Context context) {
			this.context = context;
		}




		/**
		 * Set the Dialog title from resource
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 *  设置点击确定
		 * @param listener
         */
		public void setOkListener(OnClickListener listener){
			this.okListener = listener ;
		}


		public void setResetPassword(OnClickListener listener){
			this.resetPassword = listener ;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
			View layout = inflater.inflate(R.layout.dialog_custom, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title

			layout.findViewById(R.id.Reset_protection).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(okListener != null){
						okListener.onClick(dialog, BUTTON_POSITIVE);
					}else{
						dialog.dismiss();
					}
				}
			});

			layout.findViewById(R.id.resetPas).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(resetPassword != null){
						resetPassword.onClick(dialog, BUTTON_POSITIVE);
					}else{
						dialog.dismiss();
					}
				}
			});
			layout.findViewById(R.id.cen).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

						dialog.dismiss();

				}
			});
			return dialog;
		}



		
	}
	
	
	
}
