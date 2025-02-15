package com.cs407.lab08

import android.widget.ImageView
import androidx.annotation.VisibleForTesting

/**
 * Represents a ball that can move in the ConstraintLayout. The coordinates system used in this
 * class is the screen coordinate system, where the origin (0, 0) is at the top-left corner of the
 * screen.
 *
 * The ball has the following attributes:
 * - posX: the x-coordinate of the ball's center, of type Float
 * - posY: the y-coordinate of the ball's center, of type Float
 * - velocityX: the velocity of the ball in the x-direction, of type Float
 * - velocityY: the velocity of the ball in the y-direction, of type Float
 * - accelerationX: the acceleration of the ball in the x-direction, of type Float
 * - accelerationY: the acceleration of the ball in the y-direction, of type Float
 *
 * Constructor parameters:
 * - ballView: the ImageView representing the ball, whose position you will modify
 * - backgroundWidth: the width of the background ConstraintLayout, of type Float
 * - backgroundHeight: the height of the background ConstraintLayout, of type Float
 */
class Ball(
    private val ballView: ImageView,
    private val backgroundWidth: Float,
    private val backgroundHeight: Float
) {

    // variables to records state in the previous step
    @VisibleForTesting var posX = 0f
    @VisibleForTesting var posY = 0f
    @VisibleForTesting var velocityX = 0f
    @VisibleForTesting var velocityY = 0f
    private var accelerationX = 0f
    private var accelerationY = 0f

    private var isFirstUpdate = true

    // You will use distance factor to map the distance a ball travels in meters to
    // the distance it travels in pixels: distanceInPixels = distanceInMeters * DISTANCE_FACTOR
    private val DISTANCE_FACTOR = 100f

    init {
        // Initialize the ball's position and velocity
        reset()
    }


    /**
     * Updates the ball's position and velocity based on the given acceleration and time step.
     * We assume that the acceleration changes linearly within the interval dT.
     *
     * Parameters:
     * - accX: the acceleration of the ball in the x-direction (screen coordinate system), of type Float, in m/s^2
     * - accY: the acceleration of the ball in the y-direction (screen coordinate system), of type Float, in m/s^2
     * - dT: the time step, of type Float, in seconds
     */
    fun updatePositionAndVelocity(accX: Float, accY: Float, dT: Float) {
        // If this is the first update, we only need to update the acceleration and return
        // The first update is special because we don't have a previous acceleration to calculate
        // the velocity and position
        if (isFirstUpdate) {
            accelerationX = accX
            accelerationY = accY
            isFirstUpdate = false
            return
        }

        // TODO: 1. Calculate how far the ball should move in the x direction
        val distanceX = (velocityX * dT + (1f / 6f) * (dT * dT) * (3*accelerationX + accX)) * DISTANCE_FACTOR

        // TODO: 2. Calculate how far the ball should move in the y direction
        val distanceY = (velocityY * dT + (1f / 6f) * (dT * dT) * (3*accelerationY + accY)) * DISTANCE_FACTOR

        // TODO: 3. Calculate new velocity velocityX based on acceleration and time
        val newVelocityX = velocityX + 0.5f * (accelerationX + accX) * dT

        // TODO: 4. Calculate new velocity velocityY based on acceleration and time
        val newVelocityY = velocityY + 0.5f * (accelerationY + accY) * dT

        // TODO: 5. Update the position variables, posX and posY.
        // Hint: use DISTANCE_FACTOR to convert the distance in meters to pixels
        posX += distanceX
        posY += distanceY

        // TODO: 6. Update the acceleration variables, accelerationX and accelerationY, with the new acceleration values.
        velocityX = newVelocityX
        velocityY = newVelocityY
        accelerationX = accX
        accelerationY = accY

        // Ensure the ball stays within the screen's boundaries
        checkBoundaries()

        // Finally, update the position of the ballView.
        ballView.x = posX
        ballView.y = posY
    }

    /**
     * You need to ensure that the ball's pixels do not move outside the ConstraintLayout,
     * while also allowing the ball to touch the 4 edges of the ConstraintLayout.
     * When the ball collides with a boundary, the velocity and acceleration component perpendicular
     * to that boundary should be set to 0，but the ball may still roll along the boundary.
     *
     * Hint: update velocityX, velocityY, posX, posY, accerlerationX, and accelerationY
     */
    fun checkBoundaries() {
        // TODO: implement the checkBoundaries function
        if(posX < 0){
            posX = 0f
            velocityX = 0f
        }else if(posX > backgroundWidth - ballView.width){
            posX = (backgroundWidth - ballView.width).toFloat()
            velocityX = 0f
        }

        if(posY < 0){
            posY = 0f
            velocityY = 0f
        }else if (posY > backgroundHeight - ballView.width){
            posY = (backgroundHeight - ballView.width).toFloat()
            velocityY = 0f
        }
    }

    /**
     * Resets the ball to the center of the screen with zero velocity and acceleration.
     * Make sure the center of the call is at the center of the ConstraintLayout.
     * Make sure the isFistUpdate is set to true.
     */
    fun reset() {
        // TODO: Reset the ball to its original state
        posX = (backgroundWidth - ballView.width) /2
        posY = (backgroundHeight - ballView.height) /2
        velocityY = 0f
        velocityX = 0f
        accelerationY = 0f
        accelerationX = 0f
        isFirstUpdate = true
        ballView.x = posX
        ballView.y = posY
    }
}
