package com.jkucharski.studentnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {

    Uri photoUri;
    Uri destUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            photoUri =  Uri.parse(intent.getStringExtra("IMAGE_URI"));
        }

        String cropImageName = new StringBuilder(UUID.randomUUID().toString()).append(".jpeg").toString();
        destUri = Uri.fromFile(new File(CropperActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),cropImageName));

        UCrop.Options options = new UCrop.Options();
        options.setContrastEnabled(false);
        options.setBrightnessEnabled(false);
        options.setSaturationEnabled(false);
        options.setSharpnessEnabled(false);

        UCrop.of(photoUri, destUri)
                .withOptions(options)
                .withAspectRatio(0,0)
                .useSourceImageAspectRatio()
                .withMaxResultSize(1000, 1000)
                .start(CropperActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            final Uri resultUri = UCrop.getOutput(data);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("RESULT", resultUri+"");
            setResult(-1, returnIntent);
            finish();
        }
    }
}