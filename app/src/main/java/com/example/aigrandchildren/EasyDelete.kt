package com.example.aigrandchildren

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.util.Log
import android.content.pm.PackageManager
import com.example.aigrandchildren.model.AppInfo
import android.app.usage.UsageStatsManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.app.AppOpsManager
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS

object EasyDelete {

    private lateinit var appList: MutableList<AppInfo>

    fun deleteApps(packageName: String, context: Context) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // FLAG_ACTIVITY_NEW_TASK 플래그 추가
        context.startActivity(intent)
    }

    private fun isAppInstalled(packageName: String, context: Context): Boolean {
        val packageManager = context.packageManager
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


    fun getInstalledApps(context: Context, deleteApp: Boolean): List<AppInfo> {

        val packageManager = context.packageManager
        val apps = packageManager.getInstalledApplications(0)
        val appList = mutableListOf<AppInfo>()

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, System.currentTimeMillis())

        val statsMap = mutableMapOf<String, Long>()

        for (usageStat in usageStats) {
            val packageName = usageStat.packageName
            val lastTimeUsed = usageStat.lastTimeUsed
            statsMap[packageName] = lastTimeUsed
        }

        apps.forEach { app ->
            if (app.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                val packageName = app.packageName
                val lastTimeUsed = statsMap[packageName] ?: 0L
                val appInfo = AppInfo(app.loadLabel(packageManager).toString(), packageName, app.loadIcon(packageManager), lastTimeUsed)
                Log.d("Installed App", "${appInfo.appName} (${appInfo.packageName}) - Last Time Used: $lastTimeUsed")
                appList.add(appInfo)
            }
        }

        if (deleteApp) appList.sortBy{ it.lastTimeUsed }
        else appList.sortByDescending { it.lastTimeUsed }


        return appList
    }
}