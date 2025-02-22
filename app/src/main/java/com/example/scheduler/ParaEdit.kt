package com.example.scheduler

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduler.ScheduleProcessing.GrPrCl
import com.example.scheduler.ScheduleProcessing.Para


class ParaEdit(val para: Para) : BottomSheetDialogFragment() {

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
        ChangeParaMenu(para,view)
        linearLayout.invalidate()

       // linearLayout.addView(GenParaSchedule(para))

    }
    @SuppressLint("ResourceAsColor")
    private fun ChangeParaMenu(para: Para, view: View){
        val weekRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupWeekParity)
        val numbRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupNumbPara)
        val dayRadioGroup:RadioGroup=view.findViewById(R.id.radioGroupDayPara)
        val idesForWeek= arrayOf(R.id.radioButtonAllWeek, R.id.radioButtonDownWeek, R.id.radioButtonUpWeek)
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
        weekRadioGroup.check(idesForWeek[para.weekType])
        numbRadioGroup.check(idesForNumb[para.numb-1])
        dayRadioGroup.check(idesForDays[para.dayOfWeek])

        searchView=view.findViewById(R.id.searchViewClass)
        searchView.setQuery(para.classRoom,false)

        suggestionsRecyclerView = view.findViewById(R.id.para_edit_recycler_view)

        suggestionsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        suggestionAdapter = SuggestionAdapter(emptyList()) { selectedSuggestion ->
            // Обработка выбора подсказки. Например, заполнение EditText


            // parent.groupChanged(selectedSuggestion)
            searchView.setQuery(selectedSuggestion,true)
            searchView.clearFocus();
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

    }



    private fun filterSuggestions(searchText: String): List<String> {
        val tmp=allSuggestions.filter { it.startsWith(searchText, ignoreCase = true) }


        return tmp
    }


}