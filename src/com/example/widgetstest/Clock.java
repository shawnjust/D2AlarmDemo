package com.example.widgetstest;

import java.util.TimeZone;
import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

public class Clock extends View {
	private Time mCalendar;
	private Drawable mHourHand; // ʱ��
	private Drawable mMinuteHand; // ����
	private Drawable mDial; // ���̱���

	private Drawable mPoint;

	private Drawable mCenter;

	private int mDialWidth; // ���̿��
	private int mDialHeight; // ���̸߶�
	private boolean mAttached; // ����״̬
	private final Handler mHandler = new Handler(); // ��һ��Handler��ʵ�ָ���ʱ��
	private float mMinutes;
	private float mHour;
	private boolean mChanged; // ʱ���Ƿ�ı�

	public Clock(Context context) {
		this(context, null);
	}

	public Clock(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Clock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Resources r = this.getContext().getResources();
		mDial = r.getDrawable(R.drawable.clock_face2); // ���ر�����Դ
		mHourHand = r.getDrawable(R.drawable.hl);
		mMinuteHand = r.getDrawable(R.drawable.ml);
		mCalendar = new Time(); // ��ȡ��ǰϵͳʱ��
		mDialWidth = mDial.getIntrinsicWidth(); // ��ȡ����ͼƬ�Ŀ��
		mDialHeight = mDial.getIntrinsicHeight(); // �߶ȣ�ͬ��

		mPoint = r.getDrawable(R.drawable.onetimealarm);

		mCenter = r.getDrawable(R.drawable.center);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (!mAttached) {
			mAttached = true;
			IntentFilter filter = new IntentFilter(); // ע��һ����Ϣ����������ȡʱ��ı䡢ʱ���ı��action
			filter.addAction(Intent.ACTION_TIME_TICK);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			getContext().registerReceiver(mIntentReceiver, filter, null,
					mHandler);
		}
		mCalendar = new Time();
		onTimeChanged();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mAttached) {
			getContext().unregisterReceiver(mIntentReceiver); // ��ע����Ϣ������
			mAttached = false;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		float hScale = 1.0f;
		float vScale = 1.0f;
		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
			hScale = (float) widthSize / (float) mDialWidth;
		}
		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
			vScale = (float) heightSize / (float) mDialHeight;
		}
		float scale = Math.min(hScale, vScale);
		setMeasuredDimension(
				resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
				resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mChanged = true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		boolean changed = mChanged;
		if (changed) {
			mChanged = false;
		}
		int availableWidth = this.getWidth() - 100;
		int availableHeight = this.getHeight() - 100;
		int x = this.getWidth() / 2;
		int y = this.getHeight() / 2;
		final Drawable dial = mDial;
		int w = dial.getIntrinsicWidth();
		int h = dial.getIntrinsicHeight();
		boolean scaled = false;
		if (availableWidth < w || availableHeight < h) {
			scaled = true;
			float scale = Math.min((float) availableWidth / (float) w,
					(float) availableHeight / (float) h);
			canvas.save();
			canvas.scale(scale, scale, x, y);
		}
		if (changed) {
			dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
		}
		dial.draw(canvas);
		canvas.save();
		canvas.rotate(mHour / 12.0f * 360.0f, x, y); // ����ʱ����ת�ĽǶȣ�android123��ʾ�����Ǹ�ʱ��ͼƬ����ת�Ƕȣ�ֱ�ӷ�Ӧ�ľ��Ǳ������Ǹ����ʱ��
		final Drawable hourHand = mHourHand;
		if (changed) {
			w = hourHand.getIntrinsicWidth();
			h = hourHand.getIntrinsicHeight();
			hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
					+ (h / 2));
		}
		hourHand.draw(canvas);
		canvas.restore();
		canvas.save();
		canvas.rotate(mMinutes / 60.0f * 360.0f, x, y); // ͬ��������ת�ĽǶ�
		final Drawable minuteHand = mMinuteHand;
		if (changed) {
			w = minuteHand.getIntrinsicWidth();
			h = minuteHand.getIntrinsicHeight();
			minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
					+ (h / 2));
		}
		minuteHand.draw(canvas);
		canvas.restore();

		final Drawable pointDra = mPoint;
		if (changed) {
			w = pointDra.getIntrinsicWidth();
			h = pointDra.getIntrinsicHeight();
			canvas.rotate(mHour / 12.0f * 360.0f, x, y);
			pointDra.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
					+ (h / 2));
		}
		pointDra.draw(canvas);
		canvas.restore();
		canvas.save();
		final Drawable centerDra = mCenter;
		w = centerDra.getIntrinsicWidth();
		h = centerDra.getIntrinsicHeight();
		centerDra.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
		centerDra.draw(canvas);
		canvas.restore();

		if (scaled) {
			canvas.restore();
		}

	}

	private float getHourRotation(float hour) {
		return (hour * 360 / 12);
	}

	private void onTimeChanged() { // ��ȡʱ��ı䣬���㵱ǰ��ʱ����
		mCalendar.setToNow();
		int hour = mCalendar.hour;
		int minute = mCalendar.minute;
		int second = mCalendar.second;
		mMinutes = minute + second / 60.0f;
		mHour = hour + mMinutes / 60.0f;
		mChanged = true;
	}

	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // ������ȡʱ��ı�action
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
				String tz = intent.getStringExtra("time-zone");
				mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
			}
			onTimeChanged(); // ��ȡ�µ�ʱ��

			invalidate(); // ˢ����Ļ��ǿ�������onDraw����ʵ�ַ���ʱ����߶�
		}
	};

	Vector<Object> vec;

	public void addTimePoint(Object obj) {
		vec.add(obj);
	}

	public boolean removeTimePoint(Object obj) {
		return vec.remove(obj);
	}

	OnChoseListener chose;

	interface OnChoseListener {
		public void onChose();
	}

	public void setOnChoseListener(OnChoseListener chose) {
		this.chose = chose;
	}

}