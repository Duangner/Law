package com.qf.myvolleytest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ImageView ivTest;
    NetworkImageView nivTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // requestGetString();
        //requestPostString();

        ivTest = (ImageView) findViewById(R.id.ivTest);
        nivTest = (NetworkImageView) findViewById(R.id.nivTest);

//        requestPostString();
//        requestJson();
//        requestImage();
        getNetImage();
    }

    /**
     * get方法获取网络字符串
     */
    protected void requestGetStirng(){
        //取消制定标记的所有请求
        MyApplication.requestQueue.cancelAll("qf");

        StringRequest request = new StringRequest("https://www.baidu.com/",
                //请求网络成功时回掉
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("requestGetStirng",response);
            }
        },

                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("requestGetStirng",error.getMessage());
            }
        });
        //给请求打上标记
        request.setTag("qf");

        //把请求加入队列
        MyApplication.requestQueue.add(request);
    }

    /**
     * post方法获取网络字符串
     */
    protected void requestPostString(){
        String strUrl = "http://japi.juhe.cn/health_knowledge/categoryList";

        StringRequest request = new StringRequest(Request.Method.POST,
                strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("requestPostString",response);
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("requestPostString",error.getMessage());
            }
        }){
            //重写此方法，把需要传送的参数返回
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
              Map<String,String> params = new HashMap<String,String>();
              params.put("key","a3fd7c05983fb7d303df16d73ecec2ba");
              return params;
          }
        };


        //把请求加入队列
        MyApplication.requestQueue.add(request);
    }

    private int iCnt = 0;

    public void onGet(View view) {
        iCnt++;
        Log.w("qfonget",iCnt+"");
        requestGetStirng();
//        requestPostString();
    }

    /**
     * 请求json数据
     */
    protected void requestJson(){
        String strUrl = "http://japi.juhe.cn/health_knowledge/categoryList?";
        //新建ObjectRequest或ErrorResponse
        JsonRequest request = new JsonObjectRequest(strUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.w("requestJson",response.toString());
                            Log.w("requestJson",response.getString("reason"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {

                }
        });
        MyApplication.requestQueue.add(request);
    }

    //只有文件缓存
    protected void requestImage(){
        String strUrl = "http://att.x2.hiapk.com/forum/month_1011/101115163272b86420add2dd5d.jpg";
        ImageRequest request = new ImageRequest(strUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ivTest.setImageBitmap(response);
                    }
                },
                1024,
                768,
                ImageView.ScaleType.CENTER,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("requestImage",error.getMessage());
                    }
                });
        MyApplication.requestQueue.add(request);
    }

    protected void loadImage(){
        //查找顺序：先查找内存，再查找本地文件，最后从网络下载
        //存储顺序：从网络下载->存在内存，同事存放到本地文件,如果没有写内存，只存放到本地
        ImageLoader loader = new ImageLoader(MyApplication.requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {

                return MyApplication.lruCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                MyApplication.lruCache.put(url, bitmap);
            }
        });
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(ivTest,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher);

        loader.get("http://pic4.nipic.com/20091217/3251138_103129006390_2.jpg", listener);

    }
//解决图片错位
    protected void getNetImage(){
        ImageLoader imageLoader = new ImageLoader(MyApplication.requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });

        nivTest.setDefaultImageResId(R.mipmap.ic_launcher);
        nivTest.setErrorImageResId(R.mipmap.ic_launcher);
        nivTest.setImageUrl("http://pica.nipic.com/2008-03-08/200838143321658_2.jpg",imageLoader);


    }
}
