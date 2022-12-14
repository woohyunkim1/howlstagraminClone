package com.example.howlstagramin_f16.navigation

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howlstagramin_f16.CommentActivity
import com.example.howlstagramin_f16.MainActivity
import com.example.howlstagramin_f16.model.ContentDTO
import com.example.howlstagramin_f16.model.UserDTO
import com.example.howlstagramin_f16.R
import com.example.howlstagramin_f16.databinding.FragmentHomeBinding
import com.example.howlstagramin_f16.databinding.PostDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeFragment: Fragment() {
    private lateinit var mBinding: FragmentHomeBinding
    private var TAG: String = "HomeFragment: "
    private var store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var adapter: PostAdapter? = null
    private var contentsList = arrayListOf<ContentDTO>()
    private var usersHashMap = HashMap<String, UserDTO>()
    private var contentsId = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        var followingSet: ArrayList<String>? = null

        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                var userDTO = store.collection("users").document(auth.uid!!).get()
                    .await().toObject(UserDTO::class.java)

                followingSet = ArrayList(userDTO!!.followings.keys)
                followingSet?.add(auth.currentUser!!.email!!)

                store.collection("users")
//                    .whereIn("email", followingSet!!)
                    .addSnapshotListener { users, error ->
                        if (error != null)
                            Log.e(TAG, error.toString())
                        else {
                            usersHashMap.clear()
                            for (user in users!!.documents)
                                usersHashMap[user.id] = user.toObject(UserDTO::class.java)!!
                        }
                    }
            }

            adapter = PostAdapter(followingSet)
            mBinding.postsRecyclerView.adapter = adapter
        }

        //layoutManager : ???????????? ????????? ??????.
        //LinearLayoutManager : ??????/??????
        //GirdLayoutManager : ????????? ??????
        //StaggeredGirdLayoutManager : ?????????????????? ????????? ??????
        mBinding.postsRecyclerView.layoutManager = LinearLayoutManager(activity)
        mBinding.postsRecyclerView.addItemDecoration(VerticalItemDecorator(30))

        return mBinding.root
    }

    //Adapter : ???????????? ???????????? ?????? View??? ??????.
    inner class PostAdapter(followingList: ArrayList<String>?): RecyclerView.Adapter<PostHolder>() {
        private lateinit var postDetailBinding: PostDetailBinding

        //DB?????? ????????? ????????? ????????????
        init {
            store.collection("posts")
//                .whereIn("userEmail", followingList!!)
                .addSnapshotListener { posts, e ->
                    if (e != null) {
                        Log.e(TAG, e.toString())
                        Toast.makeText(activity, R.string.upload_fail, Toast.LENGTH_SHORT).show()
                    } else {
                        contentsList.clear()
                        contentsId.clear()
                        for (post in posts!!.documents) {
                            var content = post.toObject(ContentDTO::class.java)
                            contentsList.add(content!!)
                            contentsId.add(post.id)
                        }
                    }
                    notifyDataSetChanged()
                }
        }

        //post_detail.xml??? ???????????? ??????
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
            var inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            postDetailBinding = PostDetailBinding.inflate(inflater, parent, false)

            return PostHolder(postDetailBinding)
        }

        //???????????? ????????? ???????????? ??? ????????? ??????.
        override fun onBindViewHolder(holder: PostHolder, position: Int) {
            holder.setData(contentsList[position], position)
        }

        //??? ??? ?????? ???????????? ??????????????? ????????? ??????.
        override fun getItemCount(): Int {
            return contentsList.size
        }
    }

    //?????? ???????????? Holder ??????.
    //item ????????? ??????????????? ?????? ??? ????????? ??????????????? ??????.
    //????????? ????????? ??? ???????????? ??? ?????? ??????, ????????? ??? ????????? ????????? ????????? ????????? ?????????.
    inner class PostHolder(var postDetailBinding: PostDetailBinding) : RecyclerView.ViewHolder(postDetailBinding.root) {
        private var position: Int? = null

        init {
            postDetailBinding.heartIcon.setOnClickListener {
                heartIconClickEvent(position!!)
            }
            //???????????? ????????? ??????????????? ????????? ?????? ?????? ???????????? ??????.
            postDetailBinding.usernameTextView.setOnClickListener {
                (activity as MainActivity).changeFragment(AccountFragment((it as TextView).text.toString()))
            }

            postDetailBinding.chatIcon.setOnClickListener { v->
                var intent = Intent(v.context, CommentActivity::class.java)
                intent.putExtra("contentUid", contentsId[position!!])
                startActivity(intent)
            }
        }

        fun setData(content: ContentDTO, position: Int) {
            this.position = position

            postDetailBinding.usernameTextView.text = content.userEmail
            Glide.with(postDetailBinding.root).load(content.imageUrl).into(postDetailBinding.postPhotoIV)
            postDetailBinding.favoriteCountTextView.text = "????????? ${content.favoriteCount}???"
            postDetailBinding.postTextView.text = content.exaplain

            try {   //????????? ????????? ????????????
                Glide.with(postDetailBinding.root)
                    .load(usersHashMap[content.uid]!!.profileImgUrl!!)
                    .circleCrop()
                    .into(postDetailBinding.profileImageView)
            } catch(e: NullPointerException) {  //????????? ???????????? ?????? ?????? -> ?????? ?????????
                postDetailBinding.profileImageView.setImageResource(R.drawable.ic_account)
            }

            if (content.favorites.containsKey(auth.currentUser!!.uid)) {
                //?????? ???????????? ???????????? ?????? ????????????
                postDetailBinding.heartIcon.setImageResource(R.drawable.ic_favorite)
            } else {
                //?????? ???????????? ???????????? ????????? ?????? ????????????
                postDetailBinding.heartIcon.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        private fun heartIconClickEvent(position: Int) {
            var postDoc = store.collection("posts").document(contentsId[position])
            store.runTransaction { transaction ->
                var content:ContentDTO = transaction.get(postDoc).toObject(ContentDTO::class.java)!!
                if (content.favorites.containsKey(auth.currentUser!!.uid)) {
                    //???????????? ?????? ???????????? -> ????????? ??????
                    content.favoriteCount -= 1
                    content.favorites.remove(auth.currentUser!!.uid)
                } else {
                    //???????????? ????????? ?????? ???????????? -> ?????????!
                    content.favoriteCount += 1
                    content.favorites.put(auth.currentUser!!.uid, true)
                }

                transaction.update(postDoc, "favorites", content.favorites)
                transaction.update(postDoc, "favoriteCount", content.favoriteCount)
            }.addOnSuccessListener {
                adapter?.notifyItemChanged(position)
            }.addOnFailureListener {
                Log.e(TAG, it.toString())
                Toast.makeText(activity, getString(R.string.error_alarm), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //????????? ??? ??????(?????????) ?????? ??????
    inner class VerticalItemDecorator(var divHeight: Int):RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = divHeight
            outRect.bottom = divHeight
        }
    }
}


