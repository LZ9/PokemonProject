package com.lodz.android.pokemondex.ui.pokedex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.mvvm.base.activity.BaseRefreshVmActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel
import com.lodz.android.pokemondex.R
import com.lodz.android.pokemondex.databinding.ActivityPokedexListBinding

/**
 * 图鉴页面
 * @author zhouL
 * @date 2022/3/11
 */
class PokeDexActivity : BaseRefreshVmActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PokeDexActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mViewModel by bindViewModel { PokeDexViewModel() }

    override fun getViewModel(): PokeDexViewModel = mViewModel

    private val mBinding: ActivityPokedexListBinding by bindingLayout(ActivityPokedexListBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    /** 适配器 */
    private lateinit var mAdapter: PokemonListAdapter

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.main_pokedex)
        getTitleBarLayout().elevation = 0f
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = PokemonListAdapter(getContext())
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mBinding.recyclerView.layoutManager = layoutManager
        mAdapter.onAttachedToRecyclerView(mBinding.recyclerView)// 如果使用网格布局请设置此方法
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.adapter = mAdapter
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        mViewModel.requestDataList(getContext())
    }

    override fun onDataRefresh() {
        mViewModel.requestDataList(getContext())
    }

    override fun setListeners() {
        super.setListeners()
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            toastShort(item.getGenStr())
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        mViewModel.mDataList.observe(getLifecycleOwner()) {
            mAdapter.setData(it)
        }
    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        mViewModel.requestDataList(getContext())
    }
}