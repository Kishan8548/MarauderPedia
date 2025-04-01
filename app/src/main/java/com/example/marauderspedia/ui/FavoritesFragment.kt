import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marauderspedia.R
import com.example.marauderspedia.models.Character
import com.example.marauderspedia.ui.CharacterAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter
    private val favoriteCharacters = mutableListOf<Character>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_favorites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_favorites)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CharacterAdapter(favoriteCharacters) { character ->
            // Open Character Details on Click
            val fragment = CharacterDetailsFragment.newInstance(character)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(ItemSpacingDecoration(16))
        loadFavorites()
    }

    private fun loadFavorites() {
        val sharedPreferences = requireActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all

        favoriteCharacters.clear()

        for ((_, json) in allEntries) {
            val character = Gson().fromJson(json as String, Character::class.java)
            favoriteCharacters.add(character)
        }

        if (favoriteCharacters.isEmpty()) {
            Toast.makeText(requireContext(), "No favorites added!", Toast.LENGTH_SHORT).show()
        }

        adapter.notifyDataSetChanged()
    }
}


