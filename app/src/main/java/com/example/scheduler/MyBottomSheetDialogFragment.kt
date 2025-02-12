import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.scheduler.MainActivity
import com.example.scheduler.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyBottomSheetDialogFragment : BottomSheetDialogFragment() {
    lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView: SearchView = view.findViewById(R.id.search)
        textView = view.findViewById(R.id.textView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Вызывается при отправке запроса (нажатии Enter)
                textView.text="Вы искали: $query"

                //Toast.makeText(this@MyBottomSheetDialogFragment, "Вы искали: $query", Toast.LENGTH_SHORT).show()
                // Здесь можно выполнить поиск данных
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Вызывается при изменении текста в поле поиска
                // Здесь можно выполнять фильтрацию списка в реальном времени
                // Например, вызывать функцию filterList(newText)

                return true
            }
        })


    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }
}
