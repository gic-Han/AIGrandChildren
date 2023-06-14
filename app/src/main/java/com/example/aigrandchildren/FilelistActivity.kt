package com.example.aigrandchildren

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import com.example.aigrandchildren.adapter.AppListAdapter
import com.example.aigrandchildren.model.AppInfo
import android.provider.Settings
import android.widget.GridLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager


class FilelistActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appListAdapter: AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filelist)

        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        val granted = mode == AppOpsManager.MODE_ALLOWED

        if (!granted) {
            // 권한이 허용되지 않았으므로 사용자에게 권한 요청
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

        val deleteApp = intent.getBooleanExtra("deleteApp", false)

        // 앱 정보 가져오기
        val appInfo = EasyDelete.getInstalledApps(this, deleteApp)

        // Adapter 초기화
        appListAdapter = AppListAdapter(deleteApp)

        // RecyclerView 설정
        recyclerView = findViewById(R.id.recyclerView)
        if(deleteApp) recyclerView.layoutManager = LinearLayoutManager(this)
        else recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = appListAdapter

        // 앱 정보를 RecyclerView에 연결
        appListAdapter.submitList(appInfo)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val packageName = data?.getStringExtra(Intent.EXTRA_PACKAGE_NAME)
                packageName?.let { pName ->
                    val newList = appListAdapter.currentList.filter { it.packageName != pName }
                    appListAdapter.submitList(newList)
                }
            } else {
            }
        }
    }
}