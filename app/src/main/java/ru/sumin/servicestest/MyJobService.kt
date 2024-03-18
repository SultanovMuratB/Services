package ru.sumin.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService : JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var workItem = params?.dequeueWork()
            while (workItem != null) {
                val page = workItem.intent.extras?.getInt(PAGE) ?: 0
                scope.launch {
                    for (i in 0 until 100) {
                        delay(1000)
                    }
                    params?.completeWork(workItem!!)
                    workItem = params?.dequeueWork()
                }
                jobFinished(params, false)
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    companion object {

        const val JOB_ID = 111
        private const val PAGE = "page"
        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}