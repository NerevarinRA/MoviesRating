package com.example.ck_4_navigation_drawer.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.media.Rating
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
import com.example.ck_4_navigation_drawer.*
//import com.example.ck_4_navigation_drawer.It_FavouritesFilmsArray
//import com.example.ck_4_navigation_drawer.account.authorization
//import com.example.ck_4_navigation_drawer.account.nickName
import com.example.ck_4_navigation_drawer.databinding.FragmentSecondBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.features.json.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.client.features.json.*
//import io.ktor.client.features.json.serializer.*
//import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.content.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import kotlinx.coroutines.*
//import io.ktor.serialization.gson.*
//import io.ktor.serialization.gson.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.JsonNull.content
import kotlinx.serialization.modules.SerializersModule
import java.util.*



@Serializable
data class Comment(val idComment:Int, val textOfComment: String, val nameUser: String, val idFilm: Int)
@Serializable
data class UserRatingCollection(val filmInCollection :Boolean, val currentUserRating: Float)
class SecondFragment : Fragment()
{
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!



    var CommentsFilmList: MutableList<Comment> = mutableListOf()


    private val client1 = HttpClient {
        install(JsonFeature)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val db = AppDatabase.get(requireContext())
        runBlocking {
            var nickName = "Гость"
            var authorization = false
            val job = /*GlobalScope.launch(Dispatchers.Main)*/ CoroutineScope(Dispatchers.IO).launch() {
                val nickNameList = withContext(Dispatchers.IO) { db.userDao().getNickName() }
                val authorizationList =
                    withContext(Dispatchers.IO) { db.userDao().getAuthorization() }
                if(nickNameList.isNotEmpty())
                {
                    nickName = nickNameList[0]
                    authorization = authorizationList[0]
                }

            }
            job.join()
            //val RatingBar: Int = _binding.ratingBar
            //val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

            fun dpToPx(dp: Float): Int {
                return (dp * resources.displayMetrics.density).toInt()
            }


//===============================Лента
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

//=================Постер=================================
            val imageView = ImageView(requireContext())
            val imageViewParams = LinearLayout.LayoutParams(
                dpToPx(140F),
                dpToPx(133F)
            )
            //params1.setMargins(0, 30, 0, 30)
            //dpToPxparams1.gravity = Gravity.CENTER
            imageView.layoutParams = imageViewParams
            val imageBytes1 =
                android.util.Base64.decode(DataHolder.film.wallpaper, android.util.Base64.DEFAULT)
            // Преобразование массива байтов в изображение
            val bitmap1 =
                android.graphics.BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.size)
            // Отображение изображения в ImageView
            imageView.setImageBitmap(bitmap1)



            val textViewNazvanie = TextView(requireContext())
            textViewNazvanie.setTypeface(null, Typeface.BOLD)
            textViewNazvanie.textSize = 16f
            textViewNazvanie.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            textViewNazvanie.text = DataHolder.film.nameFilm
            val textViewRating = TextView(requireContext())
            textViewRating.text = "Рейтинг:${DataHolder.film.rating}"
            textViewRating.setTypeface(null, Typeface.BOLD)


            val textViewData = TextView(requireContext())
            textViewData.text = "Дата выхода: ${DataHolder.film.year}"
            textViewData.setTypeface(null, Typeface.BOLD)

            val textView2 = TextView(requireContext())
            textView2.text = "Жанры:"
            textView2.setTypeface(null, Typeface.BOLD)

            val textViewZhanr = TextView(requireContext())
            textViewZhanr.setText(DataHolder.film.styles)
            val textViewDirector = TextView(requireContext())
            textViewDirector.text = "Режиссер: ${DataHolder.film.director}"
            textViewDirector.setTypeface(null, Typeface.BOLD)

            val textViewParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            textViewNazvanie.layoutParams = textViewParams
            textViewRating.layoutParams = textViewParams
            textViewData.layoutParams = textViewParams
            textView2.layoutParams = textViewParams
            textViewZhanr.layoutParams = textViewParams
            textViewDirector.layoutParams = textViewParams

//=========================рейтинг звезды===========================
            val ratingBar = RatingBar(requireContext())
            val ratingBarParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            ratingBarParams.gravity = Gravity.CENTER_HORIZONTAL;
            ratingBar.layoutParams = ratingBarParams
            ratingBar.numStars = 5
            ratingBar.stepSize = 1F

            ratingBar.contentDescription = "Rating Bar"
            //ratingBar.rating = DataHolder.film.rating

//==================Линейная горизонтальная разметка===============
            val linearLayoutHor = LinearLayout(requireContext())
            val linearParamsHor = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT//dpToPx(161F)
            )
            linearParamsHor.setMargins(0, 30, 0, 0)
            linearLayoutHor.orientation = LinearLayout.HORIZONTAL
            linearLayoutHor.layoutParams = linearParamsHor
            //linearLayoutHorRating.addView(textViewRating)
            //linearLayoutHorRating.addView(ratingBar)
            //val binding: SecondMainBinding = SecondMainBinding.inflate(layoutInflater)
            //val ratingBar = binding.ratingBar

//===========================вертикальная разметка==================
            val linearLayoutHorVert = LinearLayout(requireContext())
            val linearParamsHorVert = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                //dpToPx(161F)
            )
            linearLayoutHorVert.orientation = LinearLayout.VERTICAL
            linearLayoutHorVert.layoutParams = linearParamsHorVert




