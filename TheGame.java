package uk.ac.reading.sis05kol.mooc;

//Other parts of the android libraries that we use
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import java.util.Random;

public class TheGame extends GameThread{

	//Will store the image of a ball
	private Bitmap mBall;
	
	private int mBonusBall1 = 10, mBonusBall2 = 30;

	//The X and Y position of the ball on the screen
	//The point is the top left corner, not middle, of the ball
	//Initially at -100 to avoid them being drawn in 0,0 of the screen
	private float[] mBallX = {-100,-100,-100};
	private float[] mBallY = {-100,-100,-100};

	//The speed (pixel/second) of the ball in direction X and Y
	private float[] mBallSpeedX = {0,0,0};
	private float[] mBallSpeedY = {0,0,0};
	
	//Whether the ball is in play or not
	private boolean[] mBallInPlay = {true, false, false};
	private boolean mGameOver = false;

	//Will store the image of the Paddle used to hit the ball
	private Bitmap mPaddle;
	
	//Paddle's x position. Y will always be the bottom of the screen
	private float mPaddleX = 0;
	
	//Will store the image of the smiley ball (score ball)
	private Bitmap mSmileyBall;

	//The X and Y position of the ball on the screen
	//The point is the top left corner, not middle, of the ball
	//Initially at -100 to avoid them being drawn in 0,0 of the screen
	private float[] mSmileyBallX = {-100,-100,-100};
	private float[] mSmileyBallY = {-100,-100,-100};
	
	//Will store the image of the sad ball (-ve score ball)
	private Bitmap mSadBall;

	//The X and Y position of the ball on the screen
	//The point is the top left corner, not middle, of the ball
	//Initially at -100 to avoid them being drawn in 0,0 of the screen
	//private float[] mSadBallX = {-100,-100,-100};
	//private float[] mSadBallY = {-100,-100,-100};	
	private float[] mSadBallX = {-100};
	private float[] mSadBallY = {-100};	

	//This will store the min distance allowed between a big ball and the small red ball
	//This is used to check collisions
	private float mMinDistanceBetweenRedBallAndBigBall;
	
	Random randomGeneratorX = new Random();
	Random randomGeneratorY = new Random();

	//This is run before anything else, so we can prepare things here
	public TheGame(GameView gameView) {
		//House keeping
		super(gameView);

		//Prepare the image so we can draw it on the screen (using a canvas)
		mBall = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.small_red_ball);

