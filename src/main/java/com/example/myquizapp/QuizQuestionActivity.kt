package com.example.myquizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {
    private var mCurrentPosition: Int = 1
    private var mQuestionsList : ArrayList<Question>? =null
    private var mSelectedOptionPosition : Int = 0 
    private var mUserName : String? =null

    private var mCorrectAnswers: Int =0
    private var progressBar : ProgressBar? = null
    private var tvprogress : TextView? = null
    private var tvquestion : TextView?=null
    private var ivimage : ImageView?=null
    private var tvoptionOne: TextView?=null
    private var tvoptionTwo: TextView?=null
    private var tvoptionThree: TextView?=null
    private var tvoptionFour: TextView?=null
    private var btnSubmit : Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)
        mUserName = intent.getStringExtra(Constants.USER_NAME)
        progressBar = findViewById(R.id.tv_progressbar)
        tvprogress = findViewById(R.id.tv_progress)
        tvquestion = findViewById(R.id.tv_question)
        ivimage = findViewById(R.id.iv_image)
        tvoptionOne=findViewById(R.id.tv_option_one)
        tvoptionTwo=findViewById(R.id.tv_option_two)
        tvoptionThree=findViewById(R.id.tv_option_three)
        tvoptionFour=findViewById(R.id.tv_option_four)
        btnSubmit= findViewById(R.id.btn_submit)
        mQuestionsList = Constants.getQuestions()
        setQuestion()
        tvoptionOne?.setOnClickListener(this)
        tvoptionTwo?.setOnClickListener(this)
        tvoptionThree?.setOnClickListener(this)
        tvoptionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {
        defaultOptionsView()
        val question: Question = mQuestionsList!![mCurrentPosition - 1]
        ivimage?.setImageResource(question.image)
        progressBar?.progress = mCurrentPosition
        tvprogress?.text =  "$mCurrentPosition" + "/" + progressBar?.max
        tvquestion?.text = question.question
        tvoptionOne?.text = question.optionOne
        tvoptionTwo?.text = question.optionTwo
        tvoptionThree?.text = question.optionThree
        tvoptionFour?.text = question.optionFour


        if(mCurrentPosition==mQuestionsList!!.size){
            btnSubmit?.text = "FINISH"
        }
        else{
            btnSubmit?.text = "SUBMIT"
        }
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        tvoptionOne?.let{
            options.add(0,it)
        }
        tvoptionTwo?.let{
            options.add(1,it)
        }
        tvoptionThree?.let{
            options.add(2,it)
        }
        tvoptionFour?.let{
            options.add(3,it)
        }
        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface= Typeface.DEFAULT
            option.background=ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }

    }
    private fun selectedOptionView(tv:TextView, selectedOptionNum: Int){
        defaultOptionsView()
        mSelectedOptionPosition= selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background=ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }



    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tv_option_one -> {
                tvoptionOne?.let{
                    selectedOptionView(it,1)
                }
            }
            R.id.tv_option_two -> {
                tvoptionTwo?.let{
                    selectedOptionView(it,2)
                }
            }
            R.id.tv_option_three -> {
                tvoptionThree?.let{
                    selectedOptionView(it,3)
                }
            }
            R.id.tv_option_four -> {
                tvoptionFour?.let{
                    selectedOptionView(it,4)
                }
            }
            R.id.btn_submit -> {
                if(mSelectedOptionPosition==0){
                    mCurrentPosition++

                    when{
                        mCurrentPosition<=mQuestionsList!!.size ->{
                            setQuestion()
                        }
                        else -> {
                            //Toast.makeText(this, "You made it to the end", Toast.LENGTH_SHORT).show()
                            val intent= Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS,mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionsList?.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                else{
                    val question = mQuestionsList?.get(mCurrentPosition-1)
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition,R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }

                    answerView(question.correctAnswer,R.drawable.correct_option_border_bg)
                    if(mCurrentPosition== mQuestionsList!!.size){
                        btnSubmit?.text= "FINISH"

                    }else{
                        btnSubmit?.text="GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }

            }
        }
    }
    private fun answerView(answer:Int, drawableView: Int){
        when (answer){
            1 -> {
            tvoptionOne?.background = ContextCompat.getDrawable(
                this,
                drawableView
            )
            }
            2 -> {
                tvoptionTwo?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            3 -> {
                tvoptionThree?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            4 -> {
                tvoptionFour?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }
}