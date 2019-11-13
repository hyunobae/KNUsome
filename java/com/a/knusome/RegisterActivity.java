package com.a.knusome;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.a.knusome.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    private EditText idText;
    private EditText passwordText;
    private EditText nameText;
    private EditText ageText;
    private Button registerButton;
    private ImageView profileImage;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        nameText = (EditText) findViewById(R.id.nameText);
        ageText = (EditText) findViewById(R.id.ageText);
        registerButton = (Button) findViewById(R.id.registerButton);

        //profile Image 업로드
        profileImage = (ImageView)findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });

        //registerButton클릭
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //입력 안했을때 처리
                if(idText.getText().toString()==null|| passwordText.getText().toString()==null||nameText.getText().toString()==null||ageText.getText().toString()==null||imageUri==null){
                    return;
                }




                //유저 생성
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(idText.getText().toString(),passwordText.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            //실행이 완료되었을때
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //uid받아옴
                                final String uid = task.getResult().getUser().getUid();
                                //image파일에 접속
                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        //getDownLoadUrl()못씀
                                        String imageUrl = task.getResult().getUploadSessionUri().toString();

                                        //model도 database에 같이 저장장
                                        UserModel userModel = new UserModel();
                                        userModel.uid = uid;
                                        userModel.userName = nameText.getText().toString();
                                        userModel.userAge = ageText.getText().toString();
                                        userModel.profileImageUrl = imageUrl;

                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();

                                    }
                                });

                              }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FROM_ALBUM && resultCode==RESULT_OK){
            profileImage.setImageURI(data.getData());//가운데 뷰를 바꿈
            imageUri=data.getData();//이미지 경로 원본
        }
    }
}