//===========================Аннотация==========================
            val textViewAnnotation = TextView(requireContext())
            val textViewAnnotationParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textViewAnnotation.layoutParams = textViewAnnotationParams
            textViewAnnotation.text = DataHolder.film.annotation

            //кнопка избранное


            //val buttonFavourites = ToggleButton(requireContext())
            val buttonFavourites = Button(requireContext())

            val buttonFavouritesParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            buttonFavourites.layoutParams = buttonFavouritesParams
            buttonFavouritesParams.setMargins(10, 20 ,0, 0)

            //buttonFavourites.text = "Добавить в избранное"
            //buttonFavourites.textOff = "Добавить в избранное"
            //buttonFavourites.textOn = "Удалить из избранного"
            //buttonFavourites.isChecked = true
//11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111

            var CheckButtonFavourites = false
            GlobalScope.launch(Dispatchers.Main)
            {
                //получение сотояния ратингбара икнопки избранное
                try {


                    val respInfFilm: String =
                        client1.get("${URL.URL}/getRatingAndCollection?idFilm=${DataHolder.film.idFilm}&userName=${nickName}") {}
                    val gson =
                        Gson() // Предполагается, что вы используете библиотеку Gson для работы с JSON

                    val UserRatingCollectionType =
                        object : TypeToken<UserRatingCollection>() {}.type
                    var UserRatingCollectionToAdd: UserRatingCollection =
                        gson.fromJson(respInfFilm, UserRatingCollectionType)
                    ratingBar.rating = UserRatingCollectionToAdd.currentUserRating
                    CheckButtonFavourites = UserRatingCollectionToAdd.filmInCollection

                    //buttonFavourites.isChecked = CheckButtonFavourites
                    if(CheckButtonFavourites)
                    {
                        buttonFavourites.text = "Удалить из избранного"
                        buttonFavourites.setBackgroundColor(Color.RED)
                    }
                    else{
                        buttonFavourites.text = "Добавить в избранное"
                        buttonFavourites.setBackgroundColor(Color.GREEN)

                    }
                } catch (e: Exception) {
                    println("Ошибка при выполнении запроса: ${e.message}")
                }
            }
