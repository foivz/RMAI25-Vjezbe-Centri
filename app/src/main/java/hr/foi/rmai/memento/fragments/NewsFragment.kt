package hr.foi.rmai.memento.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.adapters.NewsAdapter
import hr.foi.rmai.memento.ws.NewsResponse
import hr.foi.rmai.memento.ws.WsNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingCircle: ProgressBar
    private lateinit var btnRefresh: FloatingActionButton
    private val ws = WsNews.newsService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadingCircle = view.findViewById(R.id.pb_news_fragment_loading)

        btnRefresh = view.findViewById(R.id.fab_news_fragment_refresh_news)
        btnRefresh.setOnClickListener {
            loadNews()
        }

        recyclerView = view.findViewById(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadNews()
    }

    private fun loadNews() {
        changeDisplay(true)

        ws.getNews().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>?, response: Response<NewsResponse>?) {
                if (response?.isSuccessful == true) {
                    val responseBody = response.body()
                    val news = responseBody!!.results
                    recyclerView.adapter = NewsAdapter(news)
                } else {
                }

                changeDisplay(false)
            }

            override fun onFailure(call: Call<NewsResponse>?, t: Throwable?) {
                changeDisplay(false)
            }
        })
    }

    private fun changeDisplay(isLoading: Boolean) {
        btnRefresh.isVisible = !isLoading
        recyclerView.isVisible = !isLoading
        loadingCircle.isVisible = isLoading
    }
}