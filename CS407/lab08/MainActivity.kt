package com.cs407.lab08

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.annotation.VisibleForTesting

open class MainActivity : AppCompatActivity(), SensorEventListener {

    // The sensor manager is used to access the device's sensors
    @VisibleForTesting lateinit var mSensorManager: SensorManager

    // The gyroscope sensor
    @VisibleForTesting var mGravitySensor: Sensor? = null

    // An instance of the Ball class, which represents the ball on the screen
    // It records the position and velocity of the ball
    @VisibleForTesting lateinit var ball: Ball

    // The timestamp of the last gyroscope event
    @VisibleForTesting var lastTimestamp: Long = 0L

    // The parent layout of the ball. We treat it as the background of the ball.
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the instance of the ConstraintLayout
        constraintLayout = findViewById(R.id.imageContainer)

        // Ensure the Ball is initialized after the ConstraintLayout is measured
        constraintLayout.post {
            // TODO: Initialize the ball instance
            val ballView = findViewById<ImageView>(R.id.ball)
            val backgroundWith = constraintLayout.width.toFloat()
            val backgroundHeight = constraintLayout.height.toFloat()

            ball = Ball(ballView, backgroundWith, backgroundHeight)

        }

        // TODO: call getSensorManager() to initialize the mSensorManager
        mSensorManager = getSensorManager()
        
        // TODO: call getGravitySensor() to get the gravity sensor to initialize mGravitySensor
        mGravitySensor = getGravitySensor()
    }

    override fun onResume() {
        super.onResume()
        // TODO: register a SensorEventListener to listen to the mGravitySensor and set the sensor delay as SENSOR_DELAY_FASTEST
        mGravitySensor?.let { sensor ->
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }

    }

    /**
     * Capture the sensor data and update the ball's position and velocity
     *
     * @param event the sensor event
     */
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (isGravitySensorEvent(it)) {
                // ::ball.isInitialized makes sure the ball is initialized before we update the
                // position and velocity
                if (lastTimestamp != 0L && ::ball.isInitialized) {
                    // TODO: Calculate the time difference between the current and last sensor event
                    //Hint, the timestamp stored in the event is in nanoseconds
                    val currentTime = event.timestamp
                    val dT = (currentTime - lastTimestamp) / 1_000_000_000f

                    // TODO: Update the position and velocity of the ball
                    // Hint: The y-axis from the gyroscope sensor is inverted compared to the y-axis on the screen.
                    // Hint: The direction of the gravity vector from the sensor reading is opposite to the direction of physical gravity.
                    val accX = -it.values[0]
                    val accY = -it.values[1]

                    ball.updatePositionAndVelocity(accX, accY, dT)

                    lastTimestamp = currentTime

                }else{
                    lastTimestamp = event.timestamp
                }

                // TODO: Update the lastTimestamp to the current event timestamp
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // do nothing
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: Unregister all sensors
        mSensorManager.unregisterListener(this)

    }

    fun reset(view: View?) {
        // TODO: Reset the ball's state
        if (::ball.isInitialized){
            ball.reset()
        }
        // TODO: Reset the lastTimestamp to 0
        lastTimestamp = 0L
    }


    /**
     * This function should return the SensorManager instance
     * Remove the throw NotImplementedError() line and implement the function
     */
    @VisibleForTesting
    open fun getSensorManager(): SensorManager {
        // TODO: implement this function
        //throw NotImplementedError("Please implement the getSensorManager() method")
        return getSystemService(SENSOR_SERVICE) as SensorManager
    }

    /**
     * This function should return the gravity sensor
     * Remove the throw NotImplementedError() line and implement the function
     */
    @VisibleForTesting
    open fun getGravitySensor(): Sensor? {
        // TODO: implement this function
        //throw NotImplementedError("Please implement the getGravitySensor() method")
        return mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    }

    /**
     * Check if the sensor event is from the gravity sensor
     *
     * @param event the sensor event
     * @return true if the sensor event is from the gravity sensor, false otherwise
     */
    @VisibleForTesting
    fun isGravitySensorEvent(event: SensorEvent?): Boolean {
        // TODO: implement this function
        return event?.sensor?.type == Sensor.TYPE_GRAVITY
    }
}
