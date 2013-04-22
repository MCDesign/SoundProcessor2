package mindcastdesign.media.soundtrap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SoundLevelView extends View {

	private int sndLevel = 10;
	private int sndLevelMax = 330;
	
	public SoundLevelView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SoundLevelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SoundLevelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas) {
		Log.d("aaaa","onDraw" );
		RectF rect = new RectF();
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.DKGRAY);		
		
		rect.left = rect.top = 0;
		rect.right = sndLevelMax;
		rect.bottom = 20;
		canvas.drawRoundRect(rect, 10, 199, paint);
		paint.setColor(Color.GREEN);
		//canvas.drawRect(0, 0, sndLevel, 20, paint);
		
		rect.left = rect.top = 0;
		rect.right = sndLevel;
		rect.bottom = 20;
		canvas.drawRoundRect(rect, 10, 199, paint);
	}
	
	public void setLevel(int lvl)
	{
		sndLevel = lvl;
		
		return;
	}
}