//1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111



            val text = textViewZhanr.text.toString()

            val words = text.split(", ")

            val spannableBuilder = SpannableStringBuilder()
            for (word in words) {
                val clickableSpan = object : ClickableSpan() {


                    override fun updateDrawState(ds: TextPaint) {
                        // Опционально: настройка стиля отображения текста в нажимном элементе
                        ds.isUnderlineText = true // Убраванить подчеркие
                        ds.color = Color.BLUE // Изменить цвет текста на синий
                        // Дополнительные настройки, если необходимо
                    }

                    override fun onClick(p0: View) {
                        //TODO("Not yet implemented")
                    }
                }

                spannableBuilder.append(word)
                spannableBuilder.append(" ")
                spannableBuilder.setSpan(
                    clickableSpan,
                    spannableBuilder.length - word.length - 1,
                    spannableBuilder.length - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            textViewZhanr.text = spannableBuilder
            //textViewZhanr.text = "Жанры: ${spannableBuilder}"

            textViewZhanr.movementMethod = LinkMovementMethod.getInstance()


//=====================Массовое отображение всякой фигни===============
            linearLayout.addView(linearLayoutHor)
            linearLayoutHor.addView(imageView)
            linearLayoutHor.addView(linearLayoutHorVert)
            linearLayoutHorVert.addView(textViewNazvanie)
            linearLayoutHorVert.addView(textViewRating)

            linearLayoutHorVert.addView(textViewData)
            linearLayoutHorVert.addView(textView2)
            linearLayoutHorVert.addView(textViewZhanr)
            linearLayoutHorVert.addView(textViewDirector)


            linearLayoutHorVert.addView(buttonFavourites)
            linearLayout.addView(ratingBar)
            linearLayout.addView(textViewAnnotation)


            // комментарии
            val textViewComments = TextView(requireContext())
            textViewComments.text = "Комментарии к фильму: "
            textViewComments.layoutParams = textViewParams
            textViewComments.setTypeface(null, Typeface.BOLD)

            linearLayout.addView(textViewComments)

            val drawableSearch = resources.getDrawable(android.R.drawable.ic_input_add)

            val EditComments = EditText(requireContext())
            val lineEditCommentsParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            EditComments.layoutParams = lineEditCommentsParams
            EditComments.gravity = Gravity.CENTER_HORIZONTAL

            EditComments.hint = "Напишите комментарий"
            EditComments.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableSearch, null)
            linearLayout.addView(EditComments)



            @Serializable
            data class UserRatingDTO(val userRating: Float, val userName: String, val idFilm: Int)

            fun processRatingChange(rating: Float)
            {

                GlobalScope.launch(Dispatchers.Main)
                {

                    client1.post("${URL.URL}/setNewUserRating?idFilm=${DataHolder.film.idFilm}&userName=${nickName}&userRating=${rating}"){}
                }

            }

            if (authorization) {
                buttonFavourites.isClickable = true
                EditComments.isEnabled = true;
                ratingBar.setIsIndicator(false)
                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

                    processRatingChange(ratingBar.rating)

                }

                buttonFavourites.setOnClickListener {
                    if (CheckButtonFavourites == false) {
                        FavouritesFilmsArray.add(DataHolder.film)
                        GlobalScope.launch(Dispatchers.Main)
                        {
                            //добавление в коллекцию избраного
                            try {
                                client1.post("${URL.URL}/addFilmToCollection?userName=${nickName}&idFilm=${DataHolder.film.idFilm}") {}

                            } catch (e: Exception) {
                                println("Ошибка при выполнении запроса: ${e.message}")
                            }
                        }
                        buttonFavourites.text = "Удалить из избранного"
                        CheckButtonFavourites = true
                        buttonFavourites.setBackgroundColor(Color.RED)


                    } else {
                        FavouritesFilmsArray.remove(DataHolder.film)
                        GlobalScope.launch(Dispatchers.Main)
                        {
                            //удаление из коллекции избраного
                            try {
                                client1.post("${URL.URL}/delFilmFromCollection?userName=${nickName}&idFilm=${DataHolder.film.idFilm}") {}

                            } catch (e: Exception) {
                                println("Ошибка при выполнении запроса: ${e.message}")
                            }
                        }
                        buttonFavourites.text = "Добавить в избранное"
                        buttonFavourites.setBackgroundColor(Color.GREEN)
                        CheckButtonFavourites = false


                    }

                }


            } else {
                ratingBar.setIsIndicator(true)
                buttonFavourites.isClickable = false
                buttonFavourites.text = "избранное(недоступно)"
                EditComments.isEnabled = false
                EditComments.hint = "Авторизуйтесь чтобы иметь возможность оставлять комментарии"

            }


            val linearLayoutHorTest = LinearLayout(requireContext())

            var deleteCommList = listOf(linearLayoutHorTest)

            fun ScrollComments(CommentsFilmList: List<Comment>) {
                for (Comm in CommentsFilmList.asReversed()) {
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
                    val params1 = LinearLayout.LayoutParams(
                        dpToPx(60F),
                        dpToPx(60F)
                    )
                    params1.setMargins(0, 0, 10, 0)
                    //dpToPxparams1.gravity = Gravity.CENTER

                    imageView1.layoutParams = params1
                    imageView1.setImageResource(R.drawable.standart_ava)
                    linearLayoutHor.addView(imageView1)

                    val textViewName = TextView(requireContext())
                    textViewName.setText(Comm.nameUser)
                    textViewName.setTypeface(null, Typeface.BOLD)
                    textViewName.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

                    val textViewComment = TextView(requireContext())
                    textViewComment.text = Comm.textOfComment
                    val textViewParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    textViewName.layoutParams = textViewParams
                    textViewComment.layoutParams = textViewParams

                    linearLayoutHor_vert.addView(textViewName)
                    linearLayoutHor_vert.addView(textViewComment)
                    linearLayoutHor.addView(linearLayoutHor_vert)
                    linearLayout.addView(linearLayoutHor)

                    deleteCommList += linearLayoutHor
                }

            }

            fun listComments() {
                GlobalScope.launch(Dispatchers.Main)
                {
                    // Обработка полученного ответа
                    // Например, отображение ответа в текстовом поле или логирование

                    val responseComments: String =
                        client1.get("${URL.URL}/getComments?idFilm=${DataHolder.film.idFilm}&offset=0")
                    val gson = Gson()
                    val commentsListType = object : TypeToken<List<Comment>>() {}.type
                    val commentsToAdd: List<Comment> =
                        gson.fromJson(responseComments, commentsListType)
                    CommentsFilmList.addAll(commentsToAdd)
                    ScrollComments(CommentsFilmList)
                }
            }

            listComments()

            fun delete_scrollComments(films: List<LinearLayout>) {
                for (DelComm in deleteCommList) {
                    DelComm?.let {
                        linearLayout.removeView(it)
                    }
                }
            }


            EditComments.setOnTouchListener { v, event ->
                val DRAWABLE_RIGHT = 2

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (EditComments.right - EditComments.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        // Обработка нажатия на кнопку добавления
                        GlobalScope.launch(Dispatchers.Main)
                        {
                            //Запрос на добавление нового комментария
                            if(EditComments.text.isNotEmpty()) {
                                try {
                                    client1.post("${URL.URL}/addNewComment?idFilm=${DataHolder.film.idFilm}&textOfComment=${EditComments.text}&nameUser=${nickName}") {}
                                } catch (e: Exception) {
                                    println("Ошибка при выполнении запроса: ${e.message}")

                                }
                            }
                            EditComments.text.clear()
                            delete_scrollComments(deleteCommList)
                            CommentsFilmList.clear()
                            listComments()
                        }


                        return@setOnTouchListener true
                    }
                }
                false
            }





            val linearLayout1 = binding.root.findViewById<LinearLayout>(R.id.rootContainer)
            linearLayout1?.addView(scrollView)
        }
        return root
    }




    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

}
