package com.example.scheduler

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class SearchActivity : BottomSheetDialogFragment() {

   // private lateinit var searchEditText: EditText
    private lateinit var searchView: SearchView
    private lateinit var suggestionsRecyclerView: RecyclerView
    private lateinit var suggestionAdapter: SuggestionAdapter

    // Пример данных для поиска.  Замените его вашими данными
    private val allSuggestions = Groups().groups.keys.toList()

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
        //setContentView(R.layout.activity_search)

        //searchEditText = view.findViewById(R.id.search_edit_text)
        searchView=view.findViewById(R.id.search_view)
        suggestionsRecyclerView = view.findViewById(R.id.suggestions_recycler_view)

        // Настройка RecyclerView
        suggestionsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        suggestionAdapter = SuggestionAdapter(emptyList()) { selectedSuggestion ->
            // Обработка выбора подсказки. Например, заполнение EditText
           // searchEditText.setText(selectedSuggestion)
            suggestionsRecyclerView.visibility = View.GONE
            (context as SelectGroup).groupChanged(selectedSuggestion)
            dismiss()
        }
        suggestionsRecyclerView.adapter = suggestionAdapter

        searchView?.let {
            it.isIconified = false // Разворачиваем SearchView
            it.requestFocusFromTouch() // Запрашиваем фокус (для эмуляторов/устройств без аппаратной клавиатуры)


        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {

                val filteredSuggestions = filterSuggestions(newText.toString())
                suggestionAdapter.updateData(filteredSuggestions)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                (context as SelectGroup).groupChanged(query.toString())
                dismiss()
                return true
            }




        })



    }

    // Функция для фильтрации списка подсказок на основе введенного текста
    private fun filterSuggestions(searchText: String): List<String> {
        return allSuggestions.filter { it.startsWith(searchText, ignoreCase = true) }
    }
}
