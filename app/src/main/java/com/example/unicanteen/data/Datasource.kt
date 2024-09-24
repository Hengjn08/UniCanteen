package com.example.unicanteen.data

import com.example.unicanteen.R
import com.example.unicanteen.model.Food
import com.example.unicanteen.model.User

object  Datasource{
    val foods = mutableListOf(
        Food(0,"padas", "Sdasdsadad", R.drawable.pan_mee.toString(), 6.50, true,"Mee"),
//        Food(R.string.curry_mee, R.string.CurryMeeDesc, R.drawable.curry_mee, 7.50),
//        Food(R.string.laksa, R.string.CurryMeeDesc, R.drawable.laksa, 7.50),
//        Food(R.string.wonton_noodle, R.string.CurryMeeDesc, R.drawable.wonton_noodle, 6.00),
//        Food(R.string.dumpling_noodle, R.string.CurryMeeDesc, R.drawable.dumpling_noodle, 6.00),
//        Food(R.string.char_kway_teow, R.string.CurryMeeDesc, R.drawable.char_kway_teow, 6.50),
//        Food(R.string.mee_goreng, R.string.CurryMeeDesc, R.drawable.mee_goreng, 7.00),
    )
    val users = mutableListOf(
        User(1001,"Shin","siangshin2002@gmail.com","123456abc","Normal",true)
    )
}