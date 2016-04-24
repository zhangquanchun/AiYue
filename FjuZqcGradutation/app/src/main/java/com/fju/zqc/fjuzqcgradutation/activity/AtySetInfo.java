package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.cy.imagelib.ImageLoaderUtils;
import com.cy.imagelib.ImageStorageUtils;
import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.content.DlgTextMsg;
import com.cy.widgetlibrary.view.CustomCircleImageView;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.EventBusPhone;
import com.fju.zqc.fjuzqcgradutation.bean.EventUserInfoEntity;
import com.fju.zqc.fjuzqcgradutation.bean.Intent2Setting;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentDatePicker;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentEditText;
import com.fju.zqc.fjuzqcgradutation.utils.BusEventListener;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.utils.PhotoUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgSelectSex;
import com.fju.zqc.fjuzqcgradutation.view.PopupWindows;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import de.greenrobot.event.EventBus;

/**
 * Created by zhang on 2016/3/26.
 */
public class AtySetInfo extends BaseFragmentActivity implements View.OnClickListener {
    @BindView
    private CustomTitleView vTitle;
    @BindView
    private CustomCircleImageView iv_header;
    @BindView
    private RelativeLayout rl_header;
    @BindView
    private TextView tv_sex;
    @BindView
    private RelativeLayout rl_sex;
    @BindView
    private TextView tv_birth;
    @BindView
    private RelativeLayout rl_birth;
    @BindView
    private static TextView tv_phone;
    @BindView
    private RelativeLayout rl_phone;
    @BindView
    private TextView tv_sign;
    @BindView
    private RelativeLayout rl_sign;
    @BindView
    private TextView tv_school;
    @BindView
    private RelativeLayout rl_school;
    @BindView
    private ImageView iv_open;
    @BindView
    private ImageView iv_close;
    @BindView
    private LinearLayout ll_root;
    private PopupWindows popupWindows;
    private UserInfo userInfo;
    private String sex,birth,phoneNum,sign,school;
    private boolean isShow=false;
    private UpdatePhone updatePhone=new UpdatePhone();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.aty_set_info;
    }

    @Override
    protected void initView() {
        vTitle.setTitle("设置个人信息");
        vTitle.setTxtLeftIcon(R.drawable.header_back);
        vTitle.setTxtLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        vTitle.setTxtRightIcon(R.drawable.complate_write);
        vTitle.setTxtRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPut()){
                    saveSetting();
                }
            }
        });
        rl_birth.setOnClickListener(this);
        rl_header.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_school.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_sign.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_open.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        Intent2Setting intent2Setting=(Intent2Setting)intent
                .getSerializableExtra(Intent2Setting.KEY);
        userInfo=intent2Setting.user;
        if (userInfo.getFid()!=null){
            ImageLoaderUtils.getInstance().loadImage(userInfo.getFid(),iv_header);
        }else{
            iv_header.setImageResource(R.drawable.ic_launcher);
        }
        tv_sex.setText(userInfo.getSex()==null?null:userInfo.getSex());
        tv_birth.setText(userInfo.getBirth_day()==null?null:userInfo.getBirth_day());
        tv_school.setText(userInfo.getSchool()==null?null:userInfo.getSchool());
        tv_sign.setText(userInfo.getSign()==null?null:userInfo.getSign());
        tv_phone.setText(userInfo.getMobilePhoneNumber()==null?null:userInfo.getMobilePhoneNumber());
        if (userInfo.getIs_show_phone()){
            iv_close.setVisibility(View.GONE);
            iv_open.setVisibility(View.VISIBLE);
        }else{
            iv_open.setVisibility(View.GONE);
            iv_close.setVisibility(View.VISIBLE);
        }
        EventBus.getDefault().post(updatePhone);
    }

    /**
     * 保存设置信息
     */
    private void saveSetting(){
        if (iv_close.getVisibility()==View.VISIBLE){
            isShow=false;
        }else {
            isShow=true;
        }
        UserInfo user=new UserInfo();
        user.setSex(sex);
        user.setBirth_day(birth);
        user.setSign(sign);
        user.setSchool(school);
        user.setIs_show_phone(isShow);
        final DlgLoading dlgLoading=new DlgLoading(mContext);
        dlgLoading.show("保存信息中...");
        user.update(mContext,userInfo.getObjectId(),new UpdateListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new EventUserInfoEntity(birth,phoneNum
                        ,sex,school,sign,isShow));
                finish();
                WidgetUtils.showToast("修改信息成功!");
                dlgLoading.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                WidgetUtils.showToast("修改信息失败！");
                dlgLoading.dismiss();
            }
        });
    }
    /**
     * 检查输入
     */
    private boolean checkPut(){
        sex=tv_sex.getText().toString().trim();
        birth=tv_birth.getText().toString().trim();
        phoneNum=tv_phone.getText().toString().trim();
        sign=tv_sign.getText().toString().trim();
        school=tv_school.getText().toString().trim();
        if (sex.isEmpty()){
            WidgetUtils.showToast("请选择性别");
            return false;
        }else if(birth.isEmpty()){
            WidgetUtils.showToast("请设置生日");
            return false;
        }else if(sign.isEmpty()){
            WidgetUtils.showToast("请设置个性签名");
            return false;
        }else if (school.isEmpty()){
            WidgetUtils.showToast("请设置学校");
            return false;
        }else{
            return true;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_header:
                    popupWindows=new PopupWindows(this,itemsOnClick);
                    //显示窗口
                    popupWindows.showAtLocation(findViewById(R.id.ll_root),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_sex:
                updateSex();
                break;
            case R.id.rl_birth:
                updateBirth();
                break;
            case R.id.rl_phone:
                startActivity(new Intent(mContext,AtyBandPhone.class));
                break;
            case R.id.rl_sign:
                updateSign();
                break;
            case R.id.rl_school:
                updateSchool();
                break;
            case R.id.iv_close:
                iv_close.setVisibility(View.GONE);
                iv_open.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_open:
                iv_open.setVisibility(View.GONE);
                iv_close.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 更新个性签名
     */
    private void updateSign(){
        FragmentEditText fragmentEditText=new FragmentEditText("请输入个性签名"
                ,tv_sign.getText().toString().trim());
        fragmentEditText.setHint("字数在30字内");
        fragmentEditText.setCallBack(new FragmentEditText.EditFinishCallBack() {
            @Override
            public void onConfirm(String text) {
                tv_sign.setText(text);
            }
        });
        fragmentEditText.show(getFragmentManager(),"AtySetInfo");
    }
    /**
     * 更新学校
     */
    private void updateSchool(){
        FragmentEditText fragmentEditText=new FragmentEditText("请输入所在学校"
                ,tv_school.getText().toString().trim());
        fragmentEditText.setHint("字数在15字内");
        fragmentEditText.setCallBack(new FragmentEditText.EditFinishCallBack() {
            @Override
            public void onConfirm(String text) {
                if (text.length()<15){
                    tv_school.setText(text);
                }else{
                    WidgetUtils.showToast("字数过多，请重新输入");
                }

            }
        });
        fragmentEditText.show(getFragmentManager(),"AtySetInfo");
    }

    /**
     * 更新性别
     */
    private void updateSex(){
        final DlgSelectSex dlgSelectSex=new DlgSelectSex(mContext);
        dlgSelectSex.show();
        dlgSelectSex.getRl_female().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sex.setText("女");
                dlgSelectSex.dismiss();
            }
        });
        dlgSelectSex.getRl_man().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sex.setText("男");
                dlgSelectSex.dismiss();
            }
        });
    }

    /**
     * 更新生日
     */
    private void updateBirth(){
        FragmentDatePicker datePicker=new FragmentDatePicker();
        datePicker.setInitDateString(tv_birth.getText().toString());
        datePicker.setCallBack(new FragmentDatePicker.DatePickerCallBack() {
            @Override
            public void onConfirm(int year, int monthOfYear, int dayOfMonth) {
                String dateTime=dateFormatter(year,monthOfYear,dayOfMonth);
                tv_birth.setText(dateTime);
            }
        });
        datePicker.show(getFragmentManager(),"AtySetInfo");
    }
    private String dateFormatter(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear + 1;
        String dayStr = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String monthStr = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;

        return year + "-" + monthStr + "-" + dayStr;
    }
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtils.setOnPhotoResultListener(mActivity, requestCode, resultCode,
                data, new PhotoUtils.OnPhotoResultListener() {
                    @Override
                    public void onPhotoResult(String photoPath, Bitmap photo) {
                        if (photo != null && photo.isRecycled()) {
                            photo.recycle();
                        }
                        switch (requestCode) {
                            case PhotoUtils.RequestCode.TAKE_PHOTO_BY_SYSTEM:
                                PhotoUtils.cropPhotoBySystem(mActivity, null,
                                        photoPath, 300, 300);
                                break;
                            case PhotoUtils.RequestCode.PICK_PHOTO_FROM_GALLERY:
                                PhotoUtils.cropPhotoBySystem(mActivity, null,
                                        photoPath, 300, 300);
                                break;
                            case PhotoUtils.RequestCode.CROP_PHOTO_BY_SYSTEM:
                                uploadHeadImage(photoPath);
                                break;

                        }
                    }
                });
    }

    /**相册选择*/
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            popupWindows.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    // 系统相机拍照
                    PhotoUtils.takePhotoBySystem(mActivity,null);
                    break;
                case R.id.btn_pick_photo:
                    // 相册选择
                    PhotoUtils.pickPhotoFormGallery(mActivity, null);
                    break;
                default:
                    break;
            }
        }

    };
    /**
     * 上传头像
     * @param photoP
     */
    private void uploadHeadImage(String photoP){
        Bitmap bitmap= BitmapFactory.decodeFile(photoP);
        Log.d("xxxxx", photoP + "bitmap" + bitmap);
        String photoPath1="";
        final DlgLoading dlgLoading=new DlgLoading(mContext);
        photoPath1= ImageStorageUtils.saveFile(bitmap, DataStorageUtils.getPid());
        final String photoPath=photoPath1;
        BTPFileResponse response= BmobProFile.getInstance(mContext).upload(photoPath
                , new UploadListener() {
            @Override
            public void onSuccess(String filename, String fid, final BmobFile bmobFile) {
                DataStorageUtils.setCurUserProfileFid(bmobFile.getUrl());
                ImageLoaderUtils.getInstance()
                        .loadImage(bmobFile.getUrl()
                                , iv_header);
                UserInfo userInfo=new UserInfo();
                userInfo.setFid(bmobFile.getUrl());
                userInfo.update(mContext, DataStorageUtils.getPid(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        EventBus.getDefault().post(bmobFile.getUrl());
                        WidgetUtils.showToast("上传头像成功!");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        WidgetUtils.showToast("上传头像失败!");
                    }
                });
                dlgLoading.dismiss();
            }

            @Override
            public void onProgress(int i) {
                dlgLoading.show("上传头像"+i+"%");
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                iv_header.setImageBitmap(BitmapFactory.decodeFile(photoPath));
                dlgLoading.dismiss();
                Log.d("tttt", "msg" + errorMessage+"statusCode"+statusCode);
            }
        });
    }
    private final static class UpdatePhone implements BusEventListener.MainThreadListener<EventBusPhone>{
        @Override
        public void onEventMainThread(EventBusPhone event) {
            tv_phone.setText(event.getPhone());
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(updatePhone);
        super.onDestroy();
    }
}
