package com.kisscigarette.app.ui;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.kisscigarette.app.R;
import com.kisscigarette.app.httpFrame.api.LoginApi;
import com.kisscigarette.app.httpFrame.entity.request.DataRequest;
import com.kisscigarette.app.httpFrame.entity.result.DataResult;
import com.kisscigarette.app.httpFrame.net.CustomSubscriber;
import com.kisscigarette.app.httpFrame.net.RetrofitManager;
import com.kisscigarette.app.ui.pay.PayTestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private Unbinder unbinder;


    @BindView(R.id.texttest)
    TextView texttest;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @OnClick(R.id.buttontest)
    void buttonClick() {
        JSONObject data = new JSONObject();
        data.put("key1", "孙鹏飞");
        data.put("key2", "张琪");
        data.put("key3", "王宗山");
        RetrofitManager.getInstance()
                .createReq(LoginApi.class)
                //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                .loginReq2("", new DataRequest(data.toJSONString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomSubscriber<DataResult>(getActivity()) {
                               @Override
                               protected void successResult(DataResult dataResult) {
                                   Toast.makeText(getActivity(), dataResult.toString(), Toast.LENGTH_LONG).show();
                               }

                               @Override
                               protected void errorResult(Throwable t) {

                               }
                           }

                );



    }

    @OnClick(R.id.buttontest2)
    void buttonClick2() {
        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        startActivity(new Intent(getActivity(), SeccondActivity.class), compat.toBundle());

    }

    //通过单例类得到的OkHttpClient 对象
    private OkHttpClient mClient;

    @OnClick(R.id.texttest)
    void textClick() {
        int location[] = new int[2];
        texttest.getLocationOnScreen(location);//将当前控件的坐标值赋给数组
        location[0] += texttest.getWidth() / 2;//获取横坐标控件的中心位置
        Intent intent = new Intent(getActivity(), SeccondActivity.class);
        intent.putExtra("location", location);
        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        //startActivity(intent,compat.toBundle());
        //关闭Activity启动的动画，目的是为了显示自己的动画

        /*MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "{\"data\":\"y09U9IPylf2yJJ/9zXFmXQxanO0+tVhKTkGs7oTtJshAChLoHlaUms5ifrQDHV1AJdokattyvT+1ygRJn0JPqw==\",\"key\":\"kA5sBewgS6CrcZYjzUcQtK7cepBQPxGzVYx294MI53xlwWI8nWd3c6XnJ2mfDRc/hH00iNgaLqfIvu9CIVvppw9A8r715+ZSCbG3LnHOPAexsGFKY6OqaNTGQOLaUcEARsvNZO0g8rVfSLM1X8VFINaWIcGbucPRHgr9s0m5u81g2DqK0de8+vZYN5BzmJujZg6XDrQnSlO//lWmL2ba9ggeXbRhv0kBgAc5m8jf4RS4F1klU51hLO5zB2/QcDNbXucR838EluKwTlG0R0eHSIE5nxQnSjuqeVVCcV9ILTmewqmnIofa+LVSeMmbiPlrC4vGhs4YoZh3uYKY4g6zmQ==\",\"offset\":\"g3SPfr78Mi3fUR8KBS0CT/+sbxqjy/A3mtGzwGnXnIgxVTIJwIQIYlGnFx4TrA3antkgOv71HdGjA+knLRtRz83tmDsT0Kwex3rEgSX5BLE3i4Y5rg63oTggPl4Nn67ySYBZM5ENUptDLyEBuCalc5HV1gj5QZ5wB3edhpx91E+ECdDf7FHiYUcgaiz/rczyIyrOwfKQpoEJCEwY8XBv1OT0EGxqpFv+3mmai4gAWyvnQ3ZUCrB9NRoFosHkFNccfGo/EIX1Cp+wbPENCtFr6ssvyrkkFvPumF0arzghh7C4oiPS4FurV0F36gdrza1PCPTWiUlT2KzZd0jOiTi7Pg==\",\"sign\":\"c7455ab29b939bc312\",\"devInfo\":\"Product: BKL-AL00, DEVICE: HWBKL, CPU_ABI: arm64-v8a, MANUFACTURER: HUAWEI, BRAND: HONOR, BOARD: BKL, MODEL: BKL-AL00, SDK: 26, USER: andorid, VERSION.RELEASE: 8.0.0, DISPLAY: BKL-AL00 8.0.0.192(C00GT), FINGERPRINT: HONOR/BKL-AL00/HWBKL:8.0.0/HUAWEIBKL-AL00/192(C00GT):user/release-keys, ID: HUAWEIBKL-AL00\",\"devName\":\"综合能源 HUAWEIBKL-AL00\",\"isencrypted\":\"0\",\"source\":\"app\",\"transid\":\"37B41A8EAE6586F1117EA4A0B826FE56\"}");

        final Request request = new Request.Builder()
                .url("https://192.168.43.5:8443/SSMspf/interface/searchGrid?cmdid=1001")
                //.url("http://172.28.2.81:8081/jlxmsh/attend/rest/attend/sign")
                .addHeader("IMEI", "866954037383560")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 8.0.0; zh-cn; BKL-AL00 Build/HUAWEIBKL-AL00) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
                .post(body)
                .build();
        mClient = OkHttpClientInstance.getInstance();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.e("***********", response.body().string());
            }
        });*/

        RetrofitManager.getInstance()
                .createReq(LoginApi.class)
                //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                .loginReq2("", new DataRequest("kA5sBewgS6CrcZYjzUcQtK7cepBQPxGzVYx294MI53xlwWI8nWd3c6XnJ2mfDRc/hH00iNgaLqfIvu9CIVvppw9A8r715+ZSCbG3LnHOPAexsGFKY6OqaNTGQOLaUcEARsvNZO0g8rVfSLM1X8VFINaWIcGbucPRHgr9s0m5u81g2DqK0de8+vZYN5BzmJujZg6XDrQnSlO//lWmL2ba9ggeXbRhv0kBgAc5m8jf4RS4F1klU51hLO5zB2/QcDNbXucR838EluKwTlG0R0eHSIE5nxQnSjuqeVVCcV9ILTmewqmnIofa+LVSeMmbiPlrC4vGhs4YoZh3uYKY4g6zmQ==","","",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomSubscriber<DataResult>(getActivity()) {
                               @Override
                               protected void successResult(DataResult dataResult) {
                                   Toast.makeText(getActivity(), dataResult.toString(), Toast.LENGTH_LONG).show();
                               }

                               @Override
                               protected void errorResult(Throwable t) {

                               }
                           }

                );
    }

    @OnClick(R.id.texttest2)
    void textClick2() {
        //LanguageUtil.selectLang(getActivity(),3);
        Intent intent = new Intent(getActivity(), PayTestActivity.class);
        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        startActivity(intent,compat.toBundle());
        /*try {
            RSAUtil.main();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }*/
    }

    @OnClick(R.id.texttest3)
    void textClick3() {
        JSONObject data = new JSONObject();
        data.put("key1", "818");
        data.put("key2", "张琪");
        data.put("key3", "王宗山");
        RetrofitManager.getInstance()
                .createReq(LoginApi.class)
                //.loginReq("http://61.136.27.61:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                //.loginReq("http://192.168.177.127:9000/ies/mobile/rest/test/getLogin", "sun", "111")
                .loginReq2("", new DataRequest(data.toJSONString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomSubscriber<DataResult>(getActivity()) {
                               @Override
                               protected void successResult(DataResult dataResult) {
                                   Toast.makeText(getActivity(), dataResult.toString(), Toast.LENGTH_LONG).show();
                               }

                               @Override
                               protected void errorResult(Throwable t) {

                               }
                           }

                );
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unbinder.unbind();
    }


}
