package com.jh571121692developer.myplant.CustomPromptDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jh571121692developer.myplant.R;

import cn.refactor.lib.colordialog.AnimationLoader;
import cn.refactor.lib.colordialog.util.DisplayUtil;

/**
 * 作者 : andy
 * 日期 : 15/11/10 11:28
 * 邮箱 : andyxialm@gmail.com
 * 描述 : 提示性的Dialog
 */
public class CustomPromptDialog extends Dialog {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int DEFAULT_RADIUS     = 6;
    public static final int DIALOG_TYPE_INFO    = 0;
    public static final int DIALOG_TYPE_HELP    = 1;
    public static final int DIALOG_TYPE_WRONG   = 2;
    public static final int DIALOG_TYPE_SUCCESS = 3;
    public static final int DIALOG_TYPE_WARNING = 4;
    public static final int DIALOG_TYPE_DEFAULT = DIALOG_TYPE_INFO;

    private AnimationSet mAnimIn, mAnimOut;
    private View mDialogView;
    private TextView mTitleTv, mContentTv, mPositiveBtn;
    public TextView numTv;
    private OnPositiveListener mOnPositiveListener;
    //My custom
    private ImageView addTv, delTv;
    View contentView;
    private Drawable mdrawable;

    private int mDialogType;
    private boolean mIsShowAnim;
    private CharSequence mTitle, mContent, mBtnText;

    public CustomPromptDialog(Context context) {
        this(context, 0);
    }

    public CustomPromptDialog(Context context, int theme) {
        super(context, cn.refactor.lib.colordialog.R.style.color_dialog);
        init();
    }

    public CustomPromptDialog(Context context, Drawable drawable) {
        this(context, 0);
        this.mdrawable = drawable;
        init();
    }

    private void init() {
        mAnimIn = cn.refactor.lib.colordialog.AnimationLoader.getInAnimation(getContext());
        mAnimOut = AnimationLoader.getOutAnimation(getContext());
    }

    public int getCurrentDay(){
        return Integer.parseInt(numTv.getText().toString());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initListener();

    }

