package com.lodz.android.pokemondex.ui.pokedex.list

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lodz.android.corekt.anko.getAssetsFileContent
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper
import com.lodz.android.pandora.utils.jackson.parseJsonObject
import com.lodz.android.pokemondex.bean.base.BaseListBean
import com.lodz.android.pokemondex.bean.poke.pkm.PkmGenBean
import com.lodz.android.pokemondex.bean.poke.pkm.PkmInfoBean
import com.lodz.android.pokemondex.config.Constant
import com.lodz.android.pokemondex.utils.PokeUtils

/**
 * @author zhouL
 * @date 2022/3/11
 */
class PokeDexViewModel : BaseRefreshViewModel() {

    /** 宝可梦数据列表 */
    private var mDataList: ArrayList<PkmInfoBean> = arrayListOf()
    /** 筛选后的数据 */
    var mFilterList = MutableLiveData<ArrayList<PkmGenBean>>()

    /** 查询宝可梦数据列表，上下文[context]，关键字[content] */
    fun requestDataList(context: Context, content: String) {
        CoroutinesWrapper.create(this)
            .request {
                if (mDataList.isEmpty()){
                    mDataList = PokeUtils.getPokemonList(context)
                }
                if (content.isEmpty()) {
                    return@request getDataMap(mDataList)
                }
                return@request getDataMap(filterData(mDataList, content))
            }
            .action {
                onSuccess {
                    setSwipeRefreshFinish()
                    mFilterList.value = it
                    showStatusCompleted()
                }
                onError { e, isNetwork ->
                    showStatusError(e)
                    toastShort(e.toString())
                }
            }
    }

    /** 过滤数据 */
    private fun filterData(data: List<PkmInfoBean>, content: String): List<PkmInfoBean> {
        val list = arrayListOf<PkmInfoBean>()
        data.forEach {
            if (it.name.contains(content)) {
                list.add(it)
            }
        }
        return list
    }

    /** 数据分级 */
    private fun getDataMap(data: List<PkmInfoBean>): ArrayList<PkmGenBean> {
        val gen1Bean = createPkmGenBean(Constant.POKE_GENERATION_1)
        val gen2Bean = createPkmGenBean(Constant.POKE_GENERATION_2)
        val gen3Bean = createPkmGenBean(Constant.POKE_GENERATION_3)
        val gen4Bean = createPkmGenBean(Constant.POKE_GENERATION_4)
        val gen5Bean = createPkmGenBean(Constant.POKE_GENERATION_5)
        val gen6Bean = createPkmGenBean(Constant.POKE_GENERATION_6)
        val gen7Bean = createPkmGenBean(Constant.POKE_GENERATION_7)
        val gen8Bean = createPkmGenBean(Constant.POKE_GENERATION_8)

        for (item in data) {
            if (item.generation == Constant.POKE_GENERATION_1) {
                gen1Bean.pkmList.add(item)
                continue
            }
            if (item.generation == Constant.POKE_GENERATION_2) {
                gen2Bean.pkmList.add(item)
                continue
            }
            if (item.generation == Constant.POKE_GENERATION_3) {
                gen3Bean.pkmList.add(item)
                continue
            }
            if (item.generation == Constant.POKE_GENERATION_4) {
                gen4Bean.pkmList.add(item)
                continue
            }
            if (item.generation == Constant.POKE_GENERATION_5) {
                gen5Bean.pkmList.add(item)
                continue
            }
            if (item.generation == Constant.POKE_GENERATION_6) {
                gen6Bean.pkmList.add(item)
                continue
            }
            if (item.generation == Constant.POKE_GENERATION_7) {
                gen7Bean.pkmList.add(item)
                continue
            }
            if (item.generation == Constant.POKE_GENERATION_8) {
                gen8Bean.pkmList.add(item)
                continue
            }
        }
        val list = ArrayList<PkmGenBean>()
        if (gen1Bean.pkmList.size > 0){
            list.add(gen1Bean)
        }
        if (gen2Bean.pkmList.size > 0){
            list.add(gen2Bean)
        }
        if (gen3Bean.pkmList.size > 0){
            list.add(gen3Bean)
        }
        if (gen4Bean.pkmList.size > 0){
            list.add(gen4Bean)
        }
        if (gen5Bean.pkmList.size > 0){
            list.add(gen5Bean)
        }
        if (gen6Bean.pkmList.size > 0){
            list.add(gen6Bean)
        }
        if (gen7Bean.pkmList.size > 0){
            list.add(gen7Bean)
        }
        if (gen8Bean.pkmList.size > 0){
            list.add(gen8Bean)
        }
        return list
    }

    private fun createPkmGenBean(gen: Int): PkmGenBean {
        val bean = PkmGenBean()
        bean.generation = gen
        bean.isExpand = gen == 1
        return bean
    }
}