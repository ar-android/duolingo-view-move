package com.ahmadrosid.duolingoviewmove

import android.view.View

data class Answer(
        var view: View? = null,
        var actualPositionX: Float = 0F,
        var actualPositionY: Float = 0F,
        var text: String = "",
        var removeListener: RemoveAnswerListener? = null
){
    init {
        view?.setOnClickListener {
            it.animate()
                .x(actualPositionX)
                .y(actualPositionY)
            removeListener?.onRemove(this)
        }
    }
}

interface RemoveAnswerListener{
    fun onRemove(answer: Answer)
}