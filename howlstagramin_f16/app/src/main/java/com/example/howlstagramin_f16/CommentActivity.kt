package com.example.howlstagramin_f16

import android.app.Activity
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.howlstagramin_f16.databinding.ActivityCommentBinding
import com.example.howlstagramin_f16.databinding.ActivityMainBinding
import com.example.howlstagramin_f16.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding
    private var auth: FirebaseAuth? = null
    private var store: FirebaseFirestore = FirebaseFirestore.getInstance()
    var firestore : FirebaseFirestore? = null
    var contentUid :String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        contentUid = intent.getStringExtra("contentUid")

        binding.commentBtnSend?.setOnClickListener{
            var comment = ContentDTO.Comment()
            comment.userEmail = auth?.currentUser?.email
            comment.uid = auth?.currentUser?.uid
            comment.comment = binding.commentEditMessage.text.toString()
            comment.timestamp = System.currentTimeMillis()

            try{
                firestore?.collection("post")?.document(contentUid!!)
                    ?.collection("comments")?.document()?.set(comment)

                contentUid?.let { it1 -> Log.d("contentUid", it1) }
                Log.d("comment", comment.toString())

                binding.commentEditMessage.setText("")
                setResult(Activity.RESULT_OK)

                finish()
            }catch (e: Exception){
                Log.d("comment", e.message.toString())
            }

        }


        binding.commentRecyclerview.adapter = CommentRecyclerViewAdapter()
        binding.commentRecyclerview.layoutManager = LinearLayoutManager(this)

    }

    inner class CommentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var comments: ArrayList<ContentDTO.Comment> = arrayListOf()

        init {
            store.collection("posts")
                .document(contentUid!!)
                .collection("comments")
                .orderBy("timestamp")
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        Log.e("저장", e.toString())
                         } else {
                        comments.clear()

                        if (querySnapshot == null)
                            return@addSnapshotListener

                        for (snapshot in querySnapshot.documents!!) {
                            comments.add(snapshot.toObject(ContentDTO.Comment::class.java)!!)
                        }

                    }
                    notifyDataSetChanged()

                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.commnet, parent, false)
            return CustomViewHolder(view)
        }

        private inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView
            var commentViewItemComment =
                view.findViewById<TextView>(R.id.commentviewitem_textview_comment)
            var commentViewProfile =
                view.findViewById<TextView>(R.id.commentviewitem_textview_profile)
            var commentViewImageProfile =
                view.findViewById<ImageView>(R.id.commentviewitem_imageview_profile)
            commentViewItemComment.text = comments[position].comment
            commentViewProfile.text = comments[position].userEmail

            FirebaseFirestore.getInstance()
                .collection("profileImages")
                .document(comments[position].uid!!)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var url = task.result!!["image"]
                        Glide.with(holder.itemView.context).load(url)
                            .apply(RequestOptions().circleCrop()).into(commentViewImageProfile)
                    }
                }
        }

        override fun getItemCount(): Int {
            return comments.size
        }
    }
}

