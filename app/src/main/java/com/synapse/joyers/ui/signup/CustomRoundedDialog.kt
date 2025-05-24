package com.synapse.joyers.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R
import com.synapse.joyers.apiData.request.UpdateUserRequest
import com.synapse.joyers.apiData.response.Title
import com.synapse.joyers.localstore.PreferencesManager
import com.synapse.joyers.ui.signup.adapter.TitleNameAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import androidx.core.graphics.toColorInt
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.response.BaseResponse
import com.synapse.joyers.apiData.response.Subtitle
import com.synapse.joyers.ui.auth.SplashVideoActivity

class CustomRoundedDialog(
    val context: FragmentActivity,
    private val titles: List<Title>,
) : DialogFragment() {

    private lateinit var expandableContent: LinearLayout
    private lateinit var toggleContent: TextView
    private var isExpanded = false
    private var titleId: String = ""
    private val signupViewModel: SignupViewModel by inject()
    private val preferencesManager: PreferencesManager by inject()
    private var titlesList: List<Title> = mutableListOf()
    private var subTitlesList: List<Subtitle> = mutableListOf()

    init {
        titlesList = titles
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_custom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRecyclerViews(view)
        setupListeners()
        observeApiPostData()

        view.findViewById<EditText>(R.id.et_search).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.findViewById<TextView>(R.id.tv_title).text.toString()
                        .contains("Title >")
                ) {
                    val query = s.toString()
                    searchTitles(query, null, subTitlesList)
                } else {
                    val query = s.toString()
                    searchTitles(query, titlesList, null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(android.graphics.Color.GRAY.toDrawable())
        }
    }

    private fun initViews(view: View) {
        expandableContent = view.findViewById(R.id.expandable_content)
        toggleContent = view.findViewById(R.id.tv_toggle_content)
        expandableContent.visibility = View.GONE
    }

    private fun setupRecyclerViews(view: View) {
        view.findViewById<RecyclerView>(R.id.rv_titles).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TitleNameAdapter(requireActivity(), titlesList, null) { selectedPos ->
                view.findViewById<TextView>(R.id.tv_title).textSize = 16f
                view.findViewById<TextView>(R.id.tv_title).text =
                    getStyledTitleText("Title > ", titlesList[selectedPos].title.toString())

                if (titlesList[selectedPos].subtitles?.size!! > 0) {
                    subTitlesList = titlesList[selectedPos].subtitles!!
                    layoutManager = LinearLayoutManager(context)
                    view.findViewById<RecyclerView>(R.id.rv_titles).apply {
                        adapter =
                            TitleNameAdapter(
                                requireActivity(),
                                null,
                                subTitlesList
                            ) { selectedPos ->
                                view.findViewById<TextView>(R.id.tv_apply).visibility = View.VISIBLE
                                view.findViewById<TextView>(R.id.tv_search).visibility = View.GONE
                                titleId = subTitlesList[selectedPos]._id.toString()
                            }
                    }
                    view.findViewById<TextView>(R.id.tv_apply).visibility = View.GONE
                    view.findViewById<TextView>(R.id.tv_search).visibility = View.VISIBLE
                } else {
                    view.findViewById<TextView>(R.id.tv_apply).visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.tv_search).visibility = View.GONE
                    titleId = titlesList[selectedPos]._id.toString()
                }
            }
        }

    }

    private fun setupListeners() {
        toggleContent.setOnClickListener {
            toggleExpandableSection()
        }

        view?.findViewById<ImageButton>(R.id.btn_close_dialog)?.setOnClickListener {
            dismiss()
        }

        view?.findViewById<TextView>(R.id.tv_apply)?.setOnClickListener {
            lifecycleScope.launch {
                val token = preferencesManager.getAccessToken()
                val userID = preferencesManager.getUserId()
                if (token != null && userID != null) {
                    signupViewModel.updateUser(
                        token, userID, UpdateUserRequest(
                            is_skipped = true, JoyTitleId = titleId
                        )
                    )

                }
            }
        }
    }

    private fun toggleExpandableSection() {
        if (isExpanded) collapseSection() else expandSection()
    }

    private fun expandSection() {
        expandableContent.visibility = View.VISIBLE
        expandableContent.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        expandableContent.requestLayout()
        isExpanded = true
        toggleContent.text = getString(R.string.hide)

    }

    private fun collapseSection() {
        expandableContent.visibility = View.GONE
        isExpanded = false
        toggleContent.text = getString(R.string.show)

    }

    private fun getStyledTitleText(baseTitle: String, selectedTitle: String): SpannableString {
        val fullText = "$baseTitle$selectedTitle"
        val spannable = SpannableString(fullText)

        // Load fonts
        val regularTypeface = ResourcesCompat.getFont(context, R.font.lato_bold)
        val semiBoldTypeface = ResourcesCompat.getFont(context, R.font.lato_semi_bold)

        // Regular style for "Title > "
        regularTypeface?.let {
            spannable.setSpan(
                TypefaceSpan(it),
                0,
                baseTitle.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        spannable.setSpan(
            ForegroundColorSpan(android.graphics.Color.BLACK),
            0,
            baseTitle.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            RelativeSizeSpan(1.0f),
            0,
            baseTitle.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Semi-bold style for selected title
        semiBoldTypeface?.let {
            spannable.setSpan(
                TypefaceSpan(it),
                baseTitle.length,
                fullText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        spannable.setSpan(
            ForegroundColorSpan("#D69E3A".toColorInt()),
            baseTitle.length,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            RelativeSizeSpan(1.0f),
            baseTitle.length,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannable
    }

    private fun observeApiPostData() {
        signupViewModel.userInfoResponse.observe(requireActivity()) { response ->
            val apiResultHandler = ApiResultHandler<BaseResponse>(
                requireActivity(),
                onLoading = { /*showProgress(binding.progressBar.circularProgress, true)*/ },
                onSuccess = {
                    /* showProgress(binding.progressBar.circularProgress, false)*/
                    val intent = Intent(requireActivity(), SplashVideoActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                },
                onFailure = {
                    /*showProgress(binding.progressBar.circularProgress, false)*/
                }
            )
            apiResultHandler.handleApiResult(response)
        }
    }

    fun searchTitles(query: String, titleList: List<Title>?, subTitleList: List<Subtitle>?) {
        val lowercaseQuery = query.trim().lowercase()
        if (titleList != null) {
            val updatedList = titleList.filter { item ->
                val inTitle = item.title?.lowercase()?.contains(lowercaseQuery) == true
                inTitle
            }
            view?.findViewById<RecyclerView>(R.id.rv_titles)?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TitleNameAdapter(requireActivity(), updatedList, null) { selectedPos ->
                    view?.findViewById<TextView>(R.id.tv_title)?.textSize = 16f
                    view?.findViewById<TextView>(R.id.tv_title)?.text =
                        getStyledTitleText("Title > ", updatedList[selectedPos].title.toString())

                    if (updatedList[selectedPos].subtitles?.size!! > 0) {
                        view?.findViewById<TextView>(R.id.et_search)?.text = ""
                        hideKeyboard()
                        subTitlesList = updatedList[selectedPos].subtitles!!
                        layoutManager = LinearLayoutManager(context)
                        view?.findViewById<RecyclerView>(R.id.rv_titles).apply {
                            adapter =
                                TitleNameAdapter(
                                    requireActivity(),
                                    null,
                                    subTitlesList
                                ) { selectedPos ->
                                    view?.findViewById<TextView>(R.id.tv_apply)?.visibility =
                                        View.VISIBLE
                                    view?.findViewById<TextView>(R.id.tv_search)?.visibility =
                                        View.GONE
                                    titleId = subTitlesList[selectedPos]._id.toString()
                                }
                        }
                        view?.findViewById<TextView>(R.id.tv_apply)?.visibility = View.GONE
                        view?.findViewById<TextView>(R.id.tv_search)?.visibility = View.VISIBLE
                    } else {
                        view?.findViewById<TextView>(R.id.tv_apply)?.visibility = View.VISIBLE
                        view?.findViewById<TextView>(R.id.tv_search)?.visibility = View.GONE
                        titleId = updatedList[selectedPos]._id.toString()
                    }
                }
            }
        } else {
            val updatedList = subTitleList?.filter { item ->
                val inTitle = item.name?.lowercase()?.contains(lowercaseQuery) == true
                inTitle
            }

            view?.findViewById<RecyclerView>(R.id.rv_titles)?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TitleNameAdapter(requireActivity(), null, updatedList) { selectedPos ->
                    view?.findViewById<TextView>(R.id.tv_apply)?.visibility = View.VISIBLE
                    view?.findViewById<TextView>(R.id.tv_search)?.visibility = View.GONE
                    titleId = updatedList!![selectedPos]._id.toString()
                }
            }
        }

    }

    private fun View.hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
