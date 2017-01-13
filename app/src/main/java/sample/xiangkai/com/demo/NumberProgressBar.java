package sample.xiangkai.com.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * Created by xiangkai on 2017/1/11.
 */

public class NumberProgressBar extends FrameLayout {

    private static final int UPDATEING = 0;
    private static final int UPDATAE_FINISH = 1;
    private TextView tvRate;
    private View leftLine;
    private View rightLine;
    private int rate;
    private int width;

    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == UPDATEING) {
                if (rate == 0) {
//                    计算进度条总宽度
//                    计算总宽度不能放在构造方法里
                    width = rightLine.getMeasuredWidth() - tvRate.getMeasuredWidth() * 2 / 3;
                }
                ++rate;
                tvRate.setText(rate + "%");
                ViewGroup.LayoutParams leftLineLayoutParams = leftLine.getLayoutParams();
                leftLineLayoutParams.width = (int) (NumberProgressBar.this.width * divide100(rate));
                handler.sendEmptyMessageDelayed(rate == 100 ? UPDATAE_FINISH : UPDATEING, 80);
            }
//            返回为false，会继续执行Handler2.handleMessage
            return true;
        }
    };

    Handler handler = new Handler(callback);
    private int leftLineColor;
    private int rateColor;

    public NumberProgressBar(Context context) {
        super(context);
        init(context);

    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberProgressBar);
        leftLineColor = typedArray.getColor(R.styleable.NumberProgressBar_left_line_color, Color.RED);
        rateColor = typedArray.getColor(R.styleable.NumberProgressBar_text_rate_color, Color.RED);
        typedArray.recycle();
        init(context);
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
//        这个地方的关键在于this，连接了布局与view之间的关系
        inflate(context, R.layout.number_progress_bar, this);
        tvRate = (TextView) findViewById(R.id.tv_rate);
        leftLine = findViewById(R.id.left_line);
        rightLine = findViewById(R.id.right_line);
        leftLine.setBackgroundColor(leftLineColor);
        tvRate.setTextColor(rateColor);
        updateRate();
    }

    private void updateRate() {
//        这里可能在子线程中执行
        handler.sendEmptyMessageDelayed(UPDATEING, 1000);
    }

    public double divide100(int rate) {
        BigDecimal value = new BigDecimal(rate);
        BigDecimal result = value.divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
        return result.doubleValue();
    }
}
