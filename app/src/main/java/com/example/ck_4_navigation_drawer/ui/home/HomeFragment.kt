package com.example.ck_4_navigation_drawer.ui.home


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ck_4_navigation_drawer.*
import com.example.ck_4_navigation_drawer.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.util.*



object DataHolder {
    lateinit var film: Movie
}
class HomeFragment : Fragment()
{
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    var ChipGroupList:MutableList<String> = mutableListOf()

    var films: MutableList<Movie> = mutableListOf()


    private val client = HttpClient {
        install(JsonFeature)
    }


    var executeGetRequest: (urlGet:String) -> Unit = {
        //  функция по умолчанию

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//===========================================================================================



        fun dpToPx(dp: Float): Int {
            return (dp * resources.displayMetrics.density).toInt()
        }


        var deleteFilmArray = listOf<LinearLayout>()


        val scrollView = ScrollView(requireContext())
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(30, 30, 30, 30)
        scrollView.layoutParams = layoutParams
        val linearLayout = LinearLayout(requireContext())
        val linearParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = linearParams
        scrollView.addView(linearLayout)




        val editText = EditText(requireContext())
        val editTextParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        editText.layoutParams = editTextParams
        editText.gravity = Gravity.CENTER_HORIZONTAL
        editText.hint = "Введите название фильма"

        var drawableSearch = resources.getDrawable(android.R.drawable.ic_search_category_default)

        var drawableSearchBit = (drawableSearch as BitmapDrawable).bitmap

        drawableSearchBit = Bitmap.createScaledBitmap(drawableSearchBit, 60, 60, false)

        drawableSearch = BitmapDrawable(resources, drawableSearchBit)

        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableSearch, null)

        linearLayout.addView(editText)





        val chipGroup = ChipGroup(requireContext())
        val chipGroupParams  = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.layoutParams = chipGroupParams
        linearLayout.addView(chipGroup)

