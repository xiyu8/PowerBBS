package com.example.powerbbs.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.example.powerbbs.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.powerbbs.tackle.NetRequest.*;

public class CommitContentActivity extends AppCompatActivity {
        String app_url;
        private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:     // 0是网络超时发过来的msg
                    Toast.makeText(CommitContentActivity.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                    break;
                case 1:     // 1是无网络发过来的msg
                    progressDialog.dismiss();
                    Toast.makeText(CommitContentActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_content);
       // getSupportActionBar().hide();
       // test();
        initView();

//        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
//                            //AlertDialog.Builder dialog = new AlertDialog.Builder(CommitContentActivity.this);
//                    dialog.setTitle("dddd");
//                    dialog.setMessage("dddd");
//                    dialog.setPositiveButton("dddd",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent in = new Intent(CommitContentActivity.this, LoginActivity.class);
//                            startActivity(in);
//                        }
//                    });
//                    dialog.setNegativeButton("dddd",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    dialog.show();
    }




    EditText title,content;
    Button commit;
    ImageView previewUploadPic1,previewUploadPic2,previewUploadPic3;
    String themeId;
    RadioGroup area_choose; RadioButton area[]=new RadioButton[9];
    public void initView() {
        area_choose = (RadioGroup) findViewById(R.id.area_choose);
        app_url = getResources().getString(R.string.app_url);
        title = (EditText) findViewById(R.id.myTitle);
        content=(EditText) findViewById(R.id.myContent);
        commit = (Button) findViewById(R.id.commit);
        previewUploadPic1 = (ImageView) findViewById(R.id.previewUploadPic1);
        previewUploadPic2 = (ImageView) findViewById(R.id.previewUploadPic2);
        previewUploadPic3 = (ImageView) findViewById(R.id.previewUploadPic3);


        area[0] = (RadioButton) findViewById(R.id.dz);  area[1] = (RadioButton) findViewById(R.id.jk); area[2] = (RadioButton) findViewById(R.id.jx);
        area[8] = (RadioButton) findViewById(R.id.wch); area[3] = (RadioButton) findViewById(R.id.tm); area[4] = (RadioButton) findViewById(R.id.jg);
        area[5] = (RadioButton) findViewById(R.id.ysh); area[6] = (RadioButton) findViewById(R.id.wy); area[7] = (RadioButton) findViewById(R.id.ck);

    }

    int category;
    public int getCategory(){
        int m=1;
        for (int i=0;i<9;i++){
            if(area[i].isChecked()){
                m=i+1;
            }
        }
        return m;
    }

   // String textTitle,textContent;
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commit:
                Log.e("OpenJCU", "FFFFFFFFFF"+getCategory());
                if(login) {
                    String textTitle = title.getText().toString();
                    String textContent = content.getText().toString();
                    if(textTitle.equals("") || textContent.equals("") ){
                        Toast.makeText(CommitContentActivity.this,"标题及内容不能为空", Toast.LENGTH_SHORT).show(); break;
                    }else {
                        commitTheme(textTitle, textContent);
                    }
                }else {
                    Toast.makeText(CommitContentActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(CommitContentActivity.this);
//                    dialog.setTitle("请登录");
//                    dialog.setMessage("是否登录？");
//                    dialog.setPositiveButton("登录",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent in = new Intent(CommitContentActivity.this, LoginActivity.class);
//                            startActivity(in);
//                        }
//                    });
//                    dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    dialog.show();
                }

                break;



            case R.id.previewUploadPic1: handlePicture(1); break;
            case R.id.previewUploadPic2: handlePicture(2); break;
            case R.id.previewUploadPic3: handlePicture(3); break;

            default: break;
        }
    }

    String jsondata;
    Response response;
    ProgressDialog progressDialog;
    public void commitTheme(final String textTitle, final String textContent){
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("正在发布");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if(p1) picsQuantity+=1;
        if(p2) picsQuantity+=1;
        if(p3) picsQuantity+=1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client;                                                                                                                        //$_GET['username']、$_GET['pwd']、$_GET['title']、$_GET['content']、$_GET['category']
                client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
                Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"insert_theme.php?username="+username+"&pwd="+pwd+"&title="+textTitle+"&content="+textContent+"&category="+getCategory()+"&themePics="+getPicsString()).build();
