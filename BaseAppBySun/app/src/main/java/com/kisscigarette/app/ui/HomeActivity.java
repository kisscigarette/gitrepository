package com.kisscigarette.app.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kisscigarette.app.R;
import com.kisscigarette.app.common.ActivityManager;
import com.kisscigarette.app.common.BaseActivity;
import com.kisscigarette.app.common.DigitUtil;
import com.kisscigarette.app.common.SharePreferencesUtility;
import com.kisscigarette.app.httpFrame.api.UserManagerApi;
import com.kisscigarette.app.httpFrame.entity.request.UserRequest;
import com.kisscigarette.app.httpFrame.entity.result.DataResult;
import com.kisscigarette.app.httpFrame.net.CustomSubscriber;
import com.kisscigarette.app.httpFrame.net.RetrofitManager;
import com.kisscigarette.app.ui.message.MessageFragment;
import com.kisscigarette.app.ui.person.PersonFragment;
import com.kisscigarette.app.utils.FragmentSwitchTool;
import com.kisscigarette.app.utils.PopupMenuUtil;
import com.kisscigarette.app.utils.SnackbarUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindViews;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.iwf.photopicker.PhotoPicker;

public class HomeActivity extends BaseActivity {

    private Context context;
    private ImageView ivImg;

    @BindViews({R.id.llrent, R.id.llanalyse, R.id.llmessage, R.id.llperson})
    LinearLayout[] llList;
    @BindViews({R.id.ivrent, R.id.ivanalyse, R.id.ivmessage, R.id.ivperson})
    ImageView[] ivList;
    @BindViews({R.id.tvrent, R.id.tvanalyse, R.id.tvmessage, R.id.tvperson})
    TextView[] tvList;


    private FragmentSwitchTool tool;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void init() {
        initViews();
        //UtilsStyle.setStatusBarLightMode(getWindow());
        tool = new FragmentSwitchTool(getFragmentManager(), R.id.homeContainer);
        tool.setClickableViews(llList);
        tool.addSelectedViews(new View[]{ivList[0], tvList[0]}).addSelectedViews(new View[]{ivList[1], tvList[1]})
                .addSelectedViews(new View[]{ivList[2], tvList[2]}).addSelectedViews(new View[]{ivList[3], tvList[3]});
        tool.setFragments(MainActivityFragment.class, MessageFragment.class, MessageFragment.class, PersonFragment.class);

        tool.changeTag(llList[0]);
    }


    private void initViews() {
        context = this;
        ivImg = (ImageView) findViewById(R.id.iv_img);


    }

    @OnClick(R.id.rl_click)
    void openPopMenu() {
        PopupMenuUtil.getInstance()._show(context, ivImg);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if(PopupMenuUtil.getInstance()._isShowing()){
            return false;
        }
        return super.dispatchTouchEvent(event);
    }


    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 当popupWindow 正在展示的时候 按下返回键 关闭popupWindow 否则关闭activity
            if (PopupMenuUtil.getInstance()._isShowing()) {
                PopupMenuUtil.getInstance()._rlClickAction();
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出当前程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
                    launcherIntent.addCategory(Intent.CATEGORY_HOME);
                    ActivityManager.exitApp();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                SnackbarUtil.LongSnackbar(ivImg,"修改失败",SnackbarUtil.Warning).show();
            } else if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    String bitString = DigitUtil.zoomPhoto(bitmap);
                    String username=SharePreferencesUtility.get(SharePreferencesUtility.KEY_USER_NAME,"");
                    if (bitString != null) {
                        SharePreferencesUtility.save(SharePreferencesUtility.HEADPHOTO, bitString);

                        RetrofitManager.getInstance()
                                .createReq(UserManagerApi.class)
                                .updateUser("", new UserRequest(username,bitString))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CustomSubscriber<DataResult>(this) {
                                               @Override
                                               protected void successResult(DataResult dataResult) {
                                                   SnackbarUtil.LongSnackbar(ivImg,"头像修改成功!",SnackbarUtil.Info).show();
                                               }

                                               @Override
                                               protected void errorResult(Throwable t) {

                                               }
                                           }

                                );


                    }
                    PersonFragment fragment = (PersonFragment) getFragmentManager().findFragmentByTag(String.valueOf(llList[3].getId()));
                    fragment.updateView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                SnackbarUtil.LongSnackbar(ivImg,"修改失败",SnackbarUtil.Warning).show();
            }
        }

        if (requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                File temp = new File(photos.get(0));
                if (temp.exists()) {
                    Uri uri = Uri.fromFile(temp);
                    startPhotoZoom(uri);
                }
            }
        }


    }

    private void startPhotoZoom(Uri uri) {
        CropImage.activity(uri)
                //.setGuidelines(CropImageView.Guidelines.ON)
                //.setActivityTitle("裁剪")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("完成")
                .setRequestedSize(400, 400)
                //.setCropMenuCropButtonIcon(R.drawable.ic_launcher)
                .start(this);
        /*Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image*//**//*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);*/
    }
}
