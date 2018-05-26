package com.dajeong.android.pushpush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;

public class Stage extends View { //게임 스테이지 만들기 view를 상속받는다.    //생성자 만들때 나머지 아래 세개는 xml 쓸때만 사용.

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    int gridCount;
    float unit;
    Player player;

    int currentMap[][];
    Paint gridPaint = new Paint();
    Paint boxPaint = new Paint();
    Paint goalPaint = new Paint();
    Paint goalInPaint = new Paint();

    public Stage(Context context) {
        super(context);
        //안에가 빈 회색 사각형.  모눈종이 처럼
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1);
        //장애물은 블랙 박스
        boxPaint.setColor(Color.BLACK);
        //골은 핑크박스
        goalPaint.setColor(Color.MAGENTA);
        //골인 된 박스
        goalInPaint.setColor(Color.YELLOW);

    }

    public void setConfig(int gridCount, float unit){ //둘다 넘어옴.
        this.gridCount = gridCount;
        this.unit = unit;
    }

    public void addPlayer(Player player) {
        this.player = player;

    }

    @Override
    protected void onDraw(Canvas canvas) {
       // super.onDraw(canvas);
        drawMap(canvas);
        drawPlayer(canvas);
    }


    Paint tempPaint = new Paint();
    private void drawMap(Canvas canvas){ //맵 그리기.

        for(int y =0; y<currentMap.length; y++){
            for(int x =0; x<currentMap[0].length; x++){

                if(currentMap[y][x] == 0){ //배열이 0인 부분은  안에 빈 회색 사각형.
                    tempPaint = gridPaint;
                }else if(currentMap[y][x] == 1){ //배열 1인 부분 검정 박스
                    tempPaint = boxPaint;
                }else if(currentMap[y][x] == 9){ //배열 9인 부분 골지점
                    tempPaint = goalPaint;
                }else if(currentMap[y][x] == 3){
                    tempPaint = goalInPaint;
                }
                canvas.drawRect( //사각형 그리기 좌표(x,y)에 크기가 right, bottom만큼의 사각형을 그린다.
                        x * unit,
                        y * unit,
                        x * unit + unit,
                        y * unit + unit,
                        tempPaint);
            }

        }
    }

    private void drawPlayer(Canvas canvas){
        if(player != null)
            //동그라미(player) 그리기
            canvas.drawCircle(
                    player.x*unit + unit/2,  //x좌표
                    player.y*unit + unit/2,  //y좌표
                    unit/2,  // 반지름의 크기
                    player.paint); //색상
    }

    public void move(int direction){
        switch(direction){
            case UP:
                if(collisionProcess(UP))
                player.up();
                break;
            case DOWN:
                if(collisionProcess(DOWN))
                player.down();
                break;
            case RIGHT:
                if(collisionProcess(RIGHT))
                player.right();
                break;
            case LEFT:
                if(collisionProcess(LEFT))
                player.left();
                break;
        }
        invalidate(); //꼭
        completionProcess();
    }

    //이동완료후 종료검사
    private void completionProcess() {
        int count =0;
        //모든박스가 goal에 도착하면 게임을 종료하고 메시지를 띄운다.
        for(int y =0; y<currentMap.length; y++){
            for(int x =0; x<currentMap[0].length; x++){
                //모두 3이면 (1, 9를 포함하지 않고)
                if(currentMap[y][x] == 1) //장애물일때 카운트 세고
                    count++;
            }
        }
        if(count == 0) //장애물의 수가 0 이되면 종료.
            Toast.makeText(getContext(),"다음단계로 넘어갑니다.",Toast.LENGTH_SHORT).show();
    }

    //충돌검사
    public boolean collisionProcess(int direction){
        switch(direction) {
            case UP:
                //범위밖이면 false
                if((player.y - 1) < 0 )
                    //if((player.y - 1) >= 0 )
                    return false;
                //다음 진행 할 곳의 장애물 검사.
                if(currentMap[player.y-1][player.x] == 1 || currentMap[player.y-1][player.x] == 3){ //진행할 다음곳의 배열이 1
                    if(player.y + 2 < 0 //벽에 닿았을 때
                            || currentMap[player.y-2][player.x] == 1){ //진행할 곳이 장애물이 2개일 경우
                        return false;
                    }
                    ///////////////////////추가//////////////////////////////////
                    if(currentMap[player.y-1][player.x] == 1){ //다음 이동할 곳이 장애물인 경우
                        if(currentMap[player.y-2][player.x] == 9){ // 장애물 다음 골인 경우
                            currentMap[player.y-1][player.x] = 0; //
                            currentMap[player.y-2][player.x] = 3; //골인
                            break;
                        }else if(currentMap[player.y-2][player.x] == 3){
                            return false;
                        }
                    }

                    if(currentMap[player.y-1][player.x] == 3 ){
                        if(currentMap[player.y-2][player.x] ==9){
                            currentMap[player.y-1][player.x] =9;
                            currentMap[player.y-2][player.x] =3;
                            break;
                        }else if(currentMap[player.y-2][player.x] == 3){
                            return false;
                        }
                        currentMap[player.y-1][player.x] =9;
                        currentMap[player.y-2][player.x] =1;
                        break;
                    }
                    ///////////////////////추가//////////////////////////////////
                    currentMap[player.y-1][player.x] =0; //장애물을 옮김 => play가 가고
                    currentMap[player.y-2][player.x] =1; //=> 장애물이 한칸 이동한걸 그림.
                }
                break;

            case DOWN:
                if((player.y + 1) >= gridCount )
                    //if((player.y + 1) >= gridCount )
                    return false;
                if(currentMap[player.y+1][player.x] ==1 || currentMap[player.y+1][player.x] == 3){
                    if(player.y +2 >= gridCount
                            ||currentMap[player.y+2][player.x] == 1){
                        return false;
                    }
                    ///////////////////////추가
                    if(currentMap[player.y+1][player.x] == 1){ //다음 이동할 곳이 장애물인 경우
                        if(currentMap[player.y+2][player.x] == 9){ // 장애물 다음 골인 경우
                            currentMap[player.y+1][player.x] = 0; //
                            currentMap[player.y+2][player.x] = 3; //골인
                            break;
                        }else if(currentMap[player.y+2][player.x] == 3){
                            return false;
                        }
                    }

                    if(currentMap[player.y+1][player.x] == 3 ){
                        if(currentMap[player.y+2][player.x] ==9){
                            currentMap[player.y+1][player.x] =9;
                            currentMap[player.y+2][player.x] =3;
                            break;
                        }else if(currentMap[player.y+2][player.x] == 3){
                            return false;
                        }
                        currentMap[player.y+1][player.x] =9;
                        currentMap[player.y+2][player.x] =1;
                        break;
                    }
                    ///////////////////////추가
                    currentMap[player.y+1][player.x] = 0;
                    currentMap[player.y+2][player.x] = 1;
                }
                break;

            case RIGHT:
                if ((player.x + 1) >= gridCount)
                    return false;
                //다음진행할 곳의 장애물 검사
                if (currentMap[player.y][player.x + 1] == 1 || currentMap[player.y][player.x + 1] == 3) { // ||골에 통과된 것도 장애물로 인식
                    if (player.x + 2 >= gridCount
                            || currentMap[player.y][player.x + 2] == 1
                            ) {
                        return false;
                    }
                    ///////////////////////추가
                    if(currentMap[player.y][player.x+1] == 1){ //다음 이동할 곳이 장애물인 경우
                        if(currentMap[player.y][player.x+2] == 9){ // 장애물 다음 골인 경우
                            currentMap[player.y][player.x + 1] = 0; //
                            currentMap[player.y][player.x + 2] = 3; //골인
                            break;
                        }else if(currentMap[player.y][player.x+2] == 3){
                            return false;
                        }
                    }

                    if(currentMap[player.y][player.x + 1] == 3 ){
                        if(currentMap[player.y][player.x+2] ==9){
                            currentMap[player.y][player.x+1] =9;
                            currentMap[player.y][player.x+2] =3;
                            break;
                        }else if(currentMap[player.y][player.x + 2] == 3){
                            return false;
                        }
                        currentMap[player.y][player.x+1] =9;
                        currentMap[player.y][player.x+2] =1;
                        break;
                    }
                    ///////////////////////추가

                    currentMap[player.y][player.x + 1] = 0;
                    currentMap[player.y][player.x + 2] = 1;
                }
                break;

            case LEFT:
                if((player.x - 1) < 0 )
                    //if((player.x - 1) <= 0 )
                    return false;
                if(currentMap[player.y][player.x-1] == 1 || currentMap[player.y][player.x - 1] == 3){
                    if(player.x +2 <= 0 //player.x +2 <= 0
                            ||currentMap[player.y][player.x-2] == 1){
                        return false;
                    }
                    ///////////////////////추가
                    if(currentMap[player.y][player.x-1] == 1){ //다음 이동할 곳이 장애물인 경우
                        if(currentMap[player.y][player.x-2] == 9){ // 장애물 다음 골인 경우
                            currentMap[player.y][player.x - 1] = 0; //
                            currentMap[player.y][player.x - 2] = 3; //골인
                            break;
                        }else if(currentMap[player.y][player.x-2] == 3){
                            return false;
                        }
                    }

                    if(currentMap[player.y][player.x - 1] == 3 ){
                        if(currentMap[player.y][player.x-2] ==9){
                            currentMap[player.y][player.x-1] =9;
                            currentMap[player.y][player.x-2] =3;
                            break;
                        }else if(currentMap[player.y][player.x -2] == 3){
                            return false;
                        }
                        currentMap[player.y][player.x-1] =9;
                        currentMap[player.y][player.x-2] =1;
                        break;
                    }
                    ///////////////////////추가


                    currentMap[player.y][player.x-1] = 0;
                    currentMap[player.y][player.x-2] = 1;
                }
                break;
            }

            return true;
    }

    public void setCurrentMap(int[][] map) {
        currentMap = map;
    }


}
