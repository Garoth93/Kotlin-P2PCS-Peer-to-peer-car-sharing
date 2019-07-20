package com.fourteenrows.p2pcs.activities.tutorial

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.fourteenrows.p2pcs.R

class TutorialListAdapter(
    tutorialActivity: TutorialActivity,
    private var list: List<String>,
    private var map: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    private var context = tutorialActivity

    override fun getGroup(groupPosition: Int) = list[groupPosition]

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = false

    override fun hasStableIds() = false

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val title = getGroup(groupPosition)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.tutorial_main, parent, false)
        val viewTutorial = view.findViewById(R.id.viewTutorial) as TextView
        viewTutorial.setTypeface(null, Typeface.BOLD)
        viewTutorial.text = title
        return view
    }

    override fun getChildrenCount(groupPosition: Int) = map[list[groupPosition]]!!.size

    override fun getChild(groupPosition: Int, childPosition: Int) = map[list[groupPosition]]!![childPosition]

    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val desc = getChild(groupPosition, childPosition)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.tutorial_description, parent, false)
        val viewDescription = view.findViewById(R.id.viewDescription) as TextView
        viewDescription.text = desc
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()

    override fun getGroupCount() = list.size
}