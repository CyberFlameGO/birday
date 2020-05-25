package com.minar.birday.backup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.minar.birday.MainActivity
import com.minar.birday.R

class BirdayImporter(context: Context?, attrs: AttributeSet?) : Preference(context, attrs), View.OnClickListener {

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val v = holder.itemView
        v.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val act = context as MainActivity
        act.vibrate()
        if (!act.importBirthdays()) Toast.makeText(context, context.getString(R.string.import_failed), Toast.LENGTH_SHORT).show()

    }
}