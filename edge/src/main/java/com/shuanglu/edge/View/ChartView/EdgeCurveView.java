package com.shuanglu.edge.View.ChartView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.daniel.edge.Utils.Convert.EdgeDensityUtils;
import com.daniel.edge.Utils.Log.EdgeLog;
import com.shuanglu.edge.R;
import com.shuanglu.edge.Utils.AppCompat.EdgeAppCompat;

import java.util.ArrayList;
import java.util.List;

//曲线图
public class EdgeCurveView extends View {
    private List<Point> points = new ArrayList<>();
    private List<Integer> percent = new ArrayList<>();
    private List<String> label = new ArrayList<>();
    private Paint mPaint = new Paint();
    private int border = EdgeDensityUtils.dp2px(58);
    @ColorInt
    private int colorText = Color.BLUE;
    @ColorInt
    private int colorPercentText = Color.BLUE;
    @ColorInt
    private int curveColor = Color.BLUE;

    private int mWidth = 0, mHeight = 0;

    public EdgeCurveView(Context context) {
        super(context);
        init(null);
    }

    public EdgeCurveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EdgeCurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(@Nullable AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CurveView);
        colorPercentText = typedArray.getColor(R.styleable.CurveView_percentTextColor, EdgeAppCompat.getColor(R.color.colorEdgeAccent));
        curveColor = typedArray.getColor(R.styleable.CurveView_curveColor, EdgeAppCompat.getColor(R.color.colorEdgeAccent));
        colorText = typedArray.getColor(R.styleable.CurveView_labelTextColor, EdgeAppCompat.getColor(R.color.colorEdgeAccent));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight() - border;
    }

    public void setData(List<Integer> percent, List<String> label) {
        this.mPaint.reset();
        if (this.percent != null) {
            this.percent.clear();
        }
        if (this.label != null) {
            this.label.clear();
        }
        if (this.points != null) {
            this.points.clear();
        }
        this.percent = percent;
        this.label = label;
    }

    public void refresh() {
        invalidate();
    }

    public void useData() {
        if (percent.size() == label.size()) {
            points.clear();
            int useWidth = 0;
            int space = mWidth / percent.size();
            for (int i = 0; i < percent.size(); i++) {
                EdgeLog.show(getClass(), percent.get(i) + "===");
                points.add(new Point(useWidth + space / 2, (int) (mHeight - mHeight * ((float) percent.get(i) / 100f)) + EdgeDensityUtils.dp2px(58 / 2)));
                useWidth += space;
            }
        } else {
            new Exception("大小不一致");
        }
    }

    public void drawCurve(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setColor(curveColor);
        if (points.size() > 0) {
            for (int i = 0; i < points.size() - 1; i++) {
                Point startP = points.get(i);
                Point endP = points.get(i + 1);
                int wt = (startP.x + endP.x) / 2;
                Point p3 = new Point();
                Point p4 = new Point();
                p3.y = startP.y;
                p3.x = wt;
                p4.y = endP.y;
                p4.x = wt;
                Path path = new Path();
                path.moveTo(startP.x, startP.y);
                path.cubicTo(p3.x, p3.y, p4.x, p4.y, endP.x, endP.y);
                canvas.drawPath(path, mPaint);
            }
        }
    }

    public void drawCircle(Canvas canvas) {
        mPaint.setTextSize(EdgeDensityUtils.sp2px(12));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(curveColor);
        if (points.size() > 0) {
            for (int i = 0; i < points.size(); i++) {
                canvas.drawCircle(points.get(i).x, points.get(i).y, 10, mPaint);
            }
        }
    }

    public void drawText(Canvas canvas) {
        mPaint.setTextSize(EdgeDensityUtils.sp2px(12));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(colorText);
        if (points.size() > 0) {
            for (int i = 0; i < points.size(); i++) {
                canvas.drawText(label.get(i), points.get(i).x, mHeight + EdgeDensityUtils.dp2px(58 - (58 / 2 / 2)), mPaint);
            }
        }
    }

    public void drawPercentText(Canvas canvas) {
        mPaint.setColor(colorPercentText);
        mPaint.setTextAlign(Paint.Align.CENTER);
        if (points.size() > 0) {
            for (int i = 0; i < points.size(); i++) {
                canvas.drawText(percent.get(i) + "%", points.get(i).x, points.get(i).y - EdgeDensityUtils.dp2px(58 / 2 / 2), mPaint);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        useData();
        drawCurve(canvas);
        drawCircle(canvas);
        drawText(canvas);
        drawPercentText(canvas);
    }
}
