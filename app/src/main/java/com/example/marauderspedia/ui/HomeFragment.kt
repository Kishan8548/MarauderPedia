import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marauderspedia.R
import com.example.marauderspedia.api.RetrofitClient
import com.example.marauderspedia.models.Character
import com.example.marauderspedia.ui.CharacterAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter
    private var characterList = mutableListOf<Character>()
    private var filteredList = mutableListOf<Character>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_home_fragment, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(ItemSpacingDecoration(16))
        // Enable toolbar menu options
        setHasOptionsMenu(true)
        fetchCharacters()
        return view
    }

    private fun fetchCharacters() {
        RetrofitClient.apiService.getCharacters().enqueue(object : Callback<List<Character>> {
            override fun onResponse(call: Call<List<Character>>, response: Response<List<Character>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        characterList.clear()
                        characterList.addAll(it)
                        filteredList.clear()
                        filteredList.addAll(characterList) // Set filtered list = full list

                        adapter = CharacterAdapter(filteredList) { character ->
                            val fragment = CharacterDetailsFragment.newInstance(character)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                Log.e("API_ERROR", "Failed to fetch characters", t)
            }
        })
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.ic_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterCharacters(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCharacters(newText)
                return true
            }
        })
    }


    private fun filterCharacters(query: String?) {
        filteredList.clear()
        if (!query.isNullOrEmpty()) {
            val lowerCaseQuery = query.lowercase()
            filteredList.addAll(characterList.filter { it.name.lowercase().contains(lowerCaseQuery) })
        } else {
            filteredList.addAll(characterList) // Show all if search is empty
        }
        adapter.notifyDataSetChanged()
    }
}
class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = spacing
        outRect.right = spacing
        outRect.top = spacing
        outRect.bottom = spacing
    }
}
