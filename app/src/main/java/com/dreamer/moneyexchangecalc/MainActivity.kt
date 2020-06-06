package com.dreamer.moneyexchangecalc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import android.support.v7.widget.AppCompatButton
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView


const val HUNDRED_PERCENT = 100.00

//const val SWAP_CURRENCY = false

class MainActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    // Top Section

    private lateinit var exchangeRateEditText: EditText
    private lateinit var enterBefExchSumEditText: EditText

    // Tip Section
    //   private lateinit var addTipButton: ImageButton
//    private lateinit var tipTextView: TextView
    private lateinit var amountTotalTextView: TextView
    private lateinit var swapExchButton: AppCompatButton

    // Currency of Section
    private lateinit var befExchMoneyIconButton: ImageButton
    private lateinit var resultCurrencyButton: ImageButton


    //применять setAutofillHints(View.AUTOFILL_HINT_PASSWORD) - можно с API 26 OREO - или ставим в разметке android:hint="@string/entExchSum"
//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    //чекаем версию SDK - для работы AUTO-HINT )))
    //    @RequiresApi(Build.VERSION_CODES.O)
    //Проводим инициацию вьюшек
    private fun initViews() {
        //сапостовляем объект и ID ссылку в макете разметки (файлик ресурсов R)
        //используем старый вариант (новый (Button doSomethingButton = (Button) findViewById(R.id.do_something_b))

        enterBefExchSumEditText = findViewById(R.id.enterBefExchSumEditText)
        amountTotalTextView = findViewById(R.id.amountTotalTextView)
        exchangeRateEditText = findViewById(R.id.exchangeRateEditText)
        swapExchButton = findViewById(R.id.swapExchButton)
        befExchMoneyIconButton = findViewById(R.id.befExchMoneyIconButton)
        resultCurrencyButton = findViewById(R.id.resultCurrencyButton)


        //вешаем слушатель нажатий
        swapExchButton.setOnClickListener(this)

        //затираем поля ввода
        clearEditTextField()
        //вешаем слушатель на изменения в полях ввода данных
        enterBefExchSumEditText.addTextChangedListener(this)
        exchangeRateEditText.addTextChangedListener(this)
    }

    private fun clearEditTextField() {
        //чистим поля ввода на экране
        enterBefExchSumEditText.text.clear()
        exchangeRateEditText.text.clear()
        amountTotalTextView.text = ""
        //выставляем значение из условия - курс конвертации 74руб
        exchangeRateEditText.text.append(74.toString())
        //ставим курсор справа от наполнения
        //        exchangeRateEditText.setSelection(exchangeRateEditText.getText().length())
        //балуемся с автоподсказками - правда нужен ANDROID SDK не ниже 26й версии
        //        exchangeRateEditText.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
    }

    //тут считаем денежки
    private fun calculateExchange() {
        //получаем значения из полей ввода
        val totalExchRate = exchangeRateEditText.text.toString().toDouble()
        Log.e("totalExchRate:", "$totalExchRate")
        val totalSumBefExch = enterBefExchSumEditText.text.toString().toDouble()
        Log.e("totalSumBefExch:", "$totalSumBefExch")

        //чекаем нажатие кнопки SWAP для инверсии операции между валютами
        if (OPERATION_CHANGE == false) {
            Log.e("calculateExchange:", "$OPERATION_CHANGE" + " chageCurrencyImagesRubUsd()")
            //считаем
            val totalExchangeSum = totalSumBefExch / totalExchRate


            //выводим результат на экран
            amountTotalTextView.text = String.format("%.2f", totalExchangeSum)

        } else {

            Log.e("calculateExchange:", "$OPERATION_CHANGE" + " chageCurrencyImagesUsdRub()")
            //считаем
            val totalExchangeSum = totalSumBefExch * totalExchRate
            Log.e("totalExchangeSum:", "$totalExchangeSum" + " chageCurrencyImagesUsdRub()")

            //выводим результат на экран
            amountTotalTextView.text = String.format("%.2f", totalExchangeSum)

        }


    }

    //оставим на будущее - вдруг надо будет отработаь нажатие кнопок
    override fun onClick(v: View?) {
        when (v?.id) {
            //передаем ID ресурса на котором сработало нажатие в функцию swapExch()
            R.id.swapExchButton -> swapExch()
        }
    }

    private fun swapExch() {

        if (SWAP_CURRENCY == false) {
            Log.e("SWAP_CURRENCY:", "$SWAP_CURRENCY" + " chageCurrencyImagesRubUsd()")
            //чистим поля ввода данных
            clearEditTextField()
            //меняем местами картинки с эскизами валют через функцию
            chageCurrencyImagesRubUsd()

        } else {

            Log.e("SWAP_CURRENCY:", "$SWAP_CURRENCY" + " chageCurrencyImagesUsdRub()")
            //чистим поля ввода данных
            clearEditTextField()
            //меняем местами картинки с эскизами валют через функцию
            chageCurrencyImagesUsdRub()
        }

    }

    private fun chageCurrencyImagesRubUsd() {
        //сама замена картинок валют на местности
        befExchMoneyIconButton.setImageResource(R.drawable.currency_rub_black)
        resultCurrencyButton.setImageResource(R.drawable.currency_usd)
        SWAP_CURRENCY = true
        OPERATION_CHANGE = false
    }

    private fun chageCurrencyImagesUsdRub() {
        //сама замена картинок валют на местности
        befExchMoneyIconButton.setImageResource(R.drawable.currency_usd_black)
        resultCurrencyButton.setImageResource(R.drawable.currency_rub)
        SWAP_CURRENCY = false
        OPERATION_CHANGE = true
    }


    //Отрабатываем изменения в полях с вводом данных от пользователя
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        if ((exchangeRateEditText.text.isNotBlank() && enterBefExchSumEditText.text.isNotBlank())) {
            //Бежим считать денежки
            calculateExchange()
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    companion object {
        //вспомогательные булевы переменные - ключи
        var SWAP_CURRENCY = false
        var OPERATION_CHANGE = false
    }
}
