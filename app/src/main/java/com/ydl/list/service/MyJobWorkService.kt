package com.ydl.list.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.app.job.JobWorkItem
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ydl.list.R
import com.ydl.list.main.TestWorkActivity


/**
 * @author yudongliang
 * create time 2021-10-29
 * describe :
 */
class MyJobWorkService :JobService() {

    private var mNM: NotificationManager? = null
    private var curProcessor: CommandProcessor?= null

    override fun onCreate() {
        super.onCreate()
        mNM = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        Log.i("JobWorkService", "Service Created")
    }

    override fun onStartJob(params: JobParameters): Boolean {
        curProcessor = CommandProcessor(params).also { it.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR) }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        curProcessor?.cancel(true)
        return true
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(text: String?) {
        // The PendingIntent to launch our activity if the user selects this notification
/*        val contentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0,
                Intent(this, MainActivity::class.java), PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(this, 0,
                Intent(this, MainActivity::class.java), 0
            )
        }*/
        val contentIntent = PendingIntent.getActivity(this, 0,
            Intent(this, TestWorkActivity::class.java), 0
        )
        // Set the info for the views that show in the notification panel.
        val noteBuilder: Notification.Builder = Notification.Builder(this, TestWorkActivity.id1)
            .setSmallIcon(R.mipmap.ic_launcher) // the status icon
            .setTicker(text) // the status text
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setContentTitle("Job service is running.") // the label
            .setContentText(text) // the contents of the entry
            .setChannelId(TestWorkActivity.id1)
            .setContentIntent(contentIntent) // The intent to send when the entry is clicked

        // We show this for as long as our service is processing a command.
        noteBuilder.setOngoing(true)

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM?.notify(100, noteBuilder.build())
    }

    fun hideNotification(){
        mNM?.cancel(100);
    }

    inner class CommandProcessor(private val mParams: JobParameters) :AsyncTask<Void,Void,Void>(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg params: Void?): Void? {
            var canceled = false
            var work: JobWorkItem?= null
            while (!isCancelled.also { canceled = it } && mParams.dequeueWork()?.also { work = it } != null ){
                val txt = work?.intent?.getStringExtra("key1")
                Log.i("JobWorkService", "Processing work: $work, msg: $txt")
                showNotification(txt)
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                }

                hideNotification()
                Log.i("JobWorkService", "Done with: $work");
                work?.also { mParams.completeWork(it) }
            }
            if (canceled) {
                Log.i("JobWorkService", "CANCELLED!");
            }
            return null
        }

    }
}