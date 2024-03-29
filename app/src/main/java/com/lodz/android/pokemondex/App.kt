package com.lodz.android.pokemondex

import android.content.Context
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.imageloaderkt.ImageloaderManager
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pokemondex.db.dao.ObjectBox

/**
 * @author zhouL
 * @date 2022/3/10
 */
class App : BaseApplication() {

    override fun onStartCreate() {
        initImageLoader(this)
        NetworkManager.get().init(this)
        configTitleBarLayout()
        configLoadingLayout()
        ObjectBox.init(this)
    }

    /** 配置标题栏 */
    private fun configTitleBarLayout() {
        getBaseLayoutConfig().getTitleBarLayoutConfig().backgroundColor = R.color.color_d04741
        getBaseLayoutConfig().getTitleBarLayoutConfig().titleTextColor = R.color.white
    }

    private fun configLoadingLayout() {
        getBaseLayoutConfig().getLoadingLayoutConfig().useSysDefDrawable = true
    }

    /** 初始化图片加载库 */
    private fun initImageLoader(context: Context) {
        ImageloaderManager.get().newBuilder()
            .setPlaceholderResId(R.drawable.ic_pokeball)//设置默认占位符
            .setErrorResId(R.drawable.ic_pokeball)// 设置加载失败图
            .setDirectoryFile(context.cacheDir)// 设置缓存路径
            .setDirectoryName("image_cache")// 缓存文件夹名称
            .build()
    }

    override fun configJackson(objectMapper: ObjectMapper) {
        super.configJackson(objectMapper)
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)//忽略大小写
    }

    override fun onExit() {
        NetworkManager.get().release(this)
        NetworkManager.get().clearNetworkListener()
    }
}