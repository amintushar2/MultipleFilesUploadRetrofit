package com.example.multiplefilesupload.ui.gallery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.multiplefilesupload.Pojos;
import com.example.multiplefilesupload.R;
import com.example.multiplefilesupload.fileutils.FileUtils;
import com.example.multiplefilesupload.services.ApiClient;
import com.example.multiplefilesupload.services.RealStateApiServices;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GalleryFragment extends Fragment {
    Button button;
    int propertysId=54 ;
    private static final int MY_PERMISSION_REQ=100;
    private int PICK_IMAGE_GALLERY_IMAGE=1;
    String detailss1 = "File";
    String details2 = "File";
    String details3 = "File";

    ProgressDialog progressDialog;
    Pojos pojos;

    public GalleryFragment() {

    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        button= root.findViewById(R.id.button);
        Context context;
        progressDialog =new ProgressDialog(getContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
               startActivityForResult(Intent.createChooser(intent,"Select "),PICK_IMAGE_GALLERY_IMAGE);
                progressDialog.show();
            }
        });



        return root;
    }
    @NonNull
    private RequestBody createRequestBody(String description){
        return RequestBody.create(MultipartBody.FORM,description);
    }
    @NonNull
    private MultipartBody.Part prepareMultipart(String path , Uri fileUri){
        File file = FileUtils.getFile(getContext(),fileUri);
        RequestBody requestBody =RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)),file);
        return MultipartBody.Part.createFormData(path,file.getName(),requestBody);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_GALLERY_IMAGE && resultCode==Activity.RESULT_OK&& data !=null){
            ClipData clipData = data.getClipData();
            ArrayList<Uri>files = new ArrayList<Uri>();
            for (int i =0 ; i<clipData.getItemCount();i++){
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri =item.getUri();
                files.add(uri);
            }
            uploadImage(files.get(0),files.get(1),files.get(1));
            Log.d("slction",files.toString());

        }
    }
    private void uploadImage(Uri uri, Uri uri1, Uri uri2) {

        RealStateApiServices realStateApiServices = ApiClient.getClient().create(RealStateApiServices.class);
        RequestBody propertyId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(propertysId));
        RequestBody details1 = RequestBody.create(MediaType.parse("text/plain"), details2);
        RequestBody details2 = RequestBody.create(MediaType.parse("text/plain"), details3);
        RequestBody details3 = RequestBody.create(MediaType.parse("text/plain"),detailss1 );

        Call<Pojos>call = realStateApiServices.uploadMultipleFiles(propertyId,details1,details2,details3,prepareMultipart("photo1",uri),
        prepareMultipart("photo2",uri1),
        prepareMultipart("photo3",uri2));
        call.enqueue(new Callback<Pojos>() {
            @Override
            public void onResponse(Call<Pojos> call, Response<Pojos> response) {
                progressDialog.dismiss();

                Toast.makeText(getContext(), "SUCCES", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Pojos> call, Throwable t) {
                    progressDialog.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Faild",t.getMessage());

            }
        });
    }


}