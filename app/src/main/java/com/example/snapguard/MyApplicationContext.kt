package com.example.snapguard

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.WindowManager


/**
 * Custom Application class for SnapGuard.
 *
 * This class extends the Application class and is used to set up activity lifecycle callbacks,
 * specifically to handle the prevention of screenshots and implement additional security measures.
 *
 * @property context The application context.
 */
class MyApplicationContext : Application() {

    private lateinit var context : Context

    /**
     * Called when the application is first created.
     */
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        setupActivityListener()
    }

    /**
     * Set up activity lifecycle callbacks to handle specific actions during different lifecycle stages.
     */
    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            /**
             * Called when a new activity is created.
             *
             * @param activity The created activity.
             * @param savedInstanceState If the activity is being re-initialized after previously being
             * shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
             * Otherwise, it is null.
             */

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // Prevent screenshots by setting FLAG_SECURE for the activity's window.
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }


}