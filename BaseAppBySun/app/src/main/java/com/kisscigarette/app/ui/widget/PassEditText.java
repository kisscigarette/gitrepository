package com.kisscigarette.app.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kisscigarette.app.R;


/**
 * Created by ted on 2016/7/27.
 * 账号或密码EditText
 */
public class PassEditText extends LinearLayout {
    //要显示的view
    private RelativeLayout view;
    //密码框
    public EditText et_pass;
    //显示密码与隐藏密码按钮
    private CheckBox cb_show_pass;
    //起始未获取焦点图片
    private int header;
    //起始获取焦点图片
    private int headerFocus;
    private ImageView headerImage;
    //删除输入内容图标
    public ImageView tailImage;
    //判断是否是登录界面密码输入
//    private boolean isPassWord;
    private String editType;
    private final String TYPE_NORMAL_EDIT = "0";
    private final String TYPE_HIDE_PASS = "1";
    private final String TYPE_TEXT_PHONE = "2";
    private final String TYPE_TEXT_CODEKEY = "3";
    //hint
    private String hint;

    private Context mContext;
    private Typeface typeFace;

    private OnTextChangedListener textChangedListener;


    public OnTextChangedListener getTextChangedListener() {
        return textChangedListener;
    }

    public void setTextChangedListener(OnTextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    public PassEditText(Context context) {
        this(context, null, 0);
        mContext = context;
    }

    public PassEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public interface OnTextChangedListener {
        public void textChanged(final Editable editable);
    }


    public PassEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_show_edit, null);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WholeEditText);
        header = a.getResourceId(R.styleable.WholeEditText_header, -1);
        headerFocus = a.getResourceId(R.styleable.WholeEditText_headerFocus, -1);
        editType = a.getString(R.styleable.WholeEditText_editType);
        hint = a.getString(R.styleable.WholeEditText_hint);
//        textColorHint = a.getResourceId(R.styleable.WholeEditText_textColorHint, -1);
//        textColor = a.getResourceId(R.styleable.WholeEditText_textColor, -1);
//        textSize = a.getDimensionPixelSize(R.styleable.WholeEditText_textSize, -1);


        a.recycle();
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/FZLTCXHJW.TTF");
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        headerImage = (ImageView) view.findViewById(R.id.img_header);
        tailImage = (ImageView) view.findViewById(R.id.img_tail);
        et_pass = (EditText) view.findViewById(R.id.et_name);
        cb_show_pass = (CheckBox) view.findViewById(R.id.cb_show_pass);

        et_pass.setHint(hint);
       // BaseApplication.getInstance().setEditTextInhibitInputSpace(et_pass);//保证编辑框不能输入空格和回车

        if (header < 0) {
            headerImage.setVisibility(GONE);
        } else {
            headerImage.setVisibility(VISIBLE);
            headerImage.setImageResource(header);
        }

        et_pass.addTextChangedListener(textWatcher);
        if (editType.equals(TYPE_HIDE_PASS)) {
            //如果是密码，对应显示小眼睛
            cb_show_pass.setVisibility(VISIBLE);
            tailImage.setVisibility(INVISIBLE);

            et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_pass.setTypeface(typeFace);

            // 设置密码是否可见
            cb_show_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        et_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        et_pass.setTypeface(typeFace);

                    } else {
                        et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        et_pass.setTypeface(typeFace);
                    }
                    et_pass.setSelection(et_pass.getText().length());
                }
            });

        } else if (editType.equals(TYPE_TEXT_PHONE)) {
            //如果不是密码，对应显示删除按钮
            cb_show_pass.setVisibility(INVISIBLE);
            et_pass.setInputType(InputType.TYPE_CLASS_PHONE);

        } else if (editType.equals(TYPE_NORMAL_EDIT)) {
            cb_show_pass.setVisibility(INVISIBLE);
            et_pass.setInputType(InputType.TYPE_CLASS_TEXT);
            et_pass.setTypeface(typeFace);

        } else if (editType.equals(TYPE_TEXT_CODEKEY)) {
            //如果是密码，对应显示小眼睛
            cb_show_pass.setVisibility(VISIBLE);
            tailImage.setVisibility(INVISIBLE);

            et_pass.setInputType(InputType.TYPE_CLASS_PHONE);
            et_pass.setTypeface(typeFace);
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

            // 设置密码是否可见
            cb_show_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        et_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        et_pass.setTypeface(typeFace);

                    } else {
                        et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        et_pass.setTypeface(typeFace);
                    }
                    et_pass.setSelection(et_pass.getText().length());//调整光标到最后一行
                }
            });

        }


        //编辑框获取焦点则前面图标变色
        et_pass.setOnFocusChangeListener(new
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    if (headerFocus > 0) {
                        headerImage.setImageResource(headerFocus);
                    }

                } else {
                    // 此处为失去焦点时的处理内容
                    if (header > 0) {
                        headerImage.setImageResource(header);
                    }

                }
            }
        });

    }


    //监听编辑框内容变化
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (!editType.equals(TYPE_HIDE_PASS) && !editType.equals(TYPE_TEXT_CODEKEY)) {
                if (!TextUtils.isEmpty(editable)) {
                    tailImage.setVisibility(VISIBLE);
                } else {
                    tailImage.setVisibility(INVISIBLE);
                }
                tailImage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editable.clear();
                        tailImage.setVisibility(INVISIBLE);
                    }
                });

            }

            if (textChangedListener != null) {
                textChangedListener.textChanged(editable);
            }
        }
    };

    /**
     * 获取文本值
     *
     * @return
     */
    public String getText() {
        return et_pass.getText().toString();
    }

    /**
     * 设置要是显示的值
     *
     * @param text
     */
    public void setText(String text) {
        et_pass.setText(text);
        et_pass.setSelection(text.length());
    }

    /**
     * 设置过滤器
     *
     * @param inputFilters
     */
    public void setFilters(InputFilter[] inputFilters) {
        et_pass.setFilters(inputFilters);
    }


    public void setEyeButton(int rid) {
        cb_show_pass.setButtonDrawable(rid);
    }

    public void resetEdit() {
        et_pass.setText("");
        tailImage.setVisibility(INVISIBLE);
    }


    public void setEditContent(@NonNull final String editContent) {
        et_pass.setText(editContent);
    }

}
