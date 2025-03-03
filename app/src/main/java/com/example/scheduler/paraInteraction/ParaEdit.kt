package com.example.scheduler.paraInteraction

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduler.forAll.InvaludateSchedule
import com.example.scheduler.R
import com.example.scheduler.ScheduleProcessing.GrPrCl
import com.example.scheduler.ScheduleProcessing.Para
import com.example.scheduler.forAll.SuggestionAdapter
import com.google.android.material.snackbar.Snackbar


class ParaEdit(private val para: Para,private val parent: InvaludateSchedule) : BottomSheetDialogFragment() {

  private lateinit var linearLayout: LinearLayout
  private lateinit var searchView: SearchView
  private lateinit var suggestionsRecyclerView: RecyclerView
  private lateinit var suggestionAdapter: SuggestionAdapter

    private val allSuggestions = GrPrCl().classes.keys.toList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_para_edit_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)

        linearLayout=view.findViewById(R.id.paraedit_linearLayout)
        changeParaMenu(para,view)
        linearLayout.invalidate()


       // linearLayout.addView(GenParaSchedule(para))

    }
    @SuppressLint("ResourceAsColor")
    private fun changeParaMenu(para: Para, view: View){
        var selectedOutside=false
        
        if(para.dayOfWeek==8) selectedOutside=true

        var selectedDay=para.dayOfWeek
        var selectedWeek:Int=para.weekType
        var selectedNumb:Int=para.numb
        var selectedClass:String=para.classRoom

        
        
        
        val weekRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupWeekParity)
        val numbRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupNumbPara)
        val dayRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupDayPara)
        val idesForWeek= arrayOf(
            R.id.radioButtonAllWeek,
            R.id.radioButtonDownWeek,
            R.id.radioButtonUpWeek
        )
        val checkBox:CheckBox=view.findViewById(R.id.para_edit_check_box)
        val idesForNumb= arrayOf(
            R.id.radioButtonPara1,
            R.id.radioButtonPara2,
            R.id.radioButtonPara3,
            R.id.radioButtonPara4,
            R.id.radioButtonPara5,
            R.id.radioButtonPara6,
            R.id.radioButtonPara7,
            )
        val idesForDays= arrayOf(
            R.id.radioButtonDayPara1,
            R.id.radioButtonDayPara2,
            R.id.radioButtonDayPara3,
            R.id.radioButtonDayPara4,
            R.id.radioButtonDayPara5,
            R.id.radioButtonDayPara6,

        )
        searchView=view.findViewById(R.id.searchViewClass)


        searchView.setQuery(para.classRoom,false)
        weekRadioGroup.check(idesForWeek[para.weekType])
        numbRadioGroup.check(idesForNumb[para.numb-1])
        dayRadioGroup.check(idesForDays[para.dayOfWeek])
        checkBox.isChecked=para.isOutside

        
        weekRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val checkedRadioButton = group.findViewById<View>(checkedId)
                val index = group.indexOfChild(checkedRadioButton)
                selectedWeek=index
            }
        }
        numbRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val checkedRadioButton = group.findViewById<View>(checkedId)
                val index = group.indexOfChild(checkedRadioButton)
                selectedNumb=index+1
            }
        }
        dayRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val checkedRadioButton = group.findViewById<View>(checkedId)
                val index = group.indexOfChild(checkedRadioButton)
                selectedDay=index
            }
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
                selectedOutside=isChecked

        }
        
        
        
        



        suggestionsRecyclerView = view.findViewById(R.id.para_edit_recycler_view)

        suggestionsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        suggestionAdapter = SuggestionAdapter(emptyList()) { selectedSuggestion ->
            // Обработка выбора подсказки. Например, заполнение EditText


            // parent.groupChanged(selectedSuggestion)
            searchView.setQuery(selectedSuggestion,true)
            searchView.clearFocus()
            selectedClass=selectedSuggestion
            suggestionsRecyclerView.visibility = View.GONE
            //dismiss()
        }
        suggestionsRecyclerView.adapter = suggestionAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                suggestionsRecyclerView.visibility = View.VISIBLE
                val filteredSuggestions = filterSuggestions(newText.toString())
                suggestionAdapter.updateData(filteredSuggestions)

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }
        })
        val resetBtn:Button=view.findViewById(R.id.buttonResetLesson)
        resetBtn.setOnClickListener {
            searchView.setQuery(para.classRoom,false)
            weekRadioGroup.check(idesForWeek[para.weekType])
            numbRadioGroup.check(idesForNumb[para.numb-1])
            dayRadioGroup.check(idesForDays[para.dayOfWeek])
            checkBox.isChecked=para.isOutside

        }


        val saveBtn:Button=view.findViewById(R.id.buttonSaveLesson)
        saveBtn.setOnClickListener {
               val res= para.SaveChanges(selectedDay, selectedWeek, selectedNumb, selectedOutside, selectedClass)

            Log.d("ew", "${res.first} !! ${res.second}")
            val snackbar = Snackbar.make(it, res.second, if(res.first) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_INDEFINITE)
                .setAction("закрыть") {


                }
                .setTextMaxLines(10)
                .setBackgroundTint(if(res.first) Color.argb(200,20, 20, 20) else Color.argb(255,36, 8, 8))
                .setTextColor(Color.WHITE)
                .setActionTextColor(Color.WHITE)

            snackbar.show()
            if (res.first) parent.invalidateSchedule()
        }

    }



    private fun filterSuggestions(searchText: String): List<String> {
        val tmp=allSuggestions.filter { it.startsWith(searchText, ignoreCase = true) }


        return tmp
    }


}