package com.dongjin.android.hongf.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dongjin.android.hongf.R;
import com.dongjin.android.hongf.adapter.CommentAdapter;
import com.dongjin.android.hongf.model.InterestedUser;
import com.dongjin.android.hongf.model.KaKaoInfo;
import com.dongjin.android.hongf.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {

    String key;
    DatabaseReference commentRef;
    DatabaseReference storyRef;

    CommentAdapter adapter;
    RecyclerView recyclerView;
    ImageView btnSend;
    EditText etContent;
    KaKaoInfo kaKaoInfo;

    String stringdate;
    Handler handler;
    Runnable runnable;
    private int commentCount;


    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        handler=new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    Date date = new Date();
                    Date newDate = new Date(date.getTime());
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    stringdate= dt.format(newDate);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        key=bundle.getString("key");
        commentRef= FirebaseDatabase.getInstance().getReference().child("comments").child(key);
        storyRef=FirebaseDatabase.getInstance().getReference().child("Story").child(key);
        storyRef.keepSynced(true);
        commentRef.keepSynced(true);

        storyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Review review=dataSnapshot.getValue(Review.class);
                commentCount=review.getCommentCount();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter=new CommentAdapter(this,key);

        kaKaoInfo=KaKaoInfo.getInstance();

        recyclerView=(RecyclerView)findViewById(R.id.comment_recy);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));




        btnSend=(ImageView)findViewById(R.id.comment_ig_sendbtn);
        etContent=(EditText)findViewById(R.id.comment_et_content);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=etContent.getText().toString();
                InterestedUser user=new InterestedUser();

                user.setComment(content);
                user.setUserId(kaKaoInfo.read_id_kakao());
                user.setUsername(kaKaoInfo.read_name_kakao());
                user.setProfilepath(kaKaoInfo.read_picture_kakao());
                user.setDate(stringdate);
                commentRef.push().setValue(user);

                commentCount +=1;
                HashMap<String,Object> hashMap= new HashMap<>();
                hashMap.put("commentCount",commentCount);
                storyRef.updateChildren(hashMap);



                etContent.setText("");
            }
        });





    }
}
