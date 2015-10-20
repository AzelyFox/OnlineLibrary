package kr.devs.onlinelibrarydemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
/**
 * Created by TK in DEVS
 */
public class DemoSelectActivity extends Activity {

    RelativeLayout box_chat,box_member,box_manage;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.demoselect);

        box_chat = (RelativeLayout) findViewById(R.id.scr_select_box_i);
        box_member = (RelativeLayout) findViewById(R.id.scr_select_box_ii);
        box_manage = (RelativeLayout) findViewById(R.id.scr_select_box_iii);

        box_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(DemoSelectActivity.this,DemoChatActivity.class);
                startActivity(mover);
            }
        });

        box_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(DemoSelectActivity.this,DemoMemberActivity.class);
                startActivity(mover);
            }
        });

        box_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(DemoSelectActivity.this,DemoManageActivity.class);
                startActivity(mover);
            }
        });

    }
}