		//Prepare the image of the paddle so we can draw it on the screen (using a canvas)
		mPaddle = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.yellow_ball);

		//Prepare the image of the SmileyBall so we can draw it on the screen (using a canvas)
		mSmileyBall =  BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.smiley_ball);
		
		//Prepare the image of the SadBall so we can draw it on the screen (using a canvas)
				mSadBall =  BitmapFactory.decodeResource
						(gameView.getContext().getResources(), 
								R.drawable.sad_ball);
	}

	//This is run before a new game (also after an old game)
	@Override
	public void setupBeginning() {
		//Initialise speeds
		//mCanvasWidth and mCanvasHeight are declared and managed elsewhere
		
		for (int i = 0; i < mBallX.length; i++){ 
			mBallSpeedX[i] = mCanvasWidth / 3; 
			mBallSpeedY[i] = mCanvasHeight / 3;

			//Place the ball in the middle of the screen.
			//mBall.Width() and mBall.getHeigh() gives us the height and width of the image of the ball
			mBallX[i] = 0;
			mBallY[i] = 0;
			
			mBallInPlay[i] = false;
		}
		
		mBallInPlay[0] = true;
		
		//Place Paddle in the middle of the screen
		mPaddleX = mCanvasWidth / 2;
		
		//Place SmileyBalls in random positions on the screen
		for (int i=0; i < (mSmileyBallX.length); i++){
			mSmileyBallX[i] = randomGeneratorX.nextInt(mCanvasWidth - mSmileyBall.getHeight());
			mSmileyBallY[i] = randomGeneratorY.nextInt(mCanvasHeight - mSmileyBall.getHeight());
		}
		
		//Place SadBalls in random positions on the screen
		for (int i=0; i < (mSadBallX.length); i++){
				mSadBallX[i] = randomGeneratorX.nextInt(mCanvasWidth - mSadBall.getHeight());
				mSadBallY[i] = randomGeneratorY.nextInt(mCanvasHeight - mSadBall.getHeight());
		}

		//Get the minimum distance between a small ball and a bigball
		//We leave out the square root to limit the calculations of the program
		//Remember to do that when testing the distance as well
		mMinDistanceBetweenRedBallAndBigBall = (mPaddle.getWidth() / 2 + mBall.getWidth() / 2) * (mPaddle.getWidth() / 2 + mBall.getWidth() / 2);
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//If there isn't a canvas to do nothing
		//It is ok not understanding what is happening here
		if(canvas == null) return;

		//House keeping
		super.doDraw(canvas);

		//canvas.drawBitmap(bitmap, x, y, paint) uses top/left corner of bitmap as 0,0 
		//we use 0,0 in the middle of the bitmap, so negate half of the width and height of the ball to draw the ball as expected
		//A paint of null means that we will use the image without any extra features (called Paint)
		
		//draw the image of the ball using the X and Y of the ball
		for (int i = 0; i < mBallX.length; i++){
		canvas.drawBitmap(mBall, mBallX[i] - mBall.getWidth() / 2, mBallY[i] - mBall.getHeight() / 2, null);
		}
		//Draw Paddle using X of paddle and the bottom of the screen (top/left is 0,0)
		canvas.drawBitmap(mPaddle, mPaddleX - mPaddle.getWidth() / 2, mCanvasHeight - mPaddle.getHeight() / 2, null);
		
		//Draw SmileyBalls
		for (int i = 0; i < mSmileyBallX.length; i++) {
			canvas.drawBitmap(mSmileyBall, mSmileyBallX[i] - mSmileyBall.getWidth() / 2, mSmileyBallY[i] - mSmileyBall.getHeight() / 2, null);
		}
		
		//Draw SadBalls
		for (int i = 0; i < mSadBallX.length; i++) {
			canvas.drawBitmap(mSadBall, mSadBallX[i] - mSadBall.getWidth() / 2, mSadBallY[i] - mSadBall.getHeight() / 2, null);
		}
	}

	//This is run whenever the phone is touched by the user
	@Override
	protected void actionOnTouch(float x, float y) {
		//Increase/decrease the speed of the ball making the ball move towards the touch
		mPaddleX = x - mPaddle.getWidth() / 2;
	}
	
	//This is run whenever the phone moves around its axises 
	@Override
	protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
		//Ensure the paddle stays on the screen
		if(mPaddleX >= 0 && mPaddleX <= mCanvasWidth) {
			//Change the X value according to the x direction of the phone
			mPaddleX = mPaddleX - xDirection;
			
			//after movement ensure it is still on the screen
			if(mPaddleX < 0) mPaddleX = 0;
			if(mPaddleX > mCanvasWidth) mPaddleX = mCanvasWidth;			
		}
	}
	 

	//This is run just before the game "scenario" is printed on the screen
	@Override
	protected void updateGame(float secondsElapsed) {
		float distanceBetweenBallAndPaddle;
		
		for (int j = 0; j < mBallX.length; j++){
			
			if (mBallInPlay[j]){	

				//If the ball moves down on the screen perform potential paddle collision
				if(mBallSpeedY[j] > 0) {
					//Get actual distance (without square root - remember?) between the mBall and the ball being checked
					distanceBetweenBallAndPaddle = (mPaddleX - mBallX[j]) * (mPaddleX - mBallX[j]) + (mCanvasHeight - mBallY[j]) *(mCanvasHeight - mBallY[j]);

					//Check if the actual distance is lower than the allowed => collision
					if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

						//Get the present velocity (this should also be the velocity going away after the collision)
						float velocityOfBall = (float) Math.sqrt(mBallSpeedX[j]*mBallSpeedX[j] + mBallSpeedY[j]*mBallSpeedY[j]);

						mBallSpeedX[j] = mBallX[j] - mPaddleX;
						mBallSpeedY[j] = mBallY[j] - mCanvasHeight;

						float newVelocity = (float) Math.sqrt(mBallSpeedX[j]*mBallSpeedX[j] + mBallSpeedY[j]*mBallSpeedY[j]);

						mBallSpeedX[j] = mBallSpeedX[j] * velocityOfBall / newVelocity;
						mBallSpeedY[j] = mBallSpeedY[j] * velocityOfBall / newVelocity;

					}
				}
	
		
				//Move the ball's X and Y using the speed (pixel/sec)
				mBallX[j] = mBallX[j] + secondsElapsed * mBallSpeedX[j];
				mBallY[j] = mBallY[j] + secondsElapsed * mBallSpeedY[j];


				//Check if the ball hits either the left side or the right side of the screen
				//But only do something if the ball is moving towards that side of the screen
				//If it does that => change the direction of the ball in the X direction
				if((mBallX[j] <= mBall.getWidth() / 2 && mBallSpeedX[j] < 0) || (mBallX[j] >= mCanvasWidth - mBall.getWidth() / 2 && mBallSpeedX[j] > 0) ) {
					mBallSpeedX[j] = -mBallSpeedX[j];
				}
		
				//Loop for processing smiley faces	
				for (int i = 0; i < mSmileyBallX.length; i++){
					distanceBetweenBallAndPaddle = (mSmileyBallX[i] - mBallX[j]) * (mSmileyBallX[i] - mBallX[j]) + (mSmileyBallY[i] - mBallY[j]) *(mSmileyBallY[i] - mBallY[j]);

					//Check if the actual distance is lower than the allowed => collision
					if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

						//Get the present velocity (this should also be the velocity going away after the collision)
						float velocityOfBall = (float) Math.sqrt(mBallSpeedX[j]*mBallSpeedX[j] + mBallSpeedY[j]*mBallSpeedY[j]);

						//Change the direction of the ball
						mBallSpeedX[j] = mBallX[j] - mSmileyBallX[i];
						mBallSpeedY[j] = mBallY[j] - mSmileyBallY[i];

						//Get the velocity after the collision
						float newVelocity = (float) Math.sqrt(mBallSpeedX[j]*mBallSpeedX[j] + mBallSpeedY[j]*mBallSpeedY[j]);

						//using the fraction between the original velocity and present velocity to calculate the needed
						//speeds in X and Y to get the original velocity but with the new angle.
						mBallSpeedX[j] = mBallSpeedX[j] * velocityOfBall / newVelocity;
						mBallSpeedY[j] = mBallSpeedY[j] * velocityOfBall / newVelocity;

						//Increase score
						updateScore(5);

						// move smiley face
						mSmileyBallX[i] = randomGeneratorX.nextInt(mCanvasWidth - mSmileyBall.getHeight());
						mSmileyBallY[i] = randomGeneratorY.nextInt(mCanvasHeight - mSmileyBall.getHeight());

						if (getScore()>=mBonusBall1){
							mBallInPlay[1] = true;
						}
						if (getScore()>=mBonusBall2){
							mBallInPlay[2] = true;
						}

					}
				} //end of smiley faces loop

				//Loop for processing sad faces	
				for (int i = 0; i < mSadBallX.length; i++){
					distanceBetweenBallAndPaddle = (mSadBallX[i] - mBallX[j]) * (mSadBallX[i] - mBallX[j]) + (mSadBallY[i] - mBallY[j]) *(mSadBallY[i] - mBallY[j]);

					//Check if the actual distance is lower than the allowed => collision
					if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

						//Get the present velocity (this should also be the velocity going away after the collision)
						float velocityOfBall = (float) Math.sqrt(mBallSpeedX[j]*mBallSpeedX[j] + mBallSpeedY[j]*mBallSpeedY[j]);

						//Change the direction of the ball
						mBallSpeedX[j] = mBallX[j] - mSadBallX[i];
						mBallSpeedY[j] = mBallY[j] - mSadBallY[i];

						//Get the velocity after the collision
						float newVelocity = (float) Math.sqrt(mBallSpeedX[j]*mBallSpeedX[j] + mBallSpeedY[j]*mBallSpeedY[j]);

						//using the fraction between the original velocity and present velocity to calculate the needed
						//speeds in X and Y to get the original velocity but with the new angle.
						mBallSpeedX[j] = mBallSpeedX[j] * velocityOfBall / newVelocity;
						mBallSpeedY[j] = mBallSpeedY[j] * velocityOfBall / newVelocity;

						//Increase score
						updateScore(-5);

						//move sad face
						mSadBallX[i] = randomGeneratorX.nextInt(mCanvasWidth - mSadBall.getHeight());
						mSadBallY[i] = randomGeneratorY.nextInt(mCanvasHeight - mSadBall.getHeight());
					}
				} //end of sad faces loop		


				//If the ball goes out of the top of the screen and moves towards the top of the screen =>
				//change the direction of the ball in the Y direction
				if(mBallY[j] <= mBall.getWidth() / 2 && mBallSpeedY[j] < 0) {
					mBallSpeedY[j] = -mBallSpeedY[j];
				}

				//If the ball goes out of the bottom of the screen => lose the game if last ball lost
				if(mBallY[j] >= mCanvasHeight) {

					mBallInPlay[j] = false;
					mGameOver = true;
					//Check to see if any ball in play and if so, keep game alive
					for (int k = 0; k < mBallInPlay.length; k++){
						if (mBallInPlay[k]){
							mGameOver = false;
						}
					}
					if (mGameOver){
						setState(GameThread.STATE_LOSE);
					}
				}

			} // end of ball in play loop

		} // end of ball loop

	} //end of method
	
} // end of class


// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
