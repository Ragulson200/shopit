package com.manway.shopit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ImageUploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;
    private Uri filePath;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

         @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button chooseImageButton = (Button) findViewById(R.id.choose_image_button);
         @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button uploadImageButton = findViewById(R.id.upload_image_button);

        storageReference = FirebaseStorage.getInstance().getReference();

        chooseImageButton.setOnClickListener(v -> requestStoragePermission());

        uploadImageButton.setOnClickListener(v -> uploadImage());
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        } else {
            openImageChooser();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        if (filePath != null) {
            StorageReference ref = storageReference.child("images/" + System.currentTimeMillis());

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(ImageUploadActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        // Handle successful upload
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ImageUploadActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        // Handle unsuccessful upload
                    });
        } else {
            Toast.makeText(this, "Select an image first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
