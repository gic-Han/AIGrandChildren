package com.example.aigrandchildren

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.util.Log
import android.content.pm.PackageManager
import com.example.aigrandchildren.model.AppInfo

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

    fun getInstalledApps(context: Context): List<AppInfo> {
        val packageManager = context.packageManager
        val apps = packageManager.getInstalledApplications(0)
        val appList = mutableListOf<AppInfo>()

        for (app in apps) {
            if (app.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                val appInfo = AppInfo(app.loadLabel(packageManager).toString(), app.packageName)
                Log.d("Installed App", "${appInfo.appName} (${appInfo.packageName})")
                appList.add(appInfo)
            }
        }

        return appList
    }
}