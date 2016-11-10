@file:Suppress("DEPRECATION")

package com.github.tehras.loanapplication.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.tehras.loanapplication.R
import com.github.tehras.loanapplication.data.remote.models.Payment
import kotlinx.android.synthetic.main.home_loan_chart_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class HomePaymentsChartLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr), IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return dateFormatterOutput.format(Date(value.toLong()))
    }

    override fun getDecimalDigits(): Int {
        return 2
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    init {
        View.inflate(context, R.layout.home_loan_chart_layout, this)

        line_chart_layout.initHomeLayout()
    }

    private fun LineChart.initHomeLayout() {
        this.xAxis.setDrawAxisLine(false)
        this.xAxis.setDrawGridLines(false)
        this.xAxis.valueFormatter = this@HomePaymentsChartLayout
        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
        this.xAxis.setDrawLabels(true)
        this.xAxis.textColor = resources.getColor(R.color.colorPrimary)

        this.legend.form = Legend.LegendForm.NONE

        this.axisRight.isEnabled = false
        this.axisLeft.textColor = context.resources.getColor(R.color.colorPrimary, null)
        this.axisLeft.setDrawGridLines(false)
        this.axisLeft.setDrawAxisLine(false)
        this.axisLeft.isEnabled = true

        this.description = Description()
        this.description.text = ""
        this.setNoDataText("Loading, Please Wait")
        this.setNoDataTextColor(resources.getColor(R.color.colorPrimary))

        this.setTouchEnabled(false)
    }

    fun updateData(payments: ArrayList<Payment>?) {
        if (payments == null)
            return

        val data: LineData = LineData()
        val vals: ArrayList<Entry> = ArrayList()

        payments.forEach {
            vals.add(Entry(it.getFormattedDate().toFloat(), it.balance.toFloat()))
        }

        val dataSet: LineDataSet = LineDataSet(vals, "")
        dataSet.initBalanceProperties()
        dataSet.label = ""

        data.addDataSet(dataSet)
        data.initBalanceProperties()
        line_chart_layout.data = data
        line_chart_layout.animateX(500)
        line_chart_layout.notifyDataSetChanged()
    }

    val dateFormatter = SimpleDateFormat("yyyyMM", Locale.US)
    val dateFormatterFull = SimpleDateFormat("yyyyMMdd", Locale.US)
    val dateFormatterOutput = SimpleDateFormat("MMM yy", Locale.US)

    fun Payment.getFormattedDate(): Long {
        val date: Date?

        if (paymentDate.length == 6)
            date = dateFormatter.parse(paymentDate)
        else
            date = dateFormatterFull.parse(paymentDate)
        return date.time
    }


    fun LineDataSet.initBalanceProperties() {
        this.setDrawCircles(false)

        this.lineWidth = 2.0f
        this.color = line_chart_layout.context.resources.getColor(R.color.colorPrimary, null)
        this.label = ""

        this.mode = LineDataSet.Mode.CUBIC_BEZIER
    }

    fun LineData.initBalanceProperties() {
        this.setDrawValues(false)
    }
}
