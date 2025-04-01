import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.marauderspedia.R
import com.example.marauderspedia.models.Character
import com.google.gson.Gson

class CharacterDetailsFragment : Fragment() {

    private lateinit var character: Character

    companion object {
        fun newInstance(character: Character): CharacterDetailsFragment {
            val fragment = CharacterDetailsFragment()
            val args = Bundle()
            args.putString("character_json", Gson().toJson(character))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("character_json")?.let { json ->
            character = Gson().fromJson(json, Character::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_character_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        val toolbar = activity.supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            .setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

        val imageView: ImageView = view.findViewById(R.id.character_details_image)
        val nameTextView: TextView = view.findViewById(R.id.character_details_name)
        val alternateNamesTextView: TextView = view.findViewById(R.id.character_details_alternate_names)
        val speciesTextView: TextView = view.findViewById(R.id.character_details_species)
        val genderTextView: TextView = view.findViewById(R.id.character_details_gender)
        val houseTextView: TextView = view.findViewById(R.id.character_details_house)
        val dateOfBirthTextView: TextView = view.findViewById(R.id.character_details_date_of_birth)
        val yearOfBirthTextView: TextView = view.findViewById(R.id.character_details_year_of_birth)
        val ancestryTextView: TextView = view.findViewById(R.id.character_details_ancestry)
        val wizardStatusTextView: TextView = view.findViewById(R.id.character_details_wizard)
        val eyeColorTextView: TextView = view.findViewById(R.id.character_details_eye_colour)
        val hairColorTextView: TextView = view.findViewById(R.id.character_details_hair_colour)
        val patronusTextView: TextView = view.findViewById(R.id.character_details_patronus)
        val wandTextView: TextView = view.findViewById(R.id.character_details_wand)
        val actorTextView: TextView = view.findViewById(R.id.character_details_actor)
        val aliveTextView: TextView = view.findViewById(R.id.character_details_alive)
        val hogwartsStudentTextView: TextView = view.findViewById(R.id.character_details_hogwarts_student)
        val hogwartsStaffTextView: TextView = view.findViewById(R.id.character_details_hogwarts_staff)
        val favoriteButton: Button = view.findViewById(R.id.button_add_favorite)

        nameTextView.text = character.name
        alternateNamesTextView.text = "Alternate Names: ${character.alternate_names ?: "Unknown"}"
        speciesTextView.text = "Species: ${character.species ?: "Unknown"}"
        genderTextView.text = "Gender: ${character.gender ?: "Unknown"}"
        houseTextView.text = "House: ${character.house ?: "Unknown"}"
        dateOfBirthTextView.text = "Date of Birth: ${character.dateOfBirth ?: "Unknown"}"
        yearOfBirthTextView.text = "Year of Birth: ${character.yearOfBirth ?: "Unknown"}"
        ancestryTextView.text = "Ancestry: ${character.ancestry ?: "Unknown"}"
        wizardStatusTextView.text = "Wizard: ${character.wizard ?: "Unknown"}"
        eyeColorTextView.text = "Eye Color: ${character.eyeColour ?: "Unknown"}"
        hairColorTextView.text = "Hair Color: ${character.hairColour ?: "Unknown"}"
        patronusTextView.text = "Patronus: ${character.patronus ?: "Unknown"}"
        wandTextView.text = character.wand?.let {
            "Wand: ${it.wood ?: "Unknown"} wood, ${it.core ?: "Unknown"} core, ${it.length ?: "Unknown"} inches"
        } ?: "Wand: Unknown"
        actorTextView.text = "Actor: ${character.actor ?: "Unknown"}"
        aliveTextView.text = "Alive: ${if (character.alive) "Yes" else "No"}"
        wizardStatusTextView.text = "Wizard: ${if (character.wizard) "Yes" else "No"}"
        aliveTextView.text = "Alive: ${if (character.alive) "Yes" else "No"}"
        hogwartsStudentTextView.text = "Hogwarts Student: ${if (character.hogwartsStudent) "Yes" else "No"}"
        hogwartsStaffTextView.text = "Hogwarts Staff: ${if (character.hogwartsStaff) "Yes" else "No"}"

        Glide.with(this)
            .load(character.image)
            .placeholder(R.drawable.placeholder)
            .into(imageView)


        favoriteButton.setOnClickListener {
            val favoriteButton: Button = view.findViewById(R.id.button_add_favorite)

            val sharedPreferences = requireActivity().getSharedPreferences("favorites", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val isFavorite = sharedPreferences.contains(character.name)
            if (isFavorite) {
                favoriteButton.text = "Remove from Favorite"
            } else {
                favoriteButton.text = "Add to Favorite"
            }

            favoriteButton.setOnClickListener {
                val characterJson = Gson().toJson(character)

                if (sharedPreferences.contains(character.name)) {
                    editor.remove(character.name)
                    editor.apply()
                    favoriteButton.text = "Add to Favorite"
                    Toast.makeText(requireContext(), "${character.name} removed from favorites!", Toast.LENGTH_SHORT).show()
                } else {
                    editor.putString(character.name, characterJson)
                    editor.apply()
                    favoriteButton.text = "Remove from Favorite"
                    Toast.makeText(requireContext(), "${character.name} added to favorites!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
