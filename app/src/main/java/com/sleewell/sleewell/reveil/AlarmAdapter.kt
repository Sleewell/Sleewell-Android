package com.sleewell.sleewell.reveil

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sleewell.sleewell.R
import com.sleewell.sleewell.nav.alarms.CellClickListener
import kotlinx.android.synthetic.main.layout_reminder_row.*
import kotlinx.android.synthetic.main.layout_reminder_row.view.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for Alarm Activity
 *
 * @property reminderList List of the reminders
 * @author Romane Bézier
 */
class AlarmAdapter(private val reminderList: List<Long>, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    /**
     * View holder class
     *
     * @param itemView Item of the view
     * @author Romane Bézier
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
    }

    /**
     * When create view Holder
     *
     * @param viewGroup The group of the view
     * @param i Index
     * @return The view holder
     * @author Romane Bézier
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_reminder_row, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * When the view holder is bind
     *
     * @param viewHolder The view holder
     * @param i Index
     * @author Romane Bézier
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val reminderData = reminderList[i]
        val date = Date(reminderData)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("GMT+2")
        val formatted: String = formatter.format(date)

        viewHolder.textViewTime.text = formatted
        viewHolder.textViewTime.setOnClickListener {
            //CANCEL L'ALARME
            cellClickListener.launchTimePicker()
        }
    }

    /**
     * Get size of the list
     *
     * @return Size of the list
     * @author Romane Bézier
     */
    override fun getItemCount(): Int {
        return reminderList.size ?: 0
    }
}