        val spinner_zhanr = Spinner(requireContext())
        val spinner_zhanrParams =  LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        spinner_zhanr.layoutParams = spinner_zhanrParams
        val items_spinZh = resources.getStringArray(R.array.zhanr_sort)
        val adapter_spinZh = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items_spinZh)

        spinner_zhanr.adapter = adapter_spinZh
        linearLayout.addView(spinner_zhanr)
        var dublikat_detective = true
        var dublikat_drama = true
        var dublikat_arthaus = true
        var dublikat_triller = true
        var dublikat_boevik = true
        var dublikat_comedian = true
        var dublikat_fantastic = true
        var dublikat_musicle = true
        var dublikat_melodrama = true
        var dublikat_tragedy = true
        var dublikat_nauchnayaFantastika = true
        var dublikat_biography = true
        var dublikat_adventure = true
        var dublikat_fantasy = true



        spinner_zhanr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                // Обработайте выбор элемента
                val selectedItem_zhanr = items_spinZh[position]
                // код обработки выбора элемента здесь
                when(selectedItem_zhanr)
                {
                    "Добавить фильтр жанров"-> {


                    }
                    "детектив"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_detective != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "детектив"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_detective = true
                                chipGroup.removeView(chip) // убираем чип из родительского макета
                                ChipGroupList -= "детектив"
                                formStyleString(ChipGroupList)

                            }
                            chip.text = "детектив"
                            chipGroup.addView(chip)
                            dublikat_detective = false
                        }
                    }
                    "драма"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_drama != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "драма"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_drama = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "драма"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "драма"
                            chipGroup.addView(chip)
                            dublikat_drama = false
                        }
                    }

                    "триллер"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_triller != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "триллер"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_triller = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "триллер"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "триллер"
                            chipGroup.addView(chip)
                            dublikat_triller = false
                        }
                    }
                    "артхаус"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_arthaus != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "артхаус"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_arthaus = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "артхаус"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "артхаус"
                            chipGroup.addView(chip)
                            dublikat_arthaus = false
                        }
                    }
                    "боевик"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_boevik != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "боевик"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_boevik = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "боевик"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "боевик"
                            chipGroup.addView(chip)
                            dublikat_boevik = false
                        }
                    }
                    "научная фантастика"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_nauchnayaFantastika != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "научная фантастика"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_nauchnayaFantastika = true
                                chipGroup.removeView(chip) // убираем чип из родительского макета
                                ChipGroupList -= "научная фантастика"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "научная фантастика"
                            chipGroup.addView(chip)
                            dublikat_nauchnayaFantastika = false
                        }
                    }
                    "комедия"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_comedian != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "комедия"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_comedian = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "комедия"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "комедия"
                            chipGroup.addView(chip)
                            dublikat_comedian = false
                        }
                    }
                    "фантастика"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_fantastic != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "фантастика"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_fantastic = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "фантастика"
                                formStyleString(ChipGroupList)

                            }
                            chip.text = "фантастика"
                            chipGroup.addView(chip)
                            dublikat_fantastic = false
                        }
                    }
                    "мюзикл"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_musicle != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "мюзикл"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_musicle = true
                                chipGroup.removeView(chip) // убираем чип из родительского макета
                                ChipGroupList -= "мюзикл"
                                formStyleString(ChipGroupList)

                            }
                            chip.text = "мюзикл"
                            chipGroup.addView(chip)
                            dublikat_musicle = false
                        }
                    }
                    "мелодрама"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_melodrama != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "мелодрама"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_melodrama = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "мелодрама"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "мелодрама"
                            chipGroup.addView(chip)
                            dublikat_melodrama = false
                        }
                    }
                    "трагедия"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_tragedy != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "трагедия"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_tragedy = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "трагедия"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "трагедия"
                            chipGroup.addView(chip)
                            dublikat_tragedy = false
                        }
                    }
                    "биография"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_biography != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "биография"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_biography = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "биография"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "биография"
                            chipGroup.addView(chip)
                            dublikat_biography = false
                        }
                    }
                    "приключения"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_adventure != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true // устанавливаем видимость иконки закрытия
                            ChipGroupList += "приключения"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_adventure = true
                                chipGroup.removeView(chip) // убираем чип из родительского макета
                                ChipGroupList -= "приключения"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "приключения"
                            chipGroup.addView(chip)
                            dublikat_adventure = false
                        }
                    }


                    "фэнтези"->
                    {
                        spinner_zhanr.setSelection(0)

                        if (dublikat_fantasy != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true // устанавливаем видимость иконки закрытия
                            ChipGroupList += "фэнтези"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_fantasy = true
                                chipGroup.removeView(chip) // убираем чип из родительского макета
                                ChipGroupList -= "фэнтези"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "фэнтези"
                            chipGroup.addView(chip)
                            dublikat_fantasy = false
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Действие при отсутствии выбора элемента
            }
        }

        val spinner_all = Spinner(requireContext())
        val spinner_allParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        spinner_all.layoutParams = spinner_allParams
        val items_spinAll = resources.getStringArray(R.array.all_sort)

        val adapter_spinAll = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items_spinAll)
        spinner_all.adapter = adapter_spinAll
        linearLayout.addView(spinner_all)



        fun scrollFilms(films:List<Movie>)
        {
            for (film in films)
            {
                val linearLayoutHor = LinearLayout(requireContext())
                val linearParamsHor = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(161F)
                )
                linearLayoutHor.orientation = LinearLayout.HORIZONTAL
                linearLayoutHor.layoutParams = linearParamsHor

                val linearLayoutHor_vert = LinearLayout(requireContext())
                val linearParamsHor_vert = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                linearLayoutHor_vert.orientation = LinearLayout.VERTICAL
                linearLayoutHor_vert.layoutParams = linearParamsHor_vert

                val imageView1 = ImageView(requireContext())
                val params1 = LinearLayout.LayoutParams(dpToPx(140F),
                    dpToPx(133F))
                //params1.setMargins(0, 30, 0, 30)
                //dpToPxparams1.gravity = Gravity.CENTER
                imageView1.layoutParams = params1
                // Преобразование строки base64 в байты
                val imageBytes = android.util.Base64.decode(film.wallpaper, android.util.Base64.DEFAULT)
            // Преобразование массива байтов в изображение
                val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imageView1.setImageBitmap(bitmap)
                linearLayoutHor.addView(imageView1)

                val textViewNazvanie = TextView(requireContext())
                textViewNazvanie.setTypeface(null, Typeface.BOLD)
                textViewNazvanie.textSize = 16f
                textViewNazvanie.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                textViewNazvanie.setText(film.nameFilm)
                val textViewRating = TextView(requireContext())
                textViewRating.text = "Рейтинг: ${film.rating}"
                textViewRating.setTypeface(null, Typeface.BOLD)

                val textViewData = TextView(requireContext())
                textViewData.text = "Дата выхода: ${film.year}"
                textViewData.setTypeface(null, Typeface.BOLD)

                val textView2 = TextView(requireContext())
                textView2.text = "Жанры:"
                textView2.setTypeface(null, Typeface.BOLD)

                val textViewZhanr = TextView(requireContext())
                textViewZhanr.text = film.styles
                val textViewParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                textViewNazvanie.layoutParams = textViewParams
                textViewRating.layoutParams = textViewParams
                textViewData.layoutParams = textViewParams
                textView2.layoutParams = textViewParams
                textViewZhanr.layoutParams = textViewParams

                textViewNazvanie.setOnClickListener{
                    DataHolder.film = film
                    findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
                }

                imageView1.setOnClickListener{
                    DataHolder.film = film
                    findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
                }

                // Получить текст из TextView
                val text = textViewZhanr.text.toString()


                val words = text.split(", ")



                val spannableBuilder = SpannableStringBuilder()




                for (word in words)
                {
                    val clickableSpan = object : ClickableSpan()
                    {
                        override fun onClick(view: View)
                        {
                            when(word)
                            {
                                "детектив"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_detective != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "детектив"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_detective = true
                                            chipGroup.removeView(chip) // убираем чип из родительского макета
                                            ChipGroupList -= "детектив"
                                            formStyleString(ChipGroupList)

                                        }
                                        chip.text = "детектив"
                                        chipGroup.addView(chip)
                                        dublikat_detective = false
                                    }
                                }
                                "драма"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_drama != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "драма"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_drama = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "драма"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "драма"
                                        chipGroup.addView(chip)
                                        dublikat_drama = false
                                    }
                                }

                                "триллер"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_triller != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "триллер"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_triller = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "триллер"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "триллер"
                                        chipGroup.addView(chip)
                                        dublikat_triller = false
                                    }
                                }
                                "артхаус"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_arthaus != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "артхаус"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_arthaus = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "артхаус"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "артхаус"
                                        chipGroup.addView(chip)
                                        dublikat_arthaus = false
                                    }
                                }
                                "боевик"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_boevik != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "боевик"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_boevik = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "боевик"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "боевик"
                                        chipGroup.addView(chip)
                                        dublikat_boevik = false
                                    }
                                }
                                "научная фантастика"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_nauchnayaFantastika != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "научная фантастика"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_nauchnayaFantastika = true
                                            chipGroup.removeView(chip) // убираем чип из родительского макета
                                            ChipGroupList -= "научная фантастика"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "научная фантастика"
                                        chipGroup.addView(chip)
                                        dublikat_nauchnayaFantastika = false
                                    }
                                }
                                "комедия"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_comedian != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "комедия"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_comedian = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "комедия"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "комедия"
                                        chipGroup.addView(chip)
                                        dublikat_comedian = false
                                    }
                                }
                                "фантастика"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_fantastic != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "фантастика"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_fantastic = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "фантастика"
                                            formStyleString(ChipGroupList)

                                        }
                                        chip.text = "фантастика"
                                        chipGroup.addView(chip)
                                        dublikat_fantastic = false
                                    }
                                }
                                "мюзикл"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_musicle != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "мюзикл"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_musicle = true
                                            chipGroup.removeView(chip) // убираем чип из родительского макета
                                            ChipGroupList -= "мюзикл"
                                            formStyleString(ChipGroupList)

                                        }
                                        chip.text = "мюзикл"
                                        chipGroup.addView(chip)
                                        dublikat_musicle = false
                                    }
                                }
                                "мелодрама"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_melodrama != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "мелодрама"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_melodrama = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "мелодрама"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "мелодрама"
                                        chipGroup.addView(chip)
                                        dublikat_melodrama = false
                                    }
                                }
                                "трагедия"->
                                {
                                    //editText.hint = "Поиск по жанру [трагедия]"
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_tragedy != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "трагедия"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_tragedy = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "трагедия"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "трагедия"
                                        chipGroup.addView(chip)
                                        dublikat_tragedy = false
                                    }
                                }
                                "биография"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_biography != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "биография"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_biography = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "биография"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "биография"
                                        chipGroup.addView(chip)
                                        dublikat_biography = false
                                    }
                                }
                                "приключения"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_adventure != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true // устанавливаем видимость иконки закрытия
                                        ChipGroupList += "приключения"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_adventure = true
                                            chipGroup.removeView(chip) // убираем чип из родительского макета
                                            ChipGroupList -= "приключения"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "приключения"
                                        chipGroup.addView(chip)
                                        dublikat_adventure = false
                                    }
                                }

                                "фэнтези"->
                                {
                                    spinner_zhanr.setSelection(0)

                                    if (dublikat_fantasy != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true // устанавливаем видимость иконки закрытия
                                        ChipGroupList += "фэнтези"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_fantasy = true
                                            chipGroup.removeView(chip) // убираем чип из родительского макета
                                            ChipGroupList -= "фэнтези"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "фэнтези"
                                        chipGroup.addView(chip)
                                        dublikat_fantasy = false
                                    }
                                }
                            }
                        }

                        override fun updateDrawState(ds: TextPaint)
                        {
                            // Опционально: настройка стиля отображения текста в нажимном элементе
                            ds.isUnderlineText = true // Убраванить подчеркие
                            ds.color = Color.RED // Изменить цвет текста на синий
                            // Дополнительные настройки, если необходимо
                        }
                    }

                    spannableBuilder.append(word)
                    spannableBuilder.append(" ")
                    spannableBuilder.setSpan(clickableSpan, spannableBuilder.length - word.length - 1, spannableBuilder.length - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                textViewZhanr.text = spannableBuilder
                //textViewZhanr.text = "Жанры: ${spannableBuilder}"

                textViewZhanr.movementMethod = LinkMovementMethod.getInstance()

                linearLayoutHor_vert.addView(textViewNazvanie)
                linearLayoutHor_vert.addView(textViewRating)
                linearLayoutHor_vert.addView(textViewData)
                linearLayoutHor_vert.addView(textView2)
                linearLayoutHor_vert.addView(textViewZhanr)
                linearLayoutHor.addView(linearLayoutHor_vert)


                linearLayout.addView(linearLayoutHor)
//
//                if(film == films.last())
//                {
//                    val button_podgruz = Button(requireContext())
//                    val button_podgruzParams = LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                    button_podgruz.layoutParams = button_podgruzParams
//                    button_podgruz.text = "Показать ещё"
//                    linearLayout.addView(button_podgruz)
//
//                }


                deleteFilmArray += linearLayoutHor

            }


        }


        //*********************************************************************
        fun delete_scrollFilms(films:List<LinearLayout>)
        {
            for(DelFilm in deleteFilmArray)
            {
                //DelFilm.removeView(linearLayout)
                DelFilm?.let {
                    linearLayout.removeView(it)
                }
            }
//            if(deleteFilmArray.isNotEmpty())
//            {
//                val lastView = linearLayout.getChildAt(linearLayout.childCount-1)
//                linearLayout.removeView(lastView)
//            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun handleResponse(response: String)
        {
            val gson = Gson()
            val movieListType = object : TypeToken<List<Movie>>() {}.type
            val moviesToAdd: List<Movie> = gson.fromJson(response, movieListType)
            films .addAll(moviesToAdd)
            scrollFilms(films)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        executeGetRequest= {urlGet ->

            delete_scrollFilms(deleteFilmArray)
            films.clear()

            GlobalScope.launch(Dispatchers.Main)
            {
                try {
                    val response: String = client.get(urlGet)
                    handleResponse(response)
                } catch (e: Exception) {
                    println("Ошибка при выполнении запроса: ${e.message}")
                }
            }
        }

        editText.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP)
            {
                if (event.rawX >= (editText.right - editText.compoundDrawables[DRAWABLE_RIGHT].bounds.width()))
                {
                    // Обработка нажатия на кнопку поиска
                    delete_scrollFilms(deleteFilmArray)
                    films.clear()
                    chipGroup.removeAllViews()
                    GlobalScope.launch(Dispatchers.Main)
                    {
                        executeGetRequest("${URL.URL}/findFilmByName?name=${editText.text}")
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        spinner_all.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                // Обработайте выбор элемента
                val selectedItem_all = items_spinAll[position]
                // код обработки выбора элемента здесь
                when(selectedItem_all)
                {
                    "рейтинг(по убыванию)"->
                    {
                        executeGetRequest("${URL.URL}/getAllFilmsRatingDESC?offset=0")
                    }
                    "рейтинг(по возрастанию)"->
                    {
                        executeGetRequest("${URL.URL}/getAllFilmsRatingASC?offset=0")
                    }

                    "дата выхода(новейшие)"->
                    {
                        executeGetRequest("${URL.URL}/getAllFilmsYearDESC?offset=0")
                    }
                    "дата выхода(старейшие)"->
                    {
                        executeGetRequest("${URL.URL}/getAllFilmsYearASC?offset=0")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Действие при отсутствии выбора элемента
            }
        }

        val linearLayout1 =  binding.root.findViewById<LinearLayout>(R.id.rootContainer)
        linearLayout1?.addView(scrollView)

        return root
    }

    fun formStyleString(ChipGroupList:MutableList<String>)
    {
        if(ChipGroupList.isEmpty())
        {
            executeGetRequest("${URL.URL}/getAllFilmsRatingDESC?offset=0")

        }
        else{
            var StyleString = ""
            for(style in ChipGroupList)
            {
                StyleString += "${style},"
            }

            executeGetRequest("${URL.URL}/findFilmByStyle?style=${StyleString}")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
