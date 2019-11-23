package com.plain.simpleuiloaderlib

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

/**
 * Simple UI loader layout
 *
 * @author Plain
 * @date 2019-11-23 12:08
 */
class SimpleUILoaderLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * Loading layout id
     */
    private var loadingLayoutId = -1
    /**
     * Net error layout id
     */
    private var netErrorLayoutId = -1
    /**
     * Data empty layout id
     */
    private var dataEmptyLayoutId = -1

    /**
     * Loading view
     */
    private var loadingView: View? = null
    /**
     * Net error view
     */
    private var netErrorView: View? = null
    /**
     * Data empty view
     */
    private var dataEmptyView: View? = null

    /**
     * UI status refresh listener
     */
    private var refreshListener: OnUIRefreshListener? = null

    /**
     * Current UI status
     */
    var status: UIStatus? = null

    /**
     * Layout inflater
     */
    private var inflater: LayoutInflater? = null

    enum class UIStatus {

        //Loading
        LOADING,

        //Network error
        NET_ERROR,

        //Request data is empty
        DATA_EMPTY,

        //Request success
        SUCCESS

    }

    /**
     * UI refresh listener
     *
     * Callback only when the UI status is [UIStatus.NET_ERROR] and [UIStatus.DATA_EMPTY]
     */
    interface OnUIRefreshListener {

        /**
         * Refresh callback
         *
         * @param curStatus Current UI status
         */
        fun onRefresh(curStatus: UIStatus)

    }

    /**
     * Set UI refresh listener
     *
     * @param refreshListener OnUIRefreshListener
     */
    fun setRefreshListener(refreshListener: OnUIRefreshListener) {
        this.refreshListener = refreshListener
    }

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {

        val tr = context.obtainStyledAttributes(attrs, R.styleable.SimpleUILoaderLayout)
        loadingLayoutId = tr.getResourceId(
            R.styleable.SimpleUILoaderLayout_loadingLayout,
            R.layout.layout_ui_status_loading
        )
        netErrorLayoutId = tr.getResourceId(
            R.styleable.SimpleUILoaderLayout_netErrorLayout,
            R.layout.layout_ui_status_net_error
        )
        dataEmptyLayoutId = tr.getResourceId(
            R.styleable.SimpleUILoaderLayout_dataEmptyLayout,
            R.layout.layout_ui_status_data_empty
        )
        tr.recycle()

        inflater = LayoutInflater.from(context)

        //Default load UI status is {UIStatus.LOADING}
        notifyUIStatus(UIStatus.LOADING)

    }

    /**
     * Refresh ui according to status
     *
     * @param status UIStatus
     */
    fun notifyUIStatus(status: UIStatus) {

        this.status = status

        if (null == loadingView) {
            if (-1 != loadingLayoutId) {
                loadingView = getLoadingView(loadingLayoutId)
                addView(loadingView)
            } else {
                throw IllegalArgumentException("Loading layout is null!!!")
            }
        }

        loadingView!!.visibility = if (status == UIStatus.LOADING) View.VISIBLE else View.GONE

        if (null == netErrorView) {
            if (-1 != netErrorLayoutId) {
                netErrorView = getNetErrorView(netErrorLayoutId)
                addView(netErrorView)
            } else {
                throw IllegalArgumentException("Net error layout is null!!!")
            }
        }

        netErrorView!!.visibility = if (status == UIStatus.NET_ERROR) View.VISIBLE else View.GONE

        if (null == dataEmptyView) {
            if (-1 != dataEmptyLayoutId) {
                dataEmptyView = getDataEmptyView(dataEmptyLayoutId)
                addView(dataEmptyView)
            } else {
                throw IllegalArgumentException("Data empty layout is null!!!")
            }
        }

        dataEmptyView!!.visibility = if (status == UIStatus.DATA_EMPTY) View.VISIBLE else View.GONE

        visibility = if (status == UIStatus.SUCCESS) View.GONE else View.VISIBLE

    }

    /**
     * Get loading view
     *
     * @param loadingLayoutId Layout id
     * @return View
     */
    private fun getLoadingView(loadingLayoutId: Int): View {
        return inflater!!.inflate(loadingLayoutId, this, false)
    }

    /**
     * Get error view
     *
     * @param netErrorLayoutId Layout id
     * @return View
     */
    private fun getNetErrorView(netErrorLayoutId: Int): View {
        val view = inflater!!.inflate(netErrorLayoutId, this, false)
        initUIRefreshListener(view.findViewById(R.id.refresh), UIStatus.NET_ERROR)
        return view
    }

    /**
     * Get data empty view
     *
     * @param dataEmptyLayoutId Layout id
     * @return View
     */
    private fun getDataEmptyView(dataEmptyLayoutId: Int): View {
        val view = inflater!!.inflate(dataEmptyLayoutId, this, false)
        initUIRefreshListener(view.findViewById(R.id.refresh), UIStatus.DATA_EMPTY)
        return view
    }

    /**
     * Initial UI refresh listener
     *
     * @param view View
     */
    private fun initUIRefreshListener(view: View?, status: UIStatus) {
        view?.setOnClickListener {
            if (null != refreshListener) {
                notifyUIStatus(UIStatus.LOADING)
                refreshListener!!.onRefresh(status)
            }
        }
    }

}
