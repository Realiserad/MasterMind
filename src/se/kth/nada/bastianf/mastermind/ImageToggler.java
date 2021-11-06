package se.kth.nada.bastianf.mastermind;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * An image toggler is a placeholder that displays a drawable on the screen. An image toggler differs from an ordinary image view by displaying
 * a second image when the user touches the component with her finger. The default image is restored when the user releases her finger. In this
 * way, an image toggler creates pretty much like an onMouseOver effect in Javascript.
 * @author Realiserad
 */

public class ImageToggler extends ImageView {
	Drawable secondDrawable;
	
	public ImageToggler(Context context) {
		super(context);
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					
				} else if (event.getAction() == MotionEvent.ACTION_UP);
				return true;
			}
		});
	}
	
	public void setSecondDrawable(Drawable secondDrawable) {
		this.secondDrawable = secondDrawable;
	}
}