    private void initView() {
        contentView = View.inflate(getContext(), R.layout.layout_custom_promptdialog, null);
        setContentView(contentView);
        resizeDialog();

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTv = (TextView) contentView.findViewById(R.id.tvTitle);
        mContentTv = (TextView) contentView.findViewById(R.id.tvContent);
        mPositiveBtn = (TextView) contentView.findViewById(R.id.btnPositive);

        addTv = contentView.findViewById(R.id.tvPlus);
        delTv = contentView.findViewById(R.id.tvDel);
        numTv = contentView.findViewById(R.id.tvNum);

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(numTv.getText().toString());
                current = current + 1;
                numTv.setText(String.valueOf(current));
            }
        });

        delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(numTv.getText().toString());
                current = current - 1;
                if(current < 0) current = 0;
                numTv.setText(String.valueOf(current));
            }
        });

        View llBtnGroup = findViewById(cn.refactor.lib.colordialog.R.id.llBtnGroup);
        ImageView logoIv = (ImageView) contentView.findViewById(cn.refactor.lib.colordialog.R.id.logoIv);
        if(mdrawable == null){
            logoIv.setBackgroundResource(getLogoResId(mDialogType));
        }
        else{
            logoIv.setBackground(mdrawable);
        }

        LinearLayout topLayout = (LinearLayout) contentView.findViewById(cn.refactor.lib.colordialog.R.id.topLayout);
        ImageView triangleIv = new ImageView(getContext());
        triangleIv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(getContext(), 10)));
        triangleIv.setImageBitmap(createTriangel((int) (DisplayUtil.getScreenSize(getContext()).x * 0.7), DisplayUtil.dp2px(getContext(), 10)));
        topLayout.addView(triangleIv);

        setBtnBackground(mPositiveBtn);
        setBottomCorners(llBtnGroup);


        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[] { radius, radius, radius, radius, 0, 0, 0, 0 };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(getColorResId(mDialogType)));
        LinearLayout llTop = (LinearLayout) findViewById(cn.refactor.lib.colordialog.R.id.llTop);
        llTop.setBackgroundDrawable(shapeDrawable);

        mTitleTv.setText(mTitle);
        mContentTv.setText(mContent);
        mPositiveBtn.setText(mBtnText);
    }
    private void resizeDialog() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int)(DisplayUtil.getScreenSize(getContext()).x * 0.7);
        getWindow().setAttributes(params);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startWithAnimation(mIsShowAnim);
    }

    @Override
    public void dismiss() {
        dismissWithAnimation(mIsShowAnim);
    }

    private void startWithAnimation(boolean showInAnimation) {
        if (showInAnimation) {
            mDialogView.startAnimation(mAnimIn);
        }
    }

    private void dismissWithAnimation(boolean showOutAnimation) {
        if (showOutAnimation) {
            mDialogView.startAnimation(mAnimOut);
        } else {
            super.dismiss();
        }
    }

    private int getLogoResId(int mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return cn.refactor.lib.colordialog.R.mipmap.ic_info;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return cn.refactor.lib.colordialog.R.mipmap.ic_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return cn.refactor.lib.colordialog.R.mipmap.ic_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return cn.refactor.lib.colordialog.R.mipmap.ic_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return cn.refactor.lib.colordialog.R.mipmap.ic_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return cn.refactor.lib.colordialog.R.mipmap.icon_warning;
        }
        return cn.refactor.lib.colordialog.R.mipmap.ic_info;
    }

    private int getColorResId(int mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return cn.refactor.lib.colordialog.R.color.color_type_info;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return cn.refactor.lib.colordialog.R.color.color_type_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return cn.refactor.lib.colordialog.R.color.color_type_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return cn.refactor.lib.colordialog.R.color.color_type_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return cn.refactor.lib.colordialog.R.color.color_type_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return cn.refactor.lib.colordialog.R.color.color_type_warning;
        }
        return cn.refactor.lib.colordialog.R.color.color_type_info;
    }

    private int getSelBtn(int mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return cn.refactor.lib.colordialog.R.drawable.sel_btn;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return cn.refactor.lib.colordialog.R.drawable.sel_btn_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return cn.refactor.lib.colordialog.R.drawable.sel_btn_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return cn.refactor.lib.colordialog.R.drawable.sel_btn_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return cn.refactor.lib.colordialog.R.drawable.sel_btn_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return cn.refactor.lib.colordialog.R.drawable.sel_btn_warning;
        }
        return cn.refactor.lib.colordialog.R.drawable.sel_btn;
    }

    private void initAnimListener() {
        mAnimOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        callDismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void initListener() {
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPositiveListener != null) {
                    mOnPositiveListener.onClick(CustomPromptDialog.this);
                }
            }
        });

        initAnimListener();
    }

    private void callDismiss() {
        super.dismiss();
    }

    private Bitmap createTriangel(int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(width, height, getContext().getResources().getColor(getColorResId(mDialogType)));
    }

    private Bitmap getBitmap(int width, int height, int backgroundColor) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width / 2, height);
        path.close();

        canvas.drawPath(path, paint);
        return bitmap;

    }


    private void setBtnBackground(final TextView btnOk) {
        btnOk.setTextColor(createColorStateList(getContext().getResources().getColor(getColorResId(mDialogType)),
                getContext().getResources().getColor(cn.refactor.lib.colordialog.R.color.color_dialog_gray)));
        btnOk.setBackgroundDrawable(getContext().getResources().getDrawable(getSelBtn(mDialogType)));
    }


    private void setBottomCorners(View llBtnGroup) {
        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[] { 0, 0, 0, 0, radius, radius, radius, radius };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.WHITE);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        llBtnGroup.setBackgroundDrawable(shapeDrawable);
    }

    private ColorStateList createColorStateList(int normal, int pressed) {
        return createColorStateList(normal, pressed, Color.BLACK, Color.BLACK);
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public CustomPromptDialog setAnimationEnable(boolean enable) {
        mIsShowAnim = enable;
        return this;
    }

    public CustomPromptDialog setTitleText(CharSequence title) {
        mTitle = title;
        return this;
    }

    public CustomPromptDialog setTitleText(int resId) {
        return setTitleText(getContext().getString(resId));
    }

    public CustomPromptDialog setContentText(CharSequence content) {
        mContent = content;
        return this;
    }

    public CustomPromptDialog setContentText(int resId) {
        return setContentText(getContext().getString(resId));
    }

    public TextView getTitleTextView() {
        return mTitleTv;
    }

    public TextView getContentTextView() {
        return mContentTv;
    }

    public CustomPromptDialog setDialogType(int type) {
        mDialogType = type;
        return this;
    }

    public int getDialogType() {
        return mDialogType;
    }

    public CustomPromptDialog setPositiveListener(CharSequence btnText, OnPositiveListener l) {
        mBtnText = btnText;
        return setPositiveListener(l);
    }

    public CustomPromptDialog setPositiveListener(int stringResId, OnPositiveListener l) {
        return setPositiveListener(getContext().getString(stringResId), l);
    }

    public CustomPromptDialog setPositiveListener(OnPositiveListener l) {
        mOnPositiveListener = l;
        return this;
    }

    public CustomPromptDialog setAnimationIn(AnimationSet animIn) {
        mAnimIn = animIn;
        return this;
    }

    public CustomPromptDialog setAnimationOut(AnimationSet animOut) {
        mAnimOut = animOut;
        initAnimListener();
        return this;
    }

    public interface OnPositiveListener {
        void onClick(CustomPromptDialog dialog);
    }

}
