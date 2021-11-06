package se.kth.nada.bastianf.mastermind;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class NumberPickerPreference extends DialogPreference {
	private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
	private static final String BASTIAN_NAMESPACE = "http://mastermind.bastianf.nada.kth.se";
	private static final int DEFAULT_NUMBER_PICKER_VALUE = 4;
	private static final int DEFAULT_NUMBER_PICKER_MAX_VALUE = 6;
	private static final int DEFAULT_NUMBER_PICKER_MIN_VALUE = 3;

	private String dialogMessage;
	private int numberPickerMax;
	private int numberPickerMin;
	private int numberPickerValue;

	private NumberPicker numberPicker;
	private TextView dialogMessageText;

	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		// get attributes specified in XML
		dialogMessage = loadStringAttr(context, attrs, ANDROID_NAMESPACE,
				"dialogMessage");
		numberPickerMax = loadIntAttr(context, attrs, ANDROID_NAMESPACE, "max",
				DEFAULT_NUMBER_PICKER_MAX_VALUE);
		numberPickerMin = loadIntAttr(context, attrs, BASTIAN_NAMESPACE,
				"min", DEFAULT_NUMBER_PICKER_MIN_VALUE);

		// set layout
		setDialogLayoutResource(R.layout.preference_number_picker);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}

	private static String loadStringAttr(Context context, AttributeSet attrs,
			String namespace, String attr) {
		int resId = attrs.getAttributeResourceValue(namespace, attr, 0);
		return resId == 0 ? attrs.getAttributeValue(namespace, attr) : context
				.getString(resId);
	}

	private static int loadIntAttr(Context context, AttributeSet attrs,
			String namespace, String attr, int defaultValue) {
		int resId = attrs.getAttributeResourceValue(namespace, attr, 0);
		return resId == 0 ? attrs.getAttributeIntValue(namespace, attr,
				defaultValue) : context.getResources().getInteger(resId);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		setValue(restore ? getPersistedInt(DEFAULT_NUMBER_PICKER_VALUE)
				: (Integer) defaultValue);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index, DEFAULT_NUMBER_PICKER_VALUE);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		dialogMessageText = (TextView) view
				.findViewById(R.id.text_dialog_message);
		if (dialogMessage != null) {
			dialogMessageText.setText(dialogMessage);
		}

		numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
		numberPicker.setMinValue(numberPickerMin);
		numberPicker.setMaxValue(numberPickerMax);
		numberPicker.setValue(numberPickerValue);
	}

	public void setMax(int max) {
		numberPickerMax = max;

		if (numberPickerValue > numberPickerMax) {
			setValue(numberPickerMax);
		}
	}

	public int getMax() {
		return numberPickerMax;
	}

	public void setMin(int min) {
		numberPickerMin = min;

		if (numberPickerValue < numberPickerMin) {
			setValue(numberPickerMin);
		}
	}

	public int getMin() {
		return numberPickerMin;
	}

	public void setValue(int value) {
		numberPickerValue = value;
		persistInt(numberPickerValue);
	}

	public int getValue() {
		return numberPickerValue;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		// when the user selects "OK", persist the new value
		if (positiveResult) {
			int newNumberPickerValue = numberPicker.getValue();
			if (callChangeListener(newNumberPickerValue)) {
				setValue(newNumberPickerValue);
			}
		}
	}
}