//                Request request1 = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(R.string.bbsAddr+"insert_theme.php?username="+username+"&pwd="+pwd+"&title="+textTitle+"&content="+textContent+"&category=1").build();

                try {
                    response = client.newCall(request).execute();

                    String mm=response.body().string();
                    Log.e("Open", mm);
                    String temp[] = mm.split(",");
                    themeId = temp[1];
                    //jsondata=response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=new Message();
                    message.what=0;
                    handler.sendMessage(message);
                }
                requestForUpLoadFile();

                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);

            }
        }).start();
    }

//    //    String jsondata;
//    Response response;
//    public void test() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client;
//                client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
//                Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"obtain_theme_list.php?time=2017-03-29 19:53:41").build();
//                try {
//                    response = client.newCall(request).execute();
//                    jsondata=response.body().string();
//                    parseArry(jsondata);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Message message=new Message();
//                    message.what=0;
//                    handler.sendMessage(message);
//                }
//
//            }
//        }).start();
//    }
//
//
//    List<ThemeResource> themeResourcesList;
//    public void parseArry(String data) {
//        Gson gson = new Gson();
//        ThemeResource[] themeResourcesArray = gson.fromJson(data, ThemeResource[].class);
//        themeResourcesList = gson.fromJson(data, new TypeToken<List<ThemeResource>>() {}.getType());
//
//
//
//
//
//        Message message=new Message();
//        message.what=1;
//        handler.sendMessage(message);
//    }
//
//    private android.os.Handler handler = new android.os.Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:     // 0是网络超时发过来的msg
//                    Toast.makeText(CommitContentActivity.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
//                    break;
//                case 1:     // 1是无网络发过来的msg
//                    Toast.makeText(CommitContentActivity.this, "cccc", Toast.LENGTH_SHORT).show();
//                    ((TextView)findViewById(R.id.testlist)).setText(themeResourcesList.get(8).getContent());
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
    public String getPicsString() {
        String d=null;
        if (picsQuantity == 1)
            d="1";
        if(picsQuantity==2)
            d = "1,2";
        if(picsQuantity==3)
            d = "1,2,3";
        if(picsQuantity==0)
            d = "0";
        return d;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void handlePicture(int picFlag) {

        if (ContextCompat.checkSelfPermission(CommitContentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CommitContentActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum(picFlag);
        }
    }
    public void openAlbum(int picFlag){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, picFlag); // 打开相册
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(1);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(2);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(3);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default: break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
//            case TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    try {
//                        // 将拍摄的照片显示出来
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        picture.setImageBitmap(bitmap);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data,1);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data,1);
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data,2);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data,2);
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data,3);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data,3);
                    }
                }
                break;
            default:
                break;
        }
    }
    String imagePath;
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data, int picFlag) {
        //  String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        // displayImage(imagePath); // 根据图片路径显示图片
        displayImageWithGilde(uri,picFlag);
    }

    private void handleImageBeforeKitKat(Intent data, int picFlag) {
        Uri uri = data.getData();
        //String
        imagePath = getImagePath(uri, null);
        //displayImage(imagePath);
        displayImageWithGilde(uri,picFlag);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
          //  userIcon.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayImageWithGilde(final Uri uri, final int picFlag){

        new Thread(new Runnable() {
            @Override
            public void run() {


                FutureTarget<File> future = Glide.with(CommitContentActivity.this)
                        .load(uri)
                        .downloadOnly(200, 200);
                switch (picFlag) {
                    case 1:
                    try {
                        file1Uri=uri;
                        cacheFile1 = future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("OpenJCU", "1111111111111111111111111");
                    } catch (ExecutionException e) {
                        e.printStackTrace();

                        Log.e("OpenJCU", "2222222222222222222222222");
                    }
                    p1=true;
                    break;
                    case 2:
                        try {
                            file2Uri=uri;
                            cacheFile2 = future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e("OpenJCU", "1111111111111111111111111");
                        } catch (ExecutionException e) {
                            e.printStackTrace();

                            Log.e("OpenJCU", "2222222222222222222222222");
                        }
                        p2=true;
                        break;
                    case 3:
                        try {
                            file3Uri=uri;
                            cacheFile3 = future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e("OpenJCU", "1111111111111111111111111");
                        } catch (ExecutionException e) {
                            e.printStackTrace();

                            Log.e("OpenJCU", "2222222222222222222222222");
                        }
                        p3=true;
                        break;
                }
           //     requestForUpLoadFile(uri);

            }
        }).start();

        switch (picFlag) {
            case 1:
                Glide.with(this)
                        .load(uri)
                        .override(100,100) //图片大小
                        .centerCrop()   //等比缩放
                        //    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                        .placeholder(R.drawable.bgi) //
                        .error(R.drawable.bgi)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(previewUploadPic1);
                break;
            case 2:
                Glide.with(this)
                        .load(uri)
                        .override(100,100) //图片大小
                        .centerCrop()   //等比缩放
                        //    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                        .placeholder(R.drawable.bgi) //
                        .error(R.drawable.bgi)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(previewUploadPic2);
                break;
            case 3:
                Glide.with(this)
                        .load(uri)
                        .override(100,100) //图片大小
                        .centerCrop()   //等比缩放
                        //    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                        .placeholder(R.drawable.bgi) //
                        .error(R.drawable.bgi)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(previewUploadPic3);
                break;
        }
        //将图片上传至服务器
        //   requestForUpLoadFile(uri);



    }












    File cacheFile1,cacheFile2,cacheFile3;
    Uri file1Uri,file2Uri,file3Uri;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    上传文件
     */
    int picsQuantity=0; Boolean p1=false,p2=false,p3=false;
    public interface FileUploadService {
        // 上传单个文件
        @Multipart
        @POST("processResouce/process_theme_pics.php")
        Call<ResponseBody> uploadFile(
                @Part("themeId") RequestBody description,
                @Part MultipartBody.Part file);

         //上传多个文件
        @Multipart
        @POST("processResouce/process_theme_pics.php")
        Call<ResponseBody> uploadMultipleTwoFile(
                @Part("themeId") RequestBody description,
                @Part MultipartBody.Part file1,
                @Part MultipartBody.Part file2);
        @Multipart
        @POST("processResouce/process_theme_pics.php")
        Call<ResponseBody> uploadMultipleThreeFile(
                @Part("themeId") RequestBody description,
                @Part MultipartBody.Part file1,
                @Part MultipartBody.Part file2,
                @Part MultipartBody.Part file3);
    }


    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }
    @NonNull  // MultipartBody.Part 用于填充 name filename file Content-Type
    private MultipartBody.Part prepareFilePart(String partName, File cacheFile) {
        File file = new File(imagePath);
        // 为file建立RequestBody实例
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), cacheFile);
        // MultipartBody.Part借助文件名完成最终的上传
        return MultipartBody.Part.createFormData(partName, cacheFile.getName(), requestFile);  //包含 name filename file
    }
    //正式上传的代码
    public void requestForUpLoadFile() { //指定url
        int picsQuantity=0;
        if(file1Uri==null&&file2Uri==null&file3Uri==null) return;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(app_url).addConverterFactory(GsonConverterFactory.create()).build();
        FileUploadService service = retrofit.create(FileUploadService.class); // 创建上传的service实例
        MultipartBody.Part body[]=new MultipartBody.Part[3];
        if(file1Uri!=null) {
            body[picsQuantity] = prepareFilePart("themePic1", cacheFile1);  // 创建文件的part (photo, video, ...)
            picsQuantity+=1;
        }if(file2Uri!=null) {
            body[picsQuantity] = prepareFilePart("themePic2", cacheFile2);
            picsQuantity+=1;
        }if(file3Uri!=null) {
            body[picsQuantity] = prepareFilePart("themePic3", cacheFile3);
            picsQuantity+=1;
        }
        //     MultipartBody.Part body2 = prepareFilePart("thumbnail", file2Uri);
        RequestBody description = createPartFromString(themeId); // 添加其他的part
        Call<ResponseBody> call=null;
        switch (picsQuantity) {
            case 1:
                call = service.uploadFile(description, body[0]);
                break;
            case 2:
                call = service.uploadMultipleTwoFile(description, body[0],body[1]);
                break;
            case 3:
                call = service.uploadMultipleThreeFile(description, body[0],body[1],body[2]);
                break;
            default: break;
        }
    //    Call<ResponseBody> call = service.uploadFile(description, body1); // 最后执行异步请求操作
        try {
            retrofit2.Response<ResponseBody> requestBody=call.execute();
            //Log.e("OpenJCU", "Secucces"+requestBody.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OpenJCU", "cfffffefwsdw");
        }
    }

}
