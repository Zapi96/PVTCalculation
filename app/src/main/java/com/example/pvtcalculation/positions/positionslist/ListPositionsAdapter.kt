package com.example.pvtcalculation.positions.positionslist

import com.example.pvtcalculation.Above
import com.example.pvtcalculation.R
import com.example.pvtcalculation.base.BaseRecyclerViewAdapter

class ListPositionsAdapter(callBack: (selectedPosition: Above) -> Unit) :
    BaseRecyclerViewAdapter<Above>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.it_position
}