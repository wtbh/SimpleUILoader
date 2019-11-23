package com.plain.simpleuiloader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.plain.simpleuiloaderlib.SimpleUILoaderLayout
import com.plain.simpleuiloaderlib.SimpleUILoaderLayout.UIStatus
import com.plain.simpleuiloaderlib.SimpleUILoaderLayout.UIStatus.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListener()
        initView()

    }

    private fun setListener() {
        ui_loader.setRefreshListener(object : SimpleUILoaderLayout.OnUIRefreshListener {
            override fun onRefresh(curStatus: UIStatus) {
                ui_loader.postDelayed(Runnable {
                    if (curStatus == NET_ERROR)
                        //模拟数据为空
                        ui_loader.notifyUIStatus(DATA_EMPTY)
                    else
                        //模拟加载成功
                        ui_loader.notifyUIStatus(SUCCESS)
                }, 2000)
            }
        })
    }

    private fun initView() {
        ui_loader.postDelayed(Runnable {
            //模拟网络错误
            ui_loader.notifyUIStatus(NET_ERROR)
        }, 2000)
    }
}
