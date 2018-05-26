package com.dajeong.android.pushpush;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    float stageWidth, stageHeight; //게임판의 가로 세로
    int gridCount; //가로세로 격자
    float unit; //게임 단위
    Stage stage;
    FrameLayout Container;
    Player player;
    GameMap gameMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGame();  //게임 호출
        initStage();  //스테이지 만들기
        initPlayer(); //플레이어 세팅
        initButton(); //버튼에 onClick함수 설정
    }

    private void initGame(){
        //화면전체 사이즈 가져오기
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //게임판의 가로세로 사이즈
        stageWidth = metrics.widthPixels;
        stageHeight = stageWidth;
        //가로세로 격자의 개수
        gridCount = 10;
        //게임단위 설정 예) 가로가 1024픽셀이면 한칸당 102.4픽셀이 유닛.
        unit = stageWidth/gridCount;
        //게임맵까지 세팅.
        gameMap = new GameMap();

    }

    private void initStage(){
        Container = findViewById(R.id.Container); //설정해놓은 frameLayout
        //스테이지 설정
        stage = new Stage(this);
        stage.setConfig(gridCount, unit);
        //스테이지 프레임 레이아웃에 넣기
        Container.addView(stage);

        stage.setCurrentMap(gameMap.map1);
    }

    private void initPlayer(){
        player = new Player();
        stage.addPlayer(player);
    }

    public void initButton(){ //버튼을 눌렀을 때 이동하는 방식.
        findViewById(R.id.btnUp).setOnClickListener(buttonListener);
        findViewById(R.id.btnDown).setOnClickListener(buttonListener);
        findViewById(R.id.btnRigth).setOnClickListener(buttonListener);
        findViewById(R.id.btnLeft).setOnClickListener(buttonListener);
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnUp:
                    stage.move(Stage.UP); break;
                case R.id.btnDown:
                    stage.move(Stage.DOWN); break;
                case R.id.btnRigth:
                    stage.move(Stage.RIGHT); break;
                case R.id.btnLeft:
                    stage.move(Stage.LEFT); break;
            }
        }
    };
}
