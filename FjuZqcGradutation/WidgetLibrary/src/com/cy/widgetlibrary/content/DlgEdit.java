package com.cy.widgetlibrary.content;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import com.cy.widgetlibrary.utils.BgDrawableUtils;

public class DlgEdit extends DlgBase {
	EditDialogListener listener;
	
	boolean showCancel = true;
	public EditText editText;
	private Object data;

	public DlgEdit(Context context, EditDialogListener l) {
		super(context);
		editText = new EditText(context);
		editText.setBackgroundDrawable(BgDrawableUtils.creShape(Color.WHITE, 5, 1,
				Color.parseColor("#ffcacaca"), null));
		editText.setTextColor(Color.parseColor("#ff474747"));
		editText.setHintTextColor(Color.parseColor("#ff9d9c9c"));
		addContentView(editText);
		listener = l;
	}
	
	
	public EditText getEditText() {
		return editText;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public interface EditDialogListener {
		public void onOk(String text);
		public void onCancel();
	}
	
	public static abstract class EditOkListener implements EditDialogListener {
		@Override
		public void onCancel() {
			
		}
	}
	
	public void showCancel(boolean showCancel) {
		this.showCancel = showCancel;
	}
	
	
	@Override
	public void show() {
		show(null,null,null);
	}
	
	public void show(String hint,String title) {
		show(hint,title,null);
	}
		
	public void show(String hint,String title,String defVal) {
		
		if(hint != null) {
			getEditText().setHint(hint);
		}
		
		if(defVal != null) {
			getEditText().setText(defVal);
		}
		
		getBtnLeft().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null) {
					listener.onCancel();
				}
				dialog.dismiss();
			}
		});
		
		getBtnRight().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null) {
					listener.onOk(getEditText().getText().toString().trim());
				}
				dialog.dismiss();
			}
		});
		super.show();
	}
}
