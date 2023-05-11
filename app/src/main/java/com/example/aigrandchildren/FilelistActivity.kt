package com.example.aigrandchildren

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import com.example.aigrandchildren.adapter.AppListAdapter
import com.example.aigrandchildren.model.AppInfo


class FilelistActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appListAdapter: AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filelist)
        // 앱 정보 가져오기
        val appInfo = EasyDelete.getInstalledApps(this)

        // Adapter 초기화
        appListAdapter = AppListAdapter()

        // RecyclerView 설정
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = appListAdapter

        // 앱 정보를 RecyclerView에 연결
        appListAdapter.submitList(appInfo)

        // 항목 클릭 이벤트 처리
        appListAdapter.setOnItemClickListener(object : AppListAdapter.OnItemClickListener {
            override fun onItemClick(appInfo: AppInfo) {
                EasyDelete.deleteApps(appInfo.packageName, applicationContext)

                // 앱 삭제 후 리스트 갱신
                appListAdapter.currentList.toMutableList().remove(appInfo)
                appListAdapter.submitList(appListAdapter.currentList)
            }
        })
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