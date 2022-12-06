package com.jkucharski.studentnotes;

import static android.app.Activity.RESULT_OK;
import static com.jkucharski.studentnotes.utils.Const.CAMERA_PRM_CODE;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.jkucharski.studentnotes.databinding.FragmentTextRecognitionBinding;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextRecognitionFragment extends Fragment {

    FragmentTextRecognitionBinding binding;
    FragmentManager fm;
    Bitmap imageBitmap;
    TextRecognizer recognizer;
    ActivityResultLauncher<Intent> activityResultLauncher;
    File photoFile = null;
    Uri photoUri;

    public TextRecognitionFragment(FragmentManager fm) {
        this.fm = fm;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",    /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri,
                                         File imageFile) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private void dispatchTakePictureIntent() throws IOException {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PRM_CODE);
        }else{
            photoFile = createImageFile();

            photoUri = FileProvider.getUriForFile(getActivity(), "com.jkucharski.studentnotes.provider", photoFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activityResultLauncher.launch(takePictureIntent);
        }
    }

    private void detectTextFromImage() {
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(visionText -> binding.covertedTextDisplay.setText(visionText.getText()))
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTextRecognitionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    binding.imageView.setImageBitmap(imageBitmap);
                    int rotateImage = getCameraPhotoOrientation(getContext(), photoUri, photoFile);
                    binding.imageView.setRotation(rotateImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.captureImageButton.setOnClickListener(view1 -> {
            try {
                dispatchTakePictureIntent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.covertedTextDisplay.setText("");
        });
        binding.detectTextButton.setOnClickListener(view12 -> detectTextFromImage());
    }
}