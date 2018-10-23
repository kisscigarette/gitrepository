package com.kisscigarette.app.ui.person;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.ActivityManager;
import com.kisscigarette.app.common.DigitUtil;
import com.kisscigarette.app.common.SharePreferencesUtility;
import com.kisscigarette.app.httpFrame.api.LoginApi;
import com.kisscigarette.app.httpFrame.api.UserManagerApi;
import com.kisscigarette.app.httpFrame.entity.result.UserResult;
import com.kisscigarette.app.httpFrame.net.CustomSubscriber;
import com.kisscigarette.app.httpFrame.net.RetrofitManager;
import com.kisscigarette.app.ui.login.LoginActivity;
import com.kisscigarette.app.ui.widget.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.iwf.photopicker.PhotoPicker;

public class PersonFragment extends Fragment {
    private static final int REQUEST_CODE = 100;
    private View rootView;
    private Unbinder unbinder;
    AlertDialog exitDialog;

    @BindView(R.id.image_portrait)
    CircleImageView imageportrait;
    @BindView(R.id.iv_per_bg)
    ImageView ivperbg;
    @BindView(R.id.tv_name)
    TextView tvname;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_person, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        if(SharePreferencesUtility.get(SharePreferencesUtility.IS_LOGIN,false)){
            tvname.setText(SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_NAME,"未登录"));
            updateView();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.layout_issue)
    void layoutissue() {
        FaqActivity.actionStart(getActivity(), "", "");
        //Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.layout_about)
    void layoutabout() {
        Intent intent = new Intent(getActivity(), AboutUsActivity.class);
        intent.putExtra("param1", "");
        intent.putExtra("param2", "");
        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        getActivity().startActivity(intent,compat.toBundle());
        //AboutUsActivity.actionStart(getActivity(), "", "");
    }

    @OnClick(R.id.layout_message)
    void layoutmessage() {
        NotifySetActivity.actionStart(getActivity(), "", "");
        // Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.layout_setting)
    void layoutsetting() {
        Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.lay_person)
    void personorlogin() {
        Boolean isLogin=SharePreferencesUtility.get(SharePreferencesUtility.IS_LOGIN,false);
        if(isLogin) {

            checkpermission();
            //修改头像
            try {

            /*PhotoPickerIntent photoPickerIntent = new PhotoPickerIntent(getActivity());
            photoPickerIntent.setPhotoCount(1);
            photoPickerIntent.setShowCamera(true);
            photoPickerIntent.setShowGif(false);
            startActivityForResult(photoPickerIntent, REQUEST_CODE);*/
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setPreviewEnabled(false)
                        .start(getActivity(), PhotoPicker.REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Intent intent = new Intent(getActivity(),  LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return;
        }
    }

    @OnClick(R.id.exitlogin)
    void exit() {
        exitDialog = new AlertDialog(getActivity()).builder().setTitle(getActivity().getString(R.string.str_quit_account))
                .setMsg("确定退出当前账号？")
                .setPositiveButton("确认退出", 0,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitDialog.dismiss();
                        RetrofitManager.getInstance()
                                .createReq(LoginApi.class)
                                //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                                //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                                .logout("")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CustomSubscriber<Object>(getActivity()) {
                                               @Override
                                               protected void successResult(Object Object) {
                                                   SharePreferencesUtility.update(SharePreferencesUtility.IS_LOGIN,false);
                                                   SharePreferencesUtility.remove(SharePreferencesUtility.HEADPHOTO);
                                                   getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                                                   ActivityManager.finishAllActivityExceptOne(LoginActivity.class);
                                               }

                                               @Override
                                               protected void errorResult(Throwable t) {

                                               }
                                           }

                                );

                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitDialog.dismiss();
                    }
                });
        exitDialog.show();
    }








    private boolean checkpermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);
                return false;
            }else{
                return true;
            }
        } else {
            return true;
        }

    }


    public void updateView() {
        String headmiage= SharePreferencesUtility.get(SharePreferencesUtility.HEADPHOTO,"");
        if(!"".equals(headmiage)) {
            imageportrait.setImageBitmap(DigitUtil.base64ToBitmap(headmiage));
            ivperbg.setImageBitmap(DigitUtil.base64ToBitmap(headmiage));
        }else{
            RetrofitManager.getInstance()
                    .createReq(UserManagerApi.class)
                    .downUser("", SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_NAME,"未登录"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomSubscriber<UserResult>( getActivity()) {
                                   @Override
                                   protected void successResult(UserResult userResult) {
                                       String headmiage=userResult.getHeadImage();
                                       imageportrait.setImageBitmap(DigitUtil.base64ToBitmap(headmiage));
                                       ivperbg.setImageBitmap(DigitUtil.base64ToBitmap(headmiage));
                                       SharePreferencesUtility.save(SharePreferencesUtility.HEADPHOTO,headmiage);
                                   }

                                   @Override
                                   protected void errorResult(Throwable t) {

                                   }
                               }

                    );

        }

    }
}
