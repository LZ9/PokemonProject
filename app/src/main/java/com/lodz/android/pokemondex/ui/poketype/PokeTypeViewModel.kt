package com.lodz.android.pokemondex.ui.poketype

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lodz.android.corekt.anko.getAssetsFileContent
import com.lodz.android.corekt.anko.toArrayList
import com.lodz.android.corekt.security.AES
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper
import com.lodz.android.pandora.utils.jackson.parseJsonObject
import com.lodz.android.pokemondex.R
import com.lodz.android.pokemondex.bean.base.BaseListBean
import com.lodz.android.pokemondex.bean.poke.type.PkmTypeInfoBean
import com.lodz.android.pokemondex.config.Constant
import kotlin.collections.ArrayList

/**
 * 属性ViewModel
 * @author zhouL
 * @date 2022/3/23
 */
class PokeTypeViewModel :BaseViewModel(){

    var mTypeList = MutableLiveData<ArrayList<PkmTypeInfoBean>>()

    var mTableList = MutableLiveData<ArrayList<PkmTypeInfoBean>>()

    /** 请求属性数据 */
    fun requestData(context: Context){
        CoroutinesWrapper.create(this)
            .request {
                val json = AES.decrypt(context.getAssetsFileContent(Constant.TYPE_INFO_FILE_NAME), AES.KEY) ?: ""
                val list = json.parseJsonObject<BaseListBean<PkmTypeInfoBean>>()?.records ?: ArrayList()
                Pair(list, getTableList(context, list))
            }
            .action {
                onSuccess {
                    mTypeList.value = it.first.toArrayList()
                    mTableList.value = it.second.toArrayList()
                    showStatusCompleted()
                }

                onError { e, isNetwork ->
                    toastShort(e.toString())
                    showStatusError(e)
                }
            }
    }

    /** 获取属性相克表数据 */
    private fun getTableList(context: Context, list: ArrayList<PkmTypeInfoBean>): ArrayList<PkmTypeInfoBean> {
        val tableList = ArrayList<PkmTypeInfoBean>()
        tableList.add(PkmTypeInfoBean())
        tableList.addAll(list)
        val count = (list.size + 1) * list.size
        var index = 0
        for (i in 0 until count) {
            if (i % (list.size + 1) == 0) {
                tableList.add(list[index])
                index++
                continue
            }
            val defBean = list[(i- index) % list.size] // 当前空格对应的防守方属性
            val attBean = list[index - 1] // 当前空格对应的进攻方属性
            val bean = PkmTypeInfoBean()
            attBean.attDouble.forEach {
                if (it == defBean.nameCN){
                    bean.nameCN = context.getString(R.string.poke_type_double)
                    return@forEach
                }
            }
            attBean.attNotEffective.forEach {
                if (it == defBean.nameCN){
                    bean.nameCN = context.getString(R.string.poke_type_not_effective)
                    return@forEach
                }
            }
            attBean.attInvalid.forEach {
                if (it == defBean.nameCN){
                    bean.nameCN = context.getString(R.string.poke_type_invalid)
                    return@forEach
                }
            }
            tableList.add(bean)
        }
        return tableList
    }
}