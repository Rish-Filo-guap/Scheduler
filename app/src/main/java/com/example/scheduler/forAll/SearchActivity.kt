package com.example.scheduler.forAll

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduler.R
import com.example.scheduler.scheduleProcessing.GrPrCl
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SearchActivity(var parent: ShowBottomFragmentDialogSearch) : BottomSheetDialogFragment() {

    private lateinit var groupSearchView: SearchView

    private lateinit var suggestionsRecyclerView: RecyclerView
    private lateinit var suggestionAdapter: SuggestionAdapter

    // Пример данных для поиска.  Замените его вашими данными
    private val allSuggestions = GrPrCl().groups.keys.toList() + GrPrCl().prepods.keys.toList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)


        groupSearchView = view.findViewById(R.id.group_search_view)


        suggestionsRecyclerView = view.findViewById(R.id.suggestions_recycler_view)

        // Настройка RecyclerView
        suggestionsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        suggestionAdapter = SuggestionAdapter(emptyList()) { selectedSuggestion ->
            // Обработка выбора подсказки. Например, заполнение EditText

            suggestionsRecyclerView.visibility = View.GONE

            parent.groupChanged(selectedSuggestion)
            dismiss()
        }

        suggestionsRecyclerView.adapter = suggestionAdapter
        suggestionAdapter.updateData(allSuggestions)


        groupSearchView.let {
            it.isIconified = false // Разворачиваем SearchView
            it.requestFocusFromTouch() // Запрашиваем фокус (для эмуляторов/устройств без аппаратной клавиатуры)


        }


        groupSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {

                val filteredSuggestions = filterSuggestions(newText.toString())
                suggestionAdapter.updateData(filteredSuggestions)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    if (allSuggestions.contains(query.toString())) {
                        parent.groupChanged(query.toString())
                    } else {
                        if (query.toString().length < 4 || (query.toString()[0].isDigit()))
                            Toast.makeText(context, "Ничего не найдено", Toast.LENGTH_SHORT).show()
                        else
                            Log.d("SearchActivity !!!!", query.toString())
                        parent.codeChanged(query.toString())
                    }
                    dismiss()
                    return true
                }
                return false

            }
        })
    }

    // Функция для фильтрации списка подсказок на основе введенного текста
    private fun filterSuggestions(searchText: String): List<String> {
        return allSuggestions.filter { it.startsWith(searchText, ignoreCase = true) }
    }
}
