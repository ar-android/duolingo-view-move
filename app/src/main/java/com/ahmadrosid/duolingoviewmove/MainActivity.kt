package com.ahmadrosid.duolingoviewmove

import android.animation.Animator
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RemoveAnswerListener {

    var listAnswers = mutableListOf<Answer>()

    private lateinit var actualAnswer: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actualAnswer = listOf(
                "How",
                "are",
                "you"
        )

        val answerOption = listOf(
                "morning",
                "are",
                "where",
                "not",
                "have",
                "How",
                "you"
        )

        answerOption.forEach {
            val key = TextView(answerBox.context)
            with(key){
                answerBox.addView(this)
                setBackgroundColor(Color.WHITE)
                text = it
                textSize = 18F
                setPadding(40, 20, 40, 20)
                val margin = key.layoutParams as FlexboxLayout.LayoutParams
                margin.setMargins(30, 30, 30, 30)
                layoutParams = margin
                setOnClickListener {
                    moveToAnswer(it)
                }
            }
        }

    }

    private fun moveToAnswer(view: View) {
        if(listAnswers.size < actualAnswer.size){

            view.setOnClickListener(null)

            listAnswers.add(Answer(view, view.x, view.y, (view as TextView).text.toString(), this@MainActivity))

            val topPosition = lineFirst.y - 120F

            var leftPosition = lineFirst.x
            if (listAnswers.size > 1) {
                var allWidth = 0F
                listAnswers.forEach {
                    allWidth += (it.view?.width)?.toFloat()!! + 20F
                }
                allWidth -= (view.width).toFloat()
                leftPosition = lineFirst.x + allWidth
            }

            val completeMove = object: Animator.AnimatorListener{
                override fun onAnimationRepeat(p0: Animator?) {}

                override fun onAnimationCancel(p0: Animator?) {}

                override fun onAnimationStart(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    if(listAnswers.size == actualAnswer.size){
                        showAnswer()
                    }
                }
            }

            view.animate()
                    .setListener(completeMove)
                    .x(leftPosition)
                    .y(topPosition)
        }
    }

    fun showAnswer(){
        var isCorrect = true
        actualAnswer.forEachIndexed {index, it ->
            if (!listAnswers[index].text.equals(it)) {
                isCorrect = false
            }
        }

        val answerMessage = if(isCorrect) "Jawaban anda benar" else "Jawaban kamu salah silahkan coba lagi."

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Result")
        alert.setMessage(answerMessage)
        alert.setPositiveButton("OK", {dialog, a ->
            dialog.dismiss()
        })
        alert.show()
    }

    override fun onRemove(answer: Answer) {
        listAnswers.remove(answer)
        var allWidth = 0F

        listAnswers.forEach {
            val view = it.view!!
            allWidth += (it.view?.width)?.toFloat()!! + 20F
            if (it == listAnswers.first()) {
                view.animate().x(0F)
            } else {
                allWidth -= view.width
                view.animate().x(allWidth)
            }
        }

        answer.view?.setOnClickListener {
            moveToAnswer(it)
        }
    }

}
