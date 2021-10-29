package com.ydl.list.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.ydl.list.service.MyJobWorkService
import com.ydl.list.R
import kotlinx.android.synthetic.main.activity_test_work.*


class TestWorkActivity : AppCompatActivity() {

    companion object{
        var id1 = "test_channel_01"
    }

    var jobScheduler: JobScheduler? = null
    var job: JobInfo? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_work)
        createChannel()
        //get the Job Scheduler
        jobScheduler = getSystemService(JobScheduler::class.java)
        //get the job we will be sending workitems too.
        job = getJobInfo()
        button.setOnClickListener {
             //let's create 3 fake work items and enqueue them.
            var item: JobWorkItem?
            var i: Intent?= null
            //first item
            i = Intent()
            i.putExtra("Key1", "first item") //fake data too.
            item = JobWorkItem(i)
            jobScheduler?.enqueue(job!!, item)
            //second item
            i = Intent()
            i.putExtra("Key1", "Second item") //fake data too.
            item = JobWorkItem(i)
            jobScheduler?.enqueue(job!!, item)
            //first item
            i = Intent()
            i.putExtra("Key1", "Third item") //fake data too.
            item = JobWorkItem(i)
            jobScheduler?.enqueue(job!!, item)
        }
    }

    fun getJobInfo(): JobInfo? {
        val serviceComponent = ComponentName(applicationContext, MyJobWorkService::class.java)
        val builder: JobInfo.Builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency(10 * 1000) // wait at least
        builder.setOverrideDeadline(30 * 1000) // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        //builder.setRequiresBatteryNotLow(true);  //only when the batter is not low.  API 26+
        return builder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(id1,"channel_name",NotificationManager.IMPORTANCE_LOW).also {
            it.description = "this is description!~!"
            it.enableLights(true)
            it.setShowBadge(true)
        }
        nm.createNotificationChannel(channel)

    }
}
