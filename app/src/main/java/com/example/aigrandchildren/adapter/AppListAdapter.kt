package com.example.aigrandchildren.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aigrandchildren.EasyDelete
import com.example.aigrandchildren.R
import com.example.aigrandchildren.model.AppInfo

class AppListAdapter : ListAdapter<AppInfo, AppListAdapter.ViewHolder>(AppDiffCallback()) {

    private var listener: OnItemClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        val packageNameTextView: TextView = itemView.findViewById(R.id.packageNameTextView)

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val packageManager = context.packageManager
                val intent = packageManager.getLaunchIntentForPackage(packageNameTextView.text.toString())
                if (intent != null) {
                    context.startActivity(intent)
                } else {
                    val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${packageNameTextView.text}"))
                    context.startActivity(playStoreIntent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = currentList[position]
        holder.appNameTextView.text = appInfo.appName
        holder.packageNameTextView.text = appInfo.packageName

        holder.itemView.setOnClickListener {
            listener?.onItemClick(appInfo)
            EasyDelete.deleteApps(appInfo.packageName, holder.itemView.context)
            // 앱 삭제 후 리스트 갱신
            submitList(currentList.filter { it.packageName != appInfo.packageName })
        }
    }

    // 아이템 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(appInfo: AppInfo)
    }

    // 아이템 클릭 리스너 설정 메소드
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // 리스트 갱신을 위한 DiffUtil 콜백 클래스
    private